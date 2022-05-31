package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockItemFactory<BLOCK extends Block, ITEM extends BlockItem>
{
	BlockItemFactory<Block, BlockItem> DEFAULT = BlockItem::new;

	ITEM create(BLOCK block, Item.Properties properties);

	static <BLOCK extends Block> BlockItemFactory<BLOCK, BlockItem> forBlock()
	{
		return DEFAULT::create;
	}
}
