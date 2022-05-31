package xyz.apex.forge.utility.registrator.helper;

import com.google.common.base.Objects;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import xyz.apex.java.utility.Lazy;
import xyz.apex.java.utility.nullness.NonnullSupplier;

@SuppressWarnings("unused")
public final class ItemTier implements Tier
{
	public final int level;
	public final int uses;
	public final float speed;
	public final float damage;
	public final int enchantmentValue;
	public final Lazy<Ingredient> repairIngredient;

	public ItemTier(int level, int uses, float speed, float damage, int enchantmentValue, NonnullSupplier<Ingredient> repairIngredient)
	{
		this.level = level;
		this.uses = uses;
		this.speed = speed;
		this.damage = damage;
		this.enchantmentValue = enchantmentValue;
		this.repairIngredient = Lazy.of(repairIngredient, true);
	}

	private ItemTier(Builder builder)
	{
		level = builder.level;
		uses = builder.uses;
		speed = builder.speed;
		damage = builder.damage;
		enchantmentValue = builder.enchantmentValue;
		repairIngredient = Lazy.of(builder.repairIngredient, true);
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

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(o instanceof ItemTier itemTier)
			return level == itemTier.level && uses == itemTier.uses && Float.compare(itemTier.speed, speed) == 0 && Float.compare(itemTier.damage, damage) == 0 && enchantmentValue == itemTier.enchantmentValue && Objects.equal(repairIngredient, itemTier.repairIngredient);
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(level, uses, speed, damage, enchantmentValue, repairIngredient);
	}

	@Override
	public String toString()
	{
		return "ItemTier{level=%d, uses=%d, speed=%s, damage=%s, enchantmentValue=%d, repairIngredient=%s}".formatted(level, uses, speed, damage, enchantmentValue, repairIngredient);
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public static Builder copy(Tier itemTier)
	{
		return builder().copy(itemTier);
	}

	public static final class Builder
	{
		private int level = 0;
		private int uses = 1;
		private float speed = 1F;
		private float damage = 0F;
		private int enchantmentValue = 0;
		private NonnullSupplier<Ingredient> repairIngredient = () -> Ingredient.EMPTY;

		private Builder() { }

		public Builder copy(Builder builder)
		{
			return level(builder.level)
					.uses(builder.uses)
					.speed(builder.speed)
					.damage(builder.damage)
					.enchantmentValue(builder.enchantmentValue)
					.repairIngredient(builder.repairIngredient)
			;
		}

		public Builder copy(Tier itemTier)
		{
			return level(itemTier.getLevel())
					.uses(itemTier.getUses())
					.speed(itemTier.getSpeed())
					.damage(itemTier.getAttackDamageBonus())
					.enchantmentValue(itemTier.getEnchantmentValue())
					.repairIngredient(itemTier::getRepairIngredient)
			;
		}

		public Builder level(int level)
		{
			this.level = level;
			return this;
		}

		public Builder uses(int uses)
		{
			this.uses = uses;
			return this;
		}

		public Builder speed(float speed)
		{
			this.speed = speed;
			return this;
		}

		public Builder damage(float damage)
		{
			this.damage = damage;
			return this;
		}

		public Builder enchantmentValue(int enchantmentValue)
		{
			this.enchantmentValue = enchantmentValue;
			return this;
		}

		public Builder repairIngredient(NonnullSupplier<Ingredient> repairIngredient)
		{
			this.repairIngredient = repairIngredient;
			return this;
		}

		public Builder repairIngredient(Tag<Item> repairIngredient)
		{
			return repairIngredient(() -> Ingredient.of(repairIngredient));
		}

		public Builder repairIngredient(ItemLike... repairIngredients)
		{
			return repairIngredient(() -> Ingredient.of(repairIngredients));
		}

		public ItemTier build()
		{
			return new ItemTier(this);
		}
	}
}
