package dev.apexstudios.apexcore.fabric.loader;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import dev.apexstudios.apexcore.common.inventory.ItemHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;

final class FabricItemHandler extends CombinedStorage<ItemVariant, FabricItemHandler.Slot>
{
    private final ItemHandler itemHandler;

    public FabricItemHandler(ItemHandler itemHandler)
    {
        super(Lists.newArrayList());

        this.itemHandler = itemHandler;

        for(var i = 0; i < itemHandler.size(); i++)
        {
            parts.add(new Slot(i));
        }
    }

    protected final class Slot extends SnapshotParticipant<ItemStack> implements SingleSlotStorage<ItemVariant>
    {
        private final int slotIndex;

        private Slot(int slotIndex)
        {
            this.slotIndex = slotIndex;
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction)
        {
            var amount = Ints.saturatedCast(maxAmount);
            var toInsert = resource.toStack(amount);

            if(itemHandler.insert(slotIndex, toInsert, true).getCount() < amount)
            {
                updateSnapshots(transaction);
                return amount - itemHandler.insert(slotIndex, toInsert, false).getCount();
            }

            return 0;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction)
        {
            var amount = Ints.saturatedCast(maxAmount);

            if(!itemHandler.extract(slotIndex, amount, true).isEmpty())
            {
                updateSnapshots(transaction);
                return itemHandler.extract(slotIndex, amount, false).getCount();
            }

            return 0;
        }

        @Override
        public boolean isResourceBlank()
        {
            return itemHandler.get(slotIndex).isEmpty();
        }

        @Override
        public ItemVariant getResource()
        {
            return ItemVariant.of(itemHandler.get(slotIndex));
        }

        @Override
        public long getAmount()
        {
            return itemHandler.get(slotIndex).getCount();
        }

        @Override
        public long getCapacity()
        {
            return itemHandler.getSlotLimit(slotIndex);
        }

        @Override
        protected ItemStack createSnapshot()
        {
            return itemHandler.get(slotIndex).copy();
        }

        @Override
        protected void readSnapshot(ItemStack snapshot)
        {
            itemHandler.set(slotIndex, snapshot);
        }
    }
}
