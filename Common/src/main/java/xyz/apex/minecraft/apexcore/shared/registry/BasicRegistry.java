package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class BasicRegistry<T> implements ModdedRegistry<T>
{
    public static final Logger LOGGER = LogManager.getLogger();

    protected final ResourceKey<Registry<T>> type;
    protected final ResourceLocation name;
    protected final String modId;

    private final Map<ResourceKey<T>, List<RegisterCallback<T>>> entryCallbackMap = Maps.newHashMap();
    private final Map<ResourceKey<T>, RegistryEntry<T>> entryMap = Maps.newHashMap();
    private final Map<RegistryEntry<T>, ResourceKey<T>> keyMap = Maps.newHashMap();
    private final Collection<RegistryEntry<T>> values = Collections.unmodifiableCollection(keyMap.keySet());
    private final LazyLoadedValue<Registry<T>> vanillaSupplier;

    protected BasicRegistry(ResourceKey<Registry<T>> type, String modId, Supplier<Registry<T>> vanillaSupplier)
    {
        this.type = type;
        this.modId = modId;
        this.vanillaSupplier = new LazyLoadedValue<>(vanillaSupplier);

        name = new ResourceLocation(modId, type.location().getPath());
    }

    @SuppressWarnings("unchecked")
    protected BasicRegistry(ResourceKey<Registry<T>> type, String modId)
    {
        this(type, modId, () -> (Registry<T>) Registry.REGISTRY.get(type.location()));
    }

    @SuppressWarnings("unchecked")
    private <R extends T> void onRegisterInternal(ResourceKey<T> key, RegistryEntry<R> entry, R value)
    {
        onRegister(key, entry, value);

        if(entryCallbackMap.containsKey(key))
        {
            entryCallbackMap.get(key).forEach(callback -> ((RegisterCallback<R>) callback).onRegister(key, entry, value));
            entryCallbackMap.clear();
        }

        ModdedRegistries.INSTANCE.onRegister(key, entry, value);
    }

    protected <R extends T> void onRegister(ResourceKey<T> key, RegistryEntry<R> entry, R value)
    {
    }

    protected <R extends T> RegistryEntry<R> createRegistryEntry(ResourceKey<R> key, Supplier<R> supplier)
    {
        return new BasicRegistryEntry<>(this, key, supplier);
    }

    @Override
    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return type;
    }

    @Override
    public final ResourceLocation getRegistryName()
    {
        return name;
    }

    @Override
    public final Registry<T> asVanilla()
    {
        return Objects.requireNonNull(vanillaSupplier.get(), () -> "Vanilla registry == null, This should never be the case | %s: %s".formatted(modId, type.location()));
    }

    @Override
    public final ResourceKey<T> createKey(ResourceLocation id)
    {
        if(!id.getNamespace().equals(modId)) throw new IllegalStateException("Attempt to registry key from id of differing mod | %s: %s (%s)".formatted(id, type.location(), modId));
        return ResourceKey.create(type, new ResourceLocation(modId, id.getPath()));
    }

    @Override
    public final ResourceKey<T> createKey(String name)
    {
        return ResourceKey.create(type, new ResourceLocation(modId, name));
    }

    @Override
    public final void addOnRegisterCallback(ResourceKey<T> key, RegisterCallback<T> callback)
    {
        lookupEntry(key).ifPresentOrElse(
                // immediately invoke the callback if already registered
                entry -> callback.onRegister(key, entry, entry.get()),
                // register the callback to be invoked later if not registered
                () -> entryCallbackMap.computeIfAbsent(key, $ -> Lists.newArrayList()).add(callback)
        );
    }

    @SuppressWarnings({ "unchecked", "Guava" })
    private <R extends T> RegistryEntry<R> registerInternal(ResourceKey<T> key, Supplier<R> factory)
    {
        if(entryMap.containsKey(key)) throw new IllegalStateException("Attempt to register object with duplicate key | %s: %s)".formatted(key.location(), type.location()));
        if(!key.isFor(type)) throw new IllegalStateException("Attempt to register object with key of differing type | %s[%s] (%s: %s)".formatted(key.location(), key.registry(), modId, type.location()));
        if(!key.location().getNamespace().equals(modId)) throw new IllegalStateException("Attempt to register object to registry of differing mod | %s: %s (%s)".formatted(key.location(), type.location(), modId));

        LOGGER.info("Registering {}: {}", type.location(), key.location());

        var ref = new AtomicReference<RegistryEntry<R>>();

        // replace the factory supplier with a custom one that ships registration callbacks
        var supplier = Suppliers.memoize(() -> {
            var value = factory.get();
            onRegisterInternal(key, ref.get(), value);
            return value;
        });

        var entry = createRegistryEntry((ResourceKey<R>) key, supplier);
        ref.set(entry);
        Platform.registries().register(asVanilla(), this, key, entry);

        entryMap.put(key, (RegistryEntry<T>) entry);
        keyMap.put((RegistryEntry<T>) entry, key);

        return entry;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public <R extends T> RegistryEntry<R> register(ResourceKey<T> key, Supplier<R> factory)
    {
        return registerInternal(key, factory);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public <R extends T> RegistryEntry<R> register(String name, Supplier<R> factory)
    {
        return register(createKey(name), factory);
    }

    @Override
    public final int size()
    {
        return entryMap.size();
    }

    @Override
    public final boolean contains(RegistryEntry<T> entry)
    {
        return contains(entry.getKey());
    }

    @Override
    public final boolean contains(ResourceKey<T> key)
    {
        return entryMap.containsKey(key);
    }

    @Override
    public final boolean contains(String name)
    {
        return contains(createKey(name));
    }

    @Override
    public final boolean isEmpty()
    {
        return entryMap.isEmpty();
    }

    @OverridingMethodsMustInvokeSuper
    @Nullable
    @Override
    public RegistryEntry<T> getEntry(ResourceKey<T> key)
    {
        return lookupEntry(key).orElse(null);
    }

    @OverridingMethodsMustInvokeSuper
    @Nullable
    @Override
    public RegistryEntry<T> getEntry(ResourceLocation id)
    {
        return lookupEntry(id).orElse(null);
    }

    @OverridingMethodsMustInvokeSuper
    @Nullable
    @Override
    public RegistryEntry<T> getEntry(String name)
    {
        return lookupEntry(name).orElse(null);
    }

    @Override
    public final Optional<RegistryEntry<T>> lookupEntry(ResourceKey<T> key)
    {
        if(!key.isFor(type)) return Optional.empty();
        if(!key.location().getNamespace().equals(modId)) return Optional.empty();
        return Optional.of(entryMap.get(key));
    }

    @Override
    public final Optional<RegistryEntry<T>> lookupEntry(ResourceLocation id)
    {
        if(!id.getNamespace().equals(modId)) return Optional.empty();
        return lookupEntry(createKey(id));
    }

    @Override
    public final Optional<RegistryEntry<T>> lookupEntry(String name)
    {
        return lookupEntry(createKey(name));
    }

    @Nullable
    @Override
    public final T getValue(ResourceKey<T> key)
    {
        return lookup(key).orElse(null);
    }

    @Nullable
    @Override
    public final T getValue(ResourceLocation id)
    {
        return lookup(id).orElse(null);
    }

    @Nullable
    @Override
    public final T getValue(String name)
    {
        return lookup(name).orElse(null);
    }

    @Nullable
    @Override
    public final ResourceKey<T> getKey(T value)
    {
        return lookupKey(value).orElse(null);
    }

    @Nullable
    @Override
    public final ResourceLocation getId(T value)
    {
        return lookupId(value).orElse(null);
    }

    @Nullable
    @Override
    public final String getName(T value)
    {
        return lookupName(value).orElse(null);
    }

    @Override
    public final Optional<T> lookup(ResourceKey<T> key)
    {
        if(!key.isFor(type)) return Optional.empty();
        if(!key.location().getNamespace().equals(modId)) return Optional.empty();
        return Optional.of(entryMap.get(key).get());
    }

    @Override
    public final Optional<T> lookup(ResourceLocation id)
    {
        if(!id.getNamespace().equals(modId)) return Optional.empty();
        return lookup(createKey(id));
    }

    @Override
    public final Optional<T> lookup(String name)
    {
        return lookup(createKey(name));
    }

    @Override
    public final Optional<ResourceKey<T>> lookupKey(T value)
    {
        return Optional.ofNullable(keyMap.get(value));
    }

    @Override
    public final Optional<ResourceLocation> lookupId(T value)
    {
        return lookupKey(value).map(ResourceKey::location);
    }

    @Override
    public final Optional<String> lookupName(T value)
    {
        return lookupId(value).map(ResourceLocation::getPath);
    }

    @Override
    public final Set<ResourceKey<T>> keySet()
    {
        return entryMap.keySet();
    }

    @Override
    public final Collection<RegistryEntry<T>> entries()
    {
        return values;
    }

    @Override
    public final Collection<T> values()
    {
        return values.stream().map(RegistryEntry::get).toList();
    }

    @Override
    public final Set<Map.Entry<RegistryEntry<T>, ResourceKey<T>>> entrySet()
    {
        return keyMap.entrySet();
    }

    @Override
    public final boolean isFor(RegistryEntry<?> entry)
    {
        return type.isFor(entry.getRegistryType());
    }

    @Override
    public final boolean isFor(Registry<?> vanilla)
    {
        return type.isFor(vanilla.key());
    }

    @Override
    public final boolean isFor(ResourceKey<Registry<?>> type)
    {
        return this.type.isFor(type);
    }

    @Override
    public final boolean is(ModdedRegistry<?> modded)
    {
        return type.isFor(modded.getRegistryType()) && name.equals(modded.getRegistryName());
    }

    @SuppressWarnings("unchecked")
    public final boolean hasTag(TagKey<T> tag, Holder<? extends T> obj)
    {
        return vanillaSupplier.get().getTag(tag).map(named -> named.contains((Holder<T>) obj)).orElse(false);
    }

    @Override
    public int compareTo(@NotNull ModdedRegistry<?> modded)
    {
        var c = type.location().compareTo(modded.getRegistryType().location());
        if(c == 0) return name.compareTo(modded.getRegistryName());
        return c;
    }

    @NotNull
    @Override
    public final Iterator<RegistryEntry<T>> iterator()
    {
        return keyMap.keySet().iterator();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(obj instanceof ModdedRegistry<?> modded) return is(modded);
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, name);
    }

    @Override
    public String toString()
    {
        return "Registry<%s>(%s)".formatted(type.location(), name);
    }
}
