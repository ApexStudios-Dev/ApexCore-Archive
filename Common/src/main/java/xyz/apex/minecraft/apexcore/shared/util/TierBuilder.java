package xyz.apex.minecraft.apexcore.shared.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;

import java.util.function.Supplier;

public final class TierBuilder
{
    private int uses = Tiers.WOOD.getUses();
    private float speed = Tiers.WOOD.getSpeed();
    private float attackDamageBonus = Tiers.WOOD.getAttackDamageBonus();
    private int level = Tiers.WOOD.getLevel();
    private int enchantmentValue = Tiers.WOOD.getEnchantmentValue();
    private Supplier<Ingredient> repairIngredient = Tiers.WOOD::getRepairIngredient;
    @Nullable private TagKey<Block> toolLevelTag = null; // mainly used by Forge

    private TierBuilder()
    {
    }

    public TierBuilder uses(int uses)
    {
        this.uses = uses;
        return this;
    }

    public TierBuilder speed(float speed)
    {
        this.speed = speed;
        return this;
    }

    public TierBuilder attackDamageBonus(float attackDamageBonus)
    {
        this.attackDamageBonus = attackDamageBonus;
        return this;
    }

    public TierBuilder level(int level)
    {
        this.level = level;
        return this;
    }

    public TierBuilder enchantmentValue(int enchantmentValue)
    {
        this.enchantmentValue = enchantmentValue;
        return this;
    }

    public TierBuilder repairIngredient(Supplier<Ingredient> repairIngredient)
    {
        this.repairIngredient = repairIngredient;
        return this;
    }

    public TierBuilder repairItem(Supplier<? extends ItemLike> repairIngredient)
    {
        return repairIngredient(() -> Ingredient.of(repairIngredient.get()));
    }

    public TierBuilder toolLevelTag(TagKey<Block> toolLevelTag)
    {
        this.toolLevelTag = toolLevelTag;
        return this;
    }

    public EnchancedTier build()
    {
        return Platform.INSTANCE.tierRegistry().registerTier(uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, toolLevelTag);
    }

    public static TierBuilder builder()
    {
        return new TierBuilder();
    }
}
