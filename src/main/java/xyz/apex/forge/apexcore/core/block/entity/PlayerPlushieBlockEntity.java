package xyz.apex.forge.apexcore.core.block.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;
import xyz.apex.forge.apexcore.revamp.block.entity.BaseBlockEntity;

import javax.annotation.Nullable;

public class PlayerPlushieBlockEntity extends BaseBlockEntity
{
	@Nullable private SupporterManager.SupporterInfo supporterInfo;

	public PlayerPlushieBlockEntity(TileEntityType<? extends PlayerPlushieBlockEntity> blockEntityType)
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
	public CompoundNBT serializeData()
	{
		CompoundNBT tagCompound = new CompoundNBT();

		if(supporterInfo != null)
		{
			CompoundNBT supporterTag = PlayerPlushie.writeSupporterInfoTag(supporterInfo);
			tagCompound.put(PlayerPlushie.NBT_SUPPORTER_DATA, supporterTag);
		}

		return tagCompound;
	}

	@Override
	public void deserializeData(CompoundNBT tagCompound)
	{
		supporterInfo = null;

		if(tagCompound.contains(PlayerPlushie.NBT_SUPPORTER_DATA, Constants.NBT.TAG_COMPOUND))
			supporterInfo = PlayerPlushie.getSupporterInfo(tagCompound.getCompound(PlayerPlushie.NBT_SUPPORTER_DATA));
	}
}
