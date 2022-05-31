package xyz.apex.forge.utility.registrator.entry;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fmllegacy.RegistryObject;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;

import java.util.Map;

public final class EnchantmentEntry<ENCHANTMENT extends Enchantment> extends RegistryEntry<ENCHANTMENT>
{
	public EnchantmentEntry(AbstractRegistrator<?> registrator, RegistryObject<ENCHANTMENT> delegate)
	{
		super(registrator, delegate);
	}

	public ENCHANTMENT asEnchantment()
	{
		return get();
	}

	public boolean isInEnchantmentTag(Tag<Enchantment> tag)
	{
		return asEnchantment().is(tag);
	}

	public boolean isEnchantment(Enchantment enchantment)
	{
		return asEnchantment() == enchantment;
	}

	public Map<EquipmentSlot, ItemStack> getSlotItems(LivingEntity entity)
	{
		return asEnchantment().getSlotItems(entity);
	}

	public Enchantment.Rarity getRarity()
	{
		return asEnchantment().getRarity();
	}

	public int getMinLevel()
	{
		return asEnchantment().getMinLevel();
	}

	public int getMaxLevel()
	{
		return asEnchantment().getMaxLevel();
	}

	public int getMinCost(int level)
	{
		return asEnchantment().getMinCost(level);
	}

	public  int getMaxCost(int level)
	{
		return asEnchantment().getMaxCost(level);
	}

	public int getDamageProtection(int level, DamageSource damageSource)
	{
		return asEnchantment().getDamageProtection(level, damageSource);
	}

	public float getDamageBonus(int level, MobType creatureAttribute)
	{
		return asEnchantment().getDamageBonus(level, creatureAttribute);
	}

	public boolean isCompatibleWith(Enchantment enchantment)
	{
		return asEnchantment().isCompatibleWith(enchantment);
	}

	public Component getFullName(int level)
	{
		return asEnchantment().getFullname(level);
	}

	public boolean canEnchant(ItemStack stack)
	{
		return asEnchantment().canEnchant(stack);
	}

	public boolean isTreasureOnly()
	{
		return asEnchantment().isTreasureOnly();
	}

	public boolean isCurse()
	{
		return asEnchantment().isCurse();
	}

	public boolean isDiscoverable()
	{
		return asEnchantment().isDiscoverable();
	}

	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return asEnchantment().canApplyAtEnchantingTable(stack);
	}

	public boolean isAllowedOnBooks()
	{
		return asEnchantment().isAllowedOnBooks();
	}

	public static <ENCHANTMENT extends Enchantment> EnchantmentEntry<ENCHANTMENT> cast(RegistryEntry<ENCHANTMENT> registryEntry)
	{
		return cast(EnchantmentEntry.class, registryEntry);
	}

	public static <ENCHANTMENT extends Enchantment> EnchantmentEntry<ENCHANTMENT> cast(com.tterrag.registrate.util.entry.RegistryEntry<ENCHANTMENT> registryEntry)
	{
		return cast(EnchantmentEntry.class, registryEntry);
	}
}
