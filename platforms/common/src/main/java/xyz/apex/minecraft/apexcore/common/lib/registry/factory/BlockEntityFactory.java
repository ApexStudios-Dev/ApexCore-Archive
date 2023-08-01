package xyz.apex.minecraft.apexcore.common.lib.registry.factory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Factory interface for constructing new BlockEntity entries.
 *
 * @param <T> Type of BlockEntity to be constructed.
 */
@FunctionalInterface
public interface BlockEntityFactory<T extends BlockEntity>
{
    /**
     * Returns new BlockEntity instance with given properties.
     *
     * @param blockEntityType BlockEntityType for newly constructed BlockEntity.
     * @param pos Position for BlockEntity.
     * @param blockState BlockState for BlockEntity.
     * @return Newly constructed BlockEntity with given properties.
     */
    T create(BlockEntityType<T> blockEntityType, BlockPos pos, BlockState blockState);
}
