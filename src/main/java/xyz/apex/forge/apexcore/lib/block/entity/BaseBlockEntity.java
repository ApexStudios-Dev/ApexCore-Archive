package xyz.apex.forge.apexcore.lib.block.entity;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

// Replaced with revamped code
@Deprecated(since = "4.3.5", forRemoval = true)
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

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this, be -> getUpdateTag());
	}

	@Override
	public void onDataPacket(Connection networkManager, ClientboundBlockEntityDataPacket packet)
	{
		var tag = packet.getTag();

		if(tag != null)
			readeUpdateTag(tag);
	}

	@Override
	public void setRemoved()
	{
		super.setRemoved();

		if(level != null)
			level.blockEntityChanged(worldPosition);
	}
}