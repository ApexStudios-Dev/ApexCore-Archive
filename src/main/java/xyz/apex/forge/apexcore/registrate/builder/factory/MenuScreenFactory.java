package xyz.apex.forge.apexcore.registrate.builder.factory;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

@FunctionalInterface
public interface MenuScreenFactory<MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>>
{
	SCREEN create(MENU menu, Inventory inventory, Component displayName);
}