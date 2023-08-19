package xyz.apex.minecraft.apexcore.common.lib.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
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

    public SimpleContainerMenuScreen(T menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);

        imageWidth = 0;
        imageHeight = 0;
    }

    @Override
    protected void init()
    {
        detectFrameSize();
    }

    public final void detectFrameSize()
    {
        var frameWidth = 0;
        var frameHeight = 0;

        for(var slot : menu.slots)
        {
            frameWidth = Math.max(frameWidth, slot.x);
            frameHeight = Math.max(frameHeight, slot.y);
        }

        frameWidth += SimpleContainerMenu.SLOT_SIZE + 9;
        frameHeight += SimpleContainerMenu.SLOT_SIZE + 6;

        setFrameSize(frameWidth, frameHeight);
    }

    public final void setFrameSize(int frameWidth, int frameHeight)
    {
        imageWidth = frameWidth;
        imageHeight = frameHeight;

        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;

        titleLabelX = SimpleContainerMenu.SLOT_BORDER_OFFSET;
        titleLabelY = 6;

        var playerInventory = minecraft.player.getInventory();

        // slot 9 should always exist
        // top left slot of player inventory
        var slot = menu.getSlot(menu.findSlot(playerInventory, 9).orElseThrow());
        inventoryLabelX = slot.x;
        inventoryLabelY = slot.y - 2 - (SimpleContainerMenu.SLOT_SIZE / 2);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
    {
        renderFrameBackground(graphics);
    }

    @Override
    public void renderSlot(GuiGraphics graphics, Slot slot)
    {
        super.renderSlot(graphics, slot);
        renderSlotBackground(graphics, slot);
    }

    protected void renderFrameBackground(GuiGraphics graphics)
    {
        graphics.blitSprite(SPRITE_BACKGROUND, leftPos, topPos, imageWidth, imageHeight);
    }

    protected void renderSlotBackground(GuiGraphics graphics, Slot slot)
    {
        graphics.blitSprite(SPRITE_SLOT, slot.x - 1, slot.y - 1, SimpleContainerMenu.SLOT_SIZE, SimpleContainerMenu.SLOT_SIZE);
    }
}
