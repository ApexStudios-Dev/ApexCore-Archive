package xyz.apex.minecraft.apexcore.fabric.platform;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.common.util.EnhancedTier;
import xyz.apex.minecraft.apexcore.common.util.function.Lazy;

import java.util.function.Supplier;

public final class FabricEnhancedTier implements EnhancedTier
{
    private final ResourceLocation registryName;
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int level;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;
    @Nullable private final TagKey<Block> toolLevelTag;

    FabricEnhancedTier(String registryName, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> toolLevelTag)
    {
        this.registryName = new ResourceLocation(registryName);
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.level = level;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = Lazy.of(repairIngredient);
        this.toolLevelTag = toolLevelTag;
    }

    @Override
    public ResourceLocation getRegistryName()
    {
        return registryName;
    }

    @Nullable
    @Override
    public TagKey<Block> getToolLevelTag()
    {
        return toolLevelTag;
    }

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
