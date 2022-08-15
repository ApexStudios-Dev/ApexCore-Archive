package xyz.apex.forge.apexcore.registrate;

import com.google.common.annotations.Beta;
import com.google.common.base.Suppliers;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import xyz.apex.forge.apexcore.registrate.entry.ForgeRegistryEntry;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

// Bare bones Registrate object
@SuppressWarnings({ "unchecked", "UnstableApiUsage", "unused" })
public class CoreRegistrate<OWNER extends CoreRegistrate<OWNER>>
{
	@ApiStatus.Internal
	public final Backend backend;

	protected final OWNER self = (OWNER) this;
	public final String modId;
	@Nullable private Supplier<? extends CreativeModeTab> currentTab = null;

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
		return currentTab;
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE> RegistryEntry<VALUE> get(ResourceKey<? extends Registry<TYPE>> registryType)
	{
		return backend.get(registryType);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE> RegistryEntry<VALUE> get(String name, ResourceKey<? extends Registry<TYPE>> registryType)
	{
		return backend.get(name, registryType);
	}

	@Beta
	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE> RegistryEntry<VALUE> getOptional(String name, ResourceKey<? extends Registry<TYPE>> registryType)
	{
		return backend.getOptional(name, registryType);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Collection<RegistryEntry<TYPE>> getAll(ResourceKey<? extends Registry<TYPE>> registryType)
	{
		return backend.getAll(registryType);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE> OWNER addRegisterCallback(String name, ResourceKey<? extends Registry<TYPE>> registryType, NonNullConsumer<? super VALUE> callback)
	{
		backend.addRegisterCallback(name, registryType, callback);
		return self;
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> OWNER addRegisterCallback(ResourceKey<? extends Registry<TYPE>> registryType, Runnable callback)
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

	public final <PROVIDER extends RegistrateProvider, TYPE extends IForgeRegistryEntry<TYPE>> OWNER setDataGenerator(Builder<TYPE, ?, ?, ?> builder, ProviderType<PROVIDER> providerType, NonNullConsumer<? extends PROVIDER> consumer)
	{
		backend.setDataGenerator(builder, providerType, consumer);
		return self;
	}

	public final <PROVIDER extends RegistrateProvider, TYPE extends IForgeRegistryEntry<TYPE>> OWNER setDataGenerator(String name, ResourceKey<? extends Registry<TYPE>> registryType, ProviderType<PROVIDER> providerType, NonNullConsumer<? extends PROVIDER> consumer)
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

	public final OWNER creativeModeTab(NonNullSupplier<? extends CreativeModeTab> creativeModeTab)
	{
		backend.creativeModeTab(creativeModeTab);
		return self;
	}

	public final OWNER creativeModeTab(NonNullSupplier<? extends CreativeModeTab> creativeModeTab, String name)
	{
		backend.creativeModeTab(creativeModeTab, name);
		return self;
	}

	public final OWNER transform(NonNullUnaryOperator<OWNER> func)
	{
		return func.apply(self);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE, PARENT, BUILDER extends Builder<TYPE, VALUE, PARENT, BUILDER>> BUILDER transform(NonNullFunction<OWNER, BUILDER> function)
	{
		return function.apply(self);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE, PARENT, BUILDER extends Builder<TYPE, VALUE, PARENT, BUILDER>> BUILDER entry(NonNullBiFunction<String, BuilderCallback, BUILDER> factory)
	{
		return backend.entry(factory);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE, PARENT, BUILDER extends Builder<TYPE, VALUE, PARENT, BUILDER>> BUILDER entry(String name, NonNullFunction<BuilderCallback.NewBuilderCallback, BUILDER> factory)
	{
		return backend.entry(name, factory);
	}

	/**
	 * @deprecated Use {@link #createRegistry(String, NonNullUnaryOperator)}
	 */
	@Deprecated
	public final <TYPE extends IForgeRegistryEntry<TYPE>> Supplier<IForgeRegistry<TYPE>> makeRegistry(String name, Class<? super TYPE> registryType, Supplier<RegistryBuilder<TYPE>> registryBuilderFactory)
	{
		return backend.makeRegistry(name, registryType, registryBuilderFactory);
	}

	@Beta
	@ApiStatus.Experimental
	public final <TYPE extends IForgeRegistryEntry<TYPE>> ForgeRegistryEntry<TYPE> createRegistry(String name, NonNullUnaryOperator<RegistryBuilder<TYPE>> registryBuilderModifier)
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
	public final <TYPE extends IForgeRegistryEntry<TYPE>> ForgeRegistryEntry<TYPE> createRegistry(NonNullUnaryOperator<RegistryBuilder<TYPE>> registryBuilderModifier)
	{
		return createRegistry(currentName(), registryBuilderModifier);
	}

	@Beta
	@ApiStatus.Experimental
	public final <TYPE extends IForgeRegistryEntry<TYPE>> ForgeRegistryEntry<TYPE> createRegistry(String name)
	{
		return createRegistry(name, builder -> builder);
	}

	@Beta
	@ApiStatus.Experimental
	public final <TYPE extends IForgeRegistryEntry<TYPE>> ForgeRegistryEntry<TYPE> createRegistry()
	{
		return createRegistry(currentName(), builder -> builder);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE> RegistryEntry<VALUE> simple(ResourceKey<Registry<TYPE>> registryType, NonNullSupplier<VALUE> factory)
	{
		return simple(self, currentName(), registryType, factory);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE> RegistryEntry<VALUE> simple(String name, ResourceKey<Registry<TYPE>> registryType, NonNullSupplier<VALUE> factory)
	{
		return simple(self, name, registryType, factory);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE, P> RegistryEntry<VALUE> simple(P parent, ResourceKey<Registry<TYPE>> registryType, NonNullSupplier<VALUE> factory)
	{
		return simple(parent, currentName(), registryType, factory);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>, VALUE extends TYPE, P> RegistryEntry<VALUE> simple(P parent, String name, ResourceKey<Registry<TYPE>> registryType, NonNullSupplier<VALUE> factory)
	{
		return entry(name, callback -> new NoConfigBuilder<>(backend, parent, name, callback, registryType, factory)).register();
	}

	public final class Backend extends com.tterrag.registrate.AbstractRegistrate<Backend>
	{
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
		public Backend creativeModeTab(NonNullSupplier<? extends CreativeModeTab> tab)
		{
			CoreRegistrate.this.currentTab = Suppliers.memoize(tab::get);
			return super.creativeModeTab(tab);
		}
	}
}