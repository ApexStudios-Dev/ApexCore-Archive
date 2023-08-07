package xyz.apex.minecraft.apexcore.common.lib.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

/**
 * Basic menu screen implementation.
 */
@SideOnly(PhysicalSide.CLIENT)
public class SimpleContainerMenuScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T>
{
    protected final ResourceLocation backgroundTexture;

    public SimpleContainerMenuScreen(T menu, Inventory playerInventory, Component displayName, ResourceLocation backgroundTexture)
    {
        super(menu, playerInventory, displayName);

        this.backgroundTexture = backgroundTexture;
    }

    protected void renderFg(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
    }

    @Override
    protected void init()
    {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderFg(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
    {
        var x = (width - imageWidth) / 2;
        var y = (height - imageHeight) / 2;
        graphics.blit(backgroundTexture, x, y, 0, 0, imageWidth, imageHeight);
    }
}
