package xyz.apex.forge.apexcore.lib.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;

// Simple generic sword item that does not
// break any blocks faster than a fist
public class MeleeItem extends SwordItem
{
	public MeleeItem(Properties properties, int baseAttackDamage, float attackSpeed, Tier itemTier)
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
