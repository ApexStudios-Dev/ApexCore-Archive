package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.*;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import xyz.apex.minecraft.apexcore.shared.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.shared.registry.builder.*;
import xyz.apex.minecraft.apexcore.shared.registry.entry.MenuEntry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@SuppressWarnings({ "UnusedReturnValue", "unchecked", "SuspiciousMethodCalls", "UnstableApiUsage", "rawtypes" })
public class AbstractRegistrar<S extends AbstractRegistrar<S>>
{
    private final Table<ResourceKey<? extends Registry<?>>, String, Registration<?, ?>> registrations = HashBasedTable.create();
    private final Multimap<Pair<ResourceKey<? extends Registry<?>>, String>, Consumer<?>> registerCallbacks = HashMultimap.create();
    private final Multimap<ResourceKey<? extends Registry<?>>, Runnable> afterRegisterCallbacks = HashMultimap.create();
    private final Set<ResourceKey<? extends Registry<?>>> completedRegistrations = Sets.newHashSet();
    private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> deferredRegisters = Maps.newHashMap();
    private final String modId;
    @Nullable private ModPlatform mod;

    protected AbstractRegistrar(String modId)
    {
        this.modId = modId;
    }

    public final ModPlatform getMod()
    {
        return Objects.requireNonNull(mod);
    }

    public final ResourceLocation registryName(String registrationName)
    {
        return new ResourceLocation(modId, registrationName);
    }

    public final String getModId()
    {
        return modId;
    }

    protected final S self()
    {
        return (S) this;
    }

    public final S transform(UnaryOperator<S> transformer)
    {
        return transformer.apply(self());
    }

    public final <T, R extends T, O extends AbstractRegistrar<O>, P, B extends Builder<T, R, O, P, B>> B map(Function<S, B> mapper)
    {
        return mapper.apply(self());
    }

    public final S creativeModeTab(String creativeModeTabName, Supplier<ItemStack> icon)
    {
        CreativeTabRegistry.create(registryName(creativeModeTabName), icon);
        return self();
    }

    public final S creativeModeTab(String creativeModeTabName, Consumer<CreativeModeTab.Builder> builder)
    {
        CreativeTabRegistry.create(registryName(creativeModeTabName), builder);
        return self();
    }

    public final <T, R extends T> RegistryEntry<R> simple(ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> entryFactory)
    {
        return simple(self(), registryType, registrationName, entryFactory);
    }

    public final <T, R extends T, P> RegistryEntry<R> simple(P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> entryFactory)
    {
        return new NoConfigBuilder<>(self(), parent, registryType, registrationName, entryFactory).register();
    }

    public final <T extends Item, P> ItemBuilder<T, S, P> item(P parent, String itemName, ItemBuilder.Factory<T> factory)
    {
        return new ItemBuilder<>(self(), parent, itemName, factory);
    }

    public final <P> ItemBuilder<Item, S, P> item(P parent, String itemName)
    {
        return item(parent, itemName, Item::new);
    }

    public final <T extends Item> ItemBuilder<T, S, S> item(String itemName, ItemBuilder.Factory<T> factory)
    {
        return item(self(), itemName, factory);
    }

    public final ItemBuilder<Item, S, S> item(String itemName)
    {
        return item(self(), itemName, Item::new);
    }

    public final <T extends Block, P> BlockBuilder<T, S, P> block(P parent, String blockName, BlockBuilder.Factory<T> factory)
    {
        return new BlockBuilder<>(self(), parent, blockName, factory);
    }

    public final <P> BlockBuilder<Block, S, P> block(P parent, String blockName)
    {
        return block(parent, blockName, Block::new);
    }

    public final <T extends Block> BlockBuilder<T, S, S> block(String blockName, BlockBuilder.Factory<T> factory)
    {
        return block(self(), blockName, factory);
    }

    public final BlockBuilder<Block, S, S> block(String blockName)
    {
        return block(self(), blockName, Block::new);
    }

