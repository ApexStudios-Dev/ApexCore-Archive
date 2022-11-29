package xyz.apex.minecraft.apexagnostics.fabric;

import com.google.common.base.Suppliers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import xyz.apex.minecraft.apexagnostics.vanilla.Platform;
import xyz.apex.minecraft.apexagnostics.vanilla.registry.ModdedRegistry;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class FabricPlatform implements Platform
{
    @Override
    public <T extends GameRules.Value<T>> GameRules.Key<T> registerGameRule(String gameRuleName, GameRules.Category category, GameRules.Type<T> type)
    {
        return GameRuleRegistry.register(gameRuleName, category, type);
    }

    @Override
    public GameRules.Type<GameRules.BooleanValue> registerGameRuleBooleanValue(Boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
    {
        return GameRuleFactory.createBooleanRule(defaultValue, changeListener);
    }

    @Override
    public GameRules.Type<GameRules.IntegerValue> registerGameRuleIntegerValue(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        return GameRuleFactory.createIntRule(defaultValue, changeListener);
    }

    @Override
    public <T, R extends T> Supplier<R> register(Registry<T> vanilla, ModdedRegistry<T> modded, ResourceKey<T> key, Supplier<R> factory)
    {
        var value = Registry.register(vanilla, key, factory.get());
        return Suppliers.memoize(() -> value);
    }

    public static void setupCommon()
    {
    }

    @Environment(EnvType.CLIENT)
    public static void setupClient()
    {
    }

    @Environment(EnvType.SERVER)
    public static void setupServer()
    {
    }
}
