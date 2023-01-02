package xyz.apex.minecraft.apexcore.shared.platform;

import dev.architectury.platform.Mod;
import dev.architectury.utils.Env;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;

import java.util.function.Supplier;

public interface ModPlatform extends GamePlatform
{
    String getModId();

    default Mod asMod()
    {
        return GamePlatform.INSTANCE.getMod(getModId());
    }

    @Override
    default void initialize()
    {
        GamePlatform.INSTANCE.initialize();
        GamePlatform.super.initialize();
    }

    @Override
    default void initializeSided(Env side)
    {
        GamePlatform.INSTANCE.initializeSided(side);
        GamePlatform.super.initializeSided(side);
    }

    @Override
    default void initializeDataGen()
    {
        GamePlatform.INSTANCE.initializeDataGen();
        GamePlatform.super.initializeDataGen();
    }

    @Override
    default ModLoader getModLoader()
    {
        return GamePlatform.INSTANCE.getModLoader();
    }

    @Override
    default String getModLoaderVersion()
    {
        return GamePlatform.INSTANCE.getModLoaderVersion();
    }

    @Override
    default EnhancedTier createEnhancedTier(String registryName, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> toolLevelBlock)
    {
        return GamePlatform.INSTANCE.createEnhancedTier("%s:%s".formatted(getModId(), registryName), uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, toolLevelBlock);
    }

    default ResourceLocation id(String path)
    {
        return new ResourceLocation(getModId(), path);
    }

    @Nullable AbstractRegistrar<?> getRegistrar();
}
