package xyz.apex.minecraft.apexcore.forge.platform;

import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformTierRegistry;
import xyz.apex.minecraft.apexcore.shared.util.EnchancedTier;

import java.util.function.Supplier;

public final class ForgeTierRegistry extends ForgeHolder implements PlatformTierRegistry
{
    ForgeTierRegistry(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public EnchancedTier registerTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> miningLevelTag)
    {
        var tier = PlatformTierRegistry.super.registerTier(uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, miningLevelTag);
        return new ForgeTierWrapper(tier);
    }

    private record ForgeTierWrapper(EnchancedTier tier) implements EnchancedTier
    {
        @Nullable
        @Override
        public TagKey<Block> getMiningLevelTag()
        {
            return tier.getMiningLevelTag();
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

        @Deprecated
        @Override
        public int getLevel()
        {
            return tier.getLevel();
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

        // Forge specific, Forge adds this to the Tier interface
        @Nullable
        @Override
        public TagKey<Block> getTag()
        {
            return getMiningLevelTag();
        }
    }
}
