package dev.apexstudios.apexcore.neoforge.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.inventory.BlockEntityItemHandlerProvider;
import dev.apexstudios.apexcore.common.inventory.ItemHandler;
import dev.apexstudios.apexcore.common.inventory.ItemStackHandlerProvider;
import dev.apexstudios.apexcore.common.inventory.SimpleItemHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

final class NeoForgeItemHandler implements IItemHandlerModifiable, INBTSerializable<CompoundTag>
{
    private ItemHandler itemHandler;

    private NeoForgeItemHandler(ItemHandler itemHandler)
    {
        this.itemHandler = itemHandler;
    }

    @Override
    public CompoundTag serializeNBT()
    {
        return itemHandler.serialize();
    }

    @Override
    public void deserializeNBT(CompoundTag tag)
    {
        itemHandler = new SimpleItemHandler(itemHandler.size(), tag);
    }

    @Override
    public void setStackInSlot(int slotIndex, ItemStack stack)
    {
        itemHandler.set(slotIndex, stack);
    }

    @Override
    public int getSlots()
    {
        return itemHandler.size();
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return itemHandler.get(slotIndex);
    }

    @Override
    public ItemStack insertItem(int slotIndex, ItemStack stack, boolean simulate)
    {
        return itemHandler.insert(slotIndex, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slotIndex, int amount, boolean simulate)
    {
        return itemHandler.extract(slotIndex, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slotIndex)
    {
        return itemHandler.getSlotLimit(slotIndex);
    }

    @Override
    public boolean isItemValid(int slotIndex, ItemStack stack)
    {
        return itemHandler.isItemValid(slotIndex, stack);
    }

    static CapabilityProvider forBlockEntity(BlockEntityItemHandlerProvider provider)
    {
        return new CapabilityProvider()
        {
            @Nullable private Map<Direction, LazyOptional<IItemHandler>> sidedHandlers;
            @Nullable private LazyOptional<IItemHandler> defaultHandler;

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side)
            {
                if(capability == Capabilities.ITEM_HANDLER)
                    return (side == null ? forNull() : forSide(side)).cast();

                return LazyOptional.empty();
            }

            @Override
            public void invalidate()
            {
                if(sidedHandlers != null)
                {
                    sidedHandlers.values().forEach(LazyOptional::invalidate);
                    sidedHandlers.clear();
                    sidedHandlers = null;
                }

                if(defaultHandler != null)
                {
                    defaultHandler.invalidate();
                    defaultHandler = null;
                }
            }

            private LazyOptional<IItemHandler> forSide(Direction side)
            {
                if(sidedHandlers == null)
                    sidedHandlers = Maps.newEnumMap(Direction.class);

                return sidedHandlers.computeIfAbsent(side, $ ->
                {
                    var itemHandler = provider.getItemHandler($);
                    return itemHandler == null ? LazyOptional.empty() : LazyOptional.of(() -> new NeoForgeItemHandler(itemHandler));
                });
            }

            private LazyOptional<IItemHandler> forNull()
            {
                if(defaultHandler == null)
                {
                    var itemHandler = provider.getItemHandler();
                    defaultHandler = itemHandler == null ? LazyOptional.empty() : LazyOptional.of(() -> new NeoForgeItemHandler(itemHandler));
                }

                return defaultHandler;
            }
        };
    }

    static CapabilityProvider forItemStack(ItemStack stack, ItemStackHandlerProvider provider)
    {
        return new CapabilityProvider()
        {
            @Nullable private LazyOptional<IItemHandler> handler;

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side)
            {
                return capability == Capabilities.ITEM_HANDLER ? capability().cast() : LazyOptional.empty();
            }

            @Override
            public void invalidate()
            {
                if(handler != null)
                {
                    handler.invalidate();
                    handler = null;
                }
            }

            private LazyOptional<IItemHandler> capability()
            {
                if(handler == null)
                {
                    var itemHandler = provider.getItemHandler(stack);
                    return itemHandler == null ? LazyOptional.empty() : LazyOptional.of(() -> new NeoForgeItemHandler(itemHandler));
                }

                return handler;
            }
        };
    }

    interface CapabilityProvider extends ICapabilityProvider
    {
        void invalidate();
    }
}
