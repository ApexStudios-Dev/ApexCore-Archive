package xyz.apex.forge.apexcore.lib.block;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.apache.commons.lang3.Validate;

public class RegistrateBlockEntityBlock<T extends TileEntity> extends BlockEntityBlock<T>
{
	private final NonNullLazyValue<TileEntityEntry<T>> lazyBlockEntityType;

	public RegistrateBlockEntityBlock(Properties properties, AbstractRegistrate<?> registrate)
	{
		super(properties);

		lazyBlockEntityType = new NonNullLazyValue<>(() -> obtainBlockEntityEntry(registrate));
	}

	private TileEntityEntry<T> obtainBlockEntityEntry(AbstractRegistrate<?> registrate)
	{
		String name = Validate.notNull(getRegistryName()).getPath();
		RegistryEntry<TileEntityType<T>> registryEntry = registrate.get(name, TileEntityType.class);
		Validate.isInstanceOf(TileEntityEntry.class, registryEntry);
		return (TileEntityEntry<T>) registryEntry;
	}

	protected final TileEntityEntry<T> getBlockEntityEntry()
	{
		return lazyBlockEntityType.get();
	}

	@Override
	protected TileEntityType<T> getBlockEntityType()
	{
		return getBlockEntityEntry().get();
	}
}
