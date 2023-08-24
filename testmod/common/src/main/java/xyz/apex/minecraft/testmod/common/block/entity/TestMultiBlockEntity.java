package xyz.apex.minecraft.testmod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BaseBlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.BlockEntityComponentTypes;
import xyz.apex.minecraft.testmod.common.TestMod;
import xyz.apex.minecraft.testmod.common.component.entity.BookSlotBlockEntityComponent;
import xyz.apex.minecraft.testmod.common.component.entity.InventoryBlockEntityComponent;
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
        registrar.register(InventoryBlockEntityComponent.COMPONENT_TYPE, component -> component.withSlotCount(3 * 5));
        registrar.register(BookSlotBlockEntityComponent.COMPONENT_TYPE);
        registrar.register(BlockEntityComponentTypes.NAMEABLE);
        registrar.register(BlockEntityComponentTypes.LOCK_CODE);
        registrar.register(BlockEntityComponentTypes.LOOT_TABLE);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory)
    {
        return new TestMultiBlockMenu(TestMod.TEST_MULTI_BLOCK_MENU.value(), syncId, playerInventory, this);
    }
}
