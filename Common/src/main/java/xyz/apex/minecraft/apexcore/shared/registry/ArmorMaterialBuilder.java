package xyz.apex.minecraft.apexcore.shared.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.shared.mixin.accessors.ArmorMaterialsAccessor;
import xyz.apex.minecraft.apexcore.shared.platform.Platform;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class ArmorMaterialBuilder
{
    private final String modId;
    private final String registryName;

    private ToIntFunction<EquipmentSlot> durabilityForSlot = ArmorMaterials.LEATHER::getDurabilityForSlot;
    private ToIntFunction<EquipmentSlot> defenseForSlot = ArmorMaterials.LEATHER::getDefenseForSlot;
    private int enchantmentValue = ArmorMaterials.LEATHER.getEnchantmentValue();
    private Supplier<SoundEvent> equipSound = ArmorMaterials.LEATHER::getEquipSound;
    private Supplier<Ingredient> repairIngredient = ArmorMaterials.LEATHER::getRepairIngredient;
    private float toughness = ArmorMaterials.LEATHER.getToughness();
    private float knockbackResistance = ArmorMaterials.LEATHER.getKnockbackResistance();

    private ArmorMaterialBuilder(String modId, String registryName)
    {
        this.modId = modId;
        this.registryName = registryName;
    }

    public String getModId()
    {
        return modId;
    }

    public String getRegistryName()
    {
        return registryName;
    }

    public ToIntFunction<EquipmentSlot> getDurabilityForSlot()
    {
        return durabilityForSlot;
    }

    public ToIntFunction<EquipmentSlot> getDefenseForSlot()
    {
        return defenseForSlot;
    }

    public int getEnchantmentValue()
    {
        return enchantmentValue;
    }

    public Supplier<SoundEvent> getEquipSound()
    {
        return equipSound;
    }

    public Supplier<Ingredient> getRepairIngredient()
    {
        return repairIngredient;
    }

    public float getToughness()
    {
        return toughness;
    }

    public float getKnockbackResistance()
    {
        return knockbackResistance;
    }

    public ArmorMaterialBuilder durabilityForSlot(ToIntFunction<EquipmentSlot> durabilityForSlot)
    {
        this.durabilityForSlot = durabilityForSlot;
        return this;
    }

    public ArmorMaterialBuilder durabilityFromModifier(int durabilityModifier)
    {
        return durabilityForSlot(slot -> ArmorMaterialsAccessor.getHealthPerSlot()[slot.getIndex()] * durabilityModifier);
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

    public ArmorMaterial register()
    {
        return Platform.INSTANCE.registries().registerArmorMaterial(this);
    }

    public static ArmorMaterialBuilder builder(String modId, String registryName)
    {
        return new ArmorMaterialBuilder(modId, registryName);
    }
}
