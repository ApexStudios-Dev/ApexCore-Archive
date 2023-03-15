package xyz.apex.forge.apexcore.lib.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import xyz.apex.forge.commonality.SideOnly;

@SideOnly(SideOnly.Side.CLIENT)
public class BaseMenuScreen<MENU extends AbstractContainerMenu> extends AbstractContainerScreen<MENU>
{
	protected final ResourceLocation backgroundTexture;

	public BaseMenuScreen(MENU menu, Inventory playerInventory, Component title, ResourceLocation backgroundTexture)
	{
		super(menu, playerInventory, title);

		this.backgroundTexture = backgroundTexture;
	}

	@Override
	protected void init()
	{
		super.init();
		titleLabelX = (imageWidth - font.width(title)) / 2;
	}

	@Override
	public void render(PoseStack pose, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(pose);
		super.render(pose, mouseX, mouseY, partialTicks);
		renderFg(pose, partialTicks, mouseX, mouseY);
		renderTooltip(pose, mouseX, mouseY);
	}

	protected void renderFg(PoseStack pose, float partialTicks, int mouseX, int mouseY)
	{
	}

	@Override
	protected void renderBg(PoseStack pose, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, backgroundTexture);
		var i = (width - imageWidth) / 2;
		var j = (height - imageHeight) / 2;
		blit(pose, i, j, 0, 0, imageWidth, imageHeight);
	}
}
