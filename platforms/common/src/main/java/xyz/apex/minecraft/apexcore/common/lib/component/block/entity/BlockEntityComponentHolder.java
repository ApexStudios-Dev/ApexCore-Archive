package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.ForOverride;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

// note although we extend the following interfaces
// that does not mean, all inheritors have logic implemented
// these interfaces are only implemented for instanceof checks and internal systems to function correctly
// lookup the correct component type for each interface to get logic
//
// - Nameable -> NameableBlockEntityComponent
// - Container -> InventoryBlockEntityComponent
// - MenuProvider
public sealed interface BlockEntityComponentHolder extends Nameable, Container, MenuProvider permits BaseBlockEntityComponentHolder
{
    // region: Components
    @Nullable
    <C extends BlockEntityComponent> C getComponent(BlockEntityComponentType<C> componentType);

    <C extends BlockEntityComponent> Optional<C> findComponent(BlockEntityComponentType<C> componentType);

    <C extends BlockEntityComponent> C getRequiredComponent(BlockEntityComponentType<C> componentType);

    boolean hasComponent(BlockEntityComponentType<?> componentType);

    Set<BlockEntityComponentType<?>> getComponentTypes();

    Collection<BlockEntityComponent> getComponents();

    BlockEntity getGameObject();
    // endregion

    // region: Block Wrappers
    @DoNotCall
    @ForOverride
    void playerDestroy(Level level, Player player, ItemStack tool);

    @DoNotCall
    @ForOverride
    void setPlacedBy(Level level, @Nullable LivingEntity placer, ItemStack stack);

    @DoNotCall
    @ForOverride
    void playerWillDestroy(Level level, Player player);

    @DoNotCall
    @ForOverride
    void onPlace(Level level, BlockState oldBlockState);

    @DoNotCall
    @ForOverride
    void onRemove(Level level, BlockState newBlockState);

    @DoNotCall
    @ForOverride
    InteractionResult use(Level level, Player player, InteractionHand hand, BlockHitResult hit);

    @DoNotCall
    @ForOverride
    boolean isSignalSource();

    @DoNotCall
    @ForOverride
    boolean hasAnalogOutputSignal();

    @DoNotCall
    @ForOverride
    int getAnalogOutputSignal(Level level);

    @DoNotCall
    @ForOverride
    int getSignal(BlockGetter level, Direction direction);
    // endregion

    // region: Nameable
    Component getDefaultName();

    @Deprecated
    @Override
    boolean hasCustomName();

    @Deprecated
    @Override
    Component getDisplayName();

    @Nullable
    @Deprecated
    @Override
    Component getCustomName();

    @Deprecated
    @Override
    Component getName();
    // endregion

    // region: Container
    @Override
    int getMaxStackSize();

    @Override
    void startOpen(Player player);

    @Override
    void stopOpen(Player player);

    @Override
    boolean canPlaceItem(int slot, ItemStack stack);

    @Override
    boolean canTakeItem(Container container, int slot, ItemStack stack);

    @Deprecated
    @Override
    int countItem(Item item);

    @Deprecated
    @Override
    boolean hasAnyOf(Set<Item> items);

    @Deprecated
    @Override
    boolean hasAnyMatching(Predicate<ItemStack> predicate);

    @Deprecated
    @Override
    int getContainerSize();

    @Deprecated
    @Override
    boolean isEmpty();

    @Deprecated
    @Override
    ItemStack getItem(int slot);

    @Deprecated
    @Override
    ItemStack removeItem(int slot, int amount);

    @Deprecated
    @Override
    ItemStack removeItemNoUpdate(int slot);

    @Deprecated
    @Override
    void setItem(int slot, ItemStack stack);

    @Deprecated
    @Override
    boolean stillValid(Player player);
    // endregion

    // region: Clearable
    @Deprecated
    @Override
    void clearContent();
    // endregion

    // region: MenuProvider
    @Nullable
    @Override
    AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player);
    // endregion
}
