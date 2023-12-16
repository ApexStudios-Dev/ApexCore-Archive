package dev.apexstudios.testmod.common.menu;

import dev.apexstudios.apexcore.common.inventory.ItemHandler;
import dev.apexstudios.apexcore.common.menu.BaseMenu;
import dev.apexstudios.apexcore.common.menu.SlotManager;
import dev.apexstudios.testmod.common.ref.TestBlockEntities;
import dev.apexstudios.testmod.common.ref.TestMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

public final class TestMenu extends BaseMenu
{
    public TestMenu(int containerId, Inventory inventory, ItemHandler oakItems, ItemHandler birchItems)
    {
        super(TestMenus.TEST_MENU_TYPE.value(), containerId, 176, 166);

        slotManager.addPlayerSlots(inventory);
        slotManager.addSlot(oakItems, "oak", 20, 20, 0);
        slotManager.addSlots(birchItems, "birch", 40, 20, 2, 2);
        slotManager.addGhostSlot(80, 20).ghosting(Items.DIAMOND);
        slotManager.addGhostSlot(80, 40).ghosting(Items.NETHER_STAR);
        slotManager.addShiftTarget(SlotManager.GROUP_PLAYER, "oak", true);
        slotManager.addShiftTarget("birch", "oak", false);
    }

    public static TestMenu forNetwork(int windowId, Inventory inventory, FriendlyByteBuf buffer)
    {
        var globalPos = buffer.readGlobalPos();
        var dimension = globalPos.dimension();
        var plrLevel = inventory.player.level();
        var level = plrLevel.dimension() == dimension ? plrLevel : inventory.player.getServer().getLevel(globalPos.dimension());
        var blockEntity = TestBlockEntities.TEST_BLOCK_ENTITY.get(level, globalPos.pos());
        return new TestMenu(windowId, inventory, blockEntity.oakInventory, blockEntity.birchInventory);
    }
}
