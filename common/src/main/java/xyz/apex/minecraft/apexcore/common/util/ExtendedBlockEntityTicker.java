package xyz.apex.minecraft.apexcore.common.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ExtendedBlockEntityTicker
{
    @Nullable
    <T extends BlockEntity> BlockEntityTicker<T> createBlockEntityTicker(Level level, T blockEntity);
}
