package xyz.apex.minecraft.apexcore.forge.platform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.items.IItemHandlerModifiable;

import xyz.apex.minecraft.apexcore.shared.ApexCore;
import xyz.apex.minecraft.apexcore.shared.inventory.Inventory;
import xyz.apex.minecraft.apexcore.shared.inventory.InventoryHolder;

public final class ForgeStorages
{
    ForgeStorages()
    {
        MinecraftForge.EVENT_BUS.addListener(this::onAttachCapabilities);
    }

    private void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if(!(event.getObject() instanceof InventoryHolder holder)) return;

        event.addCapability(new ResourceLocation(ApexCore.ID, "inventory"), new ICapabilityProvider() {
            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side)
            {
                if(capability != ForgeCapabilities.ITEM_HANDLER) return LazyOptional.empty();
                var inventory = holder.getInventory(side);
                if(inventory == null) return LazyOptional.empty();
                return LazyOptional.of(() -> new InventoryWrapper(inventory)).cast();
            }
        });
    }

    private record InventoryWrapper(Inventory inventory) implements IItemHandlerModifiable
    {
        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack)
        {
            inventory.setItem(slot, stack);
        }

        @Override
        public int getSlots()
        {
            return inventory.getSize();
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot)
        {
            return inventory.getItem(slot);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
        {
            return inventory.insertItem(slot, stack, simulate);
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            return inventory.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return inventory.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack)
        {
            return inventory.isItemValid(slot, stack);
        }
    }
}
