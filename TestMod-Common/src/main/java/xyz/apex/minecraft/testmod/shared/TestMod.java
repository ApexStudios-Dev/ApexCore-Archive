package xyz.apex.minecraft.testmod.shared;

import net.minecraft.world.item.Item;

import xyz.apex.minecraft.apexcore.shared.multiblock.MultiBlockType;
import xyz.apex.minecraft.apexcore.shared.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.shared.registry.Registrar;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntityEntry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.ItemEntry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.MenuEntry;

public interface TestMod extends ModPlatform
{
    String ID = "testmod";
    Registrar REGISTRAR = Registrar.create(ID);

    MultiBlockType MB_1x1x2 = MultiBlockType.builder(1, 1, 2).build();

    ItemEntry<Item> TEST_ITEM = REGISTRAR.item("test_item").register();
    BlockEntry<TestBlock> TEST_BLOCK = REGISTRAR.block("test_block", TestBlock::new).register();
    MenuEntry<TestMenu> TEST_MENU = REGISTRAR.menu("test_menu", TestMenu::new, () -> TestMenuScreen::new);
    BlockEntry<TestMultiBlock> TEST_MULTI_BLOCK = REGISTRAR.multiBlock("test_multi_block", MB_1x1x2, TestMultiBlock::new).register();
    BlockEntityEntry<TestBlockEntity> TEST_BLOCK_ENTITY = REGISTRAR.blockEntity("test_block_entity", TestBlockEntity::new).validBlock(TEST_BLOCK).validBlock(TEST_MULTI_BLOCK).register();
}
