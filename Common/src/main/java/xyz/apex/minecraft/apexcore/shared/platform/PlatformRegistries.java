package xyz.apex.minecraft.apexcore.shared.platform;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.registry.builders.ArmorMaterialBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.builders.TierBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface PlatformRegistries extends PlatformHolder
{
    <T> Collection<T> getAllKnown(ResourceKey<? extends Registry<T>> registryType, String modId);

    <T, R extends T, E extends RegistryEntry<R>> void register(ResourceKey<? extends Registry<T>> registryType, E registryEntry, Supplier<R> factory, BiConsumer<E, R> onRegister);

    EnhancedTier registerTier(TierBuilder builder);

    ArmorMaterial registerArmorMaterial(ArmorMaterialBuilder builder);

    PlatformGameRules gameRules();

    default void registerRenderType(String modId, Block block, Supplier<Supplier<RenderType>> renderTypeSupplier) {}

    interface PlatformGameRules extends PlatformHolder
    {
        // Common
        <T extends GameRules.Value<T>> GameRules.Key<T> register(String modId, String registryName, GameRules.Category category, GameRules.Type<T> type);

        // Boolean
        default GameRules.Key<GameRules.BooleanValue> registerBoolean(String modId, String registryName, GameRules.Category category, boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
        {
            return register(modId, registryName, category, createBooleanType(defaultValue, changeListener));
        }

        default GameRules.Key<GameRules.BooleanValue> registerBoolean(String modId, String registryName, GameRules.Category category, boolean defaultValue)
        {
            return registerBoolean(modId, registryName, category, defaultValue, defaultChangeListener());
        }

        GameRules.Type<GameRules.BooleanValue> createBooleanType(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener);

        default GameRules.Type<GameRules.BooleanValue> createBooleanType(boolean defaultValue)
        {
            return createBooleanType(defaultValue, defaultChangeListener());
        }

        // Integer
        default GameRules.Key<GameRules.IntegerValue> registerInteger(String modId, String registryName, GameRules.Category category, int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
        {
            return register(modId, registryName, category, createIntegerType(defaultValue, changeListener));
        }

        default GameRules.Key<GameRules.IntegerValue> registerInteger(String modId, String registryName, GameRules.Category category, int defaultValue)
        {
            return registerInteger(modId, registryName, category, defaultValue, defaultChangeListener());
        }

        GameRules.Type<GameRules.IntegerValue> createIntegerType(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener);

        default GameRules.Type<GameRules.IntegerValue> createIntegerType(int defaultValue)
        {
            return createIntegerType(defaultValue, defaultChangeListener());
        }

        // Common
        static <T extends GameRules.Value<T>> BiConsumer<MinecraftServer, T> defaultChangeListener()
        {
            return (server, value) -> { };
        }
    }
}
