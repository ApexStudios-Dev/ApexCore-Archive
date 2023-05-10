package xyz.apex.minecraft.apexcore.common.lib.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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

    protected void renderFg(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
    }

    @Override
    protected void init()
    {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderFg(poseStack, mouseX, mouseY, partialTick);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY)
    {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderTexture(0, backgroundTexture);

        var x = (width - imageWidth) / 2;
        var y = (height - imageHeight) / 2;
        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
    }
}
