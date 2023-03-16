package xyz.apex.minecraft.apexcore.common.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.common.util.EnhancedArmorMaterial;
import xyz.apex.minecraft.apexcore.common.util.function.Lazy;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class ArmorMaterialBuilder
{
    private final String modId;
    private final String registryName;

    private ToIntFunction<ArmorItem.Type> durabilityForType = ArmorMaterials.LEATHER::getDurabilityForType;
    private ToIntFunction<ArmorItem.Type> defenseForType = ArmorMaterials.LEATHER::getDefenseForType;
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

    public ArmorMaterialBuilder durabilityForSlot(ToIntFunction<ArmorItem.Type> durabilityForSlot)
    {
        this.durabilityForType = durabilityForSlot;
        return this;
    }

    public ArmorMaterialBuilder durabilityFromModifier(int durabilityModifier)
    {
        return durabilityForSlot(slot -> ArmorMaterials.HEALTH_FUNCTION_FOR_TYPE.get(slot) * durabilityModifier);
    }

    public ArmorMaterialBuilder defenseForSlot(ToIntFunction<ArmorItem.Type> defenseForSlot)
    {
        this.defenseForType = defenseForSlot;
        return this;
    }

    public ArmorMaterialBuilder defenseFromSlotProtections(int boots, int leggings, int chestplate, int helmet)
    {
        return defenseForSlot(slot -> switch(slot) {
            case BOOTS -> boots;
            case LEGGINGS -> leggings;
            case CHESTPLATE -> chestplate;
            case HELMET -> helmet;
            default -> throw new IllegalArgumentException("Unknown ArmorItem.Type for ArmorMaterial#getDefenseForType: %s".formatted(slot.getName()));
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
        return new ArmorMaterial(this);
    }

    public static ArmorMaterialBuilder builder(String modId, String registryName)
    {
        return new ArmorMaterialBuilder(modId, registryName);
    }

    public static final class ArmorMaterial implements EnhancedArmorMaterial
    {
        private final ToIntFunction<ArmorItem.Type> durabilityForType;
        private final ToIntFunction<ArmorItem.Type> defenseForType;
        private final int enchantmentValue;
        private final Supplier<SoundEvent> equipSound;
        private final Supplier<Ingredient> repairIngredient;
        private final float toughness;
        private final float knockbackResistance;
        private final ResourceLocation registryName;

        private ArmorMaterial(ArmorMaterialBuilder builder)
        {
            durabilityForType = builder.durabilityForType;
            defenseForType = builder.defenseForType;
            enchantmentValue = builder.enchantmentValue;
            equipSound = Lazy.of(builder.equipSound);
            repairIngredient = Lazy.of(builder.repairIngredient);
            toughness = builder.toughness;
            knockbackResistance = builder.knockbackResistance;

            registryName = new ResourceLocation(builder.modId, builder.registryName);
        }

        @Override
        public ResourceLocation getRegistryName()
        {
            return registryName;
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type slot)
        {
            return durabilityForType.applyAsInt(slot);
        }

        @Override
        public int getDefenseForType(ArmorItem.Type slot)
        {
            return defenseForType.applyAsInt(slot);
        }

        @Override
        public int getEnchantmentValue()
        {
            return enchantmentValue;
        }

        @Override
        public SoundEvent getEquipSound()
        {
            return equipSound.get();
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return repairIngredient.get();
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
    }
}
