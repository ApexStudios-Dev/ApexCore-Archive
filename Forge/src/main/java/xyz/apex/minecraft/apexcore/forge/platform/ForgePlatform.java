package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang3.Validate;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformGameRulesRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistry;

import java.util.function.Supplier;

public final class ForgePlatform implements Platform
{
    private final Table<ResourceKey<? extends Registry<?>>, String, DeferredRegister<?>> modRegistries = HashBasedTable.create();
    private final ForgeGameRulesRegistry gameRulesRegistry = new ForgeGameRulesRegistry(this);

    @Override
    public PlatformGameRulesRegistry gameRules()
    {
        return gameRulesRegistry;
    }

    @Override
    public <T, R extends T> Supplier<R> register(Registry<T> vanilla, ModdedRegistry<T> modded, ResourceKey<T> key, Supplier<R> factory)
    {
        var registry = getOrCreateModRegistry(vanilla.key(), modded.getRegistryName().getNamespace());
        return registry.register(key.location().getPath(), factory);
    }

    @SuppressWarnings({ "unchecked", "ConstantConditions" })
    private <T> DeferredRegister<T> getOrCreateModRegistry(ResourceKey<? extends Registry<T>> type, String modId)
    {
        if(modRegistries.contains(type, modId)) return (DeferredRegister<T>) modRegistries.get(type, modId);

        var modRegistry = DeferredRegister.create(type, modId);
        modRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        Validate.isTrue(ModLoadingContext.get().getActiveContainer().getModId().equals(modId), "ForgePlatform#getOrCreateModRegistry must be called during '%s' mod initialization", modId);
        return modRegistry;
    }
}
