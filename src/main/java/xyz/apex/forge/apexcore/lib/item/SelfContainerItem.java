package xyz.apex.forge.apexcore.lib.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class SelfContainerItem extends Item
{
	public SelfContainerItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	protected int getDamageAmount(ItemStack stack)
	{
		return getDamageAmount();
	}

	protected int getDamageAmount()
	{
		return 1;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		ItemStack result = stack.copy();

		if(result.hurt(getDamageAmount(stack), random, null))
			return ItemStack.EMPTY;

		return result;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World level, LivingEntity entity)
	{
		if(isEdible())
		{
			level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), entity.getEatingSound(stack), SoundCategory.NEUTRAL, 1F, 1F + (level.random.nextFloat() - level.random.nextFloat()) * .4F);

			for(Pair<EffectInstance, Float> pair : getFoodProperties().getEffects())
			{
				if(!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond())
					entity.addEffect(new EffectInstance(pair.getFirst()));
			}

			ItemStack result = stack.copy();

			if(result.hurt(getDamageAmount(stack), random, null))
				return ItemStack.EMPTY;

			return result;
		}

		return stack;
	}
}
