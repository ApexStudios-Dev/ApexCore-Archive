package xyz.apex.minecraft.testmod.shared;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

import xyz.apex.minecraft.apexcore.shared.inventory.InventoryMenu;

public final class TestMenu extends InventoryMenu
{
    public TestMenu(MenuType<? extends TestMenu> menuType, int containerId, Player player, FriendlyByteBuf data)
    {
        super(menuType, containerId, player, data);

        bindInventory(this, inventory, TestBlockEntity.ROWS, TestBlockEntity.COLS, 8, 18);
        bindPlayerInventory(this, player, 8, 84);
    }
}
