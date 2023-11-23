package dev.apexstudios.apexcore.common.registry;

import com.google.common.collect.*;
import com.google.errorprone.annotations.DoNotCall;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.registry.builder.*;
import dev.apexstudios.apexcore.common.registry.generic.MenuFactory;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.*;

public class AbstractRegister<O extends AbstractRegister<O>> implements RegistryHelper
{
    public static final Marker MARKER = MarkerManager.getMarker("Registries");

    protected final String ownerId;
    private final Table<ResourceKey<? extends Registry<?>>, String, Registration<?, ?>> registrations = HashBasedTable.create();
    private final Multimap<Pair<String, ResourceKey<? extends Registry<?>>>, Consumer<?>> registerListeners = HashMultimap.create();
    private final Multimap<ResourceKey<? extends Registry<?>>, Runnable> afterRegisterListeners = HashMultimap.create();
    private final Set<ResourceKey<? extends Registry<?>>> completedRegistrations = Sets.newHashSet();
    @Nullable private String currentName = null;
    private boolean skipErrors = false;
    private OptionalLike<ResourceKey<CreativeModeTab>> defaultCreativeModeTab = () -> null;

    protected AbstractRegister(String ownerId)
    {
        this.ownerId = ownerId;
    }

    @Override
    public final String ownerId()
    {
        return ownerId;
    }

    public final String currentName()
    {
        return Objects.requireNonNull(currentName, "Current name not set");
    }

    public final O skipErrors(boolean skipErrors)
    {
        if(skipErrors && Platform.get().isProduction())
            ApexCore.LOGGER.error("Ignoring skipErrors(true) as this is not a development environment!");
        else
            this.skipErrors = skipErrors;

        return self();
    }

    public final O skipErrors()
    {
        return skipErrors(true);
    }

    public final O object(String valueName)
    {
        currentName = valueName;
        return self();
    }

    public final <T, R extends T> DeferredHolder<T, R> get(ResourceKey<? extends Registry<T>> registryType)
    {
        return get(registryType, currentName());
    }

    public final <T, R extends T> DeferredHolder<T, R> get(ResourceKey<? extends Registry<T>> registryType, String valueName)
    {
        return this.<T, R>registration(registryType, valueName).delegate;
    }

    public final <T, R extends T> OptionalLike<DeferredHolder<T, R>> getOptional(ResourceKey<? extends Registry<T>> registryType)
    {
        return getOptional(registryType, currentName());
    }

    public final <T, R extends T> OptionalLike<DeferredHolder<T, R>> getOptional(ResourceKey<? extends Registry<T>> registryType, String valueName)
    {
        var registration = this.<T, R>registrationUnchecked(registryType, valueName);
        return registration == null ? OptionalLike.empty() : () -> registration.delegate;
    }

    public final <T> Collection<Holder<T>> getAll(ResourceKey<? extends Registry<T>> registryType)
    {
        return registrations.row(registryType).values().stream().map(registration -> (Holder<T>) registration.delegate).toList();
    }

    public final <T, R extends T> O addRegisterListener(ResourceKey<? extends Registry<T>> registryType, String valueName, Consumer<R> listener)
    {
        var registration = this.<T, R>registrationUnchecked(registryType, valueName);

        if(registration == null)
            registerListeners.put(Pair.of(valueName, registryType), listener);
        else
            registration.addListener(listener);

        return self();
    }

    public final <T> O addRegisterListener(ResourceKey<? extends Registry<T>> registryType, Runnable listener)
    {
        if(completedRegistrations.contains(registryType))
            listener.run();
        else
            afterRegisterListeners.put(registryType, listener);

        return self();
    }

    public final <T> boolean isRegistered(ResourceKey<? extends Registry<T>> registryType)
    {
        return completedRegistrations.contains(registryType);
    }

    protected final <P, T, R extends T, H extends DeferredHolder<T, R>, B extends AbstractBuilder<O, P, T, R, H, B>> B entry(Function<BuilderHelper, B> builderFactory)
    {
        return builderFactory.apply(this::register);
    }

