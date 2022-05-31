package xyz.apex.forge.utility.registrator.builder;

import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.entry.EnchantmentEntry;
import xyz.apex.forge.utility.registrator.factory.EnchantmentFactory;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class EnchantmentBuilder<OWNER extends AbstractRegistrator<OWNER>, ENCHANTMENT extends Enchantment, PARENT> extends RegistratorBuilder<OWNER, Enchantment, ENCHANTMENT, PARENT, EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT>, EnchantmentEntry<ENCHANTMENT>>
{
	private final EnchantmentFactory<ENCHANTMENT> enchantmentFactory;
	private final Set<EquipmentSlot> slotTypes = EnumSet.noneOf(EquipmentSlot.class);
	private final EnchantmentCategory enchantmentCategory;
	private Enchantment.Rarity rarity = Enchantment.Rarity.COMMON;

	public EnchantmentBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback, EnchantmentCategory enchantmentCategory, EnchantmentFactory<ENCHANTMENT> enchantmentFactory)
	{
		super(owner, parent, registryName, callback, Enchantment.class, EnchantmentEntry::new, EnchantmentEntry::cast);

		this.enchantmentCategory = enchantmentCategory;
		this.enchantmentFactory = enchantmentFactory;
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> rarity(Enchantment.Rarity rarity)
	{
		this.rarity = rarity;
		return this;
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> slotType(EquipmentSlot slotType)
	{
		slotTypes.add(slotType);
		return this;
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> slotTypes(EquipmentSlot... slotTypes)
	{
		Collections.addAll(this.slotTypes, slotTypes);
		return this;
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> armorSlotTypes()
	{
		return slotTypes(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> defaultLang()
	{
		return lang(Enchantment::getDescriptionId);
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> lang(String name)
	{
		return lang(Enchantment::getDescriptionId, name);
	}

	@SafeVarargs
	public final EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> tag(Tag.Named<Enchantment>... tags)
	{
		return tag(AbstractRegistrator.ENCHANTMENT_TAGS_PROVIDER, tags);
	}

	@SafeVarargs
	public final EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> removeTag(Tag.Named<Enchantment>... tags)
	{
		return removeTags(AbstractRegistrator.ENCHANTMENT_TAGS_PROVIDER, tags);
	}

	public EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> lang(String languageKey, String localizedValue)
	{
		return lang(languageKey, Enchantment::getDescriptionId, localizedValue);
	}

	@Override
	protected @NonnullType ENCHANTMENT createEntry()
	{
		var slotTypes = this.slotTypes.toArray(new EquipmentSlot[0]);
		return enchantmentFactory.create(rarity, enchantmentCategory, slotTypes);
	}
}
