package xyz.apex.forge.apexcore.registrate;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.*;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import xyz.apex.forge.apexcore.registrate.entry.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

// Bare bones Registrate object
@SuppressWarnings({ "unchecked", "UnstableApiUsage", "unused" })
public class CoreRegistrate<OWNER extends CoreRegistrate<OWNER>>
{
	@ApiStatus.Internal
	public final Backend backend;

	protected final OWNER self = (OWNER) this;
	public final String modId;

	protected CoreRegistrate(String modId)
	{
		this.modId = modId;

		backend = new Backend();
	}

	@OverridingMethodsMustInvokeSuper
	protected OWNER registerEventListeners(IEventBus bus)
	{
		backend.registerEventListeners(bus);
		return self;
	}

	public final String getModId()
	{
		return modId;
	}

	public final Backend getBackend()
	{
		return backend;
	}

	public final String currentName()
	{
		return backend.currentName();
	}

	@Nullable
	public final Supplier<? extends CreativeModeTab> currentCreativeModeTab()
	{
		return backend.currentCreativeModeTab;
	}

	public final <TYPE, VALUE extends TYPE> RegistryEntry<VALUE> get(ResourceKey<? extends Registry<TYPE>> registryType)
	{
		return backend.get(registryType);
	}

	public final <TYPE, VALUE extends TYPE> RegistryEntry<VALUE> get(String name, ResourceKey<? extends Registry<TYPE>> registryType)
	{
		return backend.get(name, registryType);
	}

	@Beta
	public final <TYPE, VALUE extends TYPE> RegistryEntry<VALUE> getOptional(String name, ResourceKey<? extends Registry<TYPE>> registryType)
	{
		return backend.getOptional(name, registryType);
	}

	public final <TYPE> Collection<RegistryEntry<TYPE>> getAll(ResourceKey<? extends Registry<TYPE>> registryType)
	{
		return backend.getAll(registryType);
	}

	public final <TYPE, VALUE extends TYPE> OWNER addRegisterCallback(String name, ResourceKey<? extends Registry<TYPE>> registryType, NonNullConsumer<? super VALUE> callback)
	{
		backend.addRegisterCallback(name, registryType, callback);
		return self;
	}

	public final <TYPE> OWNER addRegisterCallback(ResourceKey<? extends Registry<TYPE>> registryType, Runnable callback)
	{
		backend.addRegisterCallback(registryType, callback);
		return self;
	}

	public final <TYPE> boolean isRegistered(ResourceKey<? extends Registry<TYPE>> registryType)
	{
		return backend.isRegistered(registryType);
	}

	public final <PROVIDER extends RegistrateProvider> Optional<PROVIDER> getDataProvider(ProviderType<PROVIDER> providerType)
	{
		return backend.getDataProvider(providerType);
	}

	public final <PROVIDER extends RegistrateProvider, TYPE> OWNER setDataGenerator(Builder<TYPE, ?, ?, ?> builder, ProviderType<PROVIDER> providerType, NonNullConsumer<? extends PROVIDER> consumer)
	{
		backend.setDataGenerator(builder, providerType, consumer);
		return self;
	}

	public final <PROVIDER extends RegistrateProvider, TYPE> OWNER setDataGenerator(String name, ResourceKey<? extends Registry<TYPE>> registryType, ProviderType<PROVIDER> providerType, NonNullConsumer<? extends PROVIDER> consumer)
	{
		backend.setDataGenerator(name, registryType, providerType, consumer);
		return self;
	}

	public final <PROVIDER extends RegistrateProvider> OWNER addDataGenerator(ProviderType<PROVIDER> providerType, NonNullConsumer<? extends PROVIDER> consumer)
	{
		backend.addDataGenerator(providerType, consumer);
		return self;
	}

	public final MutableComponent addLang(String type, ResourceLocation id, String localizedName)
	{
		return backend.addLang(type, id, localizedName);
	}

	public final MutableComponent addLang(String type, ResourceLocation id, String suffix, String localizedName)
	{
		return backend.addLang(type, id, suffix, localizedName);
	}

