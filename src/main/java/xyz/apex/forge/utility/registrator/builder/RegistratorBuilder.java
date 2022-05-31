package xyz.apex.forge.utility.registrator.builder;

import com.tterrag.registrate.builders.BuilderCallback;

import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.entry.RegistryEntry;
import xyz.apex.java.utility.nullness.NonnullBiFunction;
import xyz.apex.java.utility.nullness.NonnullFunction;

@SuppressWarnings("unused")
public abstract class RegistratorBuilder<
		OWNER extends AbstractRegistrator<OWNER>,
		BASE extends IForgeRegistryEntry<BASE>,
		TYPE extends BASE,
		PARENT,
		BUILDER extends RegistratorBuilder<OWNER, BASE, TYPE, PARENT, BUILDER, ENTRY>,
		ENTRY extends RegistryEntry<TYPE>
> extends LegacyRegistratorBuilder<OWNER, BASE, TYPE, PARENT, BUILDER, ENTRY>
{
	private final NonnullBiFunction<OWNER, RegistryObject<TYPE>, ENTRY> registryEntryFactory;

	public RegistratorBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback, Class<? super BASE> registryType, NonnullBiFunction<OWNER, RegistryObject<TYPE>, ENTRY> registryEntryFactory, NonnullFunction<com.tterrag.registrate.util.entry.RegistryEntry<TYPE>, ENTRY> registryEntryCastor)
	{
		super(owner, parent, registryName, callback, registryType, registryEntryCastor);

		this.registryEntryFactory = registryEntryFactory;
	}

	@Override
	protected final ENTRY createEntryWrapper(RegistryObject<TYPE> delegate)
	{
		return registryEntryFactory.apply(owner, delegate);
	}
}
