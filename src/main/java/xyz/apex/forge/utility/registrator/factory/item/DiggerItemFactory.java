package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface DiggerItemFactory<ITEM extends DiggerItem>
{
	DiggerItemFactory<DiggerItem> DEFAULT = DiggerItem::new;

	ITEM create(float attackDamage, float attackSpeed, Tier itemTier, Tag<Block> diggables, Item.Properties properties);
}
