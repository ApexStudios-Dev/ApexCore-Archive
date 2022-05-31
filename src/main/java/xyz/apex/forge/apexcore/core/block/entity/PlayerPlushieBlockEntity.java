package xyz.apex.forge.apexcore.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.lib.block.entity.BaseBlockEntity;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;

import javax.annotation.Nullable;

public class PlayerPlushieBlockEntity extends BaseBlockEntity
{
	@Nullable private SupporterManager.SupporterInfo supporterInfo;

	public PlayerPlushieBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState)
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
	public CompoundTag save(CompoundTag tagCompound)
	{
		if(supporterInfo != null)
		{
			var supporterTag = PlayerPlushie.writeSupporterInfoTag(supporterInfo);
			tagCompound.put(PlayerPlushie.NBT_SUPPORTER_DATA, supporterTag);
		}

		return super.save(tagCompound);
	}

	@Override
	public void load(CompoundTag tagCompound)
	{
		super.load(tagCompound);

		supporterInfo = null;

		if(tagCompound.contains(PlayerPlushie.NBT_SUPPORTER_DATA, Constants.NBT.TAG_COMPOUND))
			supporterInfo = PlayerPlushie.getSupporterInfo(tagCompound.getCompound(PlayerPlushie.NBT_SUPPORTER_DATA));
	}

	@Override
	protected CompoundTag writeUpdateTag(CompoundTag tagCompound)
	{
		if(supporterInfo != null)
		{
			var supporterTag = PlayerPlushie.writeSupporterInfoTag(supporterInfo);
			tagCompound.put(PlayerPlushie.NBT_SUPPORTER_DATA, supporterTag);
		}

		return super.writeUpdateTag(tagCompound);
	}

	@Override
	protected void readeUpdateTag(CompoundTag tagCompound)
	{
		super.readeUpdateTag(tagCompound);

		supporterInfo = null;

		if(tagCompound.contains(PlayerPlushie.NBT_SUPPORTER_DATA, Constants.NBT.TAG_COMPOUND))
			supporterInfo = PlayerPlushie.getSupporterInfo(tagCompound.getCompound(PlayerPlushie.NBT_SUPPORTER_DATA));
	}
}
