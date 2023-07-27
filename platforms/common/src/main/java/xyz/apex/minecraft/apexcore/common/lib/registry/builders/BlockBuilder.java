package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.ColorHandlerHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RendererHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.BlockEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.BlockEntityFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.BlockFactory;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;

import java.util.function.*;

/**
 * Block builder used to construct new block instances.
 *
 * @param <P> Type of parent element.
 * @param <T> Type of block.
 * @param <M> Type of builder manager.
 */
public final class BlockBuilder<P, T extends Block, M extends BuilderManager<M>> extends AbstractBuilder<P, Block, T, BlockEntry<T>, BlockBuilder<P, T, M>, M> implements FeatureElementBuilder<P, Block, T, BlockEntry<T>, BlockBuilder<P, T, M>, M>
{
    public static final Supplier<BlockBehaviour.Properties> STONE_PROPERTIES = () -> BlockBehaviour.Properties.copy(Blocks.STONE);

    private Supplier<BlockBehaviour.Properties> initialProperties = STONE_PROPERTIES;
    private BlockPropertiesModifier propertiesModifier = BlockPropertiesModifier.identity();
    private final BlockFactory<T> blockFactory;

    BlockBuilder(P parent, M builderManager, String registrationName, BlockFactory<T> blockFactory)
    {
        super(parent, builderManager, Registries.BLOCK, registrationName, BlockEntry::new);

        this.blockFactory = blockFactory;
    }

    @Override
    protected T createObject()
    {
        return blockFactory.create(propertiesModifier.modify(initialProperties.get()));
    }

    /**
     * Registers a color handler for this block.
     *
     * @param colorHandler Color handler to be registered.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> colorHandler(Supplier<Supplier<BlockColor>> colorHandler)
    {
        return addListener(value -> PhysicalSide.CLIENT.runWhenOn(() -> () -> ColorHandlerHooks.get().registerBlockHandler(() -> value, colorHandler)));
    }

    /**
     * Registers a listener to set the render type for this block when it registers.
     *
     * @param renderType Render type to be set.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> renderType(Supplier<Supplier<RenderType>> renderType)
    {
        return addListener(value -> PhysicalSide.CLIENT.runWhenOn(() -> () -> RendererHooks.get().setBlockRenderType(() -> value, renderType)));
    }

    /**
     * Returns new builder constructing block entity type bound to this block.
     *
     * @param blockEntityFactory Factory used to construct the block entity.
     * @param <B>                Type of block entity.
     * @return New block entity type builder bound to this block.
     */
    public <B extends BlockEntity> BlockEntityBuilder<BlockBuilder<P, T, M>, B, M> blockEntity(BlockEntityFactory<B> blockEntityFactory)
    {
        return builderManager.blockEntity(this, getRegistrationName(), blockEntityFactory).validBlock(asSupplier());
    }

    /**
     * Builds and registers basic block entity type bound to this block.
     *
     * @param blockEntityFactory Factory used when constructing the block entity type.
     * @param <B>                Type of block entity.
     * @return This builder instance.
     */
    public <B extends BlockEntity> BlockBuilder<P, T, M> simpleBlockEntity(BlockEntityFactory<B> blockEntityFactory)
    {
        return blockEntity(blockEntityFactory).end();
    }

    /**
     * Returns new builder constructing item bound to this block.
     *
     * @param blockItemFactory Factory used to construct the block item.
     * @param <I>              Type of block item.
     * @return New block item builder bound to this block.
     */
    public <I extends Item> ItemBuilder<BlockBuilder<P, T, M>, I, M> item(BiFunction<T, Item.Properties, I> blockItemFactory)
    {
        return builderManager.item(self(), getRegistrationName(), properties -> blockItemFactory.apply(asSupplier().get(), properties))
                             .defaultBlockItemModel()
                             .noLang()
        ;
    }

    /**
     * @return New block item builder bound to this block.
     */
    public ItemBuilder<BlockBuilder<P, T, M>, BlockItem, M> defaultItem()
    {
        return item(BlockItem::new);
    }

    /**
     * Builds and registers basic block item bound to this block.
     *
     * @param blockItemFactory Item factory used when constructing the item.
     * @param <I>              Type of item.
     * @return This builder instance.
     */
    public <I extends Item> BlockBuilder<P, T, M> simpleItem(BiFunction<T, Item.Properties, I> blockItemFactory)
    {
        return item(blockItemFactory).end();
    }

    /**
     * Builds and registers basic block item bound to this block.
     *
     * @return This builder instance
     */
    public BlockBuilder<P, T, M> simpleItem()
    {
        return defaultItem().end();
    }

    /**
     * Copy the properties from another block onto this blocks initial properties.
     *
     * @param block Block to copy properties from.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> copyInitialPropertiesFrom(Supplier<BlockBehaviour> block)
    {
        return initialProperties(() -> BlockBehaviour.Properties.copy(block.get()));
    }

    /**
     * Initial block properties for this block.
     *
     * @param initialProperties Initial block properties.
     * @return This builder instance
     */
    public BlockBuilder<P, T, M> initialProperties(Supplier<BlockBehaviour.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return self();
    }

