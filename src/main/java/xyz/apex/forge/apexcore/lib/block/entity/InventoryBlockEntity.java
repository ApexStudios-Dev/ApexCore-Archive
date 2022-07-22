package xyz.apex.forge.apexcore.lib.block.entity;

import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class InventoryBlockEntity extends BaseBlockEntity.WithCustomName
{
	public static final String NBT_INVENTORY = "Inventory";

	protected final ItemStackHandler itemHandler;
	private final LazyOptional<IItemHandler> itemHandlerCapability = LazyOptional.of(this::getItemHandler);

	protected InventoryBlockEntity(BlockEntityType<? extends InventoryBlockEntity> blockEntityType, BlockPos pos, BlockState blockState, int slotCount)
	{
		super(blockEntityType, pos, blockState);

		itemHandler = new ItemStackHandler(slotCount);
	}

	// region: Serialization
	@MustBeInvokedByOverriders
	@Override
	public CompoundTag serializeData()
	{
		var tagCompound = super.serializeData();
		tagCompound.put(NBT_INVENTORY, serializeInventory());
		return tagCompound;
	}

	@MustBeInvokedByOverriders
	@Override
	public void deserializeData(CompoundTag tagCompound)
	{
		if(tagCompound.contains(NBT_INVENTORY, Tag.TAG_COMPOUND))
			deserializeInventory(tagCompound.getCompound(NBT_INVENTORY));

		super.deserializeData(tagCompound);
	}
	// endregion

	// region: Core
	@MustBeInvokedByOverriders
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandlerCapability.cast();

		return super.getCapability(cap, side);
	}

	@MustBeInvokedByOverriders
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

	private CompoundTag serializeInventory()
	{
		return itemHandler.serializeNBT();
	}

	private void deserializeInventory(CompoundTag tagCompound)
	{
		itemHandler.deserializeNBT(tagCompound);
		// setChanged();
	}
	// endregion
}