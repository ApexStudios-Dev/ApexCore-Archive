package dev.apexstudios.apexcore.common.registry.holder;

import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DeferredBlock<T extends Block> extends DeferredHolder<Block, T> implements ItemLike
{
    protected DeferredBlock(ResourceKey<Block> valueKey)
    {
        super(valueKey);
    }

    public BlockState defaultBlockState()
    {
        return map(Block::defaultBlockState).orElseGet(Blocks.AIR::defaultBlockState);
    }

    public boolean is(BlockState blockState)
    {
        return isPresent() && blockState.is(value());
    }

    @Override
    public Item asItem()
    {
        return value().asItem();
    }

    public static <T extends Block> DeferredBlock<T> createBlock(ResourceLocation valueId)
    {
        return createBlock(ResourceKey.create(Registries.BLOCK, valueId));
    }

    public static <T extends Block> DeferredBlock<T> createBlock(ResourceKey<Block> valueKey)
    {
        return new DeferredBlock<>(valueKey);
    }
}
