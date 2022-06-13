package xyz.apex.forge.apexcore.revamp.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.forge.apexcore.lib.util.NameableMutable;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

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
	public final void saveAdditional(CompoundTag tagCompound)
	{
		tagCompound.put(NBT_APEX, serializeData());
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
		return ClientboundBlockEntityDataPacket.create(this);
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

	public static class WithCustomName extends BaseBlockEntity implements NameableMutable
	{
		public static final String NBT_CUSTOM_NAME = InventoryBlockEntity.NBT_CUSTOM_NAME;

		@Nullable private Component customName = null;

		protected WithCustomName(BlockEntityType<? extends WithCustomName> blockEntityType, BlockPos pos, BlockState blockState)
		{
			super(blockEntityType, pos, blockState);
		}

		@Override
		public void setCustomName(@Nullable Component customName)
		{
			this.customName = customName;
		}

		@Nullable
		@Override
		public Component getCustomName()
		{
			return customName;
		}

		@Override
		public Component getDisplayName()
		{
			return customName == null ? getName() : customName;
		}

		@Override
		public Component getName()
		{
			return new TranslatableComponent(getBlockState().getBlock().getDescriptionId());
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		protected CompoundTag serializeData()
		{
			var tagCompound = super.serializeData();

			if(customName != null)
			{
				var customNameJson = TextComponent.Serializer.toJson(customName);
				tagCompound.putString(NBT_CUSTOM_NAME, customNameJson);
			}

			return tagCompound;
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		protected void deserializeData(CompoundTag tagCompound)
		{
			if(tagCompound.contains(NBT_CUSTOM_NAME, Tag.TAG_STRING))
			{
				var customNameJson = tagCompound.getString(NBT_CUSTOM_NAME);
				customName = TextComponent.Serializer.fromJson(customNameJson);
			}

			super.deserializeData(tagCompound);
		}
	}
}
