package xyz.apex.forge.apexcore.lib.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import xyz.apex.forge.apexcore.lib.container.ItemInventoryContainer;

public class ItemInventoryContainerScreen<C extends ItemInventoryContainer> extends AbstractContainerScreen<C>
{
	protected final ResourceLocation backgroundTexture;

	public ItemInventoryContainerScreen(C container, Inventory playerInventory, Component title, ResourceLocation backgroundTexture)
	{
		super(container, playerInventory, title);

		this.backgroundTexture = backgroundTexture;
	}

	@Override
	protected void init()
	{
		super.init();
		titleLabelX = (imageWidth - font.width(title)) / 2;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, backgroundTexture);
		int i = (width - imageWidth) / 2;
		int j = (height - imageHeight) / 2;
		blit(matrixStack, i, j, 0, 0, imageWidth, imageHeight);
	}
}
