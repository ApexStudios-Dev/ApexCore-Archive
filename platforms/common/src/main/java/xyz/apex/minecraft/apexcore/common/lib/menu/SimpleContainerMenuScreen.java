package xyz.apex.minecraft.apexcore.common.lib.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

/**
 * Basic menu screen implementation.
 */
@SideOnly(PhysicalSide.CLIENT)
public class SimpleContainerMenuScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T>
{
    public static final ResourceLocation SPRITE_BACKGROUND = new ResourceLocation(ApexCore.ID, "container/generic/background");
    public static final ResourceLocation SPRITE_SLOT = new ResourceLocation(ApexCore.ID, "container/generic/slot");

    public SimpleContainerMenuScreen(T menu, Inventory playerInventory, Component displayName)
    {
        super(menu, playerInventory, displayName);
    }

    protected void renderFg(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
    }

    @Override
    protected void init()
    {
        imageWidth = 176;
        imageHeight = 166;

        super.init();

        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderFg(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
    {
        graphics.blitSprite(SPRITE_BACKGROUND, (width - imageWidth) / 2, (height - imageHeight) / 2, imageWidth, imageHeight);
    }
}
