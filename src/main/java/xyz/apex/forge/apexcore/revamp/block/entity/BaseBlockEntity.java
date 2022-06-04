package xyz.apex.forge.apexcore.revamp.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BaseBlockEntity extends BlockEntity
{
	public static final String NBT_APEX = "ApexData";

	protected BaseBlockEntity(BlockEntityType<? extends BaseBlockEntity> blockEntityType, BlockPos pos, BlockState blockState)
	{
		super(blockEntityType, pos, blockState);
	}

	// region: Serialization
	protected CompoundTag serializeData()
	{
		return new CompoundTag();
	}

	protected void deserializeData(CompoundTag tagCompound)
	{
	}

	@Override
	public final CompoundTag save(CompoundTag tagCompound)
	{
		tagCompound = super.save(tagCompound);
		tagCompound.put(NBT_APEX, serializeData());
		return tagCompound;
	}

	@Override
	public final void load(CompoundTag tagCompound)
	{
		if(tagCompound.contains(NBT_APEX, Tag.TAG_COMPOUND))
			deserializeData(tagCompound.getCompound(NBT_APEX));

		super.load(tagCompound);
	}
	// endregion

	// region: Update
	@Override
	public final ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return new ClientboundBlockEntityDataPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public final CompoundTag getUpdateTag()
	{
		var updateTag = super.getUpdateTag();
		updateTag.put(NBT_APEX, serializeData());
		return updateTag;
	}

	@Override
	public final void handleUpdateTag(CompoundTag updateTag)
	{
		if(updateTag.contains(NBT_APEX, Tag.TAG_COMPOUND))
			deserializeData(updateTag.getCompound(NBT_APEX));

		super.handleUpdateTag(updateTag);
	}
	// endregion
}
