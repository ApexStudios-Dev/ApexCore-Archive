package xyz.apex.minecraft.testmod.shared;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class TestMenuScreen extends AbstractContainerScreen<TestMenu>
{
    public TestMenuScreen(TestMenu menu, Inventory inventory, Component component)
    {
        super(menu, inventory, component);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(pose);
        super.render(pose, mouseX, mouseY, partialTick);
        renderTooltip(pose, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY)
    {
        drawString(pose, font, menu.pos.toShortString(), leftPos + 128, topPos + 128, 0);
        drawString(pose, font, menu.dimensionType.location().toString(), leftPos + 128, topPos + 128 - 32, 0);
    }
}