    /**
     * Initial block properties for this block.
     *
     * @param initialProperties Initial block properties.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> initialProperties(BlockPropertiesModifier initialProperties)
    {
        this.initialProperties = () -> initialProperties.modify(STONE_PROPERTIES.get());
        return self();
    }

    /**
     * @return Initial block properties for this block.
     */
    public Supplier<BlockBehaviour.Properties> initialProperties()
    {
        return initialProperties;
    }

    /**
     * @return Modifier used to construct finalized & modified block properties.
     */
    public BlockPropertiesModifier blockPropertiesModifier()
    {
        return propertiesModifier;
    }

    /**
     * Add a new property modifier to modify the finalized block properties.
     *
     * @param propertiesModifier Modifier used to modify the finalized block properties.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> properties(BlockPropertiesModifier propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return self();
    }

    /**
     * Set the map color of this block.
     *
     * @param mapColor Map color for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> mapColor(DyeColor mapColor)
    {
        return properties(properties -> properties.mapColor(mapColor));
    }

    /**
     * Set the map color of this block.
     *
     * @param mapColor Map color for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> mapColor(MapColor mapColor)
    {
        return properties(properties -> properties.mapColor(mapColor));
    }

    /**
     * Set the map color of this block.
     *
     * @param mapColor Map color for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> mapColor(Function<BlockState, MapColor> mapColor)
    {
        return properties(properties -> properties.mapColor(mapColor));
    }

    /**
     * Mark this block as having no collision.
     *
     * @return This builder instance
     */
    public BlockBuilder<P, T, M> noCollision()
    {
        return properties(BlockBehaviour.Properties::noCollission);
    }

    /**
     * Mark this block as not occluding other nearby blocks.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> noOcclusion()
    {
        return properties(BlockBehaviour.Properties::noOcclusion);
    }

    /**
     * Set this blocks friction / slipperiness.
     *
     * @param friction Friction for this block.
     * @return This builder instance
     */
    public BlockBuilder<P, T, M> friction(float friction)
    {
        return properties(properties -> properties.friction(friction));
    }

    /**
     * Set the speed factor for entities walking on this block.
     *
     * @param speedFactor Speed factor for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> speedFactor(float speedFactor)
    {
        return properties(properties -> properties.speedFactor(speedFactor));
    }

    /**
     * Set the jump scale factor for entities jumping on this block.
     *
     * @param jumpFactor Jump factor for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> jumpFactor(float jumpFactor)
    {
        return properties(properties -> properties.jumpFactor(jumpFactor));
    }

    /**
     * Set the sound type of this block.
     *
     * @param soundType Sound type for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> sound(SoundType soundType)
    {
        return properties(properties -> properties.sound(soundType));
    }

    /**
     * Set the light level of this block.
     *
     * @param lightEmission Light level for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> lightLevel(ToIntFunction<BlockState> lightEmission)
    {
        return properties(properties -> properties.lightLevel(lightEmission));
    }

    /**
     * Set the light level of this block.
     *
     * @param lightEmission Light level for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> lightLevel(int lightEmission)
    {
        return lightLevel(blockState -> lightEmission);
    }

    /**
     * Set the strength of this block.
     *
     * @param destroyTime         How long it takes to destroy this block.
     * @param explosionResistance Explosion resistance of this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> strength(float destroyTime, float explosionResistance)
    {
        return properties(properties -> properties.strength(destroyTime, explosionResistance));
    }

    /**
     * Mark this block as being instantly broken.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> instabreak()
    {
        return properties(BlockBehaviour.Properties::instabreak);
    }

    /**
     * Set the strength of this block.
     *
     * @param strength Strength of this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> strength(float strength)
    {
        return properties(properties -> properties.strength(strength));
    }

    /**
     * Mark this block as receiving random block updates.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> randomTicks()
    {
        return properties(BlockBehaviour.Properties::randomTicks);
    }

    /**
     * Mark this block as having a dynamic hitbox.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> dynamicShape()
    {
        return properties(BlockBehaviour.Properties::dynamicShape);
    }

    /**
     * Mark this block as having no loot table.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> noLootTable()
    {
        return properties(BlockBehaviour.Properties::noLootTable);
    }

    /**
     * Mark this block as using the loot table of another block.
     *
     * @param dropsLike Block to inherit loot table from.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> dropsLike(Supplier<? extends Block> dropsLike)
    {
        return properties(properties -> properties.dropsLike(dropsLike.get()));
    }

    /**
     * Marks this block as being ignited by lava.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> ignitedByLava()
    {
        return properties(BlockBehaviour.Properties::ignitedByLava);
    }

    /**
     * Marks this block as being a liquid.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> liquid()
    {
        return properties(BlockBehaviour.Properties::liquid);
    }

    /**
     * Forces this block to always be solid.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> forceSolidOn()
    {
        return properties(BlockBehaviour.Properties::forceSolidOn);
    }

    /**
     * Forces this block to never be solid.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> forceSolidOff()
    {
        return properties(BlockBehaviour.Properties::forceSolidOff);
    }

    /**
     * Sets this blocks piston push reaction.
     *
     * @param pushReaction Piston push reaction.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> pushReaction(PushReaction pushReaction)
    {
        return properties(properties -> properties.pushReaction(pushReaction));
    }

    /**
     * Mark this block as being an air block.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> air()
    {
        return properties(BlockBehaviour.Properties::air);
    }

    /**
     * Set the predicate used when attempting to spawn entities on this block.
     *
     * @param isValidSpawn Entity spawn predicate.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn)
    {
        return properties(properties -> properties.isValidSpawn(isValidSpawn));
    }

    /**
     * Set the predicate used when checking if this block is suffocating.
     *
     * @param isSuffocating Predicate used when checking if suffocation should occur.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> isSuffocating(BlockBehaviour.StatePredicate isSuffocating)
    {
        return properties(properties -> properties.isSuffocating(isSuffocating));
    }

    /**
     * Set the predicate used when checking if this block should block players view.
     *
     * @param isViewBlocking Predicate used when checking if this block should block players view.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> isViewBlocking(BlockBehaviour.StatePredicate isViewBlocking)
    {
        return properties(properties -> properties.isViewBlocking(isViewBlocking));
    }

    /**
     * Set predicate used when checking if this block should be post processed.
     *
     * @param hasPostProcess Predicate used when checking if this block shoul be post processed.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> hasPostProcess(BlockBehaviour.StatePredicate hasPostProcess)
    {
        return properties(properties -> properties.hasPostProcess(hasPostProcess));
    }

    /**
     * Set predicate used when checking if this block should be rendered with emissive lighting.
     *
     * @param emissiveRendering Predicate when checking if this block should be rendered with emissive lighting.
     * @return This buildiner instance.
     */
    public BlockBuilder<P, T, M> emissiveRendering(BlockBehaviour.StatePredicate emissiveRendering)
    {
        return properties(properties -> properties.emissiveRendering(emissiveRendering));
    }

