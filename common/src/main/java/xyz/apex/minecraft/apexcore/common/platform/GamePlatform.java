package xyz.apex.minecraft.apexcore.common.platform;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.common.util.EnhancedTier;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface GamePlatform extends PlatformHolder
{
    GamePlatform INSTANCE = ServiceLoader.load(GamePlatform.class).findFirst().orElseThrow();

    ModLoader getModLoader();

    default String getMinecraftVersion()
    {
        return Platform.getMinecraftVersion();
    }

    String getModLoaderVersion();

    default Path getGameFolder()
    {
        return Platform.getGameFolder();
    }

    default Path getConfigFolder()
    {
        return Platform.getConfigFolder();
    }

    default Path getModsFolder()
    {
        return Platform.getModsFolder();
    }

    default Env getEnvironment()
    {
        return Platform.getEnvironment();
    }

    default boolean isModLoaded(String modId)
    {
        return Platform.isModLoaded(modId);
    }

    default Mod getMod(String modId)
    {
        return Platform.getMod(modId);
    }

    default Optional<Mod> getOptionalMod(String modId)
    {
        return Platform.getOptionalMod(modId);
    }

    default Collection<Mod> getMods()
    {
        return Platform.getMods();
    }

    default Collection<String> getModIds()
    {
        return Platform.getModIds();
    }

    default boolean isDevelopmentEnvironment()
    {
        return Platform.isDevelopmentEnvironment();
    }

    boolean isRunningDataGeneration();

    @Override
    default GamePlatform platform()
    {
        return this;
    }

    EnhancedTier createEnhancedTier(String registryName, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> toolLevelBlock);
}