    public final <T extends BlockEntity, P> BlockEntityBuilder<T, S, P> blockEntity(P parent, String blockEntityName, BlockEntityBuilder.Factory<T> factory)
    {
        return new BlockEntityBuilder<>(self(), parent, blockEntityName, factory);
    }

    public final <T extends BlockEntity> BlockEntityBuilder<T, S, S> blockEntity(String blockEntityName, BlockEntityBuilder.Factory<T> factory)
    {
        return blockEntity(self(), blockEntityName, factory);
    }

    public final <T extends AbstractContainerMenu, C extends Screen & MenuAccess<T>> MenuEntry<T> menu(String menuName, MenuBuilder.MenuFactory<T> menuFactory, Supplier<MenuBuilder.ScreenFactory<T, C>> screenFactorySupplier)
    {
        return new MenuBuilder<>(self(), self(), menuName, menuFactory, screenFactorySupplier).register();
    }

    public final <T extends Entity, P> EntityBuilder<T, S, P> entity(P parent, String entityName, EntityBuilder.Factory<T> factory)
    {
        return new EntityBuilder<>(self(), parent, entityName, factory);
    }

    public final <T extends Entity> EntityBuilder<T, S, S> entity(String entityName, EntityBuilder.Factory<T> factory)
    {
        return entity(self(), entityName, factory);
    }

    @ApiStatus.Internal
    public final <T, R extends T, P, B extends Builder<T, R, S, P, B>> RegistryEntry<R> accept(B builder, Supplier<R> entryFactory, Function<RegistrySupplier<R>, RegistryEntry<R>> registryEntryFactory)
    {
        var registrationName = builder.getRegistrationName();
        var registryType = builder.getRegistryType();
        var register = getRegister(registryType);
        var registration = new Registration<>(register, builder.getRegistryName(), registryEntryFactory, entryFactory);
        registerCallbacks.removeAll(Pair.of(registryType, registrationName)).forEach(callback -> registration.addCallback((Consumer<R>) callback));
        registrations.put(registryType, registrationName, registration);
        return registration.registryEntry;
    }

    @ApiStatus.Internal
    public final <T, R extends T> RegistrySupplier<R> getDelegate(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        return Objects.requireNonNull(this.<T, R>getRegistrationOrThrow(registryType, registrationName).delegate);
    }

