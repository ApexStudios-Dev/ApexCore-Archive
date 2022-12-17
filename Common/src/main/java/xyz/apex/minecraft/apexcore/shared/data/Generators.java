package xyz.apex.minecraft.apexcore.shared.data;

import com.google.common.collect.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;

import xyz.apex.minecraft.apexcore.shared.data.providers.LanguageProvider;
import xyz.apex.minecraft.apexcore.shared.data.providers.RecipeProvider;
import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Generators
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, Generators> MOD_GENERATORS = Maps.newHashMap();

    private final String modId;
    private final Table<Pair<String, ResourceKey<? extends Registry<?>>>, ProviderType<?>, Consumer<? extends DataProvider>> byEntry = HashBasedTable.create();
    private final ListMultimap<ProviderType<?>, Consumer<? extends DataProvider>> gens = ArrayListMultimap.create();

    private Generators(String modId)
    {
        this.modId = modId;
    }

    private <D extends DataProvider, R> void setDataGenerator(String entry, ResourceKey<? extends Registry<R>> registryType, ProviderType<D> providerType, Consumer<? extends D> consumer)
    {
        Consumer<? extends DataProvider> existing = byEntry.put(Pair.of(entry, registryType), providerType, consumer);
        if(existing != null) gens.remove(providerType, existing);
        addDataGenerator(providerType, consumer);
    }

    private <D extends DataProvider> void addDataGenerator(ProviderType<? extends D> providerType, Consumer<? extends D> consumer)
    {
        gens.put(providerType, consumer);
        GamePlatform.events().registerDataGenerators(modId);
    }

    private <D extends DataProvider> boolean shouldRegister(ProviderType<? extends D> providerType)
    {
        return gens.containsKey(providerType);
    }

    @SuppressWarnings("unchecked")
    private <D extends DataProvider> void processDataGenerator(ProviderType<D> providerType, D provider)
    {
        gens.get(providerType).stream().map(c -> (Consumer<D>) c).forEach(c -> c.accept(provider));
    }

    public static <D extends DataProvider, R> void setDataGenerator(String modId, String entry, ResourceKey<? extends Registry<R>> registryType, ProviderType<D> providerType, Consumer<? extends D> consumer)
    {
        getInstance(modId).setDataGenerator(entry, registryType, providerType, consumer);
    }

    public static <D extends DataProvider> void addDataGenerator(String modId, ProviderType<? extends D> providerType, Consumer<? extends D> consumer)
    {
        getInstance(modId).addDataGenerator(providerType, consumer);
    }

    public static <D extends DataProvider> boolean shouldRegister(String modId, ProviderType<? extends D> providerType)
    {
        return getInstance(modId).shouldRegister(providerType);
    }

    public static <D extends DataProvider> void processDataGenerator(String modId, ProviderType<D> providerType, D provider)
    {
        getInstance(modId).processDataGenerator(providerType, provider);
    }

    public static void registerDataGenerators(String modId, Supplier<PackOutput> output, CompletableFuture<HolderLookup.Provider> lookupProvider, Consumer<DataProvider> client, Consumer<DataProvider> server)
    {
        if(shouldRegister(modId, ProviderTypes.LANGUAGE)) client.accept(new LanguageProvider(output.get(), modId));

        if(shouldRegister(modId, ProviderTypes.RECIPES)) server.accept(new RecipeProvider(output.get(), modId));
    }

    private static Generators getInstance(String modId)
    {
        return MOD_GENERATORS.computeIfAbsent(modId, Generators::new);
    }
}