    public final <P, T, R extends T, H extends DeferredHolder<T, R>> NoConfigBuilder<O, P, T, R, H> noConfig(P parent, String valueName, ResourceKey<? extends Registry<T>> registryType, BiFunction<String, ResourceKey<T>, H> holderFactory, Supplier<R> valueFactory)
    {
        return entry(helper -> new NoConfigBuilder<>(self(), parent, registryType, valueName, holderFactory, helper, valueFactory));
    }

    public final <P, T, R extends T> NoConfigBuilder<O, P, T, R, DeferredHolder<T, R>> noConfig(P parent, String valueName, ResourceKey<? extends Registry<T>> registryType, Supplier<R> valueFactory)
    {
        return noConfig(parent, valueName, registryType, DeferredHolder::create, valueFactory);
    }

    public final <P, T, R extends T, H extends DeferredHolder<T, R>> NoConfigBuilder<O, P, T, R, H> noConfig(P parent, ResourceKey<? extends Registry<T>> registryType, BiFunction<String, ResourceKey<T>, H> holderFactory, Supplier<R> valueFactory)
    {
        return noConfig(parent, currentName(), registryType, holderFactory, valueFactory);
    }

    public final <P, T, R extends T> NoConfigBuilder<O, P, T, R, DeferredHolder<T, R>> noConfig(P parent, ResourceKey<? extends Registry<T>> registryType, Supplier<R> valueFactory)
    {
        return noConfig(parent, currentName(), registryType, DeferredHolder::create, valueFactory);
    }

    public final <T, R extends T, H extends DeferredHolder<T, R>> NoConfigBuilder<O, O, T, R, H> noConfig(String valueName, ResourceKey<? extends Registry<T>> registryType, BiFunction<String, ResourceKey<T>, H> holderFactory, Supplier<R> valueFactory)
    {
        return noConfig(self(), valueName, registryType, holderFactory, valueFactory);
    }

    public final <T, R extends T> NoConfigBuilder<O, O, T, R, DeferredHolder<T, R>> noConfig(String valueName, ResourceKey<? extends Registry<T>> registryType, Supplier<R> valueFactory)
    {
        return noConfig(self(), valueName, registryType, DeferredHolder::create, valueFactory);
    }

    public final <T, R extends T, H extends DeferredHolder<T, R>> NoConfigBuilder<O, O, T, R, H> noConfig(ResourceKey<? extends Registry<T>> registryType, BiFunction<String, ResourceKey<T>, H> holderFactory, Supplier<R> valueFactory)
    {
        return noConfig(self(), currentName(), registryType, holderFactory, valueFactory);
    }

    public final <T, R extends T> NoConfigBuilder<O, O, T, R, DeferredHolder<T, R>> noConfig(ResourceKey<? extends Registry<T>> registryType, Supplier<R> valueFactory)
    {
        return noConfig(self(), currentName(), registryType, DeferredHolder::create, valueFactory);
    }

    public final <T, R extends T, H extends DeferredHolder<T, R>> H simple(String valueName, ResourceKey<? extends Registry<T>> registryType, BiFunction<String, ResourceKey<T>, H> holderFactory, Supplier<R> valueFactory)
    {
        return noConfig(valueName, registryType, holderFactory, valueFactory).register();
    }

    public final <T, R extends T> DeferredHolder<T, R> simple(String valueName, ResourceKey<? extends Registry<T>> registryType, Supplier<R> valueFactory)
    {
        return simple(valueName, registryType, DeferredHolder::create, valueFactory);
    }

    public final <T, R extends T> DeferredHolder<T, R> simple(ResourceKey<? extends Registry<T>> registryType, Supplier<R> valueFactory)
    {
        return simple(currentName(), registryType, DeferredHolder::create, valueFactory);
    }

