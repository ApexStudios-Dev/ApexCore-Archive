package xyz.apex.minecraft.apexcore.shared.platform;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;
import xyz.apex.minecraft.apexcore.shared.util.Tags;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface GamePlatform
{
    GamePlatform INSTANCE = ServiceLoader.load(GamePlatform.class).findFirst().orElseThrow();

    default void initialize()
    {
        getLogger().info("Initializing | ModLoader: {} v{}...", getModLoader().getDisplayName(), getModLoaderVersion());
        if(this instanceof ModPlatform) return;
        Tags.bootstrap();
    }

    default void initializeSided(Env side)
    {
        getLogger().info("Initializing-Side<{}> | ModLoader: {} v{}...", side.toPlatform().name(), getModLoader().getDisplayName(), getModLoaderVersion());
    }

    default void initializeDataGen()
    {
        getLogger().info("Initializing-DataGeneration | ModLoader: {} v{}...", getModLoader().getDisplayName(), getModLoaderVersion());
    }

    ModLoader getModLoader();

    Logger getLogger();

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

    EnhancedTier createEnhancedTier(String registryName, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> toolLevelBlock);
}
