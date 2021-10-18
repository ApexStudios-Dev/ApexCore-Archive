package xyz.apex.forge.apexcore.lib.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BottleItem extends Item
{
	public BottleItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World level, LivingEntity entity)
	{
		if(entity instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) entity;
			CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
		}

		if(entity instanceof PlayerEntity && !((PlayerEntity) entity).abilities.instabuild)
			stack.shrink(1);

		return stack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : stack;
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 32;
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack)
	{
		return UseAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand)
	{
		return DrinkHelper.useDrink(level, player, hand);
	}
}
