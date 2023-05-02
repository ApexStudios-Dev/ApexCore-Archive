package xyz.apex.minecraft.apexcore.forge.lib.registry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryApi;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class RegistryApiImpl implements RegistryApi
{
    private final Set<String> mods = Sets.newHashSet();
    private final Table<String, ResourceKey<? extends Registry<?>>, List<Pair<ResourceLocation, Supplier<?>>>> registryEntries = HashBasedTable.create();

    @Override
    public <T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<? extends T> registryEntryFactory)
    {
        var modId = ModLoadingContext.get().getActiveNamespace();

        if(mods.add(modId))
            FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.HIGH, this::onRegister);

        var entries = registryEntries.get(modId, registryType);

        if(entries == null)
        {
            entries = Lists.newLinkedList();
            registryEntries.put(modId, registryType, entries);
        }

        entries.add(Pair.of(registryName, registryEntryFactory));
    }

    @SuppressWarnings({"unchecked", "rawtypes", "RedundantCast"})
    @Override
    public <T> Optional<Holder<T>> getDelegate(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> registryKey)
    {
        var forgeRegistry = RegistryManager.ACTIVE.getRegistry(registryType);
        if(forgeRegistry != null) return forgeRegistry.getDelegate((ResourceKey) registryKey);
        return BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryType).getHolder(registryKey);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void onRegister(RegisterEvent event)
    {
        registryEntries(event, (ResourceKey) event.getRegistryKey());
    }

    @SuppressWarnings("unchecked")
    private <T> void registryEntries(RegisterEvent event, ResourceKey<? extends Registry<T>> registryType)
    {
        var modId = ModLoadingContext.get().getActiveNamespace();
        var entries = registryEntries.get(modId, registryType);
        if(entries == null || entries.isEmpty()) return;
        entries.forEach(pair -> event.register(registryType, pair.getFirst(), (Supplier<T>) pair.getSecond()));
        entries.clear();
        registryEntries.remove(modId, registryType);
    }
}
