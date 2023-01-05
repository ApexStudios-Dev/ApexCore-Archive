package xyz.apex.minecraft.testmod.shared;

import xyz.apex.minecraft.apexcore.shared.inventory.InventoryBlock;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntityEntry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.MenuEntry;

public final class TestBlock extends InventoryBlock<TestBlockEntity, TestMenu>
{
    public TestBlock(Properties properties)
    {
        super(properties);
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
}
