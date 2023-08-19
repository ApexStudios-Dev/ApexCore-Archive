package xyz.apex.minecraft.testmod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BaseBlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.BlockEntityComponentTypes;
import xyz.apex.minecraft.testmod.common.TestMod;
import xyz.apex.minecraft.testmod.common.menu.TestMultiBlockMenu;

public final class TestMultiBlockEntity extends BaseBlockEntityComponentHolder
{
    public TestMultiBlockEntity(BlockEntityType<? extends TestMultiBlockEntity> blockEntityType, BlockPos pos, BlockState blockState)
    {
        super(blockEntityType, pos, blockState);
    }

    @Override
    protected void registerComponents(BlockEntityComponentRegistrar registrar)
    {
        registrar.register(BlockEntityComponentTypes.INVENTORY, component -> component.setSlotCount(3 * 5));
        registrar.register(BlockEntityComponentTypes.NAMEABLE);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory)
    {
        return new TestMultiBlockMenu(TestMod.TEST_MULTI_BLOCK_MENU.value(), syncId, playerInventory, getRequiredComponent(BlockEntityComponentTypes.INVENTORY));
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[] {
                0, 1, 2, 3, 4, 5,
                6, 7, 8, 9, 10, 11,
                12, 13, 14
        };
    }
}
