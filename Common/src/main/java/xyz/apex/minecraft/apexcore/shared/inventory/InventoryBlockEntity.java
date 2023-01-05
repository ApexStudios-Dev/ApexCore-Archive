package xyz.apex.minecraft.apexcore.shared.inventory;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class InventoryBlockEntity extends BlockEntity implements InventoryHolder, Nameable
{
    public static final String NBT_INVENTORY = "Inventory";
    public static final String NBT_CUSTOM_NAME = "CustomName";

    protected final Inventory inventory;
    @Nullable protected Component customName = null;

    public InventoryBlockEntity(BlockEntityType<? extends InventoryBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState, Inventory inventory)
    {
        super(blockEntityType, blockPos, blockState);

        this.inventory = inventory;
        inventory.addListener(slotIndex -> setChanged());
    }

    public InventoryBlockEntity(BlockEntityType<? extends InventoryBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState, int slotCount)
    {
        this(blockEntityType, blockPos, blockState, new Inventory(slotCount));
    }

    @Nullable
    @Override
    public Inventory getInventory(@Nullable Direction side)
    {
        return inventory;
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        tag.put(NBT_INVENTORY, inventory.serialize(new CompoundTag()));
        if(customName != null) tag.putString(NBT_CUSTOM_NAME, Component.Serializer.toJson(customName));
    }

    @Override
    public void load(CompoundTag tag)
    {
        inventory.deserialize(tag.getCompound(NBT_INVENTORY));
        if(tag.contains(NBT_CUSTOM_NAME, Tag.TAG_STRING)) customName = Component.Serializer.fromJson(tag.getString(NBT_CUSTOM_NAME));
    }

    @Override
    public Component getName()
    {
        return getBlockState().getBlock().getName();
    }

    @Override
    public Component getDisplayName()
    {
        return customName == null ? getName() : customName;
    }

    @Override
    public boolean hasCustomName()
    {
        return customName != null;
    }

    @Nullable
    @Override
    public Component getCustomName()
    {
        return customName;
    }

    public void setCustomName(@Nullable Component customName)
    {
        this.customName = customName;
    }
}
