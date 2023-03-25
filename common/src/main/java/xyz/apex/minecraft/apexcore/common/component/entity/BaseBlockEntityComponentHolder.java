package xyz.apex.minecraft.apexcore.common.component.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.component.entity.types.ContainerBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.component.entity.types.NameableBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;
import xyz.apex.minecraft.apexcore.common.util.NameableMutable;

import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ignore the vanilla implemented interfaces here
// even though they are implemented, that does not
// mean entities extending this class have that logic
// they must register the components for those logic to work
// the interfaces are implemented for vanilla to be
// able to hook in and talk to our entities
public non-sealed class BaseBlockEntityComponentHolder extends BlockEntity implements BlockEntityComponentHolder
        // see above, logic for these may not exist for
        // your entity if you dont register the matching component
        , NameableMutable, MenuEntry.ExtendedMenuProvider, WorldlyContainer, StackedContentsCompatible
{
    private static final String NBT_COMPONENT_DATA = "ComponentTypes";
    private static final String NBT_UPDATE_TAG = "IsUpdateTag";
    private static final String NBT_ADDITIONAL_DATA = "AdditionalData";

    private final Queue<Consumer<Level>> levelQueue = Queues.newArrayDeque();
    private Set<BlockEntityComponentType<?>> componentTypes = Sets.newLinkedHashSet();
    private boolean registered = false;

    protected BaseBlockEntityComponentHolder(BlockEntityType<? extends BaseBlockEntityComponentHolder> blockEntityType, BlockPos pos, BlockState blockState)
    {
        super(blockEntityType, pos, blockState);

        initializeComponents();
    }

    // region: Component
    private void initializeComponents()
    {
        // register components
        if(registered) return;
        registerComponents(this::registerComponent);
        registered = true;
        componentTypes = ImmutableSet.copyOf(componentTypes);

        // validate components
        var missingTypes = Sets.<BlockEntityComponentType<?>>newHashSet();

        for(var componentType : componentTypes)
        {
            for(var requiredType : componentType.requiredTypes())
            {
                if(hasComponent(requiredType)) continue;
                missingTypes.add(requiredType);
            }

            componentType.getFor(this).validate();
        }

        if(!missingTypes.isEmpty())
        {
            var missingNames = missingTypes.stream().map(BlockEntityComponentType::registryName).map(ResourceLocation::toString).map("'%s'"::formatted).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException("Missing required BlockComponentTypes: %s for Block: '%s'".formatted(missingNames, getClass().getName()));
        }
    }

    private <T extends BlockEntityComponent> T registerComponent(BlockEntityComponentType<T> componentType)
    {
        Validate.isTrue(!registered, "Attempt to register BlockComponentType: '%s' post registration".formatted(componentType.registryName()));
        if(hasComponent(componentType)) throw new IllegalStateException("Attempt to register duplicate BlockComponentType: '%s' for Block: '%s'".formatted(componentType.registryName(), getClass().getName()));
        componentTypes.add(componentType);
        return componentType.getFor(this);
    }

    @Override
    public void registerComponents(Registrar registrar) {}

    @Nullable
    @Override
    public final <T extends BlockEntityComponent> T getComponent(BlockEntityComponentType<T> componentType)
    {
        return hasComponent(componentType) ? componentType.getFor(this) : null;
    }

    @Override
    public final <T extends BlockEntityComponent> Optional<T> getOptionalComponent(BlockEntityComponentType<T> componentType)
    {
        return Optional.ofNullable(getComponent(componentType));
    }

    @Override
    public final <T extends BlockEntityComponent> T getRequiredComponent(BlockEntityComponentType<T> componentType)
    {
        Validate.isTrue(hasComponent(componentType), "Missing required BlockComponentType: '%s' for Block: '%s'".formatted(componentType.registryName(), getClass().getName()));
        return componentType.getFor(this);
    }

    @Override
    public final Set<BlockEntityComponentType<?>> getComponentTypes()
    {
        return componentTypes;
    }

    @Override
    public final Stream<BlockEntityComponent> components()
    {
        return componentTypes.stream().map(componentType -> componentType.getFor(this));
    }

    @Override
    public final <T extends BlockEntityComponent> boolean hasComponent(BlockEntityComponentType<T> componentType)
    {
        return componentTypes.contains(componentType);
    }

    @Override
    public final BlockEntity toBlockEntity()
    {
        return this;
    }

    @Override
    public final BlockEntityType<? extends BlockEntity> toBlockEntityType()
    {
        return getType();
    }

    @Override
    public final BlockPos toBlockPos()
    {
        return worldPosition;
    }

    @Override
    public final BlockState toBlockState()
    {
        return getBlockState();
    }

    @Nullable
    @Override
    public final Level getLevel()
    {
        return level;
    }

    @Override
    public final boolean hasLevel()
    {
        return level != null;
    }

    @Override
    public final void runForLevel(Consumer<Level> consumer)
    {
        if(level == null)
        {
            levelQueue.add(consumer);
            return;
        }

        consumer.accept(level);
    }

    @Override
    public void setLevel(Level level)
    {
        levelQueue.forEach(consumer -> consumer.accept(level));
        levelQueue.clear();

        super.setLevel(level);
    }

    protected final void runForEach(Consumer<BlockEntityComponent> consumer)
    {
        components().forEach(consumer);
    }

    // nullable if 'defaultValue' is null
    protected final <T> T callForEach(BiFunction<BlockEntityComponent, T, T> function, Predicate<T> breakOut, BiFunction<T, T, T> merger, T defaultValue)
    {
        var current = defaultValue;

        for(var componentType : componentTypes)
        {
            var next = function.apply(componentType.getFor(this), current);
            if(breakOut.test(next)) return next;
            current = merger.apply(current, next);
        }

        return current;
    }

    protected final <T> T callForEach(BiFunction<BlockEntityComponent, T, T> function, BiFunction<T, T, T> merger, T defaultValue)
    {
        return callForEach(function, value -> false, merger, defaultValue);
    }

    protected final <T> T callForEach(BiFunction<BlockEntityComponent, T, T> function, Predicate<T> breakOut, T defaultValue)
    {
        return callForEach(function, breakOut, (current, next) -> next, defaultValue);
    }

    protected final <T> T callForEach(BiFunction<BlockEntityComponent, T, T> function, T defaultValue)
    {
        return callForEach(function, value -> false, (current, next) -> next, defaultValue);
    }
    // endregion

    // region: BlockEntity
    @Override
    public void setChanged()
    {
        if(level != null)
        {
            setChanged(level, worldPosition, toBlockState());
            onMarkedDirty();
            runForEach(BlockEntityComponent::onMarkedDirty);
        }
    }

    @Override
    public final void markDirty()
    {
        // very similar to setChanged
        // but this one queues up the state change
        // for when level is not null
        // setChanged attempts to mark dirty immediately
        // but if level is null, will not actually mark dirty
        runForLevel(level -> {
            BlockEntity.setChanged(level, worldPosition, toBlockState());
            onMarkedDirty();
            runForEach(BlockEntityComponent::onMarkedDirty);
        });
    }

    // region: Nameable
    @Deprecated
    @Override
    public final Component getName()
    {
        return getOptionalComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::getName).orElseGet(() -> getDefaultName(toBlockEntity()));
    }

    @Deprecated
    @Override
    public final Component getDisplayName()
    {
        return getOptionalComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::getName).orElseGet(() -> getDefaultName(toBlockEntity()));
    }

    @Deprecated
    @Override
    public final boolean hasCustomName()
    {
        return getOptionalComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::hasCustomName).orElse(false);
    }

    @Deprecated
    @Nullable
    @Override
    public final Component getCustomName()
    {
        return getOptionalComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::getCustomName).orElse(null);
    }

    @Deprecated
    @Override
    public final void setCustomName(@Nullable Component customName)
    {
        getOptionalComponent(BlockEntityComponentTypes.NAMEABLE).ifPresent(component -> component.setCustomName(customName));
    }
    // endregion

    // region: MenuProvider
    @Deprecated
    @Nullable
    @Override
    public final AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
    {
        return getOptionalComponent(BlockEntityComponentTypes.MENU_PROVIDER).map(component -> component.createMenu(containerId, playerInventory, player)).orElse(null);
    }

    @Deprecated
    @Override
    public final void writeExtraData(FriendlyByteBuf extraData)
    {
        getOptionalComponent(BlockEntityComponentTypes.MENU_PROVIDER).ifPresent(component -> component.writeExtraData(extraData));
    }
    // endregion

    // region: WorldyContainer
    @Deprecated
    @Override
    public final int getMaxStackSize()
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(ContainerBlockEntityComponent::getMaxStackSize).orElse(64);
    }

    @Deprecated
    @Override
    public final void startOpen(Player player)
    {
        getOptionalComponent(BlockEntityComponentTypes.CONTAINER).ifPresent(component -> component.startOpen(player));
    }

    @Deprecated
    @Override
    public final void stopOpen(Player player)
    {
        getOptionalComponent(BlockEntityComponentTypes.CONTAINER).ifPresent(component -> component.stopOpen(player));
    }

    @Deprecated
    @Override
    public final boolean canPlaceItem(int slotIndex, ItemStack stack)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.canPlaceItem(slotIndex, stack)).orElse(false);
    }

    @Deprecated
    @Override
    public final boolean canTakeItem(Container container, int slotIndex, ItemStack stack)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.canTakeItem(container, slotIndex, stack)).orElse(false);
    }

    @Deprecated
    @Override
    public final int countItem(Item item)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.countItem(item)).orElse(0);
    }

    @Deprecated
    @Override
    public final boolean hasAnyOf(Set<Item> set)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.hasAnyOf(set)).orElse(false);
    }

    @Deprecated
    @Override
    public final boolean hasAnyMatching(Predicate<ItemStack> predicate)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.hasAnyMatching(predicate)).orElse(false);
    }

    @Deprecated
    @Override
    public final int getContainerSize()
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(ContainerBlockEntityComponent::getContainerSize).orElse(0);
    }

    @Deprecated
    @Override
    public final boolean isEmpty()
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(ContainerBlockEntityComponent::isEmpty).orElse(true);
    }

    @Deprecated
    @Override
    public final ItemStack getItem(int slotIndex)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.getItem(slotIndex)).orElse(ItemStack.EMPTY);
    }

    @Deprecated

    @Override
    public final ItemStack removeItem(int slotIndex, int amount)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.removeItem(slotIndex, amount)).orElse(ItemStack.EMPTY);
    }

    @Deprecated
    @Override
    public final ItemStack removeItemNoUpdate(int slotIndex)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.removeItemNoUpdate(slotIndex)).orElse(ItemStack.EMPTY);
    }

    @Deprecated
    @Override
    public final void setItem(int slotIndex, ItemStack stack)
    {
        getOptionalComponent(BlockEntityComponentTypes.CONTAINER).ifPresent(component -> component.setItem(slotIndex, stack));
    }

    @Override
    public final boolean stillValid(Player player)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.stillValid(player)).orElseGet(() -> Container.stillValidBlockEntity(this, player));
    }

    @Deprecated
    @Override
    public final int[] getSlotsForFace(Direction side)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.getSlotsForFace(side)).orElseGet(() -> new int[0]);
    }

    @Deprecated
    @Override
    public final boolean canPlaceItemThroughFace(int slotIndex, ItemStack stack, @Nullable Direction side)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.canPlaceItemThroughFace(slotIndex, stack, side)).orElse(false);
    }

    @Deprecated
    @Override
    public final boolean canTakeItemThroughFace(int slotIndex, ItemStack stack, Direction side)
    {
        return getOptionalComponent(BlockEntityComponentTypes.CONTAINER).map(component -> component.canTakeItemThroughFace(slotIndex, stack, side)).orElse(false);
    }

    // endregion

    // region: Clearable
    @Deprecated
    @Override
    public final void clearContent()
    {
        getOptionalComponent(BlockEntityComponentTypes.CONTAINER).ifPresent(ContainerBlockEntityComponent::clearContent);
    }
    // endregion

    // region: StackedContentsCompatible
    @Deprecated
    @Override
    public final void fillStackedContents(StackedContents helper)
    {
        getOptionalComponent(BlockEntityComponentTypes.CONTAINER).ifPresent(component -> component.fillStackedContents(helper));
    }
    // endregion

    // region: Overrides
    @Override
    protected final void saveAdditional(CompoundTag tagCompound)
    {
        var additionalTag = writeAdditionalNbt();
        serializeInto(tagCompound, NBT_ADDITIONAL_DATA, additionalTag);

        var componentsTag = writeComponentNbt(BlockEntityComponent::writeNbt);
        serializeInto(tagCompound, NBT_COMPONENT_DATA, componentsTag);
    }

    @Override
    public final void load(CompoundTag tagCompound)
    {
        // not the best but this works
        // there isn't actually a method invoked when loading
        // data from an incoming update
        // vanilla just invokes load() with the incoming data
        // we write an additional boolean to the outgoing data
        // marking it as an "update tag" and check for its existence here
        // and redirect to the necessary reader methods
        if(tagCompound.contains(NBT_UPDATE_TAG, Tag.TAG_BYTE) && tagCompound.getBoolean(NBT_UPDATE_TAG))
        {
            var componentsNbt = deserializeFrom(tagCompound, NBT_COMPONENT_DATA);
            if(componentsNbt instanceof CompoundTag componentsTag) readComponentNbt(componentsTag, BlockEntityComponent::readUpdateNbt);

            var additionalTag = deserializeFrom(tagCompound, NBT_UPDATE_TAG);
            if(additionalTag != null) readAdditionalUpdateNbt(additionalTag);
            return;
        }

        var componentsNbt = deserializeFrom(tagCompound, NBT_COMPONENT_DATA);
        if(componentsNbt instanceof CompoundTag componentsTag) readComponentNbt(componentsTag, BlockEntityComponent::readNbt);

        var additionalTag = deserializeFrom(tagCompound, NBT_UPDATE_TAG);
        if(additionalTag != null) readAdditionalNbt(additionalTag);
    }

    @Override
    public final CompoundTag getUpdateTag()
    {
        var updateTag = new CompoundTag();

        var additionalTag = writeAdditionalUpdateNbt();
        serializeInto(updateTag, NBT_ADDITIONAL_DATA, additionalTag);

        var componentsTag = writeComponentNbt(BlockEntityComponent::writeUpdateNbt);
        serializeInto(updateTag, NBT_COMPONENT_DATA, componentsTag);

        // only write marker if not empty
        // the less data being sent the better
        // so if nothing was written we send an empty tag
        if(!updateTag.isEmpty()) updateTag.putBoolean(NBT_UPDATE_TAG, true);
        return updateTag;
    }

    @Nullable
    private CompoundTag writeComponentNbt(Function<BlockEntityComponent, Tag> writer)
    {
        if(componentTypes.isEmpty()) return null;
        var componentsTag = new CompoundTag();

        for(var componentType : componentTypes)
        {
            var component = componentType.getFor(this);
            var componentTag = writer.apply(component);
            if(isNbtEmpty(componentTag)) continue;
            componentsTag.put(componentType.registryName().toString(), componentTag);
        }

        if(isNbtEmpty(componentsTag)) return null;
        return componentsTag;
    }

    private void readComponentNbt(CompoundTag componentsTag, BiConsumer<BlockEntityComponent, Tag> reader)
    {
        if(componentTypes.isEmpty() || isNbtEmpty(componentsTag)) return;

        for(var componentType : componentTypes)
        {
            var registryName = componentType.registryName().toString();
            if(!componentsTag.contains(registryName)) continue;
            var componentTag = componentsTag.get(registryName);
            if(isNbtEmpty(componentTag)) continue;
            // if(componentTag.isEmpty()) continue; // should be checked from isNbtEmpty
            var component = componentType.getFor(this);
            reader.accept(component, componentTag);
        }
    }

    @Override
    public final Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public final boolean triggerEvent(int eventId, int eventParam)
    {
        for(var componentType : componentTypes)
        {
            if(componentType.getFor(this).triggerEvent(eventId, eventParam)) return true;
        }

        return triggerAdditionalEvent(eventId, eventParam);
    }
    // endregion

    // the following methods exist, to "definalize" the above
    // allowing you to still hook into them during invocations
    // without allowing you to override those methods directly
    //
    // nbt methods are called prior to any component nbt being written
    // and after components have loaded any nbt data
    // this is to disallow these methods from being able to mutate the incoming
    // data before the components have time to save/load them
    @Nullable
    protected Tag writeAdditionalNbt()
    {
        return null;
    }

    protected void readAdditionalNbt(Tag nbt) {}

    @Nullable
    protected Tag writeAdditionalUpdateNbt()
    {
        return writeAdditionalNbt();
    }

    protected void readAdditionalUpdateNbt(Tag nbt)
    {
        readAdditionalNbt(nbt);
    }

    protected boolean triggerAdditionalEvent(int eventId, int eventParam)
    {
        return false;
    }

    protected void onMarkedDirty() {}
    // endregion

    public static Component getDefaultName(BlockEntity blockEntity)
    {
        return blockEntity.getBlockState().getBlock().getName();
    }

    public static void serializeInto(CompoundTag tagCompound, String key, @Nullable Tag nbt)
    {
        if(tagCompound.contains(key)) return; // already written?
        if(isNbtEmpty(nbt)) return;
        tagCompound.put(key, nbt);
    }

    @Nullable
    public static Tag deserializeFrom(CompoundTag tagCompound, String key)
    {
        // not necessary
        // if(!tagCompound.contains(key)) return null;
        var tag = tagCompound.get(key);
        if(isNbtEmpty(tag)) return null;
        return tag;
    }

    public static boolean isNbtEmpty(@Nullable Tag nbt)
    {
        if(nbt == null) return true;

        if(nbt instanceof CollectionTag<?> collection)
        {
            if(collection.isEmpty()) return true;
            return collection.stream().allMatch(BaseBlockEntityComponentHolder::isNbtEmpty);
        }
        else if(nbt instanceof CompoundTag compound)
        {
            if(compound.isEmpty()) return true;
            return compound.getAllKeys().stream().map(compound::get).allMatch(BaseBlockEntityComponentHolder::isNbtEmpty);
        }

        return false;
    }
}
