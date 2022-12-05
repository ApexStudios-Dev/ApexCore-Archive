package xyz.apex.minecraft.apexcore.forge.platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformTierRegistry;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class ForgeTierRegistry extends ForgeHolder implements PlatformTierRegistry
{
    private static final Logger LOGGER = LogManager.getLogger();

    ForgeTierRegistry(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public TierWithMiningLevel registerTier(String modId, String name, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, TagKey<Block> miningLevelTag)
    {
        LOGGER.info("Registering Item Tier: {}:{}", modId, name);
        return new TierWithMiningLevelImpl(new ForgeTier(level, uses, speed, attackDamageBonus, enchantmentValue, miningLevelTag, repairIngredient), miningLevelTag);
    }

    @Override
    public ArmorMaterial registerArmorMaterial(String modId, String name, int enchantmentValue, Supplier<SoundEvent> equipSound, Supplier<Ingredient> repairIngredient, float toughness, float knockBackResistance, ToIntFunction<EquipmentSlot> durabilityForSlot, ToIntFunction<EquipmentSlot> defenseForSlot)
    {
        LOGGER.info("Registering ArmorMaterial: {}:{}", modId, name);
        return new ArmorMaterialImpl(new ResourceLocation(modId, name), enchantmentValue, equipSound, repairIngredient, toughness, knockBackResistance, durabilityForSlot, defenseForSlot);
    }

    @Override
    public ArmorMaterialTier registerMerged(Tier tier, ArmorMaterial material)
    {
        return new ArmorMaterialTierImpl(tier, material);
    }

    private record ArmorMaterialTierImpl(Tier tier, ArmorMaterial armorMaterial) implements ArmorMaterialTier
    {
        @Nullable
        @Override
        public TagKey<Block> getTag()
        {
            return tier instanceof TierWithMiningLevel getter ? getter.getMiningLevelTag() : tier.getTag();
        }
    }

    private record TierWithMiningLevelImpl(Tier tier, TagKey<Block> miningLevelTag) implements TierWithMiningLevel
    {
        @Override
        public TagKey<Block> getMiningLevelTag()
        {
            return miningLevelTag;
        }

        @Override
        public int getUses()
        {
            return tier.getUses();
        }

        @Override
        public float getSpeed()
        {
            return tier.getSpeed();
        }

        @Override
        public float getAttackDamageBonus()
        {
            return tier.getAttackDamageBonus();
        }

        @Override
        public int getLevel()
        {
            return tier.getUses();
        }

        @Override
        public int getEnchantmentValue()
        {
            return tier.getEnchantmentValue();
        }

        @Override
        public TagKey<Block> getTag()
        {
            return miningLevelTag;
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return tier.getRepairIngredient();
        }
    }

    private record ArmorMaterialImpl(
            ResourceLocation registryName,
            int enchantmentValue,
            Supplier<SoundEvent> equipSound,
            Supplier<Ingredient> repairIngredient,
            float toughness,
            float knockBackResistance,
            ToIntFunction<EquipmentSlot> durabilityForSlot,
            ToIntFunction<EquipmentSlot> defenseForSlot
    ) implements ArmorMaterial
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
            return knockBackResistance;
        }
    }
}
