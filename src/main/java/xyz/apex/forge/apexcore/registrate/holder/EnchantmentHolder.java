package xyz.apex.forge.apexcore.registrate.holder;

import net.minecraft.world.item.enchantment.Enchantment;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.EnchantmentBuilder;
import xyz.apex.forge.apexcore.registrate.builder.factory.EnchantmentFactory;

@SuppressWarnings("unchecked")
public interface EnchantmentHolder<OWNER extends CoreRegistrate<OWNER> & EnchantmentHolder<OWNER>>
{
	private OWNER self()
	{
		return (OWNER) this;
	}

	default <ENCHANTMENT extends Enchantment> EnchantmentBuilder<OWNER, ENCHANTMENT, OWNER> enchantment(EnchantmentFactory<ENCHANTMENT> factory)
	{
		return enchantment(self(), factory);
	}

	default <ENCHANTMENT extends Enchantment> EnchantmentBuilder<OWNER, ENCHANTMENT, OWNER> enchantment(String name, EnchantmentFactory<ENCHANTMENT> factory)
	{
		return enchantment(self(), name, factory);
	}

	default <ENCHANTMENT extends Enchantment, PARENT> EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> enchantment(PARENT parent, EnchantmentFactory<ENCHANTMENT> factory)
	{
		return enchantment(parent, self().currentName(), factory);
	}

	default <ENCHANTMENT extends Enchantment, PARENT> EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> enchantment(PARENT parent, String name, EnchantmentFactory<ENCHANTMENT> factory)
	{
		return self().entry(name, callback -> new EnchantmentBuilder<>(self(), parent, name, callback, factory));
	}
}
