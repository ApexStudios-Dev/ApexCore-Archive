package xyz.apex.forge.utility.registrator.entry;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import org.apache.commons.lang3.Validate;

import xyz.apex.java.utility.Lazy;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public final class LazyRegistryEntry<T> implements Lazy<T>, NonnullSupplier<T>, NonNullSupplier<T>
{
	@Nullable private NonnullSupplier<? extends RegistryEntry<? extends T>> supplier;
	@Nullable private RegistryEntry<? extends T> value;

	public LazyRegistryEntry(NonnullSupplier<? extends RegistryEntry<? extends T>> supplier)
	{
		this.supplier = supplier;
	}

	@Override
	public T get()
	{
		if(value == null)
		{
			Validate.notNull(supplier, "LazyRegistryEntry was null when `value` was null, This should not be the case");
			value = supplier.get();
			supplier = null;
		}

		return value.get();
	}

	@Override
	public void invalidate()
	{
		throw new UnsupportedOperationException("It is illegal to invalidate LazyRegistryEntry types.");
	}
}
