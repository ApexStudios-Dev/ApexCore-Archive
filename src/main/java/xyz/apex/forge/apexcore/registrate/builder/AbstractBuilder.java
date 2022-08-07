package xyz.apex.forge.apexcore.registrate.builder;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.*;
import com.tterrag.registrate.util.entry.LazyRegistryEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.RegistryObject;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.tterrag.registrate.providers.ProviderType.LANG;

@SuppressWarnings("unchecked")
public abstract class AbstractBuilder<
		OWNER extends CoreRegistrate<OWNER>,
		TYPE,
		VALUE extends TYPE,
		PARENT,
		BUILDER extends AbstractBuilder<OWNER, TYPE, VALUE, PARENT, BUILDER, ENTRY>,
		ENTRY extends RegistryEntry<VALUE>
> implements Builder<TYPE, VALUE, PARENT, BUILDER>
{
	protected final BUILDER self = (BUILDER) this;
	protected final OWNER owner;
	protected final PARENT parent;
	protected final String name;
	private final BuilderCallback callback;
	private final ResourceKey<Registry<TYPE>> registryType;
	private final Multimap<ProviderType<? extends RegistrateTagsProvider<?>>, TagKey<?>> tagsByType = HashMultimap.create();
	private final LazyRegistryEntry<VALUE> safeSupplier = new LazyRegistryEntry<>(this);
	private final BiFunction<OWNER, RegistryObject<VALUE>, ENTRY> registryEntryFactory;
	private final Function<RegistryEntry<VALUE>, ENTRY> registryEntryCaster;

	protected AbstractBuilder(OWNER owner, PARENT parent, String name, BuilderCallback callback, ResourceKey<Registry<TYPE>> registryType, BiFunction<OWNER, RegistryObject<VALUE>, ENTRY> registryEntryFactory, Function<RegistryEntry<VALUE>, ENTRY> registryEntryCaster)
	{
		this.owner = owner;
		this.parent = parent;
		this.name = name;
		this.callback = callback;
		this.registryType = registryType;
		this.registryEntryFactory = registryEntryFactory;
		this.registryEntryCaster = registryEntryCaster;
	}

	protected abstract VALUE createEntry();

	public final BUILDER tag(ProviderType<? extends RegistrateTagsProvider<TYPE>> providerType, TagKey<TYPE>... tags)
	{
		if(!tagsByType.containsKey(providerType))
			setData(providerType, (ctx, provider) -> tagsByType.get(providerType).stream().map(tag -> (TagKey<TYPE>) tag).map(provider::tag).forEach(tag -> tag.add(asSupplier().get())));

		tagsByType.putAll(providerType, Arrays.asList(tags));
		return self;
	}

	public final BUILDER removeTag(ProviderType<? extends RegistrateTagsProvider<TYPE>> providerType, TagKey<TYPE>... tags)
	{
		if(tagsByType.containsKey(providerType))
		{
			for(var tag : tags)
			{
				tagsByType.remove(providerType, tag);
			}
		}

		return self;
	}

	public final BUILDER lang(NonNullFunction<VALUE, String> translationKeyProvider)
	{
		return lang(translationKeyProvider, (provider, value) -> provider.<TYPE>getAutomaticName(value, registryType));
	}

	public final BUILDER lang(NonNullFunction<VALUE, String> translationKeyProvider, String name)
	{
		return lang(translationKeyProvider, (provider, value) -> name);
	}

	public final BUILDER lang(NonNullFunction<VALUE, String> translationKeyProvider, BiFunction<RegistrateLangProvider, NonNullSupplier<? extends VALUE>, String> translationValueProvider)
	{
		return setData(LANG, (ctx, provider) -> provider.add(translationKeyProvider.apply(ctx.get()), translationValueProvider.apply(provider, ctx)));
	}

	public final <PROVIDER extends RegistrateProvider> BUILDER clearData(ProviderType<? extends PROVIDER> providerType)
	{
		return setData(providerType, NonNullBiConsumer.noop());
	}

	@Override
	public final ENTRY register()
	{
		return registryEntryCaster.apply(callback.accept(name, registryType, this, this::createEntry, delegate -> registryEntryFactory.apply(owner, delegate)));
	}

	@Override
	public final NonNullSupplier<VALUE> asSupplier()
	{
		return safeSupplier;
	}

	@Override
	public final ENTRY get()
	{
		return (ENTRY) Builder.super.get();
	}

	@Override
	public final VALUE getEntry()
	{
		return Builder.super.getEntry();
	}

	@Override
	public final <PROVIDER extends RegistrateProvider> BUILDER setData(ProviderType<? extends PROVIDER> type, NonNullBiConsumer<DataGenContext<TYPE, VALUE>, PROVIDER> cons)
	{
		return Builder.super.setData(type, cons);
	}

	@Override
	public final <PROVIDER extends RegistrateProvider> BUILDER addMiscData(ProviderType<? extends PROVIDER> type, NonNullConsumer<? extends PROVIDER> cons)
	{
		return Builder.super.addMiscData(type, cons);
	}

	@Override
	public final BUILDER onRegister(NonNullConsumer<? super VALUE> callback)
	{
		return Builder.super.onRegister(callback);
	}

	@Override
	public final <OR> BUILDER onRegisterAfter(ResourceKey<? extends Registry<OR>> dependencyType, NonNullConsumer<? super VALUE> callback)
	{
		return Builder.super.onRegisterAfter(dependencyType, callback);
	}

	@Override
	public final <TYPE2, VALUE2 extends TYPE2, PARENT2, BUILDER2 extends Builder<TYPE2, VALUE2, PARENT2, BUILDER2>> BUILDER2 transform(NonNullFunction<BUILDER, BUILDER2> func)
	{
		return Builder.super.transform(func);
	}

	@Override
	public final PARENT build()
	{
		return Builder.super.build();
	}

	public final OWNER getRegistrate()
	{
		return owner;
	}

	/**
	 * @deprecated Use {@link #getRegistrate()}
	 */
	@ApiStatus.Internal
	@Deprecated
	@Override
	public final com.tterrag.registrate.AbstractRegistrate<?> getOwner()
	{
		return owner.backend;
	}

	@Override
	public final PARENT getParent()
	{
		return parent;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final ResourceKey<? extends Registry<TYPE>> getRegistryKey()
	{
		return registryType;
	}
}