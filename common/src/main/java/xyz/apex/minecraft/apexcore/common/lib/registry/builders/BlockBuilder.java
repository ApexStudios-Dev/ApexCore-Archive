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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.BlockEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.BlockEntityFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.BlockFactory;

import java.util.function.*;

/**
 * Block builder used to construct new block instances.
 *
 * @param <P> Type of parent element.
 * @param <T> Type of block.
 */
public final class BlockBuilder<P, T extends Block> extends AbstractBuilder<P, Block, T, BlockEntry<T>, BlockBuilder<P, T>> implements FeatureElementBuilder<P, Block, T, BlockEntry<T>, BlockBuilder<P, T>>
{
    public static final Supplier<BlockBehaviour.Properties> STONE_PROPERTIES = () -> BlockBehaviour.Properties.copy(Blocks.STONE);

    private Supplier<BlockBehaviour.Properties> initialProperties = STONE_PROPERTIES;
    private BlockPropertiesModifier propertiesModifier = BlockPropertiesModifier.identity();
    private final BlockFactory<T> blockFactory;

    BlockBuilder(P parent, BuilderManager builderManager, String registrationName, BlockFactory<T> blockFactory)
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
    public BlockBuilder<P, T> colorHandler(Supplier<Supplier<BlockColor>> colorHandler)
    {
        return addListener(value -> PhysicalSide.CLIENT.runWhenOn(() -> () -> Services.HOOKS.registerColorHandler().registerBlockColor(() -> value, colorHandler)));
    }

    /**
     * Registers a listener to set the render type for this block when it registers.
     *
     * @param renderType Render type to be set.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> renderType(Supplier<Supplier<RenderType>> renderType)
    {
        return addListener(value -> PhysicalSide.CLIENT.runWhenOn(() -> () -> Services.HOOKS.registerRenderer().setBlockRenderType(() -> value, renderType)));
    }

    /**
     * Returns new builder constructing block entity type bound to this block.
     *
     * @param blockEntityFactory Factory used to construct the block entity.
     * @param <B>                Type of block entity.
     * @return New block entity type builder bound to this block.
     */
    public <B extends BlockEntity> BlockEntityBuilder<BlockBuilder<P, T>, B> blockEntity(BlockEntityFactory<B> blockEntityFactory)
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
    public <B extends BlockEntity> BlockBuilder<P, T> simpleBlockEntity(BlockEntityFactory<B> blockEntityFactory)
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
    public <I extends Item> ItemBuilder<BlockBuilder<P, T>, I> item(BiFunction<T, Item.Properties, I> blockItemFactory)
    {
        return builderManager.item(self(), getRegistrationName(), properties -> blockItemFactory.apply(asSupplier().get(), properties));
    }

    /**
     * @return New block item builder bound to this block.
     */
    public ItemBuilder<BlockBuilder<P, T>, BlockItem> defaultItem()
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
    public <I extends Item> BlockBuilder<P, T> simpleItem(BiFunction<T, Item.Properties, I> blockItemFactory)
    {
        return item(blockItemFactory).end();
    }

    /**
     * Builds and registers basic block item bound to this block.
     *
     * @return This builder instance
     */
    public BlockBuilder<P, T> simpleItem()
    {
        return defaultItem().end();
    }

    /**
     * Initial item properties for this block.
     *
     * @param material Material for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> initialProperties(Material material)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material));
    }

    /**
     * Initial item properties for this block.
     *
     * @param material      Material for this block.
     * @param materialColor Material color for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> initialProperties(Material material, DyeColor materialColor)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColor));
    }

    /**
     * Initial item properties for this block.
     *
     * @param material      Material for this block.
     * @param materialColor Material color for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> initialProperties(Material material, MaterialColor materialColor)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColor));
    }

    /**
     * Initial item properties for this block.
     *
     * @param material      Material for this block.
     * @param materialColor Material color for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> initialProperties(Material material, Function<BlockState, MaterialColor> materialColor)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColor));
    }

    /**
     * Copy the properties from another block onto this blocks initial properties.
     *
     * @param block Block to copy properties from.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> copyInitialPropertiesFrom(Supplier<BlockBehaviour> block)
    {
        return initialProperties(() -> BlockBehaviour.Properties.copy(block.get()));
    }

    /**
     * Initial block properties for this block.
     *
     * @param initialProperties Initial block properties.
     * @return This builder instance
     */
    public BlockBuilder<P, T> initialProperties(Supplier<BlockBehaviour.Properties> initialProperties)
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
    public BlockBuilder<P, T> initialProperties(BlockPropertiesModifier initialProperties)
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
    public BlockBuilder<P, T> properties(BlockPropertiesModifier propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return self();
    }

    /**
     * Mark this block as having no collision.
     *
     * @return This builder instance
     */
    public BlockBuilder<P, T> noCollision()
    {
        return properties(BlockBehaviour.Properties::noCollission);
    }

    /**
     * Mark this block as not occluding other nearby blocks.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T> noOcclusion()
    {
        return properties(BlockBehaviour.Properties::noOcclusion);
    }

    /**
     * Set this blocks friction / slipperiness.
     *
     * @param friction Friction for this block.
     * @return This builder instance
     */
    public BlockBuilder<P, T> friction(float friction)
    {
        return properties(properties -> properties.friction(friction));
    }

