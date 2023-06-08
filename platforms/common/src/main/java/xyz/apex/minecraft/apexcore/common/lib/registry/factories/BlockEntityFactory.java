package xyz.apex.minecraft.apexcore.common.lib.registry.factories;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Main factory used to construct new BlockEntity instances during registrations.
 *
 * @param <T> Type of block entity to be constructed.
 */
@FunctionalInterface
public interface BlockEntityFactory<T extends BlockEntity>
{
    /**
     * Returns new block entity instance for given block entity type, position and block state.
     *
     * @param blockEntityType Block entity type to create this block entity for.
     * @param pos             Position to create this block entity for.
     * @param blockState      Block state to create this block entity for.
     * @return New block entity instance for block entity type, position and block state.
     */
    T create(BlockEntityType<? extends T> blockEntityType, BlockPos pos, BlockState blockState);
}
