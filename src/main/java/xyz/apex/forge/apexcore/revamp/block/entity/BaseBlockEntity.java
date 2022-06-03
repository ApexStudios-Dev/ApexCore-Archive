package xyz.apex.forge.apexcore.revamp.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

public class BaseBlockEntity extends TileEntity
{
	public static final String NBT_APEX = "ApexData";

	protected BaseBlockEntity(TileEntityType<? extends BaseBlockEntity> blockEntityType)
	{
		super(blockEntityType);
	}

	// region: Serialization
	protected CompoundNBT serializeData()
	{
		return new CompoundNBT();
	}

	protected void deserializeData(CompoundNBT tagCompound)
	{
	}

	@Override
	public final CompoundNBT save(CompoundNBT tagCompound)
	{
		tagCompound = super.save(tagCompound);
		tagCompound.put(NBT_APEX, serializeData());
		return tagCompound;
	}

	@Override
	public final void load(BlockState blockState, CompoundNBT tagCompound)
	{
		if(tagCompound.contains(NBT_APEX, Constants.NBT.TAG_COMPOUND))
			deserializeData(tagCompound.getCompound(NBT_APEX));

		super.load(blockState, tagCompound);
	}
	// endregion

	// region: Update
	@Override
	public final SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public final CompoundNBT getUpdateTag()
	{
		CompoundNBT updateTag = super.getUpdateTag();
		updateTag.put(NBT_APEX, serializeData());
		return updateTag;
	}

	@Override
	public final void handleUpdateTag(BlockState blockState, CompoundNBT updateTag)
	{
		if(updateTag.contains(NBT_APEX, Constants.NBT.TAG_COMPOUND))
			deserializeData(updateTag.getCompound(NBT_APEX));

		super.handleUpdateTag(blockState, updateTag);
	}
	// endregion
}
