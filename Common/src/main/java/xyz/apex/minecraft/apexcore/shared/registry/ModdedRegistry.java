package xyz.apex.minecraft.apexcore.shared.registry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public interface ModdedRegistry<T> extends Iterable<RegistryEntry<T>>, Comparable<ModdedRegistry<?>>
{
    ResourceKey<? extends Registry<T>> getRegistryType();
    ResourceLocation getRegistryName();
    Registry<T> asVanilla();
    ResourceKey<T> createKey(ResourceLocation id);
    ResourceKey<T> createKey(String name);

    <R extends T> void addOnRegisterCallback(ResourceKey<T> key, RegisterCallback<R> callback);

    <R extends T> RegistryEntry<R> register(ResourceKey<T> key, Supplier<R> factory);
    <R extends T> RegistryEntry<R> register(String name, Supplier<R> factory);

    int size();
    boolean contains(RegistryEntry<T> entry);
    boolean contains(ResourceKey<T> key);
    boolean contains(String name);
    boolean isEmpty();

    @Nullable RegistryEntry<T> getEntry(ResourceKey<T> key);
    @Nullable RegistryEntry<T> getEntry(ResourceLocation id);
    @Nullable RegistryEntry<T> getEntry(String name);
    Optional<RegistryEntry<T>> lookupEntry(ResourceKey<T> key);
    Optional<RegistryEntry<T>> lookupEntry(ResourceLocation id);
    Optional<RegistryEntry<T>> lookupEntry(String name);

    @Nullable T getValue(ResourceKey<T> key);
    @Nullable T getValue(ResourceLocation id);
    @Nullable T getValue(String name);
    @Nullable ResourceKey<T> getKey(T value);
    @Nullable ResourceLocation getId(T value);
    @Nullable String getName(T value);

    Optional<T> lookup(ResourceKey<T> key);
    Optional<T> lookup(ResourceLocation id);
    Optional<T> lookup(String name);
    Optional<ResourceKey<T>> lookupKey(T value);
    Optional<ResourceLocation> lookupId(T value);
    Optional<String> lookupName(T value);

    Set<ResourceKey<T>> keySet();
    Collection<RegistryEntry<T>> entries();
    Collection<T> values();
    Set<Map.Entry<RegistryEntry<T>, ResourceKey<T>>> entrySet();

    boolean isFor(RegistryEntry<?> entry);
    boolean isFor(Registry<?> vanilla);
    boolean isFor(ResourceKey<Registry<?>> type);
    boolean is(ModdedRegistry<?> modded);

    boolean hasTag(TagKey<T> tag, Holder<? extends T> obj);

    interface RegisterCallback<R>
    {
        void onRegister(ResourceKey<? super R> key, RegistryEntry<R> entry, R value);
    }
}
