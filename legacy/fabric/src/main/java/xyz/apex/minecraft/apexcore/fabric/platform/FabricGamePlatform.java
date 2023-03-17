package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.common.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.common.platform.ModLoader;
import xyz.apex.minecraft.apexcore.common.util.EnhancedTier;

import java.util.function.Supplier;

public final class FabricGamePlatform implements GamePlatform
{
    private final FabricStorages storages = new FabricStorages();

    @Override
    public ModLoader getModLoader()
    {
        return ModLoader.FABRIC;
    }

    @Override
    public String getModLoaderVersion()
    {
        return FabricLoaderImpl.VERSION;
    }

    @Override
    public boolean isRunningDataGeneration()
    {
        return FabricDataGenHelper.ENABLED;
    }

    @Override
    public EnhancedTier createEnhancedTier(String registryName, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> toolLevelBlock)
    {
        return new FabricEnhancedTier(registryName, uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, toolLevelBlock);
    }

    @Override
    public DyeColor getDyeColor(ItemStack stack)
    {
        return stack.getItem() instanceof DyeItem item ? item.getDyeColor() : null;
    }
}
