package xyz.apex.minecraft.apexcore.shared.platform;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.util.EnchancedTier;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

/**
 * Use {@link xyz.apex.minecraft.apexcore.shared.util.TierBuilder} or {@link xyz.apex.minecraft.apexcore.shared.util.ArmorMaterialBuilder}
 *
 * You should never need to directly make use of this
 */
@ApiStatus.Internal
public interface PlatformTierRegistry extends PlatformHolder
{
    default EnchancedTier registerTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> miningLevelTag)
    {
        return new BasicTier(uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, miningLevelTag);
    }

    default ArmorMaterial registerArmorMaterial(ResourceLocation registryName, int enchantmentValue, Supplier<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient, float toughness, float knockbackResistance, ToIntFunction<EquipmentSlot> durabilityForSlot, ToIntFunction<EquipmentSlot> defenseForSlot)
    {
        return new BasicArmorMaterial(registryName, enchantmentValue, equipSound, repairIngredient, toughness, knockbackResistance, durabilityForSlot, defenseForSlot);
    }

    record BasicTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> miningLevelTag) implements EnchancedTier
    {
        @Override
        public int getUses()
        {
            return uses;
        }

        @Override
        public float getSpeed()
        {
            return speed;
        }

        @Override
        public float getAttackDamageBonus()
        {
            return attackDamageBonus;
        }

        @Override
        public int getLevel()
        {
            return level;
        }

        @Override
        public int getEnchantmentValue()
        {
            return enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return repairIngredient.get();
        }

        @Override
        public TagKey<Block> getMiningLevelTag()
        {
            return miningLevelTag;
        }
    }

    record BasicArmorMaterial(ResourceLocation registryName, int enchantmentValue, Supplier<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient, float toughness, float knockbackResistance, ToIntFunction<EquipmentSlot> durabilityForSlot, ToIntFunction<EquipmentSlot> defenseForSlot) implements ArmorMaterial
    {
        @Override
        public int getDurabilityForSlot(EquipmentSlot slot)
        {
            return durabilityForSlot.applyAsInt(slot);
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slot)
        {
            return defenseForSlot.applyAsInt(slot);
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
        public String getName()
        {
            return registryName.toString();
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
