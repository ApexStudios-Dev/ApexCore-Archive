package dev.apexstudios.apexcore.common.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BaseMenuScreen<T extends BaseMenu> extends AbstractContainerScreen<T>
{
    public BaseMenuScreen(T menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);

        imageWidth = menu.width();
        imageHeight = menu.height();
    }

    @Override
    protected void init()
    {
        imageWidth = menu.width();
        imageHeight = menu.height();

        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {

    }
}
