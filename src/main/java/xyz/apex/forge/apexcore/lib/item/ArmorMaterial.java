package xyz.apex.forge.apexcore.lib.item;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.Validate;
import xyz.apex.forge.apexcore.lib.util.reflection.Fields;

import javax.annotation.Nullable;

public class ArmorMaterial implements IArmorMaterial
{
	@Nullable private static int[] healthPerSlot = null;

	public final String name;
	public final int durabilityMultiplier;
	public final int[] slotProtections;
	public final int enchantmentValue;
	public final SoundEvent sound;
	public final float toughness;
	public final float knockbackResistance;
	public final NonNullLazyValue<DataIngredient> repairIngredient;

	public ArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, NonNullSupplier<DataIngredient> repairIngredient)
	{
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.slotProtections = slotProtections;
		this.enchantmentValue = enchantmentValue;
		this.sound = sound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = new NonNullLazyValue<>(repairIngredient);
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlotType slotType)
	{
		return getEquipmentSlotHealth(slotType) * durabilityMultiplier;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlotType slotType)
	{
		return MathHelper.positiveModulo(slotType.getIndex(), slotProtections.length);
	}

	@Override
	public int getEnchantmentValue()
	{
		return enchantmentValue;
	}

	@Override
	public SoundEvent getEquipSound()
	{
		return sound;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		return repairIngredient.get();
	}

	@Override
	public String getName()
	{
		return name;
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

	public static int[] getHealthPerSlot()
	{
		if(healthPerSlot == null)
			healthPerSlot = Fields.ARMOR_MATERIAL_HEALTH_PER_SLOT.getPrivateValue(null);
		return Validate.notNull(healthPerSlot);
	}

	public static int getEquipmentSlotHealth(EquipmentSlotType slotType)
	{
		int[] health = getHealthPerSlot();
		return health[MathHelper.positiveModulo(slotType.getIndex(), health.length)];
	}
}
