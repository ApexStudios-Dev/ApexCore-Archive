package xyz.apex.forge.apexcore.core.client.hats;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import org.apache.commons.lang3.Validate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import xyz.apex.forge.apexcore.lib.HatsRegistry;

@OnlyIn(Dist.CLIENT)
public final class HatModel extends EntityModel<AbstractClientPlayerEntity>
{
	private final ModelRenderer hatModel;
	private final HatsRegistry.OnSetupAnimation animationSetup;

	public HatModel(HatsRegistry.CompiledHat hatData, HatsRegistry.OnSetupAnimation animationSetup)
	{
		this.animationSetup = animationSetup;

		texWidth = hatData.getTextureWidth();
		texHeight = hatData.getTextureHeight();

		hatModel = hatData.getHatModelFactory().get().apply(this);
		Validate.notNull(hatModel);
	}

	@Override
	public void setupAnim(AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		PlayerRenderer playerRenderer = Minecraft.getInstance().getEntityRenderDispatcher().defaultPlayerRenderer;
		PlayerModel<AbstractClientPlayerEntity> playerModel = playerRenderer.getModel();

		copyPropertiesTo(playerModel);
		hatModel.copyFrom(playerModel.hat);
		animationSetup.setupAnim(this, player, playerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void renderToBuffer(MatrixStack pose, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		hatModel.render(pose, buffer, packedLight, packedOverlay);
	}
}
