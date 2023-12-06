package dev.apexstudios.apexcore.neoforge.loader;

import dev.apexstudios.apexcore.common.inventory.ItemHandler;
import dev.apexstudios.apexcore.common.inventory.ItemHandlerProvider;
import dev.apexstudios.apexcore.common.inventory.SimpleItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public final class NeoForgeItemHandler implements IItemHandlerModifiable, INBTSerializable<CompoundTag>
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

    public static void registerForBlocks(RegisterCapabilitiesEvent event, Block... blocks)
    {
        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, pos, blockState, blockEntity, side) -> blockEntity instanceof ItemHandlerProvider.Directional provider ? provider.getItemHandler(side).map(NeoForgeItemHandler::new).getRaw() : null,
                blocks
        );
    }

    public static <T extends BlockEntity & ItemHandlerProvider.Directional> void registerForBlockEntity(RegisterCapabilitiesEvent event, BlockEntityType<T> blockEntityType)
    {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                blockEntityType,
                (blockEntity, side) -> blockEntity.getItemHandler(side).map(NeoForgeItemHandler::new).getRaw()
        );
    }

    public static <T extends Entity & ItemHandlerProvider.Directional> void registerForEntity(RegisterCapabilitiesEvent event, EntityType<T> entityType)
    {
        event.registerEntity(
                Capabilities.ItemHandler.ENTITY,
                entityType,
                (entity, $) -> entity.getItemHandler().map(NeoForgeItemHandler::new).getRaw()
        );

        event.registerEntity(
                Capabilities.ItemHandler.ENTITY_AUTOMATION,
                entityType,
                (entity, side) -> entity.getItemHandler(side).map(NeoForgeItemHandler::new).getRaw()
        );
    }

    public static void registerForItems(RegisterCapabilitiesEvent event, ItemLike... items)
    {
        event.registerItem(
                Capabilities.ItemHandler.ITEM,
                (stack, $) -> stack.getItem() instanceof ItemHandlerProvider.Item provider ? provider.getItemHandler(stack).map(NeoForgeItemHandler::new).getRaw() : null,
                items
        );
    }
}
