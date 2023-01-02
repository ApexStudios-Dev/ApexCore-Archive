package xyz.apex.minecraft.apexcore.shared.registry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;

import java.util.function.Supplier;

public final class TierBuilder
{
    private final String modId;
    private final String registryName;

    private int uses = Tiers.WOOD.getUses();
    private float speed = Tiers.WOOD.getSpeed();
    private float attackDamageBonus = Tiers.WOOD.getAttackDamageBonus();
    private int level = Tiers.WOOD.getLevel();
    private int enchantmentValue = Tiers.WOOD.getEnchantmentValue();
    private Supplier<Ingredient> repairIngredient = Tiers.WOOD::getRepairIngredient;
    @Nullable private TagKey<Block> toolLevelTag = null;

    private TierBuilder(String modId, String registryName)
    {
        this.modId = modId;
        this.registryName = registryName;
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

    public TierBuilder repairTag(TagKey<Item> repairTag)
    {
        return repairIngredient(() -> Ingredient.of(repairTag));
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

    public EnhancedTier register()
    {
        return GamePlatform.INSTANCE.createEnhancedTier("%s:%s".formatted(modId, registryName), uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, toolLevelTag);
    }

    public static TierBuilder builder(String modId, String registryName)
    {
        return new TierBuilder(modId, registryName);
    }
}
