package xyz.apex.minecraft.apexcore.shared.platform;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public interface PlatformTierRegistry extends PlatformHolder
{
    TierWithMiningLevel registerTier(String modId, String name, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, TagKey<Block> miningLevelTag);

    ArmorMaterial registerArmorMaterial(String modId, String name, int enchantmentValue, Supplier<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient, float toughness, float knockBackResistance, ToIntFunction<EquipmentSlot> durabilityForSlot, ToIntFunction<EquipmentSlot> defenseForSlot);

    default ArmorMaterialTier registerMerged(
            // Common
            String modId, String name, int enchantmentValue, Supplier<Ingredient> repairIngredient,
            // Tier
            int uses, float speed, float attackDamageBonus, int level, TagKey<Block> miningLevelTag,
            // ArmorMaterial
            Supplier<SoundEvent> equipSound, float toughness, float knockBackResistance, ToIntFunction<EquipmentSlot> durabilityForSlot, ToIntFunction<EquipmentSlot> defenseForSlot
    ) {
        var tier = registerTier(modId, name, uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, miningLevelTag);
        var material = registerArmorMaterial(modId, name, enchantmentValue, equipSound, repairIngredient, toughness, knockBackResistance, durabilityForSlot, defenseForSlot);
        return registerMerged(tier, material);
    }

    @ApiStatus.Internal ArmorMaterialTier registerMerged(Tier tier, ArmorMaterial material);

    interface TierWithMiningLevel extends Tier
    {
        TagKey<Block> getMiningLevelTag();

        @Deprecated @Override int getLevel();
    }

    interface ArmorMaterialTier extends TierWithMiningLevel, ArmorMaterial
    {
        // region: Common
        Tier tier();
        ArmorMaterial armorMaterial();

        @Override
        default int getEnchantmentValue()
        {
            return tier().getEnchantmentValue();
        }

        @Override
        default Ingredient getRepairIngredient()
        {
            return tier().getRepairIngredient();
        }
        // endregion

        // region: Tier
        @Override
        default int getUses()
        {
            return tier().getUses();
        }

        @Override
        default float getSpeed()
        {
            return tier().getSpeed();
        }

        @Override
        default float getAttackDamageBonus()
        {
            return tier().getAttackDamageBonus();
        }

        @Override
        default int getLevel()
        {
            return tier().getLevel();
        }

        @Override
        @Nullable
        default TagKey<Block> getMiningLevelTag()
        {
            return tier() instanceof TierWithMiningLevel tier ? tier.getMiningLevelTag() : null;
        }
        // endregion

        // region: ArmorMaterial
        @Override
        default int getDurabilityForSlot(EquipmentSlot slot)
        {
            return armorMaterial().getDurabilityForSlot(slot);
        }

        @Override
        default int getDefenseForSlot(EquipmentSlot slot)
        {
            return armorMaterial().getDurabilityForSlot(slot);
        }

        @Override
        default SoundEvent getEquipSound()
        {
            return armorMaterial().getEquipSound();
        }

        @Override
        default String getName()
        {
            return armorMaterial().getName();
        }

        @Override
        default float getToughness()
        {
            return armorMaterial().getToughness();
        }

        @Override
        default float getKnockbackResistance()
        {
            return armorMaterial().getKnockbackResistance();
        }
        // endregion
    }
}
