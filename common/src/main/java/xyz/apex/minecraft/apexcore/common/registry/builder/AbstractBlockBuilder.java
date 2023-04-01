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

@SuppressWarnings("unchecked")
public abstract class AbstractBlockBuilder<T extends Block, B extends AbstractBlockBuilder<T, B>> extends Builder<Block, T, BlockEntry<T>, B>
{
    private Supplier<BlockBehaviour.Properties> initialProperties = () -> BlockBehaviour.Properties.copy(Blocks.STONE);
    private Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesModifier = Function.identity();
    @Nullable
    private ItemBuilder<?> itemBuilder = null;
    @Nullable private Pair<Integer, Integer> flammabilityOdds = null;
    @Nullable private Supplier<Supplier<RenderType>> renderType = null;
    @Nullable private Supplier<BlockColor> blockColor = null;

    protected AbstractBlockBuilder(String ownerId, String registrationName)
    {
        super(Registries.BLOCK, ownerId, registrationName);

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
    public final B flammability(int igniteOdds, int burnOdds)
    {
        flammabilityOdds = Pair.of(igniteOdds, burnOdds);
        return (B) this;
    }

    public final B renderType(Supplier<Supplier<RenderType>> renderType)
    {
        this.renderType = renderType;
        return (B) this;
    }

    public final B blockColor(Supplier<BlockColor> blockColor)
    {
        this.blockColor = blockColor;
        return (B) this;
    }
    // endregion

    // region: Initial Properties
    public final B initialProperties(Material material)
    {
        initialProperties = () -> BlockBehaviour.Properties.of(material);
        return (B) this;
    }

    public final B initialProperties(Material material, DyeColor materialColor)
    {
        initialProperties = () -> BlockBehaviour.Properties.of(material, materialColor);
        return (B) this;
    }

    public final B initialProperties(Material material, MaterialColor materialColor)
    {
        initialProperties = () -> BlockBehaviour.Properties.of(material, materialColor);
        return (B) this;
    }

    public final B initialProperties(Material material, Function<BlockState, MaterialColor> materialColor)
    {
        initialProperties = () -> BlockBehaviour.Properties.of(material, materialColor);
        return (B) this;
    }

    public final B copyProperties(Supplier<BlockBehaviour> copyFrom)
    {
        initialProperties = () -> BlockBehaviour.Properties.copy(copyFrom.get());
        return (B) this;
    }
    // endregion

    // region: Properties
    public final B properties(UnaryOperator<BlockBehaviour.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return (B) this;
    }

    public final B noCollision()
    {
        return properties(BlockBehaviour.Properties::noCollission);
    }

    public final B noOcclusion()
    {
        return properties(BlockBehaviour.Properties::noOcclusion);
    }

    public final B friction(float friction)
    {
        return properties(properties -> properties.friction(friction));
    }

    public final B speedFactor(float speedFactor)
    {
        return properties(properties -> properties.speedFactor(speedFactor));
    }

    public final B jumpFactor(float jumpFactor)
    {
        return properties(properties -> properties.jumpFactor(jumpFactor));
    }

    public final B sound(SoundType soundType)
    {
        return properties(properties -> properties.sound(soundType));
    }

    public final B lightLevel(ToIntFunction<BlockState> lightEmission)
    {
        return properties(properties -> properties.lightLevel(lightEmission));
    }

    public final B strength(float strength)
    {
        return properties(properties -> properties.strength(strength));
    }

    public final B randomTicks()
    {
        return properties(BlockBehaviour.Properties::randomTicks);
    }

    public final B dynamicShape()
    {
        return properties(BlockBehaviour.Properties::dynamicShape);
    }

    public final B noLootTable()
    {
        return properties(BlockBehaviour.Properties::noLootTable);
    }

    public final B dropsLike(Supplier<Block> block)
    {
        return properties(properties -> properties.dropsLike(block.get()));
    }

    public final B air()
    {
        return properties(BlockBehaviour.Properties::air);
    }

    public final B isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate)
    {
        return properties(properties -> properties.isValidSpawn(predicate));
    }

    public final B isRedstoneConductor(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isRedstoneConductor(predicate));
    }

    public final B isSuffocating(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isSuffocating(predicate));
    }

    public final B isViewBlocking(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.isViewBlocking(predicate));
    }

    public final B hasPostProcess(BlockBehaviour.StatePredicate predicate)
    {
        return properties(properties -> properties.hasPostProcess(predicate));
    }

    public final B requiresCorrectToolForDrops()
    {
        return properties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
    }

    public final B color(MaterialColor materialColor)
    {
        return properties(properties -> properties.color(materialColor));
    }

    public final B destroyTime(float destroyTime)
    {
        return properties(properties -> properties.destroyTime(destroyTime));
    }

    public final B explosionResistance(float explosionResistance)
    {
        return properties(properties -> properties.explosionResistance(explosionResistance));
    }

    public final B offsetType(BlockBehaviour.OffsetType offsetType)
    {
        return properties(properties -> properties.offsetType(offsetType));
    }

    public final B noParticlesOnBreak()
    {
        return properties(BlockBehaviour.Properties::noParticlesOnBreak);
    }
    // endregion

    // region: BlockItem
    public final <I extends Item> B item(BlockItemFactory<T, I> blockItemFactory, UnaryOperator<ItemBuilder<I>> itemModifier)
    {
        itemBuilder = itemModifier.apply(ItemBuilder.builder(getOwnerId(), getRegistrationName(), properties -> blockItemFactory.create(safeEntrySupplier.get(), properties)));
        return (B) this;
    }

    public final <I extends Item> B item(BlockItemFactory<T, I> blockItemFactory)
    {
        return item(blockItemFactory, UnaryOperator.identity());
    }

    public final B item(UnaryOperator<ItemBuilder<BlockItem>> itemModifier)
    {
        return item(BlockItem::new, itemModifier);
    }

    public final B item()
    {
        return item(BlockItem::new, UnaryOperator.identity());
    }

    public final B noItem()
    {
        itemBuilder = null;
        return (B) this;
    }
    // endregion

    @Override
    protected void onPostRegistered(BlockEntry<T> registryEntry)
    {
        if(itemBuilder != null) itemBuilder.register();
    }

    @Override
    protected final BlockEntry<T> createRegistryEntry(ResourceLocation registryName)
    {
        return new BlockEntry<>(registryName);
    }

    @Override
    protected final T create()
    {
        return createBlock(propertiesModifier.apply(initialProperties.get()));
    }

    protected abstract T createBlock(BlockBehaviour.Properties properties);

    @FunctionalInterface
    public interface BlockItemFactory<B extends Block, I extends Item>
    {
        I create(B block, Item.Properties properties);
    }
}
