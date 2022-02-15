package xyz.apex.forge.apexcore.core.client.hats;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import xyz.apex.forge.apexcore.lib.HatsRegistry;
import xyz.apex.forge.apexcore.lib.item.HatItem;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class LayerHat extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
{
	private final Map<ResourceLocation, HatModel> hatModelCache = Maps.newHashMap();

	public LayerHat()
	{
		super(Minecraft.getInstance().getEntityRenderDispatcher().defaultPlayerRenderer);
	}

	private HatModel getOrCompileHatModel(HatsRegistry.CompiledHat hat)
	{
		return hatModelCache.computeIfAbsent(hat.getHatName(), $ -> new HatModel(hat, hat.getAnimationSetup().get()));
	}

	@Override
	protected ResourceLocation getTextureLocation(AbstractClientPlayerEntity player)
	{
		ItemStack stack = player.getItemBySlot(EquipmentSlotType.HEAD);
		Item item = stack.getItem();

		if(item instanceof HatItem)
		{
			HatItem hatItem = (HatItem) item;
			return hatItem.getActiveHatTexture(stack);
		}

		return super.getTextureLocation(player);
	}

	@Override
	public void render(MatrixStack pose, IRenderTypeBuffer buffer, int packedLight, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(!player.isInvisible())
		{
			ItemStack stack = player.getItemBySlot(EquipmentSlotType.HEAD);
			Item item = stack.getItem();

			if(item instanceof HatItem)
			{
				HatItem hatItem = (HatItem) item;
				ResourceLocation hatTexture = hatItem.getActiveHatTexture(stack);
				HatsRegistry.CompiledHat hatData = hatItem.getHatData();

				HatModel hatModel = getOrCompileHatModel(hatData);

				hatModel.prepareMobModel(player, limbSwing, limbSwingAmount, partialTicks);
				hatModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				renderColoredCutoutModel(hatModel, hatTexture, pose, buffer, packedLight, player, 1, 1, 1);
			}
		}
	}
}
