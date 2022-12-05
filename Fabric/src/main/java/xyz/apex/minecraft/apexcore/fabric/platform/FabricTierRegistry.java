package xyz.apex.minecraft.apexcore.fabric.platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformTierRegistry;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class FabricTierRegistry extends FabricHolder implements PlatformTierRegistry
{
    private static final Logger LOGGER = LogManager.getLogger();

    FabricTierRegistry(FabricPlatform platform)
    {
        super(platform);
    }

    @Override
    public TierWithMiningLevel registerTier(String modId, String name, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, TagKey<Block> miningLevelTag)
    {
        LOGGER.info("Registering Item Tier: {}:{}", modId, name);
        return new TierWithMiningLevelImpl(new TierImpl(new ResourceLocation(modId, name), uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient), miningLevelTag);
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
        public Ingredient getRepairIngredient()
        {
            return tier.getRepairIngredient();
        }
    }

    private record TierImpl(
            ResourceLocation registryName,
            int uses,
            float speed,
            float attackDamageBonus,
            int level,
            int enchantmentValue,
            Supplier<Ingredient> repairIngredient
    ) implements Tier
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
