package xyz.apex.forge.apexcore.core.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.lib.block.entity.BaseBlockEntity;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;

import javax.annotation.Nullable;

public class PlayerPlushieBlockEntity extends BaseBlockEntity
{
	@Nullable private SupporterManager.SupporterInfo supporterInfo;

	public PlayerPlushieBlockEntity(TileEntityType<?> blockEntityType)
	{
		super(blockEntityType);
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
	public CompoundNBT save(CompoundNBT tagCompound)
	{
		if(supporterInfo != null)
		{
			CompoundNBT supporterTag = PlayerPlushie.writeSupporterInfoTag(supporterInfo);
			tagCompound.put(PlayerPlushie.NBT_SUPPORTER_DATA, supporterTag);
		}

		return super.save(tagCompound);
	}

	@Override
	public void load(BlockState blockState, CompoundNBT tagCompound)
	{
		super.load(blockState, tagCompound);

		supporterInfo = null;

		if(tagCompound.contains(PlayerPlushie.NBT_SUPPORTER_DATA, Constants.NBT.TAG_COMPOUND))
			supporterInfo = PlayerPlushie.getSupporterInfo(tagCompound.getCompound(PlayerPlushie.NBT_SUPPORTER_DATA));
	}

	@Override
	protected CompoundNBT writeUpdateTag(CompoundNBT tagCompound)
	{
		if(supporterInfo != null)
		{
			CompoundNBT supporterTag = PlayerPlushie.writeSupporterInfoTag(supporterInfo);
			tagCompound.put(PlayerPlushie.NBT_SUPPORTER_DATA, supporterTag);
		}

		return super.writeUpdateTag(tagCompound);
	}

	@Override
	protected void readeUpdateTag(CompoundNBT tagCompound)
	{
		super.readeUpdateTag(tagCompound);

		supporterInfo = null;

		if(tagCompound.contains(PlayerPlushie.NBT_SUPPORTER_DATA, Constants.NBT.TAG_COMPOUND))
			supporterInfo = PlayerPlushie.getSupporterInfo(tagCompound.getCompound(PlayerPlushie.NBT_SUPPORTER_DATA));
	}
}
