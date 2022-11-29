package xyz.apex.minecraft.apexagnostics.vanilla;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import xyz.apex.minecraft.apexagnostics.vanilla.registry.ModdedRegistry;

import java.util.ServiceLoader;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface Platform
{
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst().orElseThrow();

    <T extends GameRules.Value<T>> GameRules.Key<T> registerGameRule(String gameRuleName, GameRules.Category category, GameRules.Type<T> type);
    GameRules.Type<GameRules.BooleanValue> registerGameRuleBooleanValue(Boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener);
    GameRules.Type<GameRules.IntegerValue> registerGameRuleIntegerValue(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener);

    <T, R extends T> Supplier<R> register(Registry<T> vanilla, ModdedRegistry<T> modded, ResourceKey<T> key, Supplier<R> factory);
}
