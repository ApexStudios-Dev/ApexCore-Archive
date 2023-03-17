package xyz.apex.minecraft.apexcore.fabric.platform;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.common.inventory.Inventory;
import xyz.apex.minecraft.apexcore.common.inventory.InventoryHolder;

@SuppressWarnings("UnstableApiUsage")
public final class FabricStorages
{
    FabricStorages()
    {
        ItemStorage.SIDED.registerFallback(this::getFallbackItemStorage);
    }

    @Nullable
    private Storage<ItemVariant> getFallbackItemStorage(Level level, BlockPos pos, BlockState blockState, @Nullable BlockEntity blockEntity, Direction side)
    {
        var inventory = InventoryHolder.lookupInventory(level, pos);
        return inventory == null ? null : new InventoryWrapper(inventory);
    }

    private static final class InventoryWrapper extends CombinedStorage<ItemVariant, InventoryWrapper.Slot>
    {
        private final Inventory inventory;

        private InventoryWrapper(Inventory inventory)
        {
            super(Lists.newArrayList());

            this.inventory = inventory;

            for(var i = 0; i < inventory.getSize(); i++)
            {
                parts.add(new Slot(i));
            }
        }

        private final class Slot extends SnapshotParticipant<ItemStack> implements SingleSlotStorage<ItemVariant>
        {
            private final int slotIndex;

            private Slot(int slotIndex)
            {
                this.slotIndex = slotIndex;
            }

            @Override
            public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction)
            {
                StoragePreconditions.notBlankNotNegative(resource, maxAmount);
                var amount = Ints.saturatedCast(maxAmount);
                var toInsert = resource.toStack();

                if(inventory.insertItem(slotIndex, toInsert, true).getCount() < amount)
                {
                    updateSnapshots(transaction);
                    return amount - inventory.insertItem(slotIndex, toInsert, false).getCount();
                }

                return 0;
            }

            @Override
            public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction)
            {
                StoragePreconditions.notBlankNotNegative(resource, maxAmount);
                var amount = Ints.saturatedCast(maxAmount);

                if(!inventory.extractItem(slotIndex, amount, true).isEmpty())
                {
                    updateSnapshots(transaction);
                    return inventory.extractItem(slotIndex, amount, false).getCount();
                }

                return 0;
            }

            @Override
            public boolean isResourceBlank()
            {
                return inventory.getItem(slotIndex).isEmpty();
            }

            @Override
            public ItemVariant getResource()
            {
                return ItemVariant.of(inventory.getItem(slotIndex));
            }

            @Override
            public long getAmount()
            {
                return inventory.getItem(slotIndex).getCount();
            }

            @Override
            public long getCapacity()
            {
                return inventory.getItem(slotIndex).getMaxStackSize();
            }

            @Override
            protected ItemStack createSnapshot()
            {
                return inventory.getItem(slotIndex).copy();
            }

            @Override
            protected void readSnapshot(ItemStack snapshot)
            {
                inventory.setItem(slotIndex, snapshot);
            }
        }
    }
}
