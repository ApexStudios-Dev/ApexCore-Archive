package xyz.apex.forge.apexcore.revamp.block.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import xyz.apex.forge.apexcore.lib.util.INameableMutable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class InventoryBlockEntity extends BaseBlockEntity implements INameableMutable
{
	public static final String NBT_CUSTOM_NAME = "CustomName";
	public static final String NBT_INVENTORY = "Inventory";

	@Nullable private ITextComponent customName;
	protected final ItemStackHandler itemHandler;
	private final LazyOptional<IItemHandler> itemHandlerCapability = LazyOptional.of(this::getItemHandler);

	protected InventoryBlockEntity(TileEntityType<? extends InventoryBlockEntity> blockEntityType, int slotCount)
	{
		super(blockEntityType);

		itemHandler = new ItemStackHandler(slotCount);
	}

	// region: Serialization
	@OverridingMethodsMustInvokeSuper
	@Override
	public CompoundNBT serializeData()
	{
		CompoundNBT tagCompound = new CompoundNBT();
		tagCompound.put(NBT_INVENTORY, serializeInventory());

		if(customName != null)
			tagCompound.putString(NBT_CUSTOM_NAME, ITextComponent.Serializer.toJson(customName));

		return tagCompound;
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void deserializeData(CompoundNBT tagCompound)
	{
		if(tagCompound.contains(NBT_INVENTORY, Constants.NBT.TAG_COMPOUND))
			deserializeInventory(tagCompound.getCompound(NBT_INVENTORY));
		if(tagCompound.contains(NBT_CUSTOM_NAME, Constants.NBT.TAG_STRING))
			setCustomName(ITextComponent.Serializer.fromJson(tagCompound.getString(NBT_CUSTOM_NAME)));
	}
	// endregion

	// region: Core
	@OverridingMethodsMustInvokeSuper
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandlerCapability.cast();

		return super.getCapability(cap, side);
	}

	@OverridingMethodsMustInvokeSuper
	@Override
	public void setRemoved()
	{
		itemHandlerCapability.invalidate();
		super.setRemoved();
	}
	// endregion

	// region: Inventory
	public final IItemHandler getItemHandler()
	{
		return itemHandler;
	}

	private CompoundNBT serializeInventory()
	{
		return itemHandler.serializeNBT();
	}

	private void deserializeInventory(CompoundNBT tagCompound)
	{
		itemHandler.deserializeNBT(tagCompound);
		setChanged();
	}
	// endregion

	// region: Sync
	public void writeContainerSyncData(PacketBuffer buffer)
	{
	}

	public void readContainerSyncData(PacketBuffer buffer)
	{
	}
	// endregion

	// region: Naming
	@Override
	public final ITextComponent getName()
	{
		return new TranslationTextComponent(getBlockState().getBlock().getDescriptionId());
	}

	@Override
	@Nullable
	public final ITextComponent getCustomName()
	{
		return customName;
	}

	@Override
	public final void setCustomName(@Nullable ITextComponent customName)
	{
		this.customName = customName;
		setChanged();
	}

	@Override
	public final boolean hasCustomName()
	{
		return customName != null;
	}

	@Override
	public final ITextComponent getDisplayName()
	{
		return customName == null ? getName() : customName;
	}
	// endregion
}
