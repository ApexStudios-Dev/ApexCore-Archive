package xyz.apex.forge.utility.registrator.helper;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import xyz.apex.java.utility.Lazy;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

@SuppressWarnings("CommentedOutCode")
public final class ArmorMaterial implements net.minecraft.world.item.ArmorMaterial
{
	// public static final int[] HEALTH_PER_SLOT = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.item.ArmorMaterial.class, null, "field_77882_bY");
	public static final int[] HEALTH_PER_SLOT = net.minecraft.world.item.ArmorMaterials.HEALTH_PER_SLOT;
	public static final EquipmentSlot[] ARMOR_SLOT_TYPES = Arrays.stream(EquipmentSlot.values()).filter(slotType -> slotType.getType() == EquipmentSlot.Type.ARMOR).toArray(EquipmentSlot[]::new);

	public final ResourceLocation name;
	public final int durabilityMultiplier;
	public final int[] slotProtections;
	public final int enchantmentValue;
	public final Lazy<SoundEvent> sound;
	public final float toughness;
	public final float knockbackResistance;
	public final Lazy<Ingredient> repairIngredient;

	public ArmorMaterial(ResourceLocation name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, NonnullSupplier<SoundEvent> sound, float toughness, float knockbackResistance, NonnullSupplier<Ingredient> repairIngredient)
	{
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.slotProtections = slotProtections;
		this.enchantmentValue = enchantmentValue;
		this.sound = Lazy.of(sound, true);
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = Lazy.of(repairIngredient, true);
	}

	private ArmorMaterial(Builder builder)
	{
		this.name = builder.name;
		this.durabilityMultiplier = builder.durabilityMultiplier;
		this.enchantmentValue = builder.enchantmentValue;
		this.toughness = builder.toughness;
		this.knockbackResistance = builder.knockbackResistance;

		sound = Lazy.of(builder.sound, true);
		repairIngredient = Lazy.of(builder.repairIngredient, true);

		slotProtections = builder.slotDefenseMap
				.entrySet()
				.stream()
				.sorted(Comparator.comparingInt(entry -> entry.getKey().getIndex()))
				.mapToInt(Map.Entry::getValue)
				.toArray();
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlot slotType)
	{
		return HEALTH_PER_SLOT[slotType.getIndex()] * durabilityMultiplier;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlot slotType)
	{
		return slotProtections[slotType.getIndex()];
	}

