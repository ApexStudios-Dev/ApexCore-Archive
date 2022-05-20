package xyz.apex.forge.apexcore.core.block.entity;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

import xyz.apex.forge.apexcore.lib.util.ProfileHelper;

import javax.annotation.Nullable;

public class PlayerPlushieBlockEntity extends TileEntity
{
	public static final String NBT_GAME_PROFILE = "GameProfile";

	@Nullable private GameProfile gameProfile;

	public PlayerPlushieBlockEntity(TileEntityType<?> blockEntityType)
	{
		super(blockEntityType);
	}

	public void setGameProfile(GameProfile gameProfile)
	{
		this.gameProfile = gameProfile;
		setChanged();
	}

	public GameProfile getGameProfile()
	{
		if(gameProfile == null)
			return ProfileHelper.DUMMY_PROFILE;

		return gameProfile;
	}

	@Override
	public CompoundNBT save(CompoundNBT tagCompound)
	{
		if(gameProfile != null)
			tagCompound.put(NBT_GAME_PROFILE, NBTUtil.writeGameProfile(new CompoundNBT(), gameProfile));

		return super.save(tagCompound);
	}

	@Override
	public void load(BlockState blockState, CompoundNBT tagCompound)
	{
		super.load(blockState, tagCompound);

		gameProfile = null;

		if(tagCompound.contains(NBT_GAME_PROFILE, Constants.NBT.TAG_COMPOUND))
			gameProfile = NBTUtil.readGameProfile(tagCompound.getCompound(NBT_GAME_PROFILE));
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT updateTag = super.getUpdateTag();

		if(gameProfile != null)
			updateTag.put(NBT_GAME_PROFILE, NBTUtil.writeGameProfile(new CompoundNBT(), gameProfile));

		return updateTag;
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(worldPosition, 3, getUpdateTag());
	}
}
