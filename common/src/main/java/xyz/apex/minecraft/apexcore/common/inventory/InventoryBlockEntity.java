package xyz.apex.minecraft.apexcore.common.inventory;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.common.multiblock.MultiBlock;

import java.util.Optional;

public class InventoryBlockEntity extends BlockEntity implements InventoryHolder, Nameable
{
    public static final String NBT_INVENTORY = "Inventory";
    public static final String NBT_CUSTOM_NAME = "CustomName";

    private final Inventory inventory;
    @Nullable private Component customName = null;

    // delegate passes null
    @SuppressWarnings("ConstantValue")
    public InventoryBlockEntity(BlockEntityType<? extends InventoryBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState, Inventory inventory)
    {
        super(blockEntityType, blockPos, blockState);

        this.inventory = inventory;
        if(inventory != null) inventory.addListener(slotIndex -> setChanged());
    }

    public InventoryBlockEntity(BlockEntityType<? extends InventoryBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState, int slotCount)
    {
        this(blockEntityType, blockPos, blockState, new Inventory(slotCount));
    }

    @Override
    public Inventory getInventory()
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

    public static class Delegate extends InventoryBlockEntity
    {
        public Delegate(BlockEntityType<? extends InventoryBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState)
        {
            super(blockEntityType, blockPos, blockState, null);
        }

        @Override protected void saveAdditional(CompoundTag tag) {}
        @Override public void load(CompoundTag tag) {}

        @Override
        public void saveToItem(ItemStack stack)
        {
            lookupOrigin().ifPresent(blockEntity -> blockEntity.saveToItem(stack));
        }

        @Override
        public void setChanged()
        {
            lookupOrigin().ifPresent(BlockEntity::setChanged);
            super.setChanged();
        }

        @Nullable
        @Override
        public Packet<ClientGamePacketListener> getUpdatePacket()
        {
            return lookupOrigin().map(BlockEntity::getUpdatePacket).orElse(null);
        }

        @Override
        public CompoundTag getUpdateTag()
        {
            return lookupOrigin().map(BlockEntity::getUpdateTag).orElseGet(super::getUpdateTag);
        }

        @Override
        public boolean isRemoved()
        {
            return lookupOrigin().map(BlockEntity::isRemoved).orElseGet(super::isRemoved);
        }

        @Override
        public void setRemoved()
        {
            lookupOrigin().ifPresent(BlockEntity::setRemoved);
            super.setRemoved();
        }

        @Override
        public void clearRemoved()
        {
            lookupOrigin().ifPresent(BlockEntity::clearRemoved);
            super.clearRemoved();
        }

        @Override
        public boolean triggerEvent(int id, int type)
        {
            return lookupOrigin().map(blockEntity -> blockEntity.triggerEvent(id, type)).orElse(false);
        }

        @Override
        public boolean onlyOpCanSetNbt()
        {
            return lookupOrigin().map(BlockEntity::onlyOpCanSetNbt).orElse(false);
        }

        @Override
        public Inventory getInventory()
        {
            return lookupOriginAs(InventoryHolder.class).map(InventoryHolder::getInventory).orElseGet(super::getInventory);
        }

        private Optional<BlockEntity> lookupOrigin()
        {
            return lookupOrigin(level, worldPosition, getBlockState());
        }

        private <T> Optional<T> lookupOriginAs(Class<T> type)
        {
            return lookupOrigin().filter(type::isInstance).map(type::cast);
        }

        @Override
        public Component getName()
        {
            return lookupOriginAs(Nameable.class).map(Nameable::getName).orElseGet(() -> getBlockState().getBlock().getName());
        }

        @Override
        public Component getDisplayName()
        {
            return lookupOriginAs(Nameable.class).map(Nameable::getDisplayName).orElseGet(() -> getBlockState().getBlock().getName());
        }

        @Override
        public boolean hasCustomName()
        {
            return lookupOriginAs(Nameable.class).map(Nameable::hasCustomName).orElse(false);
        }

        @Nullable
        @Override
        public Component getCustomName()
        {
            return lookupOriginAs(Nameable.class).map(Nameable::getCustomName).orElse(null);
        }

        public void setCustomName(@Nullable Component customName)
        {
            lookupOriginAs(InventoryBlockEntity.class).ifPresent(blockEntity -> blockEntity.setCustomName(customName));
        }

        private static Optional<BlockEntity> lookupOrigin(@Nullable BlockGetter level, BlockPos pos, BlockState blockState)
        {
            if(level == null) return Optional.empty();

            if(blockState.getBlock() instanceof MultiBlock multiBlock)
            {
                if(!multiBlock.isSameBlockTypeForMultiBlock(blockState)) return Optional.empty();

                var multiBlockType = multiBlock.getMultiBlockType();

                if(!multiBlockType.isOrigin(blockState))
                {
                    var originPos = multiBlockType.getOriginPos(multiBlock, blockState, pos);
                    var originBlockState = level.getBlockState(originPos);
                    return lookupOrigin(level, originPos, originBlockState);
                }
            }

            var blockEntity = level.getBlockEntity(pos);
            return Optional.ofNullable(blockEntity);
        }
    }
}