	@Override
	public int getEnchantmentValue()
	{
		return enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound()
	{
		return sound.get();
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return repairIngredient.get();
	}

	@Override
	public String getName()
	{
		return name.toString();
	}

	@Override
	public float getToughness()
	{
		return toughness;
	}

	@Override
	public float getKnockbackResistance()
	{
		return knockbackResistance;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(o instanceof ArmorMaterial material)
			return durabilityMultiplier == material.durabilityMultiplier && enchantmentValue == material.enchantmentValue && Float.compare(material.toughness, toughness) == 0 && Float.compare(material.knockbackResistance, knockbackResistance) == 0 && Objects.equal(name, material.name) && Objects.equal(slotProtections, material.slotProtections) && Objects.equal(sound, material.sound) && Objects.equal(repairIngredient, material.repairIngredient);
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(name, durabilityMultiplier, slotProtections, enchantmentValue, sound, toughness, knockbackResistance, repairIngredient);
	}

	@Override
	public String toString()
	{
		return "ArmorMaterial{name=%s, durabilityMultiplier=%d, slotProtections=%s, enchantmentValue=%d, sound=%s, toughness=%s, knockbackResistance=%s, repairIngredient=%s}".formatted(name, durabilityMultiplier, Arrays.toString(slotProtections), enchantmentValue, sound, toughness, knockbackResistance, repairIngredient);
	}

	public static Builder builder(ResourceLocation name)
	{
		return new Builder(name);
	}

	public static Builder builder(String namespace, String name)
	{
		return builder(new ResourceLocation(namespace, name));
	}

	public static Builder copy(ResourceLocation name, ArmorMaterial armorMaterial)
	{
		return builder(name).copy(armorMaterial);
	}

	public static Builder copy(String namespace, String name, ArmorMaterial armorMaterial)
	{
		return copy(new ResourceLocation(namespace, name), armorMaterial);
	}

	// calculate the durability multiplier by dividing by the health in current slot
	// going by vanilla math this will result in correct value
	// <durability_multiplier> * <slot_health> <=> <durability> / <slot_health>
	public static int getDurabilityMultiplierForSlot(ArmorMaterial armorMaterial, EquipmentSlot slotType)
	{
		return armorMaterial.getDurabilityForSlot(slotType) / HEALTH_PER_SLOT[slotType.getIndex()];
	}

	// Method to pull value straight from vanilla armor material
	/*public static int getDurabilityMultiplier(net.minecraft.item.ArmorMaterial armorMaterial)
	{
		return ObfuscationReflectionHelper.getPrivateValue(net.minecraft.item.ArmorMaterial.class, armorMaterial, "field_78048_f");
	}*/

	@SuppressWarnings({ "unused", "UnusedReturnValue" })
	public static final class Builder
	{
		private final Map<EquipmentSlot, Integer> slotDefenseMap = Maps.newEnumMap(EquipmentSlot.class);
		private final ResourceLocation name;

		private int durabilityMultiplier = -1;
		private int enchantmentValue = -1;
		private NonnullSupplier<SoundEvent> sound = () -> SoundEvents.ARMOR_EQUIP_GENERIC;
		private float toughness = -1;
		private float knockbackResistance = -1;
		private NonnullSupplier<Ingredient> repairIngredient = () -> Ingredient.EMPTY;

		private Builder(ResourceLocation name)
		{
			this.name = name;
		}

		public Builder copy(Builder builder)
		{
			slotDefenseMap.clear();
			slotDefenseMap.putAll(builder.slotDefenseMap);

			return durabilityMultiplier(builder.durabilityMultiplier)
					.enchantmentValue(builder.enchantmentValue)
					.sound(builder.sound)
					.toughness(builder.toughness)
					.knockbackResistance(builder.knockbackResistance)
					.repairIngredient(builder.repairIngredient)
			;
		}

		public Builder copy(ArmorMaterial armorMaterial)
		{
			slotDefenseMap.clear();
			Arrays.stream(ARMOR_SLOT_TYPES).forEach(slotType -> defenseForSlot(slotType, armorMaterial.getDefenseForSlot(slotType)));
			int durabilityMultiplier = Arrays.stream(ARMOR_SLOT_TYPES).mapToInt(slotType -> getDurabilityMultiplierForSlot(armorMaterial, slotType)).max().orElse(0);

			return durabilityMultiplier(durabilityMultiplier)
					.enchantmentValue(armorMaterial.getEnchantmentValue())
					.sound(armorMaterial::getEquipSound)
					.toughness(armorMaterial.getToughness())
					.knockbackResistance(armorMaterial.getKnockbackResistance())
					.repairIngredient(armorMaterial::getRepairIngredient)
			;
		}

		public Builder defenseForSlot(EquipmentSlot slotType, int slotDefense)
		{
			Validate.isTrue(slotType.getType() == EquipmentSlot.Type.ARMOR);
			slotDefenseMap.put(slotType, slotDefense);
			return this;
		}

		public Builder durabilityMultiplier(int durabilityMultiplier)
		{
			this.durabilityMultiplier = durabilityMultiplier;
			return this;
		}

		public Builder enchantmentValue(int enchantmentValue)
		{
			this.enchantmentValue = enchantmentValue;
			return this;
		}

		public Builder sound(NonnullSupplier<SoundEvent> sound)
		{
			this.sound = sound;
			return this;
		}

		public Builder toughness(float toughness)
		{
			this.toughness = toughness;
			return this;
		}

		public Builder knockbackResistance(float knockbackResistance)
		{
			this.knockbackResistance = knockbackResistance;
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

		public ArmorMaterial build()
		{
			return new ArmorMaterial(this);
		}
	}
}
