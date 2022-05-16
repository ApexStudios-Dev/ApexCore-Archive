package xyz.apex.forge.apexcore.core.client.renderer.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public final class PlayerPlushieModel extends Model
{
	private final ModelRenderer main;
	private final ModelRenderer rightLegStubCap_r1;
	private final ModelRenderer leftLegStubCap_r1;
	private final ModelRenderer rightArmStubCap_r1;
	private final ModelRenderer leftArmStubCap_r1;
	private final ModelRenderer head_r1;
	private final ModelRenderer layer;
	private final ModelRenderer rightLegStubCap_r2;
	private final ModelRenderer leftLegStubCap_r2;
	private final ModelRenderer rightArmStubCap_r2;
	private final ModelRenderer leftArmStubCap_r2;
	private final ModelRenderer head_layer_r1;

	public PlayerPlushieModel(Function<ResourceLocation, RenderType> renderTypeFunction)
	{
		super(renderTypeFunction);

		texWidth = 64;
		texHeight = 64;

		main = new ModelRenderer(this);
		main.setPos(0.0F, 24.0F, 0.0F);
		main.texOffs(16, 16).addBox(-4.0F, -9.0F, 0.0F, 8.0F, 8.0F, 4.0F, 0.0F, false);
		main.texOffs(16, 27).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 4.0F, 0.0F, false);
		main.texOffs(16, 16).addBox(-4.0F, 0.0005F, 0.0F, 8.0F, 0.0F, 4.0F, 0.0F, false);

		rightLegStubCap_r1 = new ModelRenderer(this);
		rightLegStubCap_r1.setPos(3.0F, -1.9495F, -4.5F);
		main.addChild(rightLegStubCap_r1);
		setRotationAngle(rightLegStubCap_r1, -1.5708F, -0.2182F, 0.0F);
		rightLegStubCap_r1.texOffs(16, 48).addBox(-2.0F, 2.0495F, -2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		rightLegStubCap_r1.texOffs(16, 59).addBox(-2.0F, 0.9995F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		rightLegStubCap_r1.texOffs(16, 48).addBox(-2.0F, -5.0005F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		leftLegStubCap_r1 = new ModelRenderer(this);
		leftLegStubCap_r1.setPos(-3.0F, -1.9495F, -4.5F);
		main.addChild(leftLegStubCap_r1);
		setRotationAngle(leftLegStubCap_r1, -1.5708F, 0.2182F, 0.0F);
		leftLegStubCap_r1.texOffs(0, 16).addBox(-2.0F, 3.0005F, -2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		leftLegStubCap_r1.texOffs(0, 27).addBox(-2.0F, 0.9995F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		leftLegStubCap_r1.texOffs(0, 16).addBox(-2.0F, -5.0005F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		rightArmStubCap_r1 = new ModelRenderer(this);
		rightArmStubCap_r1.setPos(7.0F, -2.9995F, 2.0F);
		main.addChild(rightArmStubCap_r1);
		setRotationAngle(rightArmStubCap_r1, 0.0F, 0.0F, -0.2618F);
		rightArmStubCap_r1.texOffs(32, 48).addBox(-2.0F, 2.5005F, -2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		rightArmStubCap_r1.texOffs(32, 59).addBox(-2.0F, 1.4995F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		rightArmStubCap_r1.texOffs(32, 48).addBox(-2.0F, -6.5005F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

		leftArmStubCap_r1 = new ModelRenderer(this);
		leftArmStubCap_r1.setPos(-7.0F, -2.9995F, 2.0F);
		main.addChild(leftArmStubCap_r1);
		setRotationAngle(leftArmStubCap_r1, 0.0F, 0.0F, 0.2618F);
		leftArmStubCap_r1.texOffs(40, 16).addBox(-2.0F, 2.5005F, -2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		leftArmStubCap_r1.texOffs(40, 27).addBox(-2.0F, 1.4995F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		leftArmStubCap_r1.texOffs(40, 16).addBox(-2.0F, -6.5005F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

		head_r1 = new ModelRenderer(this);
		head_r1.setPos(0.0F, -13.0F, 2.0F);
		main.addChild(head_r1);
		setRotationAngle(head_r1, 0.0F, 0.0F, -0.0873F);
		head_r1.texOffs(0, 0).addBox(-4.25F, -3.75F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		layer = new ModelRenderer(this);
		layer.setPos(0.0F, 24.0F, 0.0F);
		layer.texOffs(16, 32).addBox(-4.0F, -9.0F, 0.0F, 8.0F, 8.0F, 4.0F, 0.0F, false);
		layer.texOffs(16, 43).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 4.0F, 0.0F, false);
		layer.texOffs(16, 32).addBox(-4.0F, 0.0005F, 0.0F, 8.0F, 0.0F, 4.0F, 0.0F, false);

		rightLegStubCap_r2 = new ModelRenderer(this);
		rightLegStubCap_r2.setPos(3.0F, -1.9495F, -4.5F);
		layer.addChild(rightLegStubCap_r2);
		setRotationAngle(rightLegStubCap_r2, -1.5708F, -0.2182F, 0.0F);
		rightLegStubCap_r2.texOffs(0, 48).addBox(-2.0F, 2.0005F, -2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		rightLegStubCap_r2.texOffs(0, 59).addBox(-2.0F, 0.9995F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		rightLegStubCap_r2.texOffs(0, 48).addBox(-2.0F, -5.0005F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		leftLegStubCap_r2 = new ModelRenderer(this);
		leftLegStubCap_r2.setPos(-3.0F, -1.9495F, -4.5F);
		layer.addChild(leftLegStubCap_r2);
		setRotationAngle(leftLegStubCap_r2, -1.5708F, 0.2182F, 0.0F);
		leftLegStubCap_r2.texOffs(0, 32).addBox(-2.0F, 2.0005F, -2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		leftLegStubCap_r2.texOffs(0, 43).addBox(-2.0F, 0.9995F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		leftLegStubCap_r2.texOffs(0, 32).addBox(-2.0F, -5.0005F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

		rightArmStubCap_r2 = new ModelRenderer(this);
		rightArmStubCap_r2.setPos(7.0F, -2.9995F, 2.0F);
		layer.addChild(rightArmStubCap_r2);
		setRotationAngle(rightArmStubCap_r2, 0.0F, 0.0F, -0.2618F);
		rightArmStubCap_r2.texOffs(48, 48).addBox(-2.0F, 2.5005F, -2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		rightArmStubCap_r2.texOffs(48, 59).addBox(-2.0F, 1.4995F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		rightArmStubCap_r2.texOffs(48, 48).addBox(-2.0F, -6.5005F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

		leftArmStubCap_r2 = new ModelRenderer(this);
		leftArmStubCap_r2.setPos(-7.0F, -2.9995F, 2.0F);
		layer.addChild(leftArmStubCap_r2);
		setRotationAngle(leftArmStubCap_r2, 0.0F, 0.0F, 0.2618F);
		leftArmStubCap_r2.texOffs(40, 32).addBox(-2.0F, 2.5005F, -2.0F, 4.0F, 0.0F, 4.0F, 0.0F, false);
		leftArmStubCap_r2.texOffs(40, 43).addBox(-2.0F, 1.4995F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
		leftArmStubCap_r2.texOffs(40, 32).addBox(-2.0F, -6.5005F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

		head_layer_r1 = new ModelRenderer(this);
		head_layer_r1.setPos(0.0F, -13.0F, 2.0F);
		layer.addChild(head_layer_r1);
		setRotationAngle(head_layer_r1, 0.0F, 0.0F, -0.0873F);
		head_layer_r1.texOffs(32, 0).addBox(-4.25F, -3.75F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

	}

	public PlayerPlushieModel()
	{
		this(RenderType::entityTranslucent);
	}

	@Override
	public void renderToBuffer(MatrixStack pose, IVertexBuilder buffer, int packedLight, int packedOverlay, float r, float g, float b, float a)
	{
		main.render(pose, buffer, packedLight, packedOverlay, r, g, b, a);

		pose.pushPose();
		pose.scale(1.1F, 1.1F, 1.1F);
		pose.translate(0D, -.025D, 0D);
		layer.render(pose, buffer, packedLight, packedOverlay, r, g, b, a);
		pose.popPose();
	}

	private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
