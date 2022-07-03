package xyz.apex.forge.apexcore.lib.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

@Deprecated(forRemoval = true)
public class BottleItem extends Item
{
	public BottleItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
	{
		if(entity instanceof ServerPlayer player)
			CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
		if(entity instanceof Player player && !player.getAbilities().instabuild)
			stack.shrink(1);
		return stack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : stack;
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack)
	{
		return UseAnim.DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		return ItemUtils.startUsingInstantly(level, player, hand);
	}
}