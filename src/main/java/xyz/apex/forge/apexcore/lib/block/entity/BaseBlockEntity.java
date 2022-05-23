package xyz.apex.forge.apexcore.lib.block.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public class BaseBlockEntity extends TileEntity
{
	public BaseBlockEntity(TileEntityType<?> blockEntityType)
	{
		super(blockEntityType);
	}

	protected CompoundNBT writeUpdateTag(CompoundNBT tagCompound)
	{
		return tagCompound;
	}

	protected void readeUpdateTag(CompoundNBT tagCompound)
	{
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return writeUpdateTag(super.getUpdateTag());
	}

	protected int getUpdatePacketType()
	{
		return 1;
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(worldPosition, getUpdatePacketType(), getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager networkManager, SUpdateTileEntityPacket packet)
	{
		readeUpdateTag(packet.getTag());
	}

	@Override
	public void setRemoved()
	{
		super.setRemoved();

		if(level != null)
			level.blockEntityChanged(worldPosition, this);
	}
}
