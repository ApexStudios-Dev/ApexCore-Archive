package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

public final class BlockEntry<T extends Block> extends BaseRegistryEntry<T> implements ItemLikeEntry<T>, FeaturedEntry<T>
{
    @ApiStatus.Internal
    public BlockEntry(AbstractRegistrar<?> registrar, ResourceKey<T> registryKey)
    {
        super(registrar, registryKey);
    }

    /**
     * @return Default BlockState for {@link #value()}.
     */
    public BlockState defaultBlockState()
    {
        return value().defaultBlockState();
    }

    /**
     * @return {@code true} if BlockState contains {@link #value()}.
     */
    public boolean is(BlockState blockState)
    {
        return blockState.is(value());
    }

    public static <T extends Block> BlockEntry<T> cast(RegistryEntry<?> registryEntry)
    {
        return RegistryEntry.cast(BlockEntry.class, registryEntry);
    }
}
