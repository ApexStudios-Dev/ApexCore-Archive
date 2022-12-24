package xyz.apex.minecraft.apexcore.shared.registry.builders;

import org.apache.commons.lang3.Validate;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import xyz.apex.minecraft.apexcore.shared.platform.EnvironmentExecutor;
import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.registry.FlammabilityRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntry;
import xyz.apex.minecraft.apexcore.shared.util.Properties;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;

public final class BlockBuilder<T extends Block> extends AbstractBuilder<Block, T, BlockBuilder<T>, BlockEntry<T>>
{
    private final BlockFactory<T> factory;

    private Supplier<BlockBehaviour.Properties> initialProperties = Properties.BLOCK_STONE;
    private Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesModifier = Function.identity();
    private int burnOdds = -1;
    private int igniteOdds = -1;
    private Supplier<Supplier<RenderType>> renderTypeSupplier = () -> () -> null;

    BlockBuilder(String modId, String registryName, BlockFactory<T> factory)
    {
        super(Registries.BLOCK, modId, registryName, BlockEntry::new);

        this.factory = factory;

        simpleItem();
    }

    @Override
    protected void onRegister(T value)
    {
        super.onRegister(value);

        if(burnOdds != -1 && igniteOdds != -1) FlammabilityRegistry.register(getInternalName(), burnOdds, igniteOdds);

        EnvironmentExecutor.runForClient(() -> () -> Platform.INSTANCE.registries().registerRenderType(getModId(), value, renderTypeSupplier));
    }

    @Override
    protected T construct()
    {
        return factory.create(propertiesModifier.apply(initialProperties.get()));
    }

    public BlockBuilder<T> flammability(int burnOdds, int igniteOdds)
    {
        Validate.inclusiveBetween(0, 100, burnOdds, "Burn odds must be between 0-100 (inclusive)");
        Validate.inclusiveBetween(0, 100, igniteOdds, "Burn odds must be between 0-100 (inclusive)");

        this.burnOdds = burnOdds;
        this.igniteOdds = igniteOdds;
        return this;
    }

    public BlockBuilder<T> renderType(Supplier<Supplier<RenderType>> renderTypeSupplier)
    {
        this.renderTypeSupplier = renderTypeSupplier;
        return this;
    }

    public <I extends Item> BlockBuilder<T> item(BlockItemFactory<I, T> itemFactory, Function<ItemBuilder<I>, ItemBuilder<I>> action)
    {
        return child(Registries.ITEM, (modId, registryName) -> action.apply(ItemBuilders.builder(modId, registryName, properties -> itemFactory.create(asSupplier().get(), properties))));
    }

    public BlockBuilder<T> item(Function<ItemBuilder<Item>, ItemBuilder<Item>> action)
    {
        return item(BlockItem::new, action);
    }

    public <I extends Item> BlockBuilder<T> item(BlockItemFactory<I, T> itemFactory)
    {
        return item(itemFactory, Function.identity());
    }

    public BlockBuilder<T> simpleItem()
    {
        return item(BlockItem::new, Function.identity());
    }

    public BlockBuilder<T> noItem()
    {
        return removeChild(Registries.ITEM);
    }

    public BlockBuilder<T> initialProperties(Supplier<BlockBehaviour.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return this;
    }

    public BlockBuilder<T> initialProperties(BlockBehaviour.Properties properties)
    {
        return initialProperties(() -> properties);
    }

    public BlockBuilder<T> copyFrom(Supplier<? extends Block> block)
    {
        return initialProperties(() -> BlockBehaviour.Properties.copy(block.get()));
    }

