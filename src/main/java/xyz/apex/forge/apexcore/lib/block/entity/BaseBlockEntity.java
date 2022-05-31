package xyz.apex.forge.apexcore.lib.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BaseBlockEntity extends BlockEntity
{
	public BaseBlockEntity(BlockEntityType<?> blockEntityType, BlockPos worldPosition, BlockState blockState)
	{
		super(blockEntityType, worldPosition, blockState);
	}

	protected CompoundTag writeUpdateTag(CompoundTag tagCompound)
	{
		return tagCompound;
	}

	protected void readeUpdateTag(CompoundTag tagCompound)
	{
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		return writeUpdateTag(super.getUpdateTag());
	}

	protected int getUpdatePacketType()
	{
		return 1;
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return new ClientboundBlockEntityDataPacket(worldPosition, getUpdatePacketType(), getUpdateTag());
	}

	@Override
	public void onDataPacket(Connection networkManager, ClientboundBlockEntityDataPacket packet)
	{
		readeUpdateTag(packet.getTag());
	}

	@Override
	public void setRemoved()
	{
		super.setRemoved();

		if(level != null)
			level.blockEntityChanged(worldPosition);
	}
}
