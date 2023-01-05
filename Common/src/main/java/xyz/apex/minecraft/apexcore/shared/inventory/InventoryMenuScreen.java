package xyz.apex.minecraft.apexcore.shared.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class InventoryMenuScreen<T extends InventoryMenu> extends AbstractContainerScreen<T>
{
    protected final ResourceLocation backgroundTexture;

    protected InventoryMenuScreen(T menu, Inventory inventory, Component displayName, ResourceLocation backgroundTexture)
    {
        super(menu, inventory, displayName);

        this.backgroundTexture = backgroundTexture;
    }

    protected void renderFg(PoseStack pose, int mouseX, int mouseY, float partialTick)
    {
    }

    @Override
    protected void init()
    {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(pose);
        super.render(pose, mouseX, mouseY, partialTick);
        renderFg(pose, mouseX, mouseY, partialTick);
        renderTooltip(pose, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY)
    {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderTexture(0, backgroundTexture);
        var i = (width - imageWidth) / 2;
        var j = (height - imageHeight) / 2;
        blit(pose, i, j, 0, 0, imageWidth, imageHeight);
    }
}