    public BlockBuilder<T> initialProperties(Material material)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material));
    }

    public BlockBuilder<T> initialProperties(Material material, DyeColor materialColor)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColor));
    }

    public BlockBuilder<T> initialProperties(Material material, MaterialColor materialColor)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColor));
    }

    public BlockBuilder<T> initialProperties(Material material, Function<BlockState, MaterialColor> materialColorFunction)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColorFunction));
    }

    public BlockBuilder<T> properties(UnaryOperator<BlockBehaviour.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public BlockBuilder<T> noCollission()
    {
        return properties(BlockBehaviour.Properties::noCollission);
    }

    public BlockBuilder<T> noOcclusion()
    {
        return properties(BlockBehaviour.Properties::noOcclusion);
    }

    public BlockBuilder<T> friction(float friction)
    {
        return properties(properties -> properties.friction(friction));
    }

    public BlockBuilder<T> speedFactor(float speedFactor)
    {
        return properties(properties -> properties.speedFactor(speedFactor));
    }

    public BlockBuilder<T> jumpFactor(float jumpFactor)
    {
        return properties(properties -> properties.jumpFactor(jumpFactor));
    }

    public BlockBuilder<T> sound(SoundType soundType)
    {
        return properties(properties -> properties.sound(soundType));
    }

    public BlockBuilder<T> lightLevel(ToIntFunction<BlockState> lightLevelFunction)
    {
        return properties(properties -> properties.lightLevel(lightLevelFunction));
    }

    public BlockBuilder<T> strength(float destroyTime, float explosionResistance)
    {
        return properties(properties -> properties.strength(destroyTime, explosionResistance));
    }

    public BlockBuilder<T> instabreak()
    {
        return properties(BlockBehaviour.Properties::instabreak);
    }

    public BlockBuilder<T> strength(float strength)
    {
        return properties(properties -> properties.strength(strength));
    }

    public BlockBuilder<T> randomTicks()
    {
        return properties(BlockBehaviour.Properties::randomTicks);
    }

    public BlockBuilder<T> dynamicShape()
    {
        return properties(BlockBehaviour.Properties::dynamicShape);
    }

    public BlockBuilder<T> noLootTable()
    {
        return properties(BlockBehaviour.Properties::noLootTable);
    }

    @Deprecated
    public BlockBuilder<T> dropsLike(Block block)
    {
        return properties(properties -> properties.dropsLike(block));
    }

    public BlockBuilder<T> dropsLike(Supplier<? extends Block> block)
    {
        return properties(properties -> properties.dropsLike(block.get()));
    }

    public BlockBuilder<T> air()
    {
        return properties(BlockBehaviour.Properties::air);
    }

    public BlockBuilder<T> isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate)
    {
        return properties(properties -> properties.isValidSpawn(predicate));
    }

    public BlockBuilder<T> isRedstoneConductor(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isRedstoneConductor(predicate));
    }

    public BlockBuilder<T> isSuffocating(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isSuffocating(predicate));
    }

    public BlockBuilder<T> isViewBlocking(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isViewBlocking(predicate));
    }

    public BlockBuilder<T> hasPostProcess(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.hasPostProcess(predicate));
    }

    public BlockBuilder<T> emissiveRendering(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.emissiveRendering(predicate));
    }

    public BlockBuilder<T> requiresCorrectToolForDrops()
    {
        return properties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
    }

    public BlockBuilder<T> color(MaterialColor materialColor)
    {
        return properties(properties -> properties.color(materialColor));
    }

    public BlockBuilder<T> destroyTime(float destroyTime)
    {
        return properties(properties -> properties.destroyTime(destroyTime));
    }

    public BlockBuilder<T> explosionResistance(float explosionResistance)
    {
        return properties(properties -> properties.explosionResistance(explosionResistance));
    }

    public BlockBuilder<T> offsetType(BlockBehaviour.OffsetType offsetType)
    {
        return properties(properties -> properties.offsetType(offsetType));
    }

    public BlockBuilder<T> offsetType(Function<BlockState, BlockBehaviour.OffsetType> offsetTypeFunction)
    {
        return properties(properties -> properties.offsetType(offsetTypeFunction));
    }

    public BlockBuilder<T> noParticlesOnBreak()
    {
        return properties(BlockBehaviour.Properties::noParticlesOnBreak);
    }

    public BlockBuilder<T> requiredFeatures(FeatureFlag... flags)
    {
        return properties(properties -> properties.requiredFeatures(flags));
    }

    @FunctionalInterface
    public interface BlockFactory<T extends Block>
    {
        T create(BlockBehaviour.Properties properties);
    }

    @FunctionalInterface
    public interface BlockItemFactory<I extends Item, B extends Block>
    {
        I create(B block, Item.Properties properties);
    }
}
