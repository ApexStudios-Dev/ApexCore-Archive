package xyz.apex.minecraft.apexcore.common.lib.registry.factory;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

/**
 * Factory interface for constructing new Screen entries.
 *
 * @param <M> Type of Menu.
 * @param <S> Type of Screen.
 */
@SideOnly(PhysicalSide.CLIENT)
@FunctionalInterface
public interface ScreenFactory<M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> extends MenuScreens.ScreenConstructor<M, S>
{
}
