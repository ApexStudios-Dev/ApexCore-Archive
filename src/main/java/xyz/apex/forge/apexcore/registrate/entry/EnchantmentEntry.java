package xyz.apex.forge.apexcore.registrate.entry;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.RegistryObject;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

public final class EnchantmentEntry<ENCHANTMENT extends Enchantment> extends RegistryEntry<ENCHANTMENT>
{
	public EnchantmentEntry(CoreRegistrate<?> owner, RegistryObject<ENCHANTMENT> delegate)
	{
		super(owner, delegate);
	}

	public static <ENCHANTMENT extends Enchantment> EnchantmentEntry<ENCHANTMENT> cast(com.tterrag.registrate.util.entry.RegistryEntry<ENCHANTMENT> entry)
	{
		return cast(EnchantmentEntry.class, entry);
	}
}