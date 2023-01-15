package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.platform.ModLoader;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;

import java.util.function.Supplier;

public final class FabricGamePlatform implements GamePlatform
{
    private boolean initialized = false;
    private final FabricStorages storages = new FabricStorages();

    public FabricGamePlatform()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        if(initialized) return;
        GamePlatform.super.initialize();
        initialized = true;
    }

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
}