    public final <T, R extends T, H extends DeferredHolder<T, R>> H simple(ResourceKey<? extends Registry<T>> registryType, BiFunction<String, ResourceKey<T>, H> holderFactory, Supplier<R> valueFactory)
    {
        return simple(currentName(), registryType, holderFactory, valueFactory);
    }

    public final <P, T extends Item> ItemBuilder<O, P, T> item(P parent, String itemName, Function<Item.Properties, T> itemFactory)
    {
        return entry(helper -> new ItemBuilder<>(self(), parent, itemName, helper, itemFactory));
    }

    public final <P> ItemBuilder<O, P, Item> item(P parent, String itemName)
    {
        return item(parent, itemName, Item::new);
    }

    public final <P, T extends Item> ItemBuilder<O, P, T> item(P parent, Function<Item.Properties, T> itemFactory)
    {
        return item(parent, currentName(), itemFactory);
    }

    public final <P> ItemBuilder<O, P, Item> item(P parent)
    {
        return item(parent, currentName(), Item::new);
    }

    public final <T extends Item> ItemBuilder<O, O, T> item(String itemName, Function<Item.Properties, T> itemFactory)
    {
        return item(self(), itemName, itemFactory);
    }

    public final <T extends Item> ItemBuilder<O, O, T> item(Function<Item.Properties, T> itemFactory)
    {
        return item(self(), currentName(), itemFactory);
    }

    public final ItemBuilder<O, O, Item> item(String itemName)
    {
        return item(self(), itemName, Item::new);
    }

    public final ItemBuilder<O, O, Item> item()
    {
        return item(self(), currentName(), Item::new);
    }

    public final <P, T extends Block> BlockBuilder<O, P, T> block(P parent, String blockName, Function<BlockBehaviour.Properties, T> blockFactory)
    {
        return entry(helper -> new BlockBuilder<>(self(), parent, blockName, helper, blockFactory));
    }

    public final <P> BlockBuilder<O, P, Block> block(P parent, String blockName)
    {
        return block(parent, blockName, Block::new);
    }

    public final <P, T extends Block> BlockBuilder<O, P, T> block(P parent, Function<BlockBehaviour.Properties, T> blockFactory)
    {
        return block(parent, currentName(), blockFactory);
    }

    public final <P> BlockBuilder<O, P, Block> block(P parent)
    {
        return block(parent, currentName(), Block::new);
    }

    public final <T extends Block> BlockBuilder<O, O, T> block(String blockName, Function<BlockBehaviour.Properties, T> blockFactory)
    {
        return block(self(), blockName, blockFactory);
    }

    public final <T extends Block> BlockBuilder<O, O, T> block(Function<BlockBehaviour.Properties, T> blockFactory)
    {
        return block(self(), currentName(), blockFactory);
    }

    public final BlockBuilder<O, O, Block> block(String blockName)
    {
        return block(self(), blockName, Block::new);
    }

    public final BlockBuilder<O, O, Block> block()
    {
        return block(self(), currentName(), Block::new);
    }

    public final <P, T extends Entity> EntityTypeBuilder<O, P, T> entity(P parent, String entityName, MobCategory category, EntityType.EntityFactory<T> entityFactory)
    {
        return entry(helper -> new EntityTypeBuilder<>(self(), parent, entityName, helper, category, entityFactory));
    }

    public final <P, T extends Entity> EntityTypeBuilder<O, P, T> entity(P parent, MobCategory category, EntityType.EntityFactory<T> entityFactory)
    {
        return entity(parent, currentName(), category, entityFactory);
    }

    public final <T extends Entity> EntityTypeBuilder<O, O, T> entity(String entityName, MobCategory category, EntityType.EntityFactory<T> entityFactory)
    {
        return entity(self(), entityName, category, entityFactory);
    }

    public final <T extends Entity> EntityTypeBuilder<O, O, T> entity(MobCategory category, EntityType.EntityFactory<T> entityFactory)
    {
        return entity(self(), currentName(), category, entityFactory);
    }

