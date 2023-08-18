package xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BaseBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentType;

public class InventoryBlockEntityComponent extends BaseBlockEntityComponent implements WorldlyContainer
{
    public static final BlockEntityComponentType<InventoryBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(ApexCore.ID, "inventory", InventoryBlockEntityComponent::new);

    private static final String NBT_LOOT_TABLE = RandomizableContainerBlockEntity.LOOT_TABLE_TAG;
    private static final String NBT_LOOT_TABLE_SEED = RandomizableContainerBlockEntity.LOOT_TABLE_SEED_TAG;

    private LockCode lockCode = LockCode.NO_LOCK;
    @Nullable private ResourceLocation lootTableId;
    private long lootTableSeed = -1;
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    private InventoryBlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    public void setSlotCount(int slotCount)
    {
        Validate.isTrue(!isRegistered(), "Can not set slot count post component registration");
        items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
    }

    public void setLockCode(LockCode lockCode)
    {
        this.lockCode = lockCode;
    }

    public boolean canOpen(Player player)
    {
        return BaseContainerBlockEntity.canUnlock(player, lockCode, componentHolder.getDisplayName()) && (lootTableId == null || !player.isSpectator());
    }

    public void setLootTable(ResourceLocation lootTableId, long lootTableSeed)
    {
        this.lootTableId = lootTableId;
        this.lootTableSeed = lootTableSeed;

    }

    public void setLootTable(ResourceLocation lootTableId)
    {
        setLootTable(lootTableId, -1L);
    }

    public void clearLootTable()
    {
        lootTableId = null;
        lootTableSeed = -1L;
    }

    public void unpackLootTable(@Nullable Player player)
    {
        if(lootTableId == null)
            return;
        if(!(getGameObject().getLevel() instanceof ServerLevel sLevel))
            return;

        var lootTable = sLevel.getServer().getLootData().getLootTable(lootTableId);
        var blockEntity = getGameObject();

        if(player instanceof ServerPlayer sPlayer)
            CriteriaTriggers.GENERATE_LOOT.trigger(sPlayer, lootTableId);

        // loot table system references the container
        // if we dont clear the id here, we could cause stack overflow
        // calling this method over and over again
        lootTableId = null;

        var builder = new LootParams.Builder(sLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockEntity.getBlockPos()));

        if(player != null)
            builder = builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);

        lootTable.fill(this, builder.create(LootContextParamSets.CHEST), lootTableSeed);

        clearLootTable(); // fully clear the loot table
    }

    @DoNotCall
    @Deprecated
    @Override
    public void startOpen(Player player)
    {
        componentHolder.startOpen(player);
    }

    @DoNotCall
    @Deprecated
    @Override
    public void stopOpen(Player player)
    {
        componentHolder.stopOpen(player);
    }

    @DoNotCall
    @Deprecated
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack)
    {
        return componentHolder.canPlaceItem(slot, stack, null);
    }

    @DoNotCall
    @Deprecated
    @Override
    public boolean canTakeItem(Container container, int slot, ItemStack stack)
    {
        return componentHolder.canTakeItem(slot, stack, null);
    }

    @DoNotCall
    @Deprecated
    @Override
    public int getMaxStackSize()
    {
        return componentHolder.getMaxStackSize();
    }

    @Override
    public int getContainerSize()
    {
        return items.size();
    }

    @Override
    public boolean isEmpty()
    {
        unpackLootTable(null);
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot)
    {
        unpackLootTable(null);
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        unpackLootTable(null);

        var removed = ContainerHelper.removeItem(items, slot, amount);

        if(!removed.isEmpty())
            setChanged();

        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        unpackLootTable(null);
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        unpackLootTable(null);
        items.set(slot, stack);

        var maxStackSize = getMaxStackSize();

        if(stack.getCount() > maxStackSize)
            stack.setCount(maxStackSize);

        setChanged();
    }

    @Override
    public boolean stillValid(Player player)
    {
        return Container.stillValidBlockEntity(getGameObject(), player);
    }

    @Override
    public void clearContent()
    {
        items.clear();
    }

    @Override
    public void serializeInto(CompoundTag tag, boolean forNetwork)
    {
        lockCode.addToTag(tag);

        if(lootTableId == null)
            ContainerHelper.saveAllItems(tag, items);
        else
        {
            tag.putString(NBT_LOOT_TABLE, lootTableId.toString());

            if(lootTableSeed != -1)
                tag.putLong(NBT_LOOT_TABLE_SEED, lootTableSeed);
        }
    }

    @Override
    public void deserializeFrom(CompoundTag tag, boolean fromNetwork)
    {
        lootTableId = null;
        lootTableSeed = -1L;
        items.clear();

        lockCode = LockCode.fromTag(tag);

        if(tag.contains(NBT_LOOT_TABLE, Tag.TAG_STRING))
        {
            lootTableId = new ResourceLocation(tag.getString(NBT_LOOT_TABLE));
            lootTableSeed = tag.contains(NBT_LOOT_TABLE_SEED, Tag.TAG_ANY_NUMERIC) ? tag.getLong(NBT_LOOT_TABLE_SEED) : -1L;
        }

        if(lootTableId == null)
            ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    public void onRemove(Level level, BlockState newBlockState)
    {
        var blockEntity = getGameObject();
        var blockState = blockEntity.getBlockState();

        if(blockState.is(newBlockState.getBlock()))
            return;

        var pos = blockEntity.getBlockPos();
        Containers.dropContents(level, pos, this);
        level.updateNeighbourForOutputSignal(pos, blockState.getBlock());
    }

    @Override
    public boolean hasAnalogOutputSignal()
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(Level level)
    {
        return AbstractContainerMenu.getRedstoneSignalFromContainer(this);
    }

    @DoNotCall
    @Deprecated
    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return componentHolder.getSlotsForFace(side);
    }

    @DoNotCall
    @Deprecated
    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side)
    {
        return componentHolder.canPlaceItem(slot, stack, side);
    }

    @DoNotCall
    @Deprecated
    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side)
    {
        return componentHolder.canTakeItem(slot, stack, side);
    }
}
