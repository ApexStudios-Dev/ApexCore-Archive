package xyz.apex.minecraft.apexcore.common.registry.builder;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.hooks.RendererHooks;
import xyz.apex.minecraft.apexcore.common.platform.Side;
import xyz.apex.minecraft.apexcore.common.platform.SideExecutor;
import xyz.apex.minecraft.apexcore.common.registry.FlammabilityRegistry;
import xyz.apex.minecraft.apexcore.common.registry.entry.BlockEntry;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;

public final class BlockBuilder<T extends Block> extends Builder<Block, T, BlockEntry<T>, BlockBuilder<T>>
{
    private Supplier<BlockBehaviour.Properties> initialProperties = () -> BlockBehaviour.Properties.copy(Blocks.STONE);
    private Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesModifier = Function.identity();
    private final BlockFactory<T> blockFactory;
    @Nullable private ItemBuilder<?> itemBuilder = null;
    @Nullable private Pair<Integer, Integer> flammabilityOdds = null;
    @Nullable private Supplier<Supplier<RenderType>> renderType = null;
    @Nullable private Supplier<BlockColor> blockColor = null;

    private BlockBuilder(String ownerId, String registrationName, BlockFactory<T> blockFactory)
    {
        super(Registries.BLOCK, ownerId, registrationName);

        this.blockFactory = blockFactory;

        item();

        onRegister(block -> {
            if(flammabilityOdds != null) FlammabilityRegistry.register(block, flammabilityOdds.first(), flammabilityOdds.second());
            if(renderType != null) RendererHooks.getInstance().registerRenderType(block, renderType);

            SideExecutor.runWhenOn(Side.CLIENT, () -> () -> {
                if(blockColor != null) RendererHooks.getInstance().registerBlockColor(blockColor, () -> block);
            });
        });
    }

    // region: Custom
    public BlockBuilder<T> flammability(int igniteOdds, int burnOdds)
    {
        flammabilityOdds = Pair.of(igniteOdds, burnOdds);
        return this;
    }

    public BlockBuilder<T> renderType(Supplier<Supplier<RenderType>> renderType)
    {
        this.renderType = renderType;
        return this;
    }

    public BlockBuilder<T> blockColor(Supplier<BlockColor> blockColor)
    {
        this.blockColor = blockColor;
        return this;
    }
    // endregion

    // region: Initial Properties
    public BlockBuilder<T> initialProperties(Material material)
    {
        initialProperties = () -> BlockBehaviour.Properties.of(material);
        return this;
    }

    public BlockBuilder<T> initialProperties(Material material, DyeColor materialColor)
    {
        initialProperties = () -> BlockBehaviour.Properties.of(material, materialColor);
        return this;
    }

    public BlockBuilder<T> initialProperties(Material material, MaterialColor materialColor)
    {
        initialProperties = () -> BlockBehaviour.Properties.of(material, materialColor);
        return this;
    }

    public BlockBuilder<T> initialProperties(Material material, Function<BlockState, MaterialColor> materialColor)
    {
        initialProperties = () -> BlockBehaviour.Properties.of(material, materialColor);
        return this;
    }

    public BlockBuilder<T> copyProperties(Supplier<BlockBehaviour> copyFrom)
    {
        initialProperties = () -> BlockBehaviour.Properties.copy(copyFrom.get());
        return this;
    }
    // endregion

    // region: Properties
    public BlockBuilder<T> properties(UnaryOperator<BlockBehaviour.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
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

    public BlockBuilder<T> lightLevel(ToIntFunction<BlockState> lightEmission)
    {
        return properties(properties -> properties.lightLevel(lightEmission));
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

    public BlockBuilder<T> noParticlesOnBreak()
    {
        return properties(BlockBehaviour.Properties::noParticlesOnBreak);
    }
    // endregion

    // region: BlockItem
    public <I extends Item> BlockBuilder<T> item(BlockItemFactory<T, I> blockItemFactory, UnaryOperator<ItemBuilder<I>> itemModifier)
    {
        itemBuilder = itemModifier.apply(ItemBuilder.builder(getOwnerId(), getRegistrationName(), properties -> blockItemFactory.create(safeEntrySupplier.get(), properties)));
        return this;
    }

    public <I extends Item> BlockBuilder<T> item(BlockItemFactory<T, I> blockItemFactory)
    {
        return item(blockItemFactory, UnaryOperator.identity());
    }

    public BlockBuilder<T> item(UnaryOperator<ItemBuilder<BlockItem>> itemModifier)
    {
        return item(BlockItem::new, itemModifier);
    }

    public BlockBuilder<T> item()
    {
        return item(BlockItem::new, UnaryOperator.identity());
    }

    public BlockBuilder<T> noItem()
    {
        itemBuilder = null;
        return this;
    }
    // endregion

    @Override
    protected void onPostRegistered(BlockEntry<T> registryEntry)
    {
        if(itemBuilder != null) itemBuilder.register();
    }

    @Override
    protected BlockEntry<T> createRegistryEntry(ResourceLocation registryName)
    {
        return new BlockEntry<>(registryName);
    }

    @Override
    protected T create()
    {
        return blockFactory.create(propertiesModifier.apply(initialProperties.get()));
    }

    public static <T extends Block> BlockBuilder<T> builder(String ownerId, String registrationName, BlockFactory<T> blockFactory)
    {
        return new BlockBuilder<>(ownerId, registrationName, blockFactory);
    }

    @FunctionalInterface
    public interface BlockFactory<T extends Block>
    {
        T create(BlockBehaviour.Properties properties);
    }

    @FunctionalInterface
    public interface BlockItemFactory<B extends Block, I extends Item>
    {
        I create(B block, Item.Properties properties);
    }
}
