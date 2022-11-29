package xyz.apex.minecraft.apexcore.fabric.platform;

import com.google.common.base.Suppliers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformGameRulesRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistry;

import java.util.function.Supplier;

@SuppressWarnings("NullableProblems")
public final class FabricPlatform implements Platform
{
    private final FabricGameRulesRegistry gameRulesRegistry = new FabricGameRulesRegistry(this);

    @Override
    public PlatformGameRulesRegistry gameRules()
    {
        return gameRulesRegistry;
    }

    @Override
    public <T, R extends T> Supplier<R> register(Registry<T> vanilla, ModdedRegistry<T> modded, ResourceKey<T> key, Supplier<R> factory)
    {
        var value = Registry.register(vanilla, key, factory.get());
        return Suppliers.memoize(() -> value);
    }
}
