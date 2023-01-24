package xyz.apex.minecraft.apexcore.common.platform;

import dev.architectury.platform.Mod;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.common.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.util.EnhancedTier;

import java.util.function.Supplier;

public interface ModPlatform extends GamePlatform
{
    String getModId();

    default Mod asMod()
    {
        return platform().getMod(getModId());
    }

    void initialize();

    @Override
    default ModLoader getModLoader()
    {
        return platform().getModLoader();
    }

    @Override
    default String getModLoaderVersion()
    {
        return platform().getModLoaderVersion();
    }

    @Override
    default boolean isRunningDataGeneration()
    {
        return platform().isRunningDataGeneration();
    }

    @Override
    default GamePlatform platform()
    {
        return GamePlatform.INSTANCE;
    }

    @Override
    default EnhancedTier createEnhancedTier(String registryName, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> toolLevelBlock)
    {
        return platform().createEnhancedTier("%s:%s".formatted(getModId(), registryName), uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, toolLevelBlock);
    }

    default ResourceLocation id(String path)
    {
        return new ResourceLocation(getModId(), path);
    }

    @Nullable AbstractRegistrar<?> getRegistrar();
}
