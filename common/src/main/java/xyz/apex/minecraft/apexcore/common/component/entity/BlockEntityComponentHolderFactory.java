package xyz.apex.minecraft.apexcore.common.component.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

@FunctionalInterface
public interface BlockEntityComponentHolderFactory<T extends BlockEntity & BlockEntityComponentHolder>
{
    T create(Consumer<BlockEntityComponentHolder.Registrar> registrar, BlockPos pos, BlockState blockState);
}
