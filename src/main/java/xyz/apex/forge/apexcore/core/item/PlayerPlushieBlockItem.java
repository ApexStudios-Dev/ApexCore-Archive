package xyz.apex.forge.apexcore.core.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

import xyz.apex.forge.apexcore.core.client.renderer.ApexCoreItemStackBlockEntityRenderer;
import xyz.apex.forge.apexcore.lib.item.WearableBlockItem;

import java.util.function.Consumer;

public final class PlayerPlushieBlockItem extends WearableBlockItem
{
	public PlayerPlushieBlockItem(Block block, Properties properties)
	{
		super(block, properties, EquipmentSlot.HEAD);
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer)
	{
		consumer.accept(new IItemRenderProperties() {
			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer()
			{
				return ApexCoreItemStackBlockEntityRenderer.getInstance();
			}
		});
	}
}
