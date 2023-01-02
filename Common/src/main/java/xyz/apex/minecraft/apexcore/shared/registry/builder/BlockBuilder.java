package xyz.apex.minecraft.apexcore.shared.registry.builder;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.VoxelShape;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.shared.registry.FlammabilityRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.HitBoxRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntry;
import xyz.apex.minecraft.apexcore.shared.util.Properties;
import xyz.apex.minecraft.apexcore.shared.util.function.TriFunction;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;

public final class BlockBuilder<R extends Block, O extends AbstractRegistrar<O>, P> extends AbstractBuilder<Block, R, O, P, BlockBuilder<R, O, P>>
{
    private final Factory<R> factory;
    private Supplier<BlockBehaviour.Properties> initialProperties = Properties.BLOCK_STONE;
    private Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesModifier = Function.identity();
    @Nullable private ItemBuilder<?, O, BlockBuilder<R, O, P>> itemBuilder;
    private int burnOdds = -1;
    private int igniteOdds = -1;
    private Supplier<Supplier<RenderType>> renderTypeSupplier = () -> () -> null;
    @Nullable private Supplier<VoxelShape> hitbox;
    private TriFunction<VoxelShape, R, BlockState, VoxelShape> hitboxModifier = (current, block, blockState) -> current;
    private Supplier<Supplier<BlockColor>> blockColorSupplier = () -> () -> null;
    private int burnTime = -1;
    @Nullable private BlockEntityBuilder<?, O, BlockBuilder<R, O, P>> blockEntityBuilder;

