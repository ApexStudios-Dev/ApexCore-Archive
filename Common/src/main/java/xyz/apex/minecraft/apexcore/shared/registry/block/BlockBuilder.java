package xyz.apex.minecraft.apexcore.shared.registry.block;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import xyz.apex.minecraft.apexcore.shared.registry.RegistryEntryBuilder;
import xyz.apex.minecraft.apexcore.shared.util.FlammabilityRegistry;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;

public final class BlockBuilder<T extends Block> extends RegistryEntryBuilder<Block, T, BlockBuilder<T>>
{
    private final Function<BlockBehaviour.Properties, T> factory;
    private Supplier<BlockBehaviour.Properties> properties = () -> BlockBehaviour.Properties.copy(Blocks.STONE);
    private Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesModifier = UnaryOperator.identity();

    BlockBuilder(BlockRegistry registry, String name, Function<BlockBehaviour.Properties, T> factory)
    {
        super(registry, name);

        this.factory = factory;
    }

    public BlockBuilder<T> flammability(int burnOdds, int igniteOdds)
    {
        return onRegister((key, entry, value) -> FlammabilityRegistry.register(key.location(), burnOdds, igniteOdds));
    }

    public BlockBuilder<T> initialProperties(Supplier<BlockBehaviour.Properties> properties)
    {
        this.properties = properties;
        return this;
    }

    public BlockBuilder<T> initialProperties(Material material)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material));
    }

    public BlockBuilder<T> initialProperties(Material material, DyeColor dyeColor)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, dyeColor));
    }

    public BlockBuilder<T> initialProperties(Material material, MaterialColor materialColor)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, materialColor));
    }

    public BlockBuilder<T> initialProperties(Material material, Function<BlockState, MaterialColor> function)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, function));
    }

    public BlockBuilder<T> copyFrom(Supplier<Block> copy)
    {
        return initialProperties(() -> BlockBehaviour.Properties.copy(copy.get()));
    }

    public BlockBuilder<T> properties(UnaryOperator<BlockBehaviour.Properties> properties)
    {
        propertiesModifier = propertiesModifier.andThen(properties);
        return this;
    }

    public BlockBuilder<T> noCollision()
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

    public BlockBuilder<T> lightLevel(ToIntFunction<BlockState> lightLevel)
    {
        return properties(properties -> properties.lightLevel(lightLevel));
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

    public BlockBuilder<T> dropsLike(Supplier<Block> block)
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

    public BlockBuilder<T> offsetType(Function<BlockState, BlockBehaviour.OffsetType> offsetType)
    {
        return properties(properties -> properties.offsetType(offsetType));
    }

    @Override
    protected T build()
    {
        var properties = propertiesModifier.apply(this.properties.get());
        return factory.apply(properties);
    }

    @Override
    public BlockRegistryEntry<T> register()
    {
        return (BlockRegistryEntry<T>) super.register();
    }
}
