package xyz.apex.forge.apexcore.lib.block;

import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class RegistrateBlockEntityBlock<T extends TileEntity> extends BlockEntityBlock<T>
{
	public RegistrateBlockEntityBlock(Properties properties)
	{
		super(properties);
	}

	protected abstract TileEntityEntry<T> getBlockEntityEntry();

	@Override
	protected TileEntityType<T> getBlockEntityType()
	{
		return getBlockEntityEntry().get();
	}
}
