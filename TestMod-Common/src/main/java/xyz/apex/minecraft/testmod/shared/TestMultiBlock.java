package xyz.apex.minecraft.testmod.shared;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.inventory.InventoryBlock;
import xyz.apex.minecraft.apexcore.shared.multiblock.MultiBlockType;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntityEntry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.MenuEntry;

public final class TestMultiBlock extends InventoryBlock.AsMultiBlock<TestBlockEntity, TestMenu>
{
    public TestMultiBlock(MultiBlockType multiBlockType, Properties properties)
    {
        super(multiBlockType, properties);
    }

    @Override
    public BlockEntityEntry<TestBlockEntity> getInventoryBlockEntityType()
    {
        return TestMod.TEST_BLOCK_ENTITY;
    }

    @Override
    public MenuEntry<TestMenu> getInventoryMenuType()
    {
        return TestMod.TEST_MENU;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return RenderShape.MODEL;
    }
}
