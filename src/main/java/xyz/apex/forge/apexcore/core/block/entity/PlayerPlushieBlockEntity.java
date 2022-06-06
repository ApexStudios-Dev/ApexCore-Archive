package xyz.apex.forge.apexcore.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;
import xyz.apex.forge.apexcore.revamp.block.entity.BaseBlockEntity;

import javax.annotation.Nullable;

public class PlayerPlushieBlockEntity extends BaseBlockEntity
{
	@Nullable private SupporterManager.SupporterInfo supporterInfo;

	public PlayerPlushieBlockEntity(BlockEntityType<? extends PlayerPlushieBlockEntity> blockEntityType, BlockPos pos, BlockState blockState)
	{
		super(blockEntityType, pos, blockState);
	}

	public void setSupporterInfo(SupporterManager.SupporterInfo supporterInfo)
	{
		this.supporterInfo = supporterInfo;
		setChanged();
	}

	@Nullable
	public SupporterManager.SupporterInfo getSupporterInfo()
	{
		return supporterInfo;
	}

	@Override
	public CompoundTag serializeData()
	{
		var tagCompound = new CompoundTag();

		if(supporterInfo != null)
		{
			var supporterTag = PlayerPlushie.writeSupporterInfoTag(supporterInfo);
			tagCompound.put(PlayerPlushie.NBT_SUPPORTER_DATA, supporterTag);
		}

		return tagCompound;
	}

	@Override
	public void deserializeData(CompoundTag tagCompound)
	{
		supporterInfo = null;

		if(tagCompound.contains(PlayerPlushie.NBT_SUPPORTER_DATA, Constants.NBT.TAG_COMPOUND))
			supporterInfo = PlayerPlushie.getSupporterInfo(tagCompound.getCompound(PlayerPlushie.NBT_SUPPORTER_DATA));
	}
}
