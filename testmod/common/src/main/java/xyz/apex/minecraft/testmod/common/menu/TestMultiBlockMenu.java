package xyz.apex.minecraft.testmod.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import xyz.apex.minecraft.apexcore.common.lib.menu.EnhancedSlot;
import xyz.apex.minecraft.apexcore.common.lib.menu.SimpleContainerMenu;

public final class TestMultiBlockMenu extends SimpleContainerMenu
{
    public TestMultiBlockMenu(MenuType<? extends TestMultiBlockMenu> menuType, int windowId, Inventory playerInventory, Container container)
    {
        super(menuType, windowId, playerInventory, container);
    }

    @Override
    protected void bindSlots(Inventory playerInventory)
    {
        bindContainer(container, 3, 5, 44, 18, this::addSlot);

        addSlot(new EnhancedSlot(container, 15, 144, 36));

        bindPlayerInventory(playerInventory, this::addSlot);
    }

    public static TestMultiBlockMenu forNetwork(MenuType<? extends TestMultiBlockMenu> menuType, int syncId, Inventory inventory, FriendlyByteBuf buffer)
    {
        return new TestMultiBlockMenu(menuType, syncId, inventory, new SimpleContainer((3 * 5) + 1));
    }
}
