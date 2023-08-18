package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.ColorHandlerHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RendererHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryProviderListener;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.BlockEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.BlockEntityFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.BlockFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.BlockItemFactory;
import xyz.apex.minecraft.apexcore.common.lib.resgen.AdvancementProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderLookup;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderTypes;
import xyz.apex.minecraft.apexcore.common.lib.resgen.RecipeProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.loot.BlockLootProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.loot.LootTypes;
import xyz.apex.minecraft.apexcore.common.lib.resgen.model.ModelBuilder;
import xyz.apex.minecraft.apexcore.common.lib.resgen.model.ModelProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.state.BlockStateGenerator;
import xyz.apex.minecraft.apexcore.common.lib.resgen.state.MultiVariantBuilder;
import xyz.apex.minecraft.apexcore.common.lib.resgen.state.Variant;
import xyz.apex.minecraft.apexcore.common.lib.resgen.tag.TagsProvider;

import java.util.function.*;
import java.util.stream.Stream;

/**
 * Block Builder implementation.
 * <p>
 * Used to build and register Block entries.
 *
 * @param <O> Type of Registrar.
 * @param <T> Type of Block [Entry].
 * @param <P> Type of Parent.
 */
public final class BlockBuilder<O extends AbstractRegistrar<O>, T extends Block, P> extends AbstractBuilder<O, P, Block, T, BlockBuilder<O, T, P>, BlockEntry<T>> implements FeaturedBuilder<O, P, Block, T, BlockBuilder<O, T, P>, BlockEntry<T>>
{
    private final BlockFactory<T> blockFactory;
    private Supplier<BlockBehaviour.Properties> initialProperties = BlockBehaviour.Properties::of;
    private Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesModifier = Function.identity();

    @Nullable private Supplier<Supplier<BlockColor>> colorHandler = null;
    @Nullable private Supplier<Supplier<RenderType>> renderType = null;

    @ApiStatus.Internal
    public BlockBuilder(O registrar, P parent, String registrationName, BlockFactory<T> blockFactory)
    {
        super(registrar, parent, Registries.BLOCK, registrationName);

        this.blockFactory = blockFactory;

        defaultBlockState().defaultLootTable();
    }

