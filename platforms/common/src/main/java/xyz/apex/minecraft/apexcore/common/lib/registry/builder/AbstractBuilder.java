package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryProviderListener;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderLookup;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderTypes;
import xyz.apex.minecraft.apexcore.common.lib.resgen.lang.LanguageBuilder;
import xyz.apex.minecraft.apexcore.common.lib.resgen.lang.LanguageProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.loot.LootTableProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.loot.LootTableSubProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.loot.LootType;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base implementation for all Builders, to be extended by all further implementations.
 *
 * @param <O> Type of Registrar.
 * @param <P> Type of Parent.
 * @param <T> Type of Registry.
 * @param <R> Type of Entry.
 * @param <B> Type of Builder.
 * @param <E> Type of RegistryEntry.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBuilder<O extends AbstractRegistrar<O>, P, T, R extends T, B extends AbstractBuilder<O, P, T, R, B, E>, E extends RegistryEntry<R>> implements Builder<O, P, T, R, B, E>
{
    protected final O registrar;
    protected final P parent;
    protected final ResourceKey<? extends Registry<T>> registryType;
    protected final ResourceKey<R> registryKey;
    protected final B self = (B) this;

    private final Supplier<R> entrySupplier = Suppliers.memoize(this::getEntry);
    private final Map<LootType<?>, BiConsumer<? extends LootTableSubProvider, E>> lootProviders = Maps.newHashMap();

    protected AbstractBuilder(O registrar, P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        this.registrar = registrar;
        this.parent = parent;
        this.registryType = registryType;
        registryKey = (ResourceKey<R>) ResourceKey.create(registryType, new ResourceLocation(registrar.getOwnerId(), registrationName));

        ProviderTypes.LOOT_TABLES.addListener(registrar.getOwnerId(), (provider, lookup) -> generateLoot(provider, lookup, get()));
        defaultLangGB().defaultLangUS();
        onRegister(this::onRegister);
    }

    @ApiStatus.OverrideOnly
    @DoNotCall
    protected void onRegister(R entry)
    {
    }

    @DoNotCall
    protected abstract E createRegistryEntry();

    @DoNotCall
    protected abstract R createEntry();

    @DoNotCall
    protected abstract String getDescriptionId(E entry);

    @Override
    public final O registrar()
    {
        return registrar;
    }

    @Override
    public final P parent()
    {
        return parent;
    }

    @Override
    public final ResourceKey<? extends Registry<T>> registryType()
    {
        return registryType;
    }

    @Override
    public final ResourceKey<R> registryKey()
    {
        return registryKey;
    }

    @Override
    public final ResourceLocation registryName()
    {
        return registryKey.location();
    }

    @Override
    public final String registrationName()
    {
        return registryName().getPath();
    }

    @Override
    public String ownerId()
    {
        return registrar.getOwnerId();
    }

    @Override
    public final E get()
    {
        return (E) registrar.get(registryType, registrationName());
    }

    @Override
    public final R getEntry()
    {
        return get().get();
    }

    @Override
    public final Supplier<R> asSupplier()
    {
        return entrySupplier;
    }

    @Override
    public final E register()
    {
        return registrar.register(registryType, registrationName(), this::createEntry, this::createRegistryEntry);
    }

    @Override
    public final P build()
    {
        register();
        return parent;
    }

    @Override
    public final B onRegister(Consumer<R> listener)
    {
        registrar.addRegisterListener(registryType, registrationName(), listener);
        return self;
    }

    @Override
    public final <OR> B onRegisterAfter(ResourceKey<? extends Registry<OR>> dependencyType, Consumer<R> listener)
    {
        return onRegister(entry -> {
            if(registrar.isRegistered(dependencyType))
                listener.accept(entry);
            else
                registrar.addRegisterListener(dependencyType, () -> listener.accept(entry));
        });
    }

    @Override
    public final <D extends DataProvider> B setProvider(ProviderType<D> providerType, RegistryProviderListener<D, R, E> listener)
    {
        registrar.setResourceGenerator(providerType, registryType, registrationName(), listener);
        return self();
    }

    @Override
    public final <D extends DataProvider> B clearProvider(ProviderType<D> providerType)
    {
        return setProvider(providerType, (provider, lookup, entry) -> { });
    }

    /**
     * Set the LootTable generator for this Block.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public final <L extends LootTableSubProvider> B setLootTableProvider(LootType<L> lootType, BiConsumer<L, E> listener)
    {
        lootProviders.put(lootType, listener);
        return self;
    }

    /**
     * Clears the currently registered LootTable generator.
     *
     * @return This Builder.
     */
    public final <L extends LootTableSubProvider> B clearLootTableProvider(LootType<L> lootType)
    {
        lootProviders.remove(lootType);
        return self;
    }

    private void generateLoot(LootTableProvider provider, ProviderLookup lookup, E entry)
    {
        // split into new function, cause we need the generic type
        lootProviders.keySet().forEach(lootType -> generateLoot(provider, lootType, entry));
    }

    private <L extends LootTableSubProvider> void generateLoot(LootTableProvider provider, LootType<L> lootType, E entry)
    {
        var lootProvider = lootProviders.get(lootType);

        if(lootProvider != null)
            ((BiConsumer<L, E>) lootProvider).accept(provider.subProvider(lootType), entry);
    }

    /**
     * Registers default English-US translation provider.
     *
     * @return This Builder.
     */
    public final B defaultLangUS()
    {
        return lang("en_us", (provider, entry) -> LanguageProvider.toEnglishName(entry.getRegistryName()));
    }

    /**
     * Registers default English-GB translation provider.
     *
     * @return This Builder.
     */
    public final B defaultLangGB()
    {
        return lang("en_gb", (provider, entry) -> LanguageProvider.toEnglishName(entry.getRegistryName()));
    }

    /**
     * Set a region specific translation for this Entry.
     *
     * @param region Region to register translation for.
     * @param value Translation to register.
     * @return This Builder.
     */
    public final B lang(String region, String value)
    {
        return lang(region, (provider, entry) -> value);
    }

    /**
     * Set a region specific translation for this Entry.
     *
     * @param region Region to register translation for.
     * @param valueProvider Translation to register.
     * @return This Builder.
     */
    public final B lang(String region, BiFunction<LanguageBuilder, E, String> valueProvider)
    {
        return setProvider(
                ProviderTypes.LANGUAGES,
                (provider, lookup, entry) -> {
                    var builder = provider.region(region);
                    provider.region(region).add(
                            getDescriptionId(entry),
                            valueProvider.apply(builder, entry)
                    );
                }
        );
    }

    /**
     * Mark this entry as not generating any Translations.
     *
     * @return This Builder.
     */
    public final B noLang()
    {
        return clearProvider(ProviderTypes.LANGUAGES);
    }

    @Override
    public final B with(Consumer<B> consumer)
    {
        consumer.accept(self);
        return self;
    }

    @Override
    public final B self()
    {
        return self;
    }
}
