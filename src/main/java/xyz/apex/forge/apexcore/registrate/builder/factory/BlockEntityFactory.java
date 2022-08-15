package xyz.apex.forge.apexcore.registrate.builder.factory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockEntityFactory<BLOCK_ENTITY extends BlockEntity>
{
	BLOCK_ENTITY create(BlockEntityType<BLOCK_ENTITY> blockEntityType, BlockPos pos, BlockState blockState);
}