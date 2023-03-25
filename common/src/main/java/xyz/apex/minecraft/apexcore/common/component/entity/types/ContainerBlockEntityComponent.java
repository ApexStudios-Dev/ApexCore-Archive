package xyz.apex.minecraft.apexcore.common.component.entity.types;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.entity.*;
import xyz.apex.minecraft.apexcore.common.util.function.QuadConsumer;
import xyz.apex.minecraft.apexcore.common.util.function.TriPredicate;

import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class ContainerBlockEntityComponent extends BaseBlockEntityComponent implements BlockEntityComponent.Ticker, WorldlyContainer, StackedContentsCompatible
{
    public static final BlockEntityComponentType<ContainerBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(
            new ResourceLocation(ApexCore.ID, "container"),
            ContainerBlockEntityComponent::new,
            BlockEntityComponentTypes.MENU_PROVIDER
    );

    private NonNullList<ItemStack> items = NonNullList.withSize(0, ItemStack.EMPTY);
    private final List<ItemStack> itemsView = Collections.unmodifiableList(items);
    private final List<ContainerListener> listeners = Lists.newArrayList();
    private int maxStackSize = 64;
    private int validRange = 8;
    private Predicate<Player> isPlayerValid = player -> true;
    private Function<Direction, int[]> slotsForSide = side -> IntStream.range(0, items.size()).toArray();
    private BiPredicate<Integer, ItemStack> canPlaceItem = (slotIndex, stack) -> true;
    private BiPredicate<Integer, ItemStack> canTakeItem = (slotIndex, stack) -> true;
    private TriPredicate<Integer, ItemStack, Direction> canPlaceItemThroughFace = (slotIndex, stack, side) -> canPlaceItem.test(slotIndex, stack);
    private TriPredicate<Integer, ItemStack, Direction> canTakeItemThroughFace = (slotIndex, stack, side) -> canTakeItem.test(slotIndex, stack);
    private final Counter counter = new Counter();

    private ContainerBlockEntityComponent(BlockEntityComponentHolder holder)
    {
        super(holder);
    }

    @Override
    public void onMarkedDirty()
    {
        listeners.forEach(listener -> listener.containerChanged(this));
    }

    public ContainerBlockEntityComponent withSize(int size)
    {
        items = NonNullList.withSize(size, ItemStack.EMPTY);
        return this;
    }

    public ContainerBlockEntityComponent withMaxStackSize(int maxStackSize)
    {
        // vanilla does not support stack sizes above 64 or below 0
        this.maxStackSize = Mth.clamp(maxStackSize, 0, 64);
        return this;
    }

    public ContainerBlockEntityComponent withValidRange(int validRange)
    {
        this.validRange = validRange;
        return this;
    }

    public ContainerBlockEntityComponent withPlayerValidator(Predicate<Player> isPlayerValid)
    {
        this.isPlayerValid = isPlayerValid;
        return this;
    }

    public ContainerBlockEntityComponent withSlotsForWise(Function<Direction, int[]> slotsForSide)
    {
        this.slotsForSide = slotsForSide;
        return this;
    }

    public ContainerBlockEntityComponent withItemPlaceValidator(BiPredicate<Integer, ItemStack> canPlaceItem)
    {
        this.canPlaceItem = canPlaceItem;
        return this;
    }

    public ContainerBlockEntityComponent withItemRemoveValidator(BiPredicate<Integer, ItemStack> canTakeItem)
    {
        this.canTakeItem = canTakeItem;
        return this;
    }

    public ContainerBlockEntityComponent withItemPlaceValidator(TriPredicate<Integer, ItemStack, Direction> canPlaceItemThroughFace)
    {
        this.canPlaceItemThroughFace = canPlaceItemThroughFace;
        return this;
    }

    public ContainerBlockEntityComponent withItemRemoveValidator(TriPredicate<Integer, ItemStack, Direction> canTakeItemThroughFace)
    {
        this.canTakeItemThroughFace = canTakeItemThroughFace;
        return this;
    }

    public ContainerBlockEntityComponent addListener(ContainerListener listener)
    {
        listeners.add(listener);
        return this;
    }

    public ContainerBlockEntityComponent removeListener(ContainerListener listener)
    {
        listeners.remove(listener);
        return this;
    }

    public ContainerBlockEntityComponent addOpenedListener(QuadConsumer<Level, BlockPos, BlockState, Container> listener)
    {
        counter.openListeners.add(listener);
        return this;
    }

    public ContainerBlockEntityComponent removeOpenedListener(QuadConsumer<Level, BlockPos, BlockState, Container> listener)
    {
        counter.openListeners.remove(listener);
        return this;
    }

    public ContainerBlockEntityComponent addClosedListener(QuadConsumer<Level, BlockPos, BlockState, Container> listener)
    {
        counter.closeListeners.add(listener);
        return this;
    }

    public ContainerBlockEntityComponent removeClosedListener(QuadConsumer<Level, BlockPos, BlockState, Container> listener)
    {
        counter.closeListeners.remove(listener);
        return this;
    }

    @Override
    public Runnable createTicker(boolean isClientSide)
    {
        return isClientSide ? null : this::serverTick;
    }

    private void serverTick()
    {
        if(toBlockEntity().isRemoved()) return;
        runForLevel(level -> counter.recheckOpeners(level, toBlockPos(), toBlockState()));
    }

    public int getOpenCount()
    {
        return counter.getOpenerCount();
    }

    public List<ItemStack> getItems()
    {
        return itemsView;
    }

    @Override
    public Tag writeNbt()
    {
        return ContainerHelper.saveAllItems(new CompoundTag(), items, true);
    }

    @Override
    public void readNbt(Tag nbt)
    {
        if(!(nbt instanceof CompoundTag tag)) return;
        items = NonNullList.withSize(items.size(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    public ItemStack getItem(int slotIndex)
    {
        getOptionalComponent(BlockEntityComponentTypes.LOOTABLE).ifPresent(lootable -> lootable.unpackLootTable(null));
        return slotIndex < 0 || slotIndex >= items.size() ? ItemStack.EMPTY : items.get(slotIndex);
    }

    @Override
    public ItemStack removeItem(int slotIndex, int amount)
    {
        getOptionalComponent(BlockEntityComponentTypes.LOOTABLE).ifPresent(lootable -> lootable.unpackLootTable(null));
        var current = items.get(slotIndex);
        if(current.isEmpty() || !canTakeItem.test(slotIndex, current.copy())) return ItemStack.EMPTY;
        var removed = ContainerHelper.removeItem(items, slotIndex, amount);
        if(!removed.isEmpty()) markDirty();
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotIndex)
    {
        getOptionalComponent(BlockEntityComponentTypes.LOOTABLE).ifPresent(lootable -> lootable.unpackLootTable(null));
        var current = items.get(slotIndex);
        if(current.isEmpty() || !canTakeItem.test(slotIndex, current)) return ItemStack.EMPTY;
        items.set(slotIndex, ItemStack.EMPTY);
        return current;
    }

    @Override
    public void setItem(int slotIndex, ItemStack stack)
    {
        getOptionalComponent(BlockEntityComponentTypes.LOOTABLE).ifPresent(lootable -> lootable.unpackLootTable(null));
        // if(!canPlaceItem.test(slotIndex, stack)) return; // we cant check here, since this method doesnt return some state saying if insertion of successful or not
        items.set(slotIndex, stack);
        if(!stack.isEmpty() && stack.getCount() > maxStackSize) stack.setCount(maxStackSize);
        markDirty();
    }

    @Override
    public int getContainerSize()
    {
        return items.size();
    }

    @Override
    public boolean isEmpty()
    {
        getOptionalComponent(BlockEntityComponentTypes.LOOTABLE).ifPresent(lootable -> lootable.unpackLootTable(null));
        if(items.isEmpty()) return true;
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public void setChanged()
    {
        markDirty();
    }

    @Override
    public boolean stillValid(Player player)
    {
        if(!MenuProviderBlockEntityComponent.canOpen(holder, player)) return false;
        if(!Container.stillValidBlockEntity(toBlockEntity(), player, validRange)) return false;
        return isPlayerValid.test(player);
    }

    @Override
    public void clearContent()
    {
        items.clear();
        markDirty();
    }

    @Override
    public void fillStackedContents(StackedContents helper)
    {
        items.forEach(helper::accountStack);
    }

    @Override
    public int getMaxStackSize()
    {
        return maxStackSize;
    }

    @Override
    public void startOpen(Player player)
    {
        if(toBlockEntity().isRemoved() || player.isSpectator()) return;
        runForLevel(level -> counter.incrementOpeners(player, level, toBlockPos(), toBlockState()));
    }

    @Override
    public void stopOpen(Player player)
    {
        if(toBlockEntity().isRemoved() || player.isSpectator()) return;
        runForLevel(level -> counter.decrementOpeners(player, level, toBlockPos(), toBlockState()));
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack stack)
    {
        return canPlaceItem.test(slotIndex, stack);
    }

    @Override
    public boolean canTakeItem(Container container, int slotIndex, ItemStack stack)
    {
        return canTakeItem.test(slotIndex, stack);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return slotsForSide.apply(side);
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotIndex, ItemStack stack, @Nullable Direction side)
    {
        return canPlaceItem.test(slotIndex, stack) && canPlaceItemThroughFace.test(slotIndex, stack, side);
    }

    @Override
    public boolean canTakeItemThroughFace(int slotIndex, ItemStack stack, Direction side)
    {
        return canTakeItem.test(slotIndex, stack) && canTakeItemThroughFace.test(slotIndex, stack, side);
    }

    private final class Counter extends ContainerOpenersCounter
    {
        private final List<QuadConsumer<Level, BlockPos, BlockState, Container>> openListeners = Lists.newArrayList();
        private final List<QuadConsumer<Level, BlockPos, BlockState, Container>> closeListeners = Lists.newArrayList();

        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState blockState)
        {
            openListeners.forEach(listener -> listener.accept(level, pos, blockState, ContainerBlockEntityComponent.this));
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState blockState)
        {
            closeListeners.forEach(listener -> listener.accept(level, pos, blockState, ContainerBlockEntityComponent.this));
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount)
        {
        }

        @Override
        protected boolean isOwnContainer(Player player)
        {
            if(!stillValid(player)) return false;
            if(!player.hasContainerOpen()) return false;

            var menuComponent = getComponent(BlockEntityComponentTypes.MENU_PROVIDER);
            if(menuComponent != null && player.containerMenu.getType() == menuComponent.getMenuType()) return true;

            return false;
        }
    }
}
