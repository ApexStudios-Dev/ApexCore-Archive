package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.Registrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;
import xyz.apex.minecraft.apexcore.common.lib.resgen.lang.LanguageBuilder;
import xyz.apex.minecraft.apexcore.common.lib.resgen.lang.LanguageProvider;

import java.util.List;
import java.util.function.*;

public abstract non-sealed class AbstractBuilder<P, T, R extends T, E extends RegistryEntry<R>, B extends AbstractBuilder<P, T, R, E, B, M>, M extends BuilderManager<M>> implements Builder<P, T, R, E, B, M>
{
    private final P parent;
    protected final M builderManager;
    protected ResourceKey<T> registryKey;
    protected final ResourceKey<? extends Registry<T>> registryType;
    private final Function<RegistryEntry<R>, E> registryEntryFactory;
    private final Supplier<R> safeSupplier = Suppliers.memoize(this::createObject);
    private final List<Consumer<R>> listeners = Lists.newLinkedList();

    protected AbstractBuilder(P parent, M builderManager, ResourceKey<? extends Registry<T>> registryType, String registrationName, Function<RegistryEntry<R>, E> registryEntryFactory)
    {
        this.parent = parent;
        this.builderManager = builderManager;
        this.registryType = registryType;
        this.registryEntryFactory = Util.memoize(registryEntryFactory);

        registryKey = ResourceKey.create(registryType, new ResourceLocation(builderManager.getOwnerId(), registrationName));

        defaultLangGB().defaultLangUS();
    }

    protected abstract R createObject();

    @Override
    public final M getBuilderManager()
    {
        return builderManager;
    }

    @Override
    public final RegistrarManager getRegistrarManager()
    {
        return builderManager.getRegistrarManager();
    }

    @Override
    public final Registrar<T> getRegistrar()
    {
        return getRegistrarManager().get(registryType);
    }

    @Override
    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registryType;
    }

    @Override
    public final String getOwnerId()
    {
        return builderManager.getOwnerId();
    }

    @Override
    public final String getRegistrationName()
    {
        return getRegistryName().getPath();
    }

    @Override
    public final ResourceLocation getRegistryName()
    {
        return registryKey.location();
    }

    @Override
    public final ResourceKey<T> getRegistryKey()
    {
        return registryKey;
    }

    @Override
    public final Supplier<R> asSupplier()
    {
        return safeSupplier;
    }

    @Override
    public final P getParent()
    {
        return parent;
    }

    @Override
    public final B registrationModifier(UnaryOperator<String> registrationNameModifier)
    {
        var registrationName = registrationNameModifier.apply(getRegistrationName());
        registryKey = ResourceKey.create(registryType, new ResourceLocation(getOwnerId(), registrationName));
        return self();
    }

    @Override
    public final B withRegistrationNamePrefix(String prefix)
    {
        return registrationModifier(registrationName -> prefix + registrationName);
    }

    @Override
    public final B withRegistrationNameSuffix(String suffix)
    {
        return registrationModifier(registrationName -> registrationName + suffix);
    }

    @Override
    public final B addListener(Runnable listener)
    {
        return addListener(value -> listener.run());
    }

    @Override
    public final B addListener(Consumer<R> listener)
    {
        listeners.add(listener);
        return self();
    }

    @Override
    public final P end()
    {
        register();
        return parent;
    }

    @Override
    public final E register()
    {
        var delegate = getRegistrar().register(getRegistrationName(), safeSupplier);
        listeners.forEach(delegate::addListener);
        listeners.clear();
        return registryEntryFactory.apply(delegate);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final B self()
    {
        return (B) this;
    }

    @Override
    public final <D extends DataProvider> B addProvider(ProviderType<D> providerType, BiConsumer<D, ProviderType.RegistryContext<T, R>> listener)
    {
        providerType.addListener(registryKey, listener);
        return self();
    }

    @Override
    public final <D extends DataProvider> B addMiscProvider(ProviderType<D> providerType, BiConsumer<D, ProviderType.RegistryContext<T, R>> listener)
    {
        providerType.addMiscListener(registryKey, listener);
        return self();
    }

    @Override
    public final <D extends DataProvider> B setProvider(ProviderType<D> providerType, BiConsumer<D, ProviderType.RegistryContext<T, R>> listener)
    {
        providerType.setListener(registryKey, listener);
        return self();
    }

    @Override
    public final <D extends DataProvider> B clearProvider(ProviderType<D> providerType)
    {
        providerType.clearListener(registryKey);
        return self();
    }

    @ApiStatus.Internal
    protected abstract String getDescriptionId(ProviderType.RegistryContext<T, R> context);

    public final B defaultLangUS()
    {
        return lang("en_us", (provider, context) -> LanguageProvider.toEnglishName(context.registryName()));
    }

    public final B defaultLangGB()
    {
        return lang("en_gb", (provider, context) -> LanguageProvider.toEnglishName(context.registryName()));
    }

    public final B lang(String region, String value)
    {
        return lang(region, (provider, context) -> value);
    }

    public final B lang(String region, BiFunction<LanguageBuilder, ProviderType.RegistryContext<T, R>, String> valueProvider)
    {
        return setProvider(
                LanguageProvider.PROVIDER_TYPE,
                (provider, context) -> {
                    var builder = provider.region(region);
                    builder.add(
                            getDescriptionId(context),
                            valueProvider.apply(builder, context)
                    );
                }
        );
    }

    public final B noLang()
    {
        return clearProvider(LanguageProvider.PROVIDER_TYPE);
    }

    public final B addMiscLang(BiConsumer<LanguageProvider, ProviderType.RegistryContext<T, R>> listener)
    {
        return addMiscProvider(LanguageProvider.PROVIDER_TYPE, listener);
    }

    public final B addMiscLang(String region, BiConsumer<LanguageBuilder, ProviderType.RegistryContext<T, R>> listener)
    {
        return addMiscLang((provider, context) -> listener.accept(provider.region(region), context));
    }
}
