package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

import java.util.Optional;

public final class BlockEntityEntry<T extends BlockEntity> extends BaseRegistryEntry<BlockEntityType<T>>
{
    @ApiStatus.Internal
    public BlockEntityEntry(AbstractRegistrar<?> registrar, ResourceKey<BlockEntityType<T>> registryKey)
    {
        super(registrar, registryKey);
    }

    @Nullable
    public T create(BlockPos pos, BlockState blockState)
    {
        return value().create(pos, blockState);
    }

    @Nullable
    public T getBlockEntity(BlockGetter level, BlockPos pos)
    {
        var blockState = level.getBlockState(pos);

        // multi blocks need special casing
        if(blockState.getBlock() instanceof BlockComponentHolder componentHolder)
            return componentHolder.getBlockEntity(value(), level, pos, blockState);

        return value().getBlockEntity(level, pos);
    }

    public Optional<T> getBlockEntityOptional(BlockGetter level, BlockPos pos)
    {
        return Optional.ofNullable(getBlockEntity(level, pos));
    }

    public boolean isValid(Block block)
    {
        return value().validBlocks.contains(block);
    }

    public boolean isValid(BlockState blockState)
    {
        return value().isValid(blockState);
    }

    public boolean is(BlockEntity other)
    {
        return is(other.getType());
    }

    public boolean is(BlockEntityType<?> other)
    {
        return value() == other;
    }

    public static <T extends BlockEntity> BlockEntityEntry<T> cast(RegistryEntry<?> registryEntry)
    {
        return RegistryEntry.cast(BlockEntityEntry.class, registryEntry);
    }
}