	public final MutableComponent addRawLang(String key, String value)
	{
		return backend.addRawLang(key, value);
	}

	public final <PROVIDER extends RegistrateProvider> void generateData(ProviderType<? extends PROVIDER> providerType, PROVIDER provider)
	{
		backend.genData(providerType, provider);
	}

	public final OWNER skipErrors(boolean skipErrors)
	{
		backend.skipErrors(skipErrors);
		return self;
	}

	public final OWNER skipErrors()
	{
		return skipErrors(true);
	}

	public final OWNER object(String name)
	{
		backend.object(name);
		return self;
	}

	public final Supplier<CreativeModeTab> buildCreativeModeTab(String registryName, @Nullable List<Object> beforeEntries, @Nullable List<Object> afterEntries, Consumer<CreativeModeTab.Builder> configurator, @Nullable String englishTranslation)
	{
		return backend.buildCreativeModeTab(registryName, beforeEntries, afterEntries, configurator, englishTranslation);
	}

	public final Supplier<CreativeModeTab> buildCreativeModeTab(String registryName, @Nullable List<Object> beforeEntries, @Nullable List<Object> afterEntries, Consumer<CreativeModeTab.Builder> configurator)
	{
		return backend.buildCreativeModeTab(registryName, beforeEntries, afterEntries, configurator);
	}

	public final Supplier<CreativeModeTab> buildCreativeModeTab(String registryName, Consumer<CreativeModeTab.Builder> configurator)
	{
		return backend.buildCreativeModeTab(registryName, configurator);
	}

	public final Supplier<CreativeModeTab> buildCreativeModeTab(String registryName, Consumer<CreativeModeTab.Builder> configurator, @Nullable String englishTranslation)
	{
		return backend.buildCreativeModeTab(registryName, configurator, englishTranslation);
	}

	public final OWNER creativeModeTab(String registryName, @Nullable List<Object> beforeEntries, @Nullable List<Object> afterEntries, Consumer<CreativeModeTab.Builder> configurator)
	{
		backend.creativeModeTab(registryName, beforeEntries, afterEntries, configurator);
		return self;
	}

	public final OWNER creativeModeTab(String registryName, @Nullable List<Object> beforeEntries, @Nullable List<Object> afterEntries, Consumer<CreativeModeTab.Builder> configurator, @Nullable String englishTranslation)
	{
		backend.creativeModeTab(registryName, beforeEntries, afterEntries, configurator, englishTranslation);
		return self;
	}

	public final OWNER creativeModeTab(String registryName, Consumer<CreativeModeTab.Builder> configurator)
	{
		backend.creativeModeTab(registryName, configurator);
		return self;
	}

	public final OWNER creativeModeTab(String registryName, Consumer<CreativeModeTab.Builder> configurator, @Nullable String englishTranslation)
	{
		backend.creativeModeTab(registryName, configurator, englishTranslation);
		return self;
	}

	public final OWNER creativeModeTab(Supplier<? extends CreativeModeTab> creativeModeTab)
	{
		backend.creativeModeTab(creativeModeTab);
		return self;
	}

	public final OWNER modifyCreativeModeTab(Supplier<? extends CreativeModeTab> creativeModeTab, Consumer<CreativeModeTabModifier> modifier)
	{
		backend.modifyCreativeModeTab(creativeModeTab, modifier);
		return self;
	}

	public final OWNER transform(NonNullUnaryOperator<OWNER> func)
	{
		return func.apply(self);
	}

	public final <TYPE, VALUE extends TYPE, PARENT, BUILDER extends Builder<TYPE, VALUE, PARENT, BUILDER>> BUILDER transform(NonNullFunction<OWNER, BUILDER> function)
	{
		return function.apply(self);
	}

	public final <TYPE, VALUE extends TYPE, PARENT, BUILDER extends Builder<TYPE, VALUE, PARENT, BUILDER>> BUILDER entry(NonNullBiFunction<String, BuilderCallback, BUILDER> factory)
	{
		return backend.entry(factory);
	}

