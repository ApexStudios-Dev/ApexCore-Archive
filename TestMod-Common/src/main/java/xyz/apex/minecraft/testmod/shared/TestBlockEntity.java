package xyz.apex.minecraft.testmod.shared;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.inventory.InventoryBlockEntity;

public final class TestBlockEntity extends InventoryBlockEntity
{
    public static final int ROWS = 3;
    public static final int COLS = 9;
    public static final int SLOT_COUNT = ROWS * COLS;

    TestBlockEntity(BlockEntityType<? extends TestBlockEntity> blockEntityType, BlockPos pos, BlockState blockState)
    {
        super(blockEntityType, pos, blockState, SLOT_COUNT);
    }
}
