package dev.apexstudios.apexcore.common.registry.builder;

import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.holder.DeferredBlock;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class BlockBuilder<O extends AbstractRegister<O>, P, T extends Block> extends AbstractBuilder<O, P, Block, T, DeferredBlock<T>, BlockBuilder<O, P, T>>
{
    private final Function<BlockBehaviour.Properties, T> blockFactory;
    private OptionalLike<BlockBehaviour.Properties> initialProperties = () -> null;
    private Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesModifier = Function.identity();
    private OptionalLike<OptionalLike<BlockColor>> colorHandler = () -> null;

    @ApiStatus.Internal
    public BlockBuilder(O owner, P parent, String valueName, BuilderHelper helper, Function<BlockBehaviour.Properties, T> blockFactory)
    {
        super(owner, parent, Registries.BLOCK, valueName, DeferredBlock::createBlock, helper);

        this.blockFactory = blockFactory;
    }

    public BlockBuilder<O, P, T> color(OptionalLike<OptionalLike<BlockColor>> colorHandler)
    {
        this.colorHandler = OptionalLike.of(colorHandler);
        return this;
    }

    public BlockBuilder<O, P, T> initialProperties(OptionalLike<BlockBehaviour.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return this;
    }

    public BlockBuilder<O, P, T> copyPropertiesFrom(OptionalLike<BlockBehaviour> copyFrom)
    {
        return initialProperties(() -> copyFrom.map(BlockBehaviour.Properties::copy).getRaw());
    }

    public BlockBuilder<O, P, T> properties(UnaryOperator<BlockBehaviour.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public <I extends Item> ItemBuilder<O, BlockBuilder<O, P, T>, I> item(BiFunction<T, Item.Properties, I> itemFactory)
    {
        return owner.item(this, getValueName().getPath(), properties -> itemFactory.apply(asSupplier().get(), properties));
    }

    public ItemBuilder<O, BlockBuilder<O, P, T>, BlockItem> item()
    {
        return item(BlockItem::new);
    }

    public BlockBuilder<O, P, T> defaultItem()
    {
        return item().build();
    }

    @Override
    protected T createValue()
    {
        return blockFactory.apply(propertiesModifier.apply(initialProperties.orElseGet(BlockBehaviour.Properties::of)));
    }

    @Override
    protected void onRegister(T value)
    {
        Platform.get().registerColorHandler(getOwnerId(), value, colorHandler);
    }
}