    public final <P, T extends BlockEntity> BlockEntityTypeBuilder<O, P, T> blockEntity(P parent, String blockEntityName, BlockEntityTypeBuilder.Factory<T> blockEntityFactory)
    {
        return entry(helper -> new BlockEntityTypeBuilder<>(self(), parent, blockEntityName, helper, blockEntityFactory));
    }

    public final <P, T extends BlockEntity> BlockEntityTypeBuilder<O, P, T> blockEntity(P parent, BlockEntityTypeBuilder.Factory<T> blockEntityFactory)
    {
        return blockEntity(parent, currentName(), blockEntityFactory);
    }

    public final <T extends BlockEntity> BlockEntityTypeBuilder<O, O, T> blockEntity(String blockEntityName, BlockEntityTypeBuilder.Factory<T> blockEntityFactory)
    {
        return blockEntity(self(), blockEntityName, blockEntityFactory);
    }

    public final <T extends BlockEntity> BlockEntityTypeBuilder<O, O, T> blockEntity(BlockEntityTypeBuilder.Factory<T> blockEntityFactory)
    {
        return blockEntity(self(), currentName(), blockEntityFactory);
    }

    public final <P, T extends BlockEntity> BlockEntityTypeBuilder<O, P, T> blockEntity(P parent, String blockEntityName, BlockEntityType.BlockEntitySupplier<T> blockEntityFactory)
    {
        return blockEntity(parent, blockEntityName, BlockEntityTypeBuilder.Factory.fromVanilla(blockEntityFactory));
    }

    public final <P, T extends BlockEntity> BlockEntityTypeBuilder<O, P, T> blockEntity(P parent, BlockEntityType.BlockEntitySupplier<T> blockEntityFactory)
    {
        return blockEntity(parent, currentName(), blockEntityFactory);
    }

    public final <T extends BlockEntity> BlockEntityTypeBuilder<O, O, T> blockEntity(String blockEntityName, BlockEntityType.BlockEntitySupplier<T> blockEntityFactory)
    {
        return blockEntity(self(), blockEntityName, blockEntityFactory);
    }

    public final <T extends BlockEntity> BlockEntityTypeBuilder<O, O, T> blockEntity(BlockEntityType.BlockEntitySupplier<T> blockEntityFactory)
    {
        return blockEntity(self(), currentName(), blockEntityFactory);
    }

    public final <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> DeferredHolder<MenuType<?>, MenuType<T>> menu(String menuName, MenuFactory<T> menuFactory, OptionalLike<OptionalLike<MenuScreens.ScreenConstructor<T, S>>> screenFactory)
    {
        return noConfig(menuName, Registries.MENU, () -> Platform.get().factory().menuType(menuFactory))
                .onRegister(menuType -> RegistryHelper.registerMenuScreenFactory(menuType, screenFactory))
        .register();
    }

    public final <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> DeferredHolder<MenuType<?>, MenuType<T>> menu(MenuFactory<T> menuFactory, OptionalLike<OptionalLike<MenuScreens.ScreenConstructor<T, S>>> screenFactory)
    {
        return menu(currentName(), menuFactory, screenFactory);
    }

    public final ResourceKey<CreativeModeTab> creativeModeTab(String creativeModeTabName, UnaryOperator<CreativeModeTab.Builder> properties)
    {
        return simple(creativeModeTabName, Registries.CREATIVE_MODE_TAB, () -> properties.apply(RegistryHelper.defaultCreativeModeTab(ownerId, creativeModeTabName)).build()).registryKey;
    }

    public final ResourceKey<CreativeModeTab> creativeModeTab(String creativeModeTabName)
    {
        return creativeModeTab(creativeModeTabName, UnaryOperator.identity());
    }

    public final ResourceKey<CreativeModeTab> creativeModeTab(UnaryOperator<CreativeModeTab.Builder> properties)
    {
        return creativeModeTab(currentName(), properties);
    }

    public final ResourceKey<CreativeModeTab> creativeModeTab()
    {
        return creativeModeTab(currentName(), UnaryOperator.identity());
    }