    /**
     * Set the speed factor for entities walking on this block.
     *
     * @param speedFactor Speed factor for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> speedFactor(float speedFactor)
    {
        return properties(properties -> properties.speedFactor(speedFactor));
    }

    /**
     * Set the jump scale factor for entities jumping on this block.
     *
     * @param jumpFactor Jump factor for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> jumpFactor(float jumpFactor)
    {
        return properties(properties -> properties.jumpFactor(jumpFactor));
    }

    /**
     * Set the sound type of this block.
     *
     * @param soundType Sound type for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> sound(SoundType soundType)
    {
        return properties(properties -> properties.sound(soundType));
    }

    /**
     * Set the light level of this block.
     *
     * @param lightEmission Light level for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> lightLevel(ToIntFunction<BlockState> lightEmission)
    {
        return properties(properties -> properties.lightLevel(lightEmission));
    }

    /**
     * Set the light level of this block.
     *
     * @param lightEmission Light level for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> lightLevel(int lightEmission)
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
    public BlockBuilder<P, T> strength(float destroyTime, float explosionResistance)
    {
        return properties(properties -> properties.strength(destroyTime, explosionResistance));
    }

    /**
     * Mark this block as being instantly broken.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T> instabreak()
    {
        return properties(BlockBehaviour.Properties::instabreak);
    }

    /**
     * Set the strength of this block.
     *
     * @param strength Strength of this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> strength(float strength)
    {
        return properties(properties -> properties.strength(strength));
    }

    /**
     * Mark this block as receiving random block updates.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T> randomTicks()
    {
        return properties(BlockBehaviour.Properties::randomTicks);
    }

    /**
     * Mark this block as having a dynamic hitbox.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T> dynamicShape()
    {
        return properties(BlockBehaviour.Properties::dynamicShape);
    }

    /**
     * Mark this block as having no loot table.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T> noLootTable()
    {
        return properties(BlockBehaviour.Properties::noLootTable);
    }

    /**
     * Mark this block as using the loot table of another block.
     *
     * @param dropsLike Block to inherit loot table from.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> dropsLike(Supplier<? extends Block> dropsLike)
    {
        return properties(properties -> properties.dropsLike(dropsLike.get()));
    }

    /**
     * Mark this block as being an air block.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T> air()
    {
        return properties(BlockBehaviour.Properties::air);
    }

    /**
     * Set the predicate used when attempting to spawn entities on this block.
     *
     * @param isValidSpawn Entity spawn predicate.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn)
    {
        return properties(properties -> properties.isValidSpawn(isValidSpawn));
    }

    /**
     * Set the predicate used when checking if this block is suffocating.
     *
     * @param isSuffocating Predicate used when checking if suffocation should occur.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> isSuffocating(BlockBehaviour.StatePredicate isSuffocating)
    {
        return properties(properties -> properties.isSuffocating(isSuffocating));
    }

    /**
     * Set the predicate used when checking if this block should block players view.
     *
     * @param isViewBlocking Predicate used when checking if this block should block players view.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> isViewBlocking(BlockBehaviour.StatePredicate isViewBlocking)
    {
        return properties(properties -> properties.isViewBlocking(isViewBlocking));
    }

    /**
     * Set predicate used when checking if this block should be post processed.
     *
     * @param hasPostProcess Predicate used when checking if this block shoul be post processed.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> hasPostProcess(BlockBehaviour.StatePredicate hasPostProcess)
    {
        return properties(properties -> properties.hasPostProcess(hasPostProcess));
    }

    /**
     * Set predicate used when checking if this block should be rendered with emissive lighting.
     *
     * @param emissiveRendering Predicate when checking if this block should be rendered with emissive lighting.
     * @return This buildiner instance.
     */
    public BlockBuilder<P, T> emissiveRendering(BlockBehaviour.StatePredicate emissiveRendering)
    {
        return properties(properties -> properties.emissiveRendering(emissiveRendering));
    }

    /**
     * Mark this block as requiring the correct tool item to be used when harvesting it for drops to spawn.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T> requiresCorrectToolForDrops()
    {
        return properties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
    }

    /**
     * Set the material color of this block.
     *
     * @param materialColor Material color for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> materialColor(MaterialColor materialColor)
    {
        return properties(properties -> properties.color(materialColor));
    }

    /**
     * Set the material color of this block.
     *
     * @param materialColor Material color for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> materialColor(DyeColor materialColor)
    {
        return materialColor(materialColor.getMaterialColor());
    }

    /**
     * Set how long it takes to harvest this block.
     *
     * @param destroyTime Destroy time of this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> destroyTime(float destroyTime)
    {
        return properties(properties -> properties.destroyTime(destroyTime));
    }

    /**
     * Set the explosion resistance of this block.
     *
     * @param explosionResistance Explosion resistance of this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> explosionResistance(float explosionResistance)
    {
        return properties(properties -> properties.explosionResistance(explosionResistance));
    }

    /**
     * Set type of offsets to be applied to this blocks model when being rendered in level.
     *
     * @param offsetType Offset type for this block.
     * @return This builder instance.
     */
    public BlockBuilder<P, T> offsetType(BlockBehaviour.OffsetType offsetType)
    {
        return properties(properties -> properties.offsetType(offsetType));
    }

    /**
     * Mark this block as spawning no particles when being harvested.
     *
     * @return This builder instance.
     */
    public BlockBuilder<P, T> noParticlesOnBreak()
    {
        return properties(BlockBehaviour.Properties::noParticlesOnBreak);
    }

    @Override
    public BlockBuilder<P, T> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        if(getParent() instanceof FeatureElementBuilder<?, ?, ?, ?, ?> feature)
            feature.requiredFeatures(requiredFeatures);
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
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
