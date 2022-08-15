package xyz.apex.forge.apexcore.registrate.builder.factory;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

@FunctionalInterface
public interface ForgeMenuFactory<MENU extends AbstractContainerMenu>
{
	MENU create(MenuType<MENU> menuType, int windowId, Inventory inventory, @Nullable FriendlyByteBuf buffer);
}