	public final <TYPE, VALUE extends TYPE, PARENT, BUILDER extends Builder<TYPE, VALUE, PARENT, BUILDER>> BUILDER entry(String name, NonNullFunction<BuilderCallback, BUILDER> factory)
	{
		return backend.entry(name, factory);
	}

	/**
	 * @deprecated Use {@link #createRegistry(String, NonNullUnaryOperator)}
	 */
	@Deprecated
	public final <TYPE> ResourceKey<Registry<TYPE>> makeRegistry(String name, Supplier<RegistryBuilder<TYPE>> registryBuilderFactory)
	{
		return backend.makeRegistry(name, registryBuilderFactory);
	}

	@Beta
	@ApiStatus.Experimental
	public final <TYPE> ForgeRegistryEntry<TYPE> createRegistry(String name, NonNullUnaryOperator<RegistryBuilder<TYPE>> registryBuilderModifier)
	{
		var registryType = ResourceKey.<TYPE>createRegistryKey(new ResourceLocation(modId, name));
		var forgeRegistryHolder = new AtomicReference<IForgeRegistry<TYPE>>();

		OneTimeEventReceiver.addModListener(NewRegistryEvent.class, event -> {
			var registryBuilder = registryBuilderModifier.apply(new RegistryBuilder<>()).setName(registryType.location());
			event.create(registryBuilder, forgeRegistryHolder::set);
		});

		return new ForgeRegistryEntry<>(self, registryType, forgeRegistryHolder::get);
	}

	@Beta
	@ApiStatus.Experimental
	public final <TYPE> ForgeRegistryEntry<TYPE> createRegistry(NonNullUnaryOperator<RegistryBuilder<TYPE>> registryBuilderModifier)
	{
		return createRegistry(currentName(), registryBuilderModifier);
	}

	@Beta
	@ApiStatus.Experimental
	public final <TYPE> ForgeRegistryEntry<TYPE> createRegistry(String name)
	{
		return createRegistry(name, builder -> builder);
	}

	@Beta
	@ApiStatus.Experimental
	public final <TYPE> ForgeRegistryEntry<TYPE> createRegistry()
	{
		return createRegistry(currentName(), builder -> builder);
	}

	public final <TYPE, VALUE extends TYPE> RegistryEntry<VALUE> simple(ResourceKey<Registry<TYPE>> registryType, NonNullSupplier<VALUE> factory)
	{
		return simple(self, currentName(), registryType, factory);
	}

	public final <TYPE, VALUE extends TYPE> RegistryEntry<VALUE> simple(String name, ResourceKey<Registry<TYPE>> registryType, NonNullSupplier<VALUE> factory)
	{
		return simple(self, name, registryType, factory);
	}

	public final <TYPE, VALUE extends TYPE, P> RegistryEntry<VALUE> simple(P parent, ResourceKey<Registry<TYPE>> registryType, NonNullSupplier<VALUE> factory)
	{
		return simple(parent, currentName(), registryType, factory);
	}

	public final <TYPE, VALUE extends TYPE, P> RegistryEntry<VALUE> simple(P parent, String name, ResourceKey<Registry<TYPE>> registryType, NonNullSupplier<VALUE> factory)
	{
		return entry(name, callback -> new NoConfigBuilder<>(backend, parent, name, callback, registryType, factory)).register();
	}

	public final class Backend extends com.tterrag.registrate.AbstractRegistrate<Backend>
	{
		@Nullable private Supplier<? extends CreativeModeTab> currentCreativeModeTab;

		private Backend()
		{
			super(CoreRegistrate.this.modId);
		}

		@Override
		protected Backend registerEventListeners(IEventBus bus)
		{
			return super.registerEventListeners(bus);
		}

		@Override
		protected String currentName()
		{
			return super.currentName();
		}

		@Override
		public Backend creativeModeTab(Supplier<? extends CreativeModeTab> creativeModeTab)
		{
			currentCreativeModeTab = creativeModeTab;
			return super.creativeModeTab(creativeModeTab);
		}
	}
}
