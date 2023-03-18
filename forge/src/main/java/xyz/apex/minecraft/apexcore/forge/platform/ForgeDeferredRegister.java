package xyz.apex.minecraft.apexcore.forge.platform;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryManager;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

class ForgeDeferredRegister<T> extends DeferredRegister<T>
{
    private boolean registered = false;
    @Nullable private RegisterEvent.RegisterHelper<T> registerHelper;

    ForgeDeferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        super(ownerId, registryType);

        Validate.isTrue(ModLoadingContext.get().getActiveNamespace().equals(ownerId), "DeferredRegister.register must be called while your mod is actively in context!");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.HIGHEST, this::onRegister);
    }

    @Override
    protected boolean isRegistered()
    {
        return registered;
    }

    @Override
    protected <R extends T> void registryEntry(RegistryEntry<R> registryEntry, Supplier<R> registryEntryFactory)
    {
        Validate.notNull(registerHelper);
        registerHelper.register(registryEntry.getRegistryName(), registryEntryFactory.get());
    }

    @Override
    protected Runnable buildRegisterCallback(ResourceLocation registryName, Consumer<T> registerCallback)
    {
        return () -> {
            var forgeRegistry = RegistryManager.ACTIVE.getRegistry(registryType);

            if(forgeRegistry != null)
            {
                forgeRegistry.getDelegate(registryName).ifPresent(delegate -> registerCallback.accept(delegate.value()));
                return;
            }

            findRegistry(registryType).flatMap(registry -> registry.getOptional(registryName)).ifPresent(registerCallback);
        };
    }

    @Override
    protected void updateReference(RegistryEntry<? extends T> registryEntry)
    {
        var forgeRegistry = RegistryManager.ACTIVE.getRegistry(registryType);

        if(forgeRegistry != null)
        {
            registryEntry.updateReference(new ForgeRegistryLookup<>(forgeRegistry));
            return;
        }

        super.updateReference(registryEntry);
    }

    @Override
    protected <R extends T> void updatePostRegistration(String registrationName)
    {
        throw new IllegalStateException("Attempt to register new entry '%s' post registration!".formatted(registrationName));
    }

    @Override
    protected void updatePostRegistration(ResourceLocation registryName)
    {
        throw new IllegalStateException("Attempt to register new entry '%s' post registration!".formatted(registryName));
    }

    private void onRegister(RegisterEvent event)
    {
        event.register(registryType, helper -> {
            if(registered) throw new IllegalStateException("Attempt to register deferred register of type '%s' for mod '%s' more than once".formatted(registryType.location(), ownerId));
            registerHelper = helper;
            registerEntries();
            updateReferences();
            registered = true;
            registerHelper = null;
        });
    }

    private record ForgeRegistryLookup<T>(IForgeRegistry<T> registry) implements HolderLookup.RegistryLookup<T>
    {
        @Override
        public ResourceKey<? extends Registry<? extends T>> key()
        {
            return registry.getRegistryKey();
        }

        @Override
        public Lifecycle registryLifecycle()
        {
            return Lifecycle.stable();
        }

        @Override
        public Stream<Holder.Reference<T>> listElements()
        {
            return registry
                    .getKeys()
                    .stream()
                    .map(registry::getDelegate)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    ;
        }

        @Override
        public Stream<HolderSet.Named<T>> listTags()
        {
            return Stream.empty();
        }

        @Override
        public Optional<Holder.Reference<T>> get(ResourceKey<T> registryKey)
        {
            return registry.getDelegate(registryKey);
        }

        @Override
        public Optional<HolderSet.Named<T>> get(TagKey<T> tag)
        {
            return Optional.empty();
        }
    }
}
