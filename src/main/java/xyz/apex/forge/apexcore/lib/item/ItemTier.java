package xyz.apex.forge.apexcore.lib.item;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class ItemTier implements IItemTier
{
	public final int uses;
	public final float speed;
	public final float damage;
	public final int level;
	public final int enchantmentValue;
	public final NonNullLazyValue<DataIngredient> repairIngredient;

	public ItemTier(int level, int uses, float speed, float damage, int enchantmentValue, NonNullSupplier<DataIngredient> repairIngredient)
	{
		this.uses = uses;
		this.speed = speed;
		this.damage = damage;
		this.level = level;
		this.enchantmentValue = enchantmentValue;
		this.repairIngredient = new NonNullLazyValue<>(repairIngredient);
	}

	@Override
	public int getUses()
	{
		return uses;
	}

	@Override
	public float getSpeed()
	{
		return speed;
	}

	@Override
	public float getAttackDamageBonus()
	{
		return damage;
	}

	@Override
	public int getLevel()
	{
		return level;
	}

	@Override
	public int getEnchantmentValue()
	{
		return enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return repairIngredient.get();
	}
}