    public final ResourceKey<CreativeModeTab> defaultCreativeModeTab(String creativeModeTabName, UnaryOperator<CreativeModeTab.Builder> properties)
    {
        var creativeModeTab = creativeModeTab(creativeModeTabName, properties);
        defaultCreativeModeTab(creativeModeTab);
        return creativeModeTab;
    }

    public final ResourceKey<CreativeModeTab> defaultCreativeModeTab(String creativeModeTabName)
    {
        return defaultCreativeModeTab(creativeModeTabName, UnaryOperator.identity());
    }

    public final ResourceKey<CreativeModeTab> defaultCreativeModeTab(UnaryOperator<CreativeModeTab.Builder> properties)
    {
        return defaultCreativeModeTab(currentName(), properties);
    }

    public final ResourceKey<CreativeModeTab> defaultCreativeModeTab()
    {
        return defaultCreativeModeTab(currentName(), UnaryOperator.identity());
    }

    // this must be called prior to any `item()` calls
    // in which you want to inherit this creative tab as the default one
    // this is due to the check being in the ItemBuilder constructor
    // to allow the removal api to work with the default tab
    public final O defaultCreativeModeTab(ResourceKey<CreativeModeTab> defaultCreativeModeTab)
    {
        this.defaultCreativeModeTab = () -> defaultCreativeModeTab;
        return self();
    }

    @Nullable
    public final ResourceKey<CreativeModeTab> getDefaultCreativeModeTab()
    {
        return defaultCreativeModeTab.getRaw();
    }

    public final O withDefaultCreativeModeTab(Consumer<ResourceKey<CreativeModeTab>> consumer)
    {
        defaultCreativeModeTab.ifPresent(consumer);
        return self();
    }

    protected final O self()
    {
        return (O) this;
    }

    public final void register()
    {
        RegistryHelper.get(ownerId).register(this);
    }

    @Deprecated
    @DoNotCall
    @Override
    public final void register(AbstractRegister<?> register)
    {
        throw new IllegalStateException("It is illegal to call AbstractRegister.register(AbstractRegister<?>)");
    }

    @Override
    public final <T> Optional<Holder.Reference<T>> getDelegate(ResourceKey<T> registryKey)
    {
        return RegistryHelper.get(ownerId).getDelegate(registryKey);
    }

    @Override
    public final void registerColorHandler(ItemLike item, OptionalLike<OptionalLike<ItemColor>> colorHandler)
    {
        RegistryHelper.get(ownerId).registerColorHandler(item, colorHandler);
    }

    @Override
    public final void registerColorHandler(Block block, OptionalLike<OptionalLike<BlockColor>> colorHandler)
    {
        RegistryHelper.get(ownerId).registerColorHandler(block, colorHandler);
    }

    @Override
    public final void registerRenderType(Block block, OptionalLike<OptionalLike<RenderType>> renderType)
    {
        RegistryHelper.get(ownerId).registerRenderType(block, renderType);
    }

    @Override
    public final void registerRenderType(Fluid fluid, OptionalLike<OptionalLike<RenderType>> renderType)
    {
        RegistryHelper.get(ownerId).registerRenderType(fluid, renderType);
    }

    @Override
    public final <T extends Entity> void registerEntityAttributes(EntityType<T> entityType, OptionalLike<AttributeSupplier.Builder> attributes)
    {
        RegistryHelper.get(ownerId).registerEntityAttributes(entityType, attributes);
    }

