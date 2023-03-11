package xyz.apex.minecraft.apexcore.forge.platform;

import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.versions.forge.ForgeVersion;

import xyz.apex.minecraft.apexcore.common.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.common.platform.ModLoader;
import xyz.apex.minecraft.apexcore.common.util.EnhancedTier;

import java.util.function.Supplier;

public final class ForgeGamePlatform implements GamePlatform
{
    private ForgeStorages storages = new ForgeStorages();

    @Override
    public ModLoader getModLoader()
    {
        return ModLoader.FORGE;
    }

    @Override
    public String getModLoaderVersion()
    {
        return ForgeVersion.getVersion();
    }

    @Override
    public boolean isRunningDataGeneration()
    {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public EnhancedTier createEnhancedTier(String registryName, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> toolLevelBlock)
    {
        return new ForgeEnhancedTier(registryName, uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, toolLevelBlock);
    }

    @Nullable
    @Override
    public DyeColor getDyeColor(ItemStack stack)
    {
        return DyeColor.getColor(stack);
    }
}
