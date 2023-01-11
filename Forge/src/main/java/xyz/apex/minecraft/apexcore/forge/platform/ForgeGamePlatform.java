package xyz.apex.minecraft.apexcore.forge.platform;

import dev.architectury.utils.Env;
import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.versions.forge.ForgeVersion;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.platform.ModLoader;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;

public final class ForgeGamePlatform implements GamePlatform
{
    private boolean initialized = false;
    private final Set<Env> initializedSides = EnumSet.noneOf(Env.class);
    private boolean initializedDataGen = false;
    private ForgeStorages storages = new ForgeStorages();

    public ForgeGamePlatform()
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
    public void initializeSided(Env side)
    {
        if(initializedSides.contains(side)) return;
        GamePlatform.super.initializeSided(side);
        initializedSides.add(side);
    }

    @Override
    public void initializeDataGen()
    {
        if(initializedDataGen) return;
        GamePlatform.super.initializeDataGen();
        initializedDataGen = true;
    }

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
}
