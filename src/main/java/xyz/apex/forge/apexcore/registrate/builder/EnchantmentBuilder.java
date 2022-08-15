package xyz.apex.forge.apexcore.registrate.builder;

import com.tterrag.registrate.builders.BuilderCallback;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.ForgeRegistries;

import xyz.apex.forge.apexcore.core.init.ACRegistry;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.factory.EnchantmentFactory;
import xyz.apex.forge.apexcore.registrate.entry.EnchantmentEntry;
import xyz.apex.forge.apexcore.registrate.holder.EnchantmentHolder;

import java.util.Collections;
import java.util.EnumSet;

public final class EnchantmentBuilder<
		OWNER extends CoreRegistrate<OWNER> & EnchantmentHolder<OWNER>,
		ENCHANTMENT extends Enchantment,
		PARENT
> extends AbstractBuilder<OWNER, Enchantment, ENCHANTMENT, PARENT, EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT>, EnchantmentEntry<ENCHANTMENT>>
{
	private final EnchantmentFactory<ENCHANTMENT> enchantmentFactory;
	private Enchantment.Rarity enchantmentRarity = Enchantment.Rarity.COMMON;
	private EnchantmentCategory enchantmentCategory = ACRegistry.ENCHANTMENT_CATEGORY_NONE;
	private final EnumSet<EquipmentSlot> equipmentSlots = EnumSet.noneOf(EquipmentSlot.class);

	public EnchantmentBuilder(OWNER owner, PARENT parent, String name, BuilderCallback callback, EnchantmentFactory<ENCHANTMENT> enchantmentFactory)
	{
		super(owner, parent, name, callback, Enchantment.class, ForgeRegistries.Keys.ENCHANTMENTS, EnchantmentEntry::new, EnchantmentEntry::cast);

		this.enchantmentFactory = enchantmentFactory;
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> rarity(Enchantment.Rarity enchantmentRarity)
	{
		this.enchantmentRarity = enchantmentRarity;
		return this;
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> category(EnchantmentCategory enchantmentCategory)
	{
		this.enchantmentCategory = enchantmentCategory;
		return this;
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> armorSlots()
	{
		return slot(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> slot(EquipmentSlot... slots)
	{
		Collections.addAll(equipmentSlots, slots);
		return this;
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> defaultLang()
	{
		return lang(Enchantment::getDescriptionId);
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> lang(String name)
	{
		return lang(Enchantment::getDescriptionId, name);
	}

	@Override
	protected ENCHANTMENT createEntry()
	{
		return enchantmentFactory.create(enchantmentRarity, enchantmentCategory, equipmentSlots.toArray(EquipmentSlot[]::new));
	}
}