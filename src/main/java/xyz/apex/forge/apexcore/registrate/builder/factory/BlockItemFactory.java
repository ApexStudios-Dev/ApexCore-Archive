package xyz.apex.forge.apexcore.registrate.builder.factory;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockItemFactory<BLOCK extends Block, ITEM extends Item>
{
	BlockItemFactory<Block, Item> DEFAULT = BlockItem::new;

	ITEM create(BLOCK block, Item.Properties properties);

	static <BLOCK extends Block> BlockItemFactory<BLOCK, Item> blockItem()
	{
		return BlockItem::new;
	}
}