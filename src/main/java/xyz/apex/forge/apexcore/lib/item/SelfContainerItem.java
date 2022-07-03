package xyz.apex.forge.apexcore.lib.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

@Deprecated(forRemoval = true)
public class SelfContainerItem extends Item
{
	public static final Random RNG = new Random();

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
		var result = stack.copy();

		if(result.hurt(getDamageAmount(stack), RNG, null))
			return ItemStack.EMPTY;

		return result;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity)
	{
		if(isEdible())
		{
			level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), entity.getEatingSound(stack), SoundSource.NEUTRAL, 1F, 1F + (level.random.nextFloat() - level.random.nextFloat()) * .4F);

			for(var pair : getFoodProperties().getEffects())
			{
				if(!level.isClientSide && pair.getFirst() != null && level.random.nextFloat() < pair.getSecond())
					entity.addEffect(new MobEffectInstance(pair.getFirst()));
			}

			var result = stack.copy();

			if(result.hurt(getDamageAmount(stack), level.random, null))
				return ItemStack.EMPTY;

			return result;
		}

		return stack;
	}
}