    @ApiStatus.Internal
    public final <T, R extends T> Optional<RegistrySupplier<R>> getOptionalDelegate(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);
        if(registration == null) return Optional.empty();
        return Optional.of(registration.delegate);
    }

    public final <T, R extends T> RegistryEntry<R> get(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        return this.<T, R>getRegistrationOrThrow(registryType, registrationName).registryEntry;
    }

    public final <T, R extends T> Optional<RegistryEntry<R>> getOptional(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);
        return registration == null ? Optional.empty() : Optional.of(registration.registryEntry);
    }

    public final <T> Collection<RegistryEntry<T>> getAll(ResourceKey<? extends Registry<T>> registryType)
    {
        return stream(registryType).toList();
    }

    public final <T> Stream<RegistryEntry<T>> stream(ResourceKey<? extends Registry<T>> registryType)
    {
        return registrations.row(registryType).values().stream().map(registration -> (RegistryEntry<T>) registration.registryEntry);
    }

    public final <T, R extends T> S addRegisterCallback(ResourceKey<? extends Registry<T>> registryType, String registrationName, Consumer<R> callback)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);

        if(registration == null) registerCallbacks.put(Pair.of(registryType, registrationName), callback);
        else registration.addCallback(callback);

        return self();
    }

    public final <T> S addRegisterCallback(ResourceKey<? extends Registry<T>> registryType, Runnable callback)
    {
        afterRegisterCallbacks.put(registryType, callback);
        return self();
    }

    public final boolean isRegistered(ResourceKey<? extends Registry<?>> registryType)
    {
        return completedRegistrations.contains(registryType);
    }

    @Nullable
    private <T, R extends T> Registration<T, R> getRegistrationUnchecked(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        return registrations.contains(registryType, registrationName) ? (Registration<T, R>) registrations.get(registryType, registrationName) : null;
    }

    private <T, R extends T> Registration<T, R> getRegistrationOrThrow(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);
        if(registration == null) throw new IllegalArgumentException("Unknown registration %s for type %s".formatted(registrationName, registryType));
        return registration;
    }

    @ApiStatus.Internal
    public final <T> void register(ResourceKey<? extends Registry<T>> registryType)
    {
        if(completedRegistrations.contains(registryType)) return;
        registrations.row(registryType).forEach((key, value) -> value.register());
        getRegister(registryType).register();
    }

    @ApiStatus.Internal
    public final void register()
    {
        forEachRegistry(registry -> register(registry.key()));
    }

    @ApiStatus.Internal
    public final <T> void lateRegister(ResourceKey<? extends Registry<T>> registryType)
    {
        if(completedRegistrations.contains(registryType)) return;
        afterRegisterCallbacks.removeAll(registryType).forEach(Runnable::run);
        completedRegistrations.add(registryType);
    }

    @ApiStatus.Internal
    public final void lateRegister()
    {
        forEachRegistry(registry -> lateRegister(registry.key()));
    }

    @ApiStatus.Internal
    public final void setMod(ModPlatform mod)
    {
        if(this.mod != null) throw new IllegalStateException("AbstractRegistrar#setMod should only ever be called once! Do not manually call this method!");
        if(!modId.equals(mod.getModId())) throw new IllegalStateException("AbstractRegistrar#setMod should be called with ModPlatform of same ModID");
        this.mod = mod;
    }

    private <T> DeferredRegister<T> getRegister(ResourceKey<? extends Registry<T>> registryType)
    {
        return (DeferredRegister<T>) deferredRegisters.computeIfAbsent(registryType, type -> DeferredRegister.create(modId, (ResourceKey) type));
    }

    // mostly used for fabric, to call the #register & #lateRegister methods
    // since fabric does not have any register events to listen to
    // forge uses the register events to call these methods at correct time and in correct orders
    // order in this method might not be perfect 1:1 with forge registration order
    // but its close enough and registration order shouldn't matter as much when running on fabric
    public static void forEachRegistry(Consumer<Registry<?>> consumer)
    {
        // these must go first
        consumer.accept(BuiltInRegistries.FLUID);
        consumer.accept(BuiltInRegistries.BLOCK);
        consumer.accept(BuiltInRegistries.ITEM);

        // all other registries not specified above
        BuiltInRegistries.REGISTRY.forEach(registry -> {
            var registryType = registry.key();
            if(registryType.equals(Registries.FLUID)) return;
            if(registryType.equals(Registries.BLOCK)) return;
            if(registryType.equals(Registries.ITEM)) return;
            consumer.accept(registry);
        });
    }

    private static final class Registration<T, R extends T>
    {
        private final RegistrySupplier<R> delegate;
        private final RegistryEntry<R> registryEntry;
        private final List<Consumer<R>> callbacks = Lists.newArrayList();
        private boolean registered = false;

        private Registration(DeferredRegister<T> register, ResourceLocation registryName, Function<RegistrySupplier<R>, RegistryEntry<R>> registryEntryFactory, Supplier<R> entryFactory)
        {
            delegate = register.register(registryName, entryFactory);
            registryEntry = registryEntryFactory.apply(delegate);
        }

        private void register()
        {
            delegate.listen(value -> {
                if(registered) return;
                callbacks.forEach(callback -> callback.accept(value));
                callbacks.clear();
                registered = true;
            });
        }

        private void addCallback(Consumer<R> consumer)
        {
            if(registered) consumer.accept(registryEntry.get());
            else callbacks.add(consumer);
        }
    }
}
