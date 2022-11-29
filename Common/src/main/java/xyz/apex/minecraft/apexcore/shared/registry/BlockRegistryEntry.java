package xyz.apex.minecraft.apexcore.shared.registry;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public final class BlockRegistryEntry<T extends Block> extends ItemLikeRegistryEntry<T>
{
    public BlockRegistryEntry(ModdedRegistry<? super T> modded, ResourceKey<T> key, Supplier<T> supplier)
    {
        super(modded, key, supplier);
    }

    @NotNull
    @Override
    public Item asItem()
    {
        return get().asItem();
    }

    public boolean is(Block block)
    {
        return map(value -> value == block).orElse(false);
    }

    public BlockState defaultBlockState()
    {
        return get().defaultBlockState();
    }

    public boolean hasBlockState(BlockState blockState)
    {
        return is(blockState.getBlock());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean hasBlockTag(TagKey<Block> tag)
    {
        return modded.hasTag((TagKey) tag, getHolder());
    }
}
