package xyz.apex.forge.apexcore.lib.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

// Simple generic sword item that does not
// break any blocks faster than a fist
public class MeleeItem extends SwordItem
{
	public MeleeItem(Properties properties, int baseAttackDamage, float attackSpeed, IItemTier itemTier)
	{
		super(itemTier, baseAttackDamage, attackSpeed, properties);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState blockState)
	{
		return 1F;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState blockState)
	{
		return false;
	}
}
