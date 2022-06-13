package xyz.apex.forge.apexcore.lib.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class DummyEntityRenderer<ENTITY extends Entity> extends EntityRenderer<ENTITY>
{
	public DummyEntityRenderer(EntityRendererProvider.Context ctx)
	{
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(ENTITY entity)
	{
		return MissingTextureAtlasSprite.getLocation();
	}

	@Override
	protected void renderNameTag(ENTITY entity, Component displayName, PoseStack pose, MultiBufferSource buffer, int packedLight)
	{
		// NOOP
	}
}
