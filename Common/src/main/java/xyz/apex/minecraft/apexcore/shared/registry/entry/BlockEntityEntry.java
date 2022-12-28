package xyz.apex.minecraft.apexcore.shared.registry.entry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class BlockEntityEntry<T extends BlockEntity> extends RegistryEntry<BlockEntityType<T>>
{
    public BlockEntityEntry(ResourceKey<BlockEntityType<T>> registryKey)
    {
        super(Registries.BLOCK_ENTITY_TYPE, registryKey);
    }

    public boolean isBlockEntity(@Nullable BlockEntity blockEntity)
    {
        return blockEntity != null && is(blockEntity.getType());
    }

    public boolean is(BlockEntityType<?> blockEntityType)
    {
        return isPresent() && get() == blockEntityType;
    }

    @Nullable
    public T create(BlockPos pos, BlockState blockState)
    {
        return get().create(pos, blockState);
    }

    public boolean isValid(BlockState blockState)
    {
        return get().isValid(blockState);
    }

    @Nullable
    public T getBlockEntity(BlockGetter level, BlockPos pos)
    {
        return get().getBlockEntity(level, pos);
    }

    public Optional<T> findBlockEntity(BlockGetter level, BlockPos pos)
    {
        return Optional.ofNullable(getBlockEntity(level, pos));
    }
}
