package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.BaseRegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;

import java.util.function.Supplier;

/**
 * Simple Builder implementation.
 * <p>
 * Used to build and register simple entries.
 *
 * @param <O> Type of Registrar.
 * @param <T> Type of Registry.
 * @param <R> Type of Entry.
 * @param <P> Type of Parent.
 */
public final class SimpleBuilder<O extends AbstractRegistrar<O>, T, R extends T, P> extends AbstractBuilder<O, P, T, R, SimpleBuilder<O, T, R, P>, RegistryEntry<R>>
{
    private final Supplier<R> entryFactory;

    @ApiStatus.Internal
    public SimpleBuilder(O registrar, P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> entryFactory)
    {
        super(registrar, parent, registryType, registrationName);

        this.entryFactory = entryFactory;

        noLang();
    }

    @Override
    protected RegistryEntry<R> createRegistryEntry()
    {
        return new BaseRegistryEntry<>(registrar, registryKey);
    }

    @Override
    protected R createEntry()
    {
        return entryFactory.get();
    }

    @Override
    protected String getDescriptionId(RegistryEntry<R> entry)
    {
        return registryName().toLanguageKey("unk.%s".formatted(registryType.location().getPath()));
    }
}