    public BlockBuilder(O owner, P parent, String registrationName, Factory<R> factory)
    {
        super(owner, parent, Registries.BLOCK, registrationName);

        this.factory = factory;

        simpleItem();

        onRegister(block -> {
            var registryName =  getRegistryName();

            if(burnTime != -1) FuelRegistry.register(burnTime, block);
            if(burnOdds != -1 && igniteOdds != -1) FlammabilityRegistry.register(registryName, burnOdds, igniteOdds);
            if(hitbox != null) HitBoxRegistry.register(registryName, hitbox, hitboxModifier);

            EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
                var renderType = renderTypeSupplier.get().get();
                var blockColor = blockColorSupplier.get().get();

                if(renderType != null) RenderTypeRegistry.register(renderType, block);
                if(blockColor != null) ColorHandlerRegistry.registerBlockColors(blockColor, block);
            });
        });
    }

    public <B extends BlockEntity> BlockEntityBuilder<B, O, BlockBuilder<R, O, P>> blockEntity(BlockEntityBuilder.Factory<B> blockEntityFactory)
    {
        var blockEntityBuilder = new BlockEntityBuilder<>(owner, this, getRegistrationName(), blockEntityFactory).validBlock(safeSupplier);
        this.blockEntityBuilder = blockEntityBuilder;
        return blockEntityBuilder;
    }

    public <B extends BlockEntity> BlockBuilder<R, O, P> simpleBlockEntity(BlockEntityBuilder.Factory<B> blockEntityFactory)
    {
        return blockEntity(blockEntityFactory).build();
    }

    public BlockBuilder<R, O, P> noBlockEntity()
    {
        blockEntityBuilder = null;
        return this;
    }

    public BlockBuilder<R, O, P> burnTime(int burnTime)
    {
        this.burnTime = burnTime;
        return this;
    }

    public BlockBuilder<R, O, P> blockColor(Supplier<Supplier<BlockColor>> blockColorSupplier)
    {
        this.blockColorSupplier = blockColorSupplier;
        return this;
    }

    public BlockBuilder<R, O, P> hitbox(Supplier<VoxelShape> hitbox)
    {
        return hitbox(hitbox, (current, block, blockState) -> current);
    }

    public BlockBuilder<R, O, P> hitbox(Supplier<VoxelShape> hitbox, TriFunction<VoxelShape, R, BlockState, VoxelShape> hitboxModifier)
    {
        this.hitbox = hitbox;
        this.hitboxModifier = hitboxModifier;
        return this;
    }

    public BlockBuilder<R, O, P> flammability(int burnOdds, int igniteOdds)
    {
        this.burnOdds = burnOdds;
        this.igniteOdds = igniteOdds;
        return this;
    }

    // NOTE: Forge allows registering this directly in the block/item models and should be preferred
    public BlockBuilder<R, O, P> renderType(Supplier<Supplier<RenderType>> renderTypeSupplier)
    {
        this.renderTypeSupplier = renderTypeSupplier;
        return this;
    }

    public <I extends Item> ItemBuilder<I, O, BlockBuilder<R, O, P>> item(ItemFactory<I, R> itemFactory)
    {
        var itemBuilder = new ItemBuilder<>(owner, this, getRegistrationName(), itemFactory.toItemFactory(safeSupplier));
        this.itemBuilder = itemBuilder;
        return itemBuilder;
    }

    public <I extends Item> BlockBuilder<R, O, P> simpleItem(ItemFactory<I, R> itemFactory)
    {
        return item(itemFactory).build();
    }

    public BlockBuilder<R, O, P> simpleItem()
    {
        return simpleItem(BlockItem::new);
    }

    public BlockBuilder<R, O, P> noItem()
    {
        itemBuilder = null;
        return this;
    }

    public BlockBuilder<R, O, P> initialProperties(Supplier<BlockBehaviour.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return this;
    }

    public BlockBuilder<R, O, P> initialProperties(BlockBehaviour.Properties properties)
    {
        return initialProperties(() -> properties);
    }

    public BlockBuilder<R, O, P> copyFrom(Supplier<? extends Block> block)
    {
        return initialProperties(() -> BlockBehaviour.Properties.copy(block.get()));
    }

    public BlockBuilder<R, O, P> initialProperties(Material material)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material));
    }

    public BlockBuilder<R, O, P> initialProperties(Material material, DyeColor materialColor)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColor));
    }

    public BlockBuilder<R, O, P> initialProperties(Material material, MaterialColor materialColor)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColor));
    }

    public BlockBuilder<R, O, P> initialProperties(Material material, Function<BlockState, MaterialColor> materialColorFunction)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColorFunction));
    }

    public BlockBuilder<R, O, P> properties(UnaryOperator<BlockBehaviour.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public BlockBuilder<R, O, P> noCollission()
    {
        return properties(BlockBehaviour.Properties::noCollission);
    }

    public BlockBuilder<R, O, P> noOcclusion()
    {
        return properties(BlockBehaviour.Properties::noOcclusion);
    }

    public BlockBuilder<R, O, P> friction(float friction)
    {
        return properties(properties -> properties.friction(friction));
    }

    public BlockBuilder<R, O, P> speedFactor(float speedFactor)
    {
        return properties(properties -> properties.speedFactor(speedFactor));
    }

    public BlockBuilder<R, O, P> jumpFactor(float jumpFactor)
    {
        return properties(properties -> properties.jumpFactor(jumpFactor));
    }

    public BlockBuilder<R, O, P> sound(SoundType soundType)
    {
        return properties(properties -> properties.sound(soundType));
    }

    public BlockBuilder<R, O, P> lightLevel(ToIntFunction<BlockState> lightLevelFunction)
    {
        return properties(properties -> properties.lightLevel(lightLevelFunction));
    }

    public BlockBuilder<R, O, P> strength(float destroyTime, float explosionResistance)
    {
        return properties(properties -> properties.strength(destroyTime, explosionResistance));
    }

    public BlockBuilder<R, O, P> instabreak()
    {
        return properties(BlockBehaviour.Properties::instabreak);
    }

    public BlockBuilder<R, O, P> strength(float strength)
    {
        return properties(properties -> properties.strength(strength));
    }

    public BlockBuilder<R, O, P> randomTicks()
    {
        return properties(BlockBehaviour.Properties::randomTicks);
    }

    public BlockBuilder<R, O, P> dynamicShape()
    {
        return properties(BlockBehaviour.Properties::dynamicShape);
    }

    public BlockBuilder<R, O, P> noLootTable()
    {
        return properties(BlockBehaviour.Properties::noLootTable);
    }

    @Deprecated
    public BlockBuilder<R, O, P> dropsLike(Block block)
    {
        return properties(properties -> properties.dropsLike(block));
    }

    public BlockBuilder<R, O, P> dropsLike(Supplier<? extends Block> block)
    {
        return properties(properties -> properties.dropsLike(block.get()));
    }

    public BlockBuilder<R, O, P> air()
    {
        return properties(BlockBehaviour.Properties::air);
    }

    public BlockBuilder<R, O, P> isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate)
    {
        return properties(properties -> properties.isValidSpawn(predicate));
    }

    public BlockBuilder<R, O, P> isRedstoneConductor(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isRedstoneConductor(predicate));
    }

    public BlockBuilder<R, O, P> isSuffocating(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isSuffocating(predicate));
    }

    public BlockBuilder<R, O, P> isViewBlocking(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isViewBlocking(predicate));
    }

    public BlockBuilder<R, O, P> hasPostProcess(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.hasPostProcess(predicate));
    }

    public BlockBuilder<R, O, P> emissiveRendering(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.emissiveRendering(predicate));
    }

    public BlockBuilder<R, O, P> requiresCorrectToolForDrops()
    {
        return properties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
    }

    public BlockBuilder<R, O, P> color(MaterialColor materialColor)
    {
        return properties(properties -> properties.color(materialColor));
    }

    public BlockBuilder<R, O, P> destroyTime(float destroyTime)
    {
        return properties(properties -> properties.destroyTime(destroyTime));
    }

    public BlockBuilder<R, O, P> explosionResistance(float explosionResistance)
    {
        return properties(properties -> properties.explosionResistance(explosionResistance));
    }

    public BlockBuilder<R, O, P> offsetType(BlockBehaviour.OffsetType offsetType)
    {
        return properties(properties -> properties.offsetType(offsetType));
    }

    public BlockBuilder<R, O, P> offsetType(Function<BlockState, BlockBehaviour.OffsetType> offsetTypeFunction)
    {
        return properties(properties -> properties.offsetType(offsetTypeFunction));
    }

    public BlockBuilder<R, O, P> noParticlesOnBreak()
    {
        return properties(BlockBehaviour.Properties::noParticlesOnBreak);
    }

    public BlockBuilder<R, O, P> requiredFeatures(FeatureFlag... flags)
    {
        return properties(properties -> properties.requiredFeatures(flags));
    }

    @Override
    protected R createEntry()
    {
        return factory.create(propertiesModifier.apply(initialProperties.get()));
    }

    @Override
    protected BlockEntry<R> createRegistryEntry()
    {
        return new BlockEntry<>(owner, registryKey);
    }

    @Override
    public BlockEntry<R> register()
    {
        var result = (BlockEntry<R>) super.register();
        if(itemBuilder != null) itemBuilder.register();
        if(blockEntityBuilder != null) blockEntityBuilder.register();
        return result;
    }

    @FunctionalInterface
    public interface Factory<T extends Block>
    {
        T create(BlockBehaviour.Properties properties);
    }

    @FunctionalInterface
    public interface ItemFactory<I extends Item, B extends Block>
    {
        I create(B block, Item.Properties properties);

        default ItemBuilder.Factory<I> toItemFactory(Supplier<B> block)
        {
            return properties -> create(block.get(), properties);
        }
    }
}