    @Override
    public final <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, OptionalLike<OptionalLike<EntityRendererProvider<T>>> rendererProvider)
    {
        RegistryHelper.get(ownerId).registerEntityRenderer(entityType, rendererProvider);
    }

    @Override
    public final void registerItemDispenseBehavior(ItemLike item, OptionalLike<DispenseItemBehavior> dispenseBehavior)
    {
        RegistryHelper.get(ownerId).registerItemDispenseBehavior(item, dispenseBehavior);
    }

    @Override
    public final <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> blockEntityType, OptionalLike<OptionalLike<BlockEntityRendererProvider<T>>> rendererProvider)
    {
        RegistryHelper.get(ownerId).registerBlockEntityRenderer(blockEntityType, rendererProvider);
    }

    @Override
    public final void registerCreativeModeTabItemGenerator(ResourceKey<CreativeModeTab> creativeModeTab, CreativeModeTab.DisplayItemsGenerator generator)
    {
        RegistryHelper.get(ownerId).registerCreativeModeTabItemGenerator(creativeModeTab, generator);
    }

    private <T, R extends T, H extends DeferredHolder<T, R>> H register(ResourceKey<? extends Registry<T>> registryType, Supplier<H> holderFactory, Supplier<R> valueFactory)
    {
        var registration = new Registration<>(registryType, valueFactory, holderFactory);
        ApexCore.LOGGER.debug(MARKER, "Captured registration for entry {} of type {}", registration.delegate.registryKey.location(), registryType.location());
        registerListeners.removeAll(Pair.of(registration.delegate.registryKey.location().getPath(), registryType)).forEach(listener -> registration.addListener((Consumer<R>) listener));
        registrations.put(registryType, registration.delegate.registryKey.location().getPath(), registration);
        return (H) registration.delegate;
    }

    private <T, R extends T> Registration<T, R> registration(ResourceKey<? extends Registry<T>> registryType, String valueName)
    {
        var registration = this.<T, R>registrationUnchecked(registryType, valueName);
        return Objects.requireNonNull(registration, () -> "Unknown registration %s for type %s".formatted(valueName, registryType));
    }

    @Nullable
    private <T, R extends T> Registration<T, R> registrationUnchecked(ResourceKey<? extends Registry<T>> registryType, String valueName)
    {
        var registration = registrations.get(registryType, valueName);
        return registration == null ? null : (Registration<T, R>) registration;
    }

    @ApiStatus.Internal
    @DoNotCall
    public void onRegister(@Nullable ResourceKey<? extends Registry<?>> registryType, RegistrationHelper helper)
    {
        if(registryType == null)
        {
            ApexCore.LOGGER.debug(MARKER, "Skipping invalid registry with no super type");
            return;
        }

        if(!registerListeners.isEmpty())
        {
            registerListeners.asMap().forEach((k, v) -> ApexCore.LOGGER.warn(MARKER, "Found {} unused register callback(s) for entry {} [{}]. Was the entry ever registered?", v.size(), k.getLeft(), k.getRight().location()));
            registerListeners.clear();

            if(!Platform.get().isProduction())
                throw new IllegalStateException("Found unused register callbacks, see logs");

            return;
        }

        var registrationsForType = registrations.row(registryType);

        if(!registrationsForType.isEmpty())
        {
            ApexCore.LOGGER.debug(MARKER, "({}) Registering {} known objects of type {}", ownerId, registrationsForType.size(), registryType.location());

            for(var registration : registrationsForType.values())
            {
                try
                {
                    registration.register(helper);
                    ApexCore.LOGGER.debug(MARKER, "Registered {} to registry {}", registration.delegate.registryKey.location(), registryType);
                }
                catch(Exception e)
                {
                    var msg = ApexCore.LOGGER.getMessageFactory().newMessage("Unexpected error while registering entry {} to registry {}", registration.delegate.registryKey.location(), registryType);

                    if(skipErrors)
                        ApexCore.LOGGER.error(MARKER, msg, e);
                    else
                        throw new RuntimeException(msg.getFormattedMessage(), e);
                }
            }
        }
    }

    @ApiStatus.Internal
    @DoNotCall
    public void onRegisterLate(@Nullable ResourceKey<? extends Registry<?>> registryType)
    {
        if(registryType == null)
            return;

        var listeners = afterRegisterListeners.get(registryType);
        listeners.forEach(Runnable::run);
        listeners.clear();
        completedRegistrations.add(registryType);
    }
}
