package xyz.apex.minecraft.apexcore.shared.platform;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistry;

import java.util.ServiceLoader;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface Platform
{
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst().orElseThrow();

    PlatformGameRulesRegistry gameRules();

    <T, R extends T> Supplier<R> register(Registry<T> vanilla, ModdedRegistry<T> modded, ResourceKey<T> key, Supplier<R> factory);
}