    @Override
    protected void onRegister(T entry)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> {
            if(colorHandler != null)
                ColorHandlerHooks.get().registerBlockHandler(this::getEntry, colorHandler);
            if(renderType != null)
                RendererHooks.get().setBlockRenderType(() -> entry, renderType);
        });
    }

    /**
     * Registers a color handler for this Block.
     *
     * @param colorHandler Color handler to be registered.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> colorHandler(Supplier<Supplier<BlockColor>> colorHandler)
    {
        this.colorHandler = colorHandler;
        return this;
    }

    /**
     * Registers a RenderType for this Block.
     *
     * @param renderType RenderType for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> renderType(Supplier<Supplier<RenderType>> renderType)
    {
        this.renderType = renderType;
        return this;
    }

    /**
     * Returns new Builder constructing BlockEntity bound to this Block.
     *
     * @param blockEntityFactory Factory used to construct the BlockEntity.
     * @return New Builder constructing BlockEntity bound to this Block.
     * @param <B> Type of BlockEntity.
     */
    public <B extends BlockEntity> BlockEntityBuilder<O, B, BlockBuilder<O, T, P>> blockEntity(BlockEntityFactory<B> blockEntityFactory)
    {
        return registrar.blockEntity(self, registrationName(), blockEntityFactory).validBlock(asSupplier());
    }

    /**
     * Constructs and registers new default BlockEntity bound to this Block.
     *
     * @param blockEntityFactory Factory used to construct the BlockEntity.
     * @return This Builder.
     * @param <B> Type of BlockEntity.
     */
    public <B extends BlockEntity> BlockBuilder<O, T, P> defaultBlockEntity(BlockEntityFactory<B> blockEntityFactory)
    {
        return blockEntity(blockEntityFactory).build();
    }

    /**
     * Returns new Builder constructing Item bound to this Block.
     *
     * @param itemFactory Factory used to construct the Item.
     * @return New Builder constructing Item bound to this Block.
     * @param <I> Type of Item.
     */
    public <I extends Item> ItemBuilder<O, I, BlockBuilder<O, T, P>> item(BlockItemFactory<I, T> itemFactory)
    {
        return registrar.item(self, registrationName(), properties -> itemFactory.create(getEntry(), properties)).defaultBlockItemModel().noLang().noRecipe().noAdvancement();
    }

    /**
     * @return New Builder constructing Item bound to this Block.
     */
    public ItemBuilder<O, BlockItem, BlockBuilder<O, T, P>> item()
    {
        return item(BlockItem::new);
    }

    /**
     * Constructs and registers new default Item bound to this Block.
     *
     * @param itemFactory Factory used to construct the Item.
     * @return This Builder.
     * @param <I> Type of Item.
     */
    public <I extends Item> BlockBuilder<O, T, P> defaultItem(BlockItemFactory<I, T> itemFactory)
    {
        return item(itemFactory).build();
    }

    /**
     * Constructs and registers new default Item bound to this Block.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> defaultItem()
    {
        return item(BlockItem::new).build();
    }

    /**
     * Initial Block properties for this Block.
     *
     * @param initialProperties Initial Block properties.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> initialProperties(Supplier<BlockBehaviour.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return this;
    }

    /**
     * Copy Initial Block properties from another Block.
     *
     * @param block Block to copy the Initial properties from.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> copyInitialPropertiesFrom(Supplier<BlockBehaviour> block)
    {
        return initialProperties(() -> BlockBehaviour.Properties.copy(block.get()));
    }

    /**
     * Adds a new property modifier, used to modify the finalized Block properties.
     *
     * @param propertiesModifier Modifier used to modify the finalized Block properties.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> properties(UnaryOperator<BlockBehaviour.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    /**
     * Set the map color for this Block.
     *
     * @param mapColor Map color for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> mapColor(DyeColor mapColor)
    {
        return properties(properties -> properties.mapColor(mapColor));
    }

    /**
     * Set the map color for this Block.
     *
     * @param mapColor Map color for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> mapColor(MapColor mapColor)
    {
        return properties(properties -> properties.mapColor(mapColor));
    }

    /**
     * Set the map color for this Block.
     *
     * @param mapColor Map color for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> mapColor(Function<BlockState, MapColor> mapColor)
    {
        return properties(properties -> properties.mapColor(mapColor));
    }

    /**
     * Mark this Block as having no collision.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> noCollision()
    {
        return properties(BlockBehaviour.Properties::noCollission);
    }

    /**
     * Mark this Block as not occluding other nearby Blocks.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> noOcclusion()
    {
        return properties(BlockBehaviour.Properties::noOcclusion);
    }

    /**
     * Set this Blocks friction/slipperiness.
     *
     * @param friction Friction for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> friction(float friction)
    {
        return properties(properties -> properties.friction(friction));
    }

    /**
     * Set the speed factor for Entities walking on this Block.
     *
     * @param speedFactor Speed factor this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> speedFactor(float speedFactor)
    {
        return properties(properties -> properties.speedFactor(speedFactor));
    }

    /**
     * Set the jump scale for Entities jumping on this Block.
     *
     * @param jumpFactor Jump factor for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> jumpFactor(float jumpFactor)
    {
        return properties(properties -> properties.jumpFactor(jumpFactor));
    }

    /**
     * Set the sound type for this Block.
     *
     * @param soundType Sound type for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> sound(SoundType soundType)
    {
        return properties(properties -> properties.sound(soundType));
    }

    /**
     * Set the light level of this Block.
     *
     * @param lightEmission Light level for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> lightLevel(ToIntFunction<BlockState> lightEmission)
    {
        return properties(properties -> properties.lightLevel(lightEmission));
    }

    /**
     * Set the light level of this Block.
     *
     * @param lightEmission Light level for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> lightLevel(int lightEmission)
    {
        // if block is set up to use the LIT property, only show light emission if light
        // if block is not set up to use the LIT property, always show light emission
        return lightLevel(blockState -> blockState.getOptionalValue(BlockStateProperties.LIT).orElse(true) ? lightEmission : 0);
    }

    /**
     * Set the strength of this Block.
     *
     * @param destroyTime How long it takes to destroy this Block.
     * @param explosionResistance  Explosion resistance of this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> strength(float destroyTime, float explosionResistance)
    {
        return properties(properties -> properties.strength(destroyTime, explosionResistance));
    }

    /**
     * Mark this Block as breaking instantly.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> instabreak()
    {
        return properties(BlockBehaviour.Properties::instabreak);
    }

    /**
     * Set the strength of this Block.
     *
     * @param strength Strength of this block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> strength(float strength)
    {
        return properties(properties -> properties.strength(strength));
    }

    /**
     * Mark this Block as receiving random Block updates.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> randomTicks()
    {
        return properties(BlockBehaviour.Properties::randomTicks);
    }

    /**
     * Mark this Block as having a dynamic hitbox.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> dynamicShape()
    {
        return properties(BlockBehaviour.Properties::dynamicShape);
    }

    /**
     * Mark this Block as having no LootTable.
     * <p>
     * Also clears the currently registered LootTable generator.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> noLootTable()
    {
        return properties(BlockBehaviour.Properties::noLootTable).clearLootTable();
    }

    /**
     * Mark this Block as using the LootTable of another Block.
     * <p>
     * Also clears the currently registered LootTable generator.
     *
     * @param block Block to inherit LootTable from.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> dropsLike(Supplier<Block> block)
    {
        return properties(properties -> properties.dropsLike(block.get())).clearLootTable();
    }

    /**
     * Mark this Block as being ignited by Lava.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> ignitedByLava()
    {
        return properties(BlockBehaviour.Properties::ignitedByLava);
    }

    /**
     * Mark this Block as being a Liquid.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> liquid()
    {
        return properties(BlockBehaviour.Properties::liquid);
    }

    /**
     * Forces this Block to always be solid.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> forceSolidOn()
    {
        return properties(BlockBehaviour.Properties::forceSolidOn);
    }

    /**
     * Forces this Block to never be solid.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> forceSolidOff()
    {
        return properties(BlockBehaviour.Properties::forceSolidOff);
    }

    /**
     * Set the Piston PushReaction for this Block.
     *
     * @param pushReaction Piston PushReaction.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> pushReaction(PushReaction pushReaction)
    {
        return properties(properties -> properties.pushReaction(pushReaction));
    }

    /**
     * Mark this Block as being air-like.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> air()
    {
        return properties(BlockBehaviour.Properties::air);
    }

    /**
     * Set predicate used when attempting to spawn an Entity on this Block.
     *
     * @param predicate Entity spawn predicate.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate)
    {
        return properties(properties -> properties.isValidSpawn(predicate));
    }

    /**
     * Set predicate used when checking if this Block should conduct a Redstone signal or not.
     *
     * @param predicate Redstone conductor predicate.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> isRedstoneConductor(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isRedstoneConductor(predicate));
    }

    /**
     * Set predicate used when checking if this Block is suffocating.
     *
     * @param predicate Entity suffocation predicate.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> isSuffocating(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isSuffocating(predicate));
    }

    /**
     * Set predicate used when checking if this Block should block Players view.
     *
     * @param predicate Player view blocking predicate.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> isViewBlocking(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isViewBlocking(predicate));
    }

    /**
     * Set predicate used when checking if this Block should be post-processed.
     *
     * @param predicate PostProcess predicate.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> hasPostProcess(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.hasPostProcess(predicate));
    }

    /**
     * Set predicate used when checking if this Block should be rendered with emissive lighting.
     *
     * @param predicate Emissive lighting predicate.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> emissiveRendering(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.emissiveRendering(predicate));
    }

    /**
     * Mark this Block as requiring the correct Tool for LootTable drops.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> requiresCorrectToolForDrops()
    {
        return properties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
    }

    /**
     * Set how long it takes to harvest this Block.
     *
     * @param destroyTime Destroy time for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> destroyTime(float destroyTime)
    {
        return properties(properties -> properties.destroyTime(destroyTime));
    }

    /**
     * Set the explosion resistance for this Block.
     *
     * @param explosionResistance Explosion resistance for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> explosionResistance(float explosionResistance)
    {
        return properties(properties -> properties.explosionResistance(explosionResistance));
    }

    /**
     * Set the type of offsets applied to this Blocks model when being rendered.
     *
     * @param offsetType Offset type for this Block.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> offsetType(BlockBehaviour.OffsetType offsetType)
    {
        return properties(properties -> properties.offsetType(offsetType));
    }

    /**
     * Mark this Block as not spawning any terrain particles when being harvested.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> noTerrainParticles()
    {
        return properties(BlockBehaviour.Properties::noTerrainParticles);
    }

    /**
     * Set the NoteBlock Instrument type for this Block.
     *
     * @param instrument NoteBlock Instrument type.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> instrument(NoteBlockInstrument instrument)
    {
        return properties(properties -> properties.instrument(instrument));
    }

    /**
     * Mark this Block as being replaceable by other Blocks.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> replaceable()
    {
        return properties(BlockBehaviour.Properties::replaceable);
    }

    @Override
    public BlockBuilder<O, T, P> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
    }

    /**
     * Set the BlockState generator for this Block.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> blockState(BiFunction<ProviderLookup, BlockEntry<T>, BlockStateGenerator> listener)
    {
        return setProvider(ProviderTypes.BLOCK_STATES, (provider, lookup, entry) -> provider.add(listener.apply(lookup, entry)));
    }

    /**
     * Set the Default BlockState generator for this Block.
     *
     * @param modelFactory Factory used to lookup desired Block Model.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> defaultBlockState(TriFunction<ModelProvider, ProviderLookup, BlockEntry<T>, ModelBuilder> modelFactory)
    {
        return blockState((lookup, entry) -> MultiVariantBuilder
                .builder(
                        entry.value(),
                        Variant.variant().model(modelFactory.apply(lookup.lookup(ModelProvider.PROVIDER_TYPE), lookup, entry))
                )
        );
    }

    /**
     * Set the Default BlockState generator for this Block.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> defaultBlockState()
    {
        return defaultBlockState((provider, lookup, entry) -> {
            var assetPath = entry.getRegistryName().withPrefix("block/");
            return provider.withParent(assetPath, "block/cube_all").texture("all", assetPath);
        });
    }

    /**
     * Resister a set of Tags for this Block.
     *
     * @param tags Tags to be set.
     * @return This Builder.
     */
    @SafeVarargs
    public final BlockBuilder<O, T, P> tag(TagKey<Block>... tags)
    {
        return tag((provider, lookup, entry) -> Stream.of(tags).map(provider::tag).forEach(tag -> tag.addElement(entry.value())));
    }

    /**
     * Set the Tag generator for this Block.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> tag(RegistryProviderListener<TagsProvider<Block>, T, BlockEntry<T>> listener)
    {
        return setProvider(ProviderTypes.BLOCK_TAGS, listener);
    }

    /**
     * Clears the currently registered Tag generator.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> noTags()
    {
        return clearProvider(ProviderTypes.ITEM_TAGS);
    }

    /**
     * Set the Recipe generator for this Block.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> recipe(RegistryProviderListener<RecipeProvider, T, BlockEntry<T>> listener)
    {
        return setProvider(ProviderTypes.RECIPES, listener);
    }

    /**
     * Clears the currently registered Recipe generator.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> noRecipe()
    {
        return clearProvider(ProviderTypes.RECIPES);
    }

    /**
     * Set the Advancement generator for this Block.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> advancement(RegistryProviderListener<AdvancementProvider, T, BlockEntry<T>> listener)
    {
        return setProvider(ProviderTypes.ADVANCEMENTS, listener);
    }

    /**
     * Clears the currently registered Advancement generator.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> noAdvancement()
    {
        return clearProvider(ProviderTypes.ADVANCEMENTS);
    }

    /**
     * Set the LootTable generator for this Block.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> lootTable(BiConsumer<BlockLootProvider, BlockEntry<T>> listener)
    {
        return setLootTableProvider(LootTypes.BLOCKS, listener);
    }

    /**
     * Set the Default LootTable generator for this Block.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> defaultLootTable()
    {
        return lootTable((provider, entry) -> provider.dropSelf(entry.value()));
    }

    /**
     * Clears the currently registered LootTable generator.
     *
     * @return This Builder.
     */
    public BlockBuilder<O, T, P> clearLootTable()
    {
        return clearLootTableProvider(LootTypes.BLOCKS);
    }

    @Override
    protected BlockEntry<T> createRegistryEntry()
    {
        return new BlockEntry<>(registrar, registryKey);
    }

    @Override
    protected T createEntry()
    {
        return blockFactory.create(propertiesModifier.apply(initialProperties.get()));
    }

    @Override
    protected String getDescriptionId(BlockEntry<T> entry)
    {
        return entry.value().getDescriptionId();
    }
}