    /**
     * Mark this block as requiring the correct tool item to be used when harvesting it for drops to spawn.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> requiresCorrectToolForDrops()
    {
        return properties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
    }

    /**
     * Set how long it takes to harvest this block.
     *
     * @param destroyTime Destroy time of this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> destroyTime(float destroyTime)
    {
        return properties(properties -> properties.destroyTime(destroyTime));
    }

    /**
     * Set the explosion resistance of this block.
     *
     * @param explosionResistance Explosion resistance of this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> explosionResistance(float explosionResistance)
    {
        return properties(properties -> properties.explosionResistance(explosionResistance));
    }

    /**
     * Set type of offsets to be applied to this blocks model when being rendered in level.
     *
     * @param offsetType Offset type for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> offsetType(BlockBehaviour.OffsetType offsetType)
    {
        return properties(properties -> properties.offsetType(offsetType));
    }

    /**
     * Mark this block as spawning no particles when being harvested.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> noParticlesOnBreak()
    {
        return properties(BlockBehaviour.Properties::noParticlesOnBreak);
    }

    /**
     * Sets this blocks note block instrument.
     *
     * @param instrument Note block instrument for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> instrument(NoteBlockInstrument instrument)
    {
        return properties(properties -> properties.instrument(instrument));
    }

    /**
     * Marks this block as being replaceable by other blocks.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T, M> replaceable()
    {
        return properties(BlockBehaviour.Properties::replaceable);
    }

    // TODO: Resource Gen providers [ blockstate, loot table, recipe, tag ]

    @Override
    public BlockBuilder<P, T, M> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        if(getParent() instanceof FeatureElementBuilder<?, ?, ?, ?, ?, ?> feature)
            feature.requiredFeatures(requiredFeatures);
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
    }

    @Override
    protected String getDescriptionId(ProviderType.RegistryContext<Block, T> context)
    {
        return context.value().getDescriptionId();
    }

    /**
     * Functional modifier class used to keep track of block property modifications.
     */
    @FunctionalInterface
    interface BlockPropertiesModifier extends UnaryOperator<BlockBehaviour.Properties>
    {
        /**
         * Returns the given properties after being applied with the current set of modifications.
         *
         * @param properties Properties to be modified.
         * @return Properties after being applied with the current set of modifications.
         */
        default BlockBehaviour.Properties modify(BlockBehaviour.Properties properties)
        {
            return apply(properties);
        }

        /**
         * Append a new properties modifier onto the current stack.
         *
         * @param modifier Properties modifier to be appended.
         * @return New properties modifier with the given one appended onto the end of the stack.
         */
        default BlockPropertiesModifier andThen(BlockPropertiesModifier modifier)
        {
            return properties -> modifier.apply(apply(properties));
        }

        /**
         * @return Properties modifier applying no modifications.
         */
        static BlockPropertiesModifier identity()
        {
            return properties -> properties;
        }
    }
}
