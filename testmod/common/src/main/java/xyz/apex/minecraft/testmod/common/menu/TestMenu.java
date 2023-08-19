package xyz.apex.minecraft.testmod.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import xyz.apex.minecraft.apexcore.common.lib.menu.EnhancedSlot;
import xyz.apex.minecraft.apexcore.common.lib.menu.SimpleContainerMenu;

public final class TestMenu extends SimpleContainerMenu
{
    public TestMenu(MenuType<? extends TestMenu> menuType, int syncId, Inventory inventory)
    {
        super(menuType, syncId, inventory, new SimpleContainer(ItemStack.EMPTY));
    }

    public TestMenu(MenuType<? extends TestMenu> menuType, int syncId, Inventory inventory, FriendlyByteBuf buffer)
    {
        this(menuType, syncId, inventory);
    }

    @Override
    protected void bindSlots(Inventory playerInventory)
    {
        addSlot(new EnhancedSlot(container, 0, 80, 37));
        bindPlayerInventory(playerInventory, this::addSlot);
    }
}
