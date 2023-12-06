package dev.apexstudios.apexcore.common.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.apexstudios.apexcore.common.ApexCore;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BaseMenuScreen<T extends BaseMenu> extends AbstractContainerScreen<T>
{
    public static final ResourceLocation SPRITE_BACKGROUND = new ResourceLocation(ApexCore.ID, "container/generic/background");
    public static final ResourceLocation SPRITE_SLOT = new ResourceLocation(ApexCore.ID, "container/generic/slot");

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

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);

        renderDynamicMenuElements(graphics);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    private void renderDynamicMenuElements(GuiGraphics graphics)
    {
        RenderSystem.disableDepthTest();
        graphics.pose().pushPose();
        graphics.pose().translate(leftPos, topPos, 0F);

        graphics.blitSprite(SPRITE_BACKGROUND, 0, 0, imageWidth, imageHeight);

        graphics.pose().pushPose();
        graphics.pose().translate(0F, 0F, 100F);

        for(var slot : menu.slotManager)
        {
            if(slot.isActive())
                graphics.blitSprite(SPRITE_SLOT, slot.x - 1, slot.y - 1, 18, 18);
        }

        graphics.pose().popPose();
        graphics.pose().popPose();
        RenderSystem.enableDepthTest();
    }
}
