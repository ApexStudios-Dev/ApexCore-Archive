package xyz.apex.minecraft.apexagnostics.forge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import xyz.apex.minecraft.apexagnostics.vanilla.Platform;
import xyz.apex.minecraft.apexagnostics.vanilla.registry.ModdedRegistry;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class ForgePlatform implements Platform
{
    private final Table<ResourceKey<? extends Registry<?>>, String, DeferredRegister<?>> modRegistries = HashBasedTable.create();

    @Override
    public <T extends GameRules.Value<T>> GameRules.Key<T> registerGameRule(String gameRuleName, GameRules.Category category, GameRules.Type<T> type)
    {
        return GameRules.register(gameRuleName, category, type);
    }

    @Override
    public GameRules.Type<GameRules.BooleanValue> registerGameRuleBooleanValue(Boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
    {
        return GameRules.BooleanValue.create(defaultValue, changeListener);
    }

    @Override
    public GameRules.Type<GameRules.IntegerValue> registerGameRuleIntegerValue(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        return GameRules.IntegerValue.create(defaultValue, changeListener);
    }

    @Override
    public <T, R extends T> Supplier<R> register(Registry<T> vanilla, ModdedRegistry<T> modded, ResourceKey<T> key, Supplier<R> factory)
    {
        var registry = getOrCreateModRegistry(vanilla.key(), modded.getRegistryName().getNamespace());
        return registry.register(key.location().getPath(), factory);
    }

    @SuppressWarnings("unchecked")
    private <T> DeferredRegister<T> getOrCreateModRegistry(ResourceKey<? extends Registry<T>> type, String modId)
    {
        if(modRegistries.contains(type, modId)) return (DeferredRegister<T>) modRegistries.get(type, modId);

        var modRegistry = DeferredRegister.create(type, modId);
        modRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        return modRegistry;
    }
}
