package xyz.apex.minecraft.apexcore.shared.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.shared.hooks.ArmorMaterialHooks;
import xyz.apex.minecraft.apexcore.shared.platform.Platform;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class ArmorMaterialBuilder
{
    private ToIntFunction<EquipmentSlot> durabilityForSlot = ArmorMaterials.LEATHER::getDurabilityForSlot;
    private ToIntFunction<EquipmentSlot> defenseForSlot = ArmorMaterials.LEATHER::getDefenseForSlot;
    private int enchantmentValue = ArmorMaterials.LEATHER.getEnchantmentValue();
    private Supplier<SoundEvent> equipSound = ArmorMaterials.LEATHER::getEquipSound;
    private Supplier<Ingredient> repairIngredient = ArmorMaterials.LEATHER::getRepairIngredient;
    private float toughness = ArmorMaterials.LEATHER.getToughness();
    private float knockbackResistance = ArmorMaterials.LEATHER.getKnockbackResistance();
    private final ResourceLocation registryName;

    private ArmorMaterialBuilder(ResourceLocation registryName)
    {
        this.registryName = registryName;
    }

    public ArmorMaterialBuilder durabilityForSlot(ToIntFunction<EquipmentSlot> durabilityForSlot)
    {
        this.durabilityForSlot = durabilityForSlot;
        return this;
    }

    public ArmorMaterialBuilder durabilityFromModifier(int durabilityModifier)
    {
        return durabilityForSlot(slot -> ArmorMaterialHooks.getHealthPerSlot()[slot.getIndex()] * durabilityModifier);
    }

    public ArmorMaterialBuilder defenseForSlot(ToIntFunction<EquipmentSlot> defenseForSlot)
    {
        this.defenseForSlot = defenseForSlot;
        return this;
    }

    public ArmorMaterialBuilder defenseFromSlotProtections(int feet, int legs, int chest, int head)
    {
        return defenseForSlot(slot -> switch(slot) {
            case FEET -> feet;
            case LEGS -> legs;
            case CHEST -> chest;
            case HEAD -> head;
            default -> throw new IllegalArgumentException("Unknown EquipmentSlot for ArmorMaterial#getDefenseForSlot: %s".formatted(slot.getName()));
        });
    }

    public ArmorMaterialBuilder enchantmentValue(int enchantmentValue)
    {
        this.enchantmentValue = enchantmentValue;
        return this;
    }

    public ArmorMaterialBuilder equipSound(Supplier<SoundEvent> equipSound)
    {
        this.equipSound = equipSound;
        return this;
    }

    public ArmorMaterialBuilder repairIngredient(Supplier<Ingredient> repairIngredient)
    {
        this.repairIngredient = repairIngredient;
        return this;
    }

    public ArmorMaterialBuilder repairItem(Supplier<? extends ItemLike> repairIngredient)
    {
        return repairIngredient(() -> Ingredient.of(repairIngredient.get()));
    }

    public ArmorMaterialBuilder toughness(float toughness)
    {
        this.toughness = toughness;
        return this;
    }

    public ArmorMaterialBuilder knockbackResistance(float knockbackResistance)
    {
        this.knockbackResistance = knockbackResistance;
        return this;
    }

    public ArmorMaterial build()
    {
        return Platform.INSTANCE.tierRegistry().registerArmorMaterial(registryName, enchantmentValue, equipSound, repairIngredient, toughness, knockbackResistance, durabilityForSlot, defenseForSlot);
    }

    public static ArmorMaterialBuilder builder(ResourceLocation registryName)
    {
        return new ArmorMaterialBuilder(registryName);
    }

    public static ArmorMaterialBuilder builder(String modId, String name)
    {
        return builder(new ResourceLocation(modId, name));
    }
}
