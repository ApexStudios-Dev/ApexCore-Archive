package xyz.apex.minecraft.apexcore.shared.registry.entry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockEntry<T extends Block> extends ItemLikeEntry<T>
{
    public BlockEntry(ResourceKey<T> registryKey)
    {
        super(Registries.BLOCK, registryKey);
    }

    public boolean isBlock(@Nullable Block block)
    {
        return isPresent() && get() == block;
    }

    public BlockState defaultBlockState()
    {
        return get().defaultBlockState();
    }

    public boolean hasBlockState(BlockState blockState)
    {
        return blockState.is(get());
    }

    @SuppressWarnings("deprecation")
    public boolean hasBlockTag(TagKey<Block> tag)
    {
        return get().builtInRegistryHolder().is(tag);
    }
}
