package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.ForOverride;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.block.entity.BaseBlockEntity;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.BlockEntityComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.InventoryBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.NameableBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.lib.helper.InteractionResultHelper;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public non-sealed class BaseBlockEntityComponentHolder extends BaseBlockEntity implements BlockEntityComponentHolder
{
    private static final String NBT_COMPONENTS = "Components";

    private final Map<BlockEntityComponentType<?>, BlockEntityComponent> componentRegistry = registerComponents();

    public BaseBlockEntityComponentHolder(BlockEntityType<? extends BaseBlockEntityComponentHolder> blockEntityType, BlockPos pos, BlockState blockState)
    {
        super(blockEntityType, pos, blockState);
    }

    // region: Components
    @OverridingMethodsMustInvokeSuper
    @ForOverride
    protected void registerComponents(BlockEntityComponentRegistrar registrar)
    {
    }

    private Map<BlockEntityComponentType<?>, BlockEntityComponent> registerComponents()
    {
        var registrar = new BlockEntityComponentRegistrar() {
            private final Multimap<BlockEntityComponentType<?>, Consumer<BlockEntityComponent>> listeners = HashMultimap.create();

            @SuppressWarnings("unchecked")
            @Override
            public <C extends BlockEntityComponent> void register(BlockEntityComponentType<C> componentType, Consumer<C> consumer)
            {
                listeners.put(componentType, (Consumer<BlockEntityComponent>) consumer);
            }
        };

        registerComponents(registrar);

        var map = Maps.<BlockEntityComponentType<?>, BlockEntityComponent>newHashMap();

        for(var componentType : registrar.listeners.keys())
        {
            var component = componentType.newInstance(this);
            registrar.listeners.get(componentType).forEach(listener -> listener.accept(component));
            ((BaseBlockEntityComponent) component).postRegistration();
            map.put(componentType, component);
        }

        return ImmutableMap.copyOf(map);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public final <C extends BlockEntityComponent> C getComponent(BlockEntityComponentType<C> componentType)
    {
        var component = componentRegistry.get(componentType);
        return component == null ? null : (C) component;
    }

    @Override
    public final <C extends BlockEntityComponent> Optional<C> findComponent(BlockEntityComponentType<C> componentType)
    {
        return Optional.ofNullable(getComponent(componentType));
    }

    @Override
    public final <C extends BlockEntityComponent> C getRequiredComponent(BlockEntityComponentType<C> componentType)
    {
        return Objects.requireNonNull(getComponent(componentType), "Missing required Block Component: %s".formatted(componentType));
    }

    @Override
    public final boolean hasComponent(BlockEntityComponentType<?> componentType)
    {
        return componentRegistry.containsKey(componentType);
    }

    @Override
    public final Set<BlockEntityComponentType<?>> getComponentTypes()
    {
        return componentRegistry.keySet();
    }

    @Override
    public final Collection<BlockEntityComponent> getComponents()
    {
        return componentRegistry.values();
    }

    @Override
    public final BlockEntity getGameObject()
    {
        return this;
    }
    // endregion

    // region: Events
    @Override
    protected void serializeInto(CompoundTag tag, boolean forNetwork)
    {
        super.serializeInto(tag, forNetwork);

        // serialize components after inheritors
        // we dont want them to mess with the data
        var componentsTag = new CompoundTag();

        componentRegistry.forEach((componentType, component) -> {
            var componentTag = new CompoundTag();
            component.serializeInto(componentTag, forNetwork);

            if(!componentTag.isEmpty())
                componentsTag.put(componentType.registryName().toString(), componentTag);
        });

        tag.put(NBT_COMPONENTS, componentsTag);
    }

    @Override
    protected void deserializeFrom(CompoundTag tag, boolean fromNetwork)
    {
        // deserialize components before inheritors
        // we dont want them to mess with the data
        if(tag.contains(NBT_COMPONENTS, Tag.TAG_COMPOUND))
        {
            var componentsTag = tag.getCompound(NBT_COMPONENTS);

            componentRegistry.forEach((componentType, component) -> {
                var key = componentType.registryName().toString();

                if(componentsTag.contains(key, Tag.TAG_COMPOUND))
                {
                    var componentTag = componentsTag.getCompound(key);
                    component.deserializeFrom(componentTag, fromNetwork);
                }
            });
        }

        // should not be visible to inheritors
        if(tag.contains(NBT_COMPONENTS))
            tag.remove(NBT_COMPONENTS);

        super.deserializeFrom(tag, fromNetwork);
    }

    @Override
    public boolean triggerEvent(int id, int type)
    {
        for(var component : getComponents())
        {
            if(component.triggerEvent(id, type))
                return true;
        }

        return super.triggerEvent(id, type);
    }

    // region: Block Wrappers
    @Override
    public void playerDestroy(Level level, Player player, ItemStack tool)
    {
        getComponents().forEach(component -> component.playerDestroy(level, player, tool));
    }

    @Override
    public void setPlacedBy(Level level, @Nullable LivingEntity placer, ItemStack stack)
    {
        getComponents().forEach(component -> component.setPlacedBy(level, placer, stack));
    }

    @Override
    public void playerWillDestroy(Level level, Player player)
    {
        getComponents().forEach(component -> component.playerWillDestroy(level, player));
    }

    @Override
    public void onPlace(Level level, BlockState oldBlockState)
    {
        getComponents().forEach(component -> component.onPlace(level, oldBlockState));
    }

    @Override
    public void onRemove(Level level, BlockState newBlockState)
    {
        getComponents().forEach(component -> component.onRemove(level, newBlockState));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand, BlockHitResult hit)
    {
        for(var component : getComponents())
        {
            var result = component.use(level, player, hand, hit);

            if(result.consumesAction())
                return result;
        }

        return InteractionResultHelper.BlockUse.noActionTaken();
    }

    @Override
    public boolean isSignalSource()
    {
        return getComponents().stream().anyMatch(BlockEntityComponent::isSignalSource);
    }

    @Override
    public boolean hasAnalogOutputSignal()
    {
        return getComponents().stream().anyMatch(BlockEntityComponent::hasAnalogOutputSignal);
    }

    @Override
    public int getAnalogOutputSignal(Level level)
    {
        for(var component : getComponents())
        {
            var analogOutputSignal = component.getAnalogOutputSignal(level);

            if(analogOutputSignal > 0)
                return analogOutputSignal;
        }

        return 0;
    }

    @Override
    public int getSignal(BlockGetter level, Direction direction)
    {
        for(var component : getComponents())
        {
            var analogOutputSignal = component.getSignal(level, direction);

            if(analogOutputSignal > 0)
                return analogOutputSignal;
        }

        return 0;
    }
    // endregion
    // endregion

    // region: Nameable
    public Component getDefaultName()
    {
        return getBlockState().getBlock().getName();
    }

    @Deprecated
    @Override
    public final boolean hasCustomName()
    {
        return findComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::hasCustomName).orElse(false);
    }

    @Deprecated
    @Override
    public final Component getDisplayName()
    {
        return findComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::getDisplayName).orElseGet(this::getDefaultName);
    }

    @Nullable
    @Deprecated
    @Override
    public final Component getCustomName()
    {
        return findComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::getCustomName).orElse(null);
    }

    @Deprecated
    @Override
    public final Component getName()
    {
        return findComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::getName).orElseGet(this::getDefaultName);
    }
    // endregion

    // region: Container
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack, @Nullable Direction side)
    {
        return true;
    }

    @Override
    public boolean canTakeItem(int slot, ItemStack stack, @Nullable Direction side)
    {
        return true;
    }

    @Override
    public int getMaxStackSize()
    {
        return Container.LARGE_MAX_STACK_SIZE;
    }

    @Override
    public void startOpen(Player player)
    {
    }

    @Override
    public void stopOpen(Player player)
    {
    }

    @Deprecated
    @Override
    public final boolean canPlaceItem(int slot, ItemStack stack)
    {
        return canPlaceItem(slot, stack, null);
    }

    @Deprecated
    @Override
    public final boolean canTakeItem(Container container, int slot, ItemStack stack)
    {
        return canTakeItem(slot, stack, null);
    }

    @Deprecated
    @Override
    public final int countItem(Item item)
    {
        return findComponent(BlockEntityComponentTypes.INVENTORY).map(inventory -> inventory.countItem(item)).orElse(0);
    }

    @Deprecated
    @Override
    public final boolean hasAnyOf(Set<Item> items)
    {
        return findComponent(BlockEntityComponentTypes.INVENTORY).map(inventory -> inventory.hasAnyOf(items)).orElse(false);
    }

    @Deprecated
    @Override
    public final boolean hasAnyMatching(Predicate<ItemStack> predicate)
    {
        return findComponent(BlockEntityComponentTypes.INVENTORY).map(inventory -> inventory.hasAnyMatching(predicate)).orElse(false);
    }

    @Deprecated
    @Override
    public final int getContainerSize()
    {
        return findComponent(BlockEntityComponentTypes.INVENTORY).map(Container::getContainerSize).orElse(0);
    }

    @Deprecated
    @Override
    public final boolean isEmpty()
    {
        return findComponent(BlockEntityComponentTypes.INVENTORY).map(InventoryBlockEntityComponent::isEmpty).orElse(true);
    }

    @Deprecated
    @Override
    public final ItemStack getItem(int slot)
    {
        return findComponent(BlockEntityComponentTypes.INVENTORY).map(inventory -> inventory.getItem(slot)).orElse(ItemStack.EMPTY);
    }

    @Deprecated
    @Override
    public final ItemStack removeItem(int slot, int amount)
    {
        return findComponent(BlockEntityComponentTypes.INVENTORY).map(inventory -> inventory.removeItem(slot, amount)).orElse(ItemStack.EMPTY);
    }

    @Deprecated
    @Override
    public final ItemStack removeItemNoUpdate(int slot)
    {
        return findComponent(BlockEntityComponentTypes.INVENTORY).map(inventory -> inventory.removeItemNoUpdate(slot)).orElse(ItemStack.EMPTY);
    }

    @Deprecated
    @Override
    public final void setItem(int slot, ItemStack stack)
    {
        findComponent(BlockEntityComponentTypes.INVENTORY).ifPresent(inventory -> inventory.setItem(slot, stack));
    }

    @Deprecated
    @Override
    public final boolean stillValid(Player player)
    {
        return findComponent(BlockEntityComponentTypes.INVENTORY).map(inventory -> inventory.stillValid(player)).orElse(false);
    }
    // endregion

    // region: WorldlyContainer
    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Deprecated
    @Override
    public final boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side)
    {
        return canPlaceItem(slot, stack, side);
    }

    @Deprecated
    @Override
    public final boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side)
    {
        return canTakeItem(slot, stack, side);
    }
    // endregion

    // region: Clearable
    @Deprecated
    @Override
    public final void clearContent()
    {
        findComponent(BlockEntityComponentTypes.INVENTORY).ifPresent(Clearable::clearContent);
    }
    // endregion

    // region: MenuProvider
    @Nullable
    @Override
    public final AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
    {
        var component = getComponent(BlockEntityComponentTypes.INVENTORY);

        if(component == null || !component.canOpen(player))
            return null;

        component.unpackLootTable(player);
        return createMenu(windowId, playerInventory);
    }

    @DoNotCall
    @ForOverride
    @Nullable
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory)
    {
        return null;
    }
    // endregion
}
