package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.component.Component;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentManager;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentType;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Base implementation for block entity components holders.
 */
public class BlockEntityComponentHolder extends BlockEntity implements ComponentHolder<BlockEntity, BlockEntityComponentHolder>
{
    public static final String NBT_COMPONENTS = "Components";
    public static final String NBT_DATA = "Data";
    public static final String NBT_FOR_NETWORK = "ForNetwork";
    public static final String NBT_FOR_ITEM = "ForItem";

    protected final ComponentManager<BlockEntity, BlockEntityComponentHolder> componentManager;

    public BlockEntityComponentHolder(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState)
    {
        super(blockEntityType, pos, blockState);

        componentManager = new ComponentManager<>(this);
        // convert from generic ComponentHolder.Registrar to BlockEntity specific Registrar
        componentManager.registerComponents(registrar -> registerComponents(registrar::register));
    }

    // region: ComponentHolder
    protected void registerComponents(Registrar registrar)
    {
    }

    @Nullable
    public final <T extends BlockEntityComponent> T getComponent(BlockEntityComponentType<T> componentType)
    {
        return componentManager.getComponent(componentType);
    }

    public final <T extends BlockEntityComponent> Optional<T> getOptionalComponent(BlockEntityComponentType<T> componentType)
    {
        return componentManager.getOptionalComponent(componentType);
    }

    public final <T extends BlockEntityComponent> T getComponentOrThrow(BlockEntityComponentType<T> componentType) throws NoSuchElementException
    {
        return componentManager.getComponentOrThrow(componentType);
    }

    public final boolean hasComponent(BlockEntityComponentType<?> componentType)
    {
        return componentManager.hasComponent(componentType);
    }

    public final Collection<BlockEntityComponent> getBlockEntityComponents()
    {
        return blockEntityComponents().collect(ImmutableList.toImmutableList());
    }

    public final Stream<BlockEntityComponent> blockEntityComponents()
    {
        return componentManager.components().filter(BlockEntityComponent.class::isInstance).map(BlockEntityComponent.class::cast);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getComponent(BlockEntityComponentType)} instead.
     */
    @Deprecated
    @Nullable
    @Override
    public final <T extends Component<BlockEntity, BlockEntityComponentHolder>> T getComponent(ComponentType<BlockEntity, BlockEntityComponentHolder, T> componentType)
    {
        return componentManager.getComponent(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getOptionalComponent(BlockEntityComponentType)} instead.
     */
    @Deprecated
    @Override
    public final <T extends Component<BlockEntity, BlockEntityComponentHolder>> Optional<T> getOptionalComponent(ComponentType<BlockEntity, BlockEntityComponentHolder, T> componentType)
    {
        return componentManager.getOptionalComponent(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getComponentOrThrow(BlockEntityComponentType)} instead.
     */
    @Deprecated
    @Override
    public final <T extends Component<BlockEntity, BlockEntityComponentHolder>> T getComponentOrThrow(ComponentType<BlockEntity, BlockEntityComponentHolder, T> componentType) throws NoSuchElementException
    {
        return componentManager.getComponentOrThrow(componentType);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #hasComponent(BlockEntityComponentType)} instead.
     */
    @Deprecated
    @Override
    public final boolean hasComponent(ComponentType<BlockEntity, BlockEntityComponentHolder, ?> componentType)
    {
        return componentManager.hasComponent(componentType);
    }

    @Override
    public final boolean registeredComponents()
    {
        return componentManager.registeredComponents();
    }

    @Override
    public final Set<ComponentType<BlockEntity, BlockEntityComponentHolder, ?>> getComponentTypes()
    {
        return componentManager.getComponentTypes();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #getBlockEntityComponents()} instead.
     */
    @Deprecated
    @Override
    public final Collection<Component<BlockEntity, BlockEntityComponentHolder>> getComponents()
    {
        return componentManager.getComponents();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Deprecated use {@link #blockEntityComponents()} instead.
     */
    @Deprecated
    @Override
    public final Stream<Component<BlockEntity, BlockEntityComponentHolder>> components()
    {
        return componentManager.components();
    }

    @Override
    public final BlockEntity toGameObject()
    {
        return this;
    }
    // endregion

    // region: Hooks
    private void serializeComponents(CompoundTag tag, boolean forNetwork, boolean isItem)
    {
        var componentsTag = new CompoundTag();
        var tags = new CompoundTag();

        for(var componentType : getComponentTypes())
        {
            if(!(componentType.get(this) instanceof BlockEntityComponent component)) continue;
            if(isItem && !component.savesToItem()) continue;
            var componentTag = component.serialize(forNetwork);
            if(componentTag == null) continue;
            tags.put(componentType.registryName().toString(), componentTag);
        }

        componentsTag.put(NBT_DATA, tags);
        // markers used during deserialization, so we know where the tag came from
        componentsTag.putBoolean(NBT_FOR_NETWORK, forNetwork);
        componentsTag.putBoolean(NBT_FOR_ITEM, isItem);

        tag.put(NBT_COMPONENTS, componentsTag);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        serializeComponents(tag, false, false);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void load(CompoundTag tag)
    {
        if(!tag.contains(NBT_COMPONENTS, Tag.TAG_COMPOUND)) return;
        var componentsTag = tag.getCompound(NBT_COMPONENTS);
        if(!componentsTag.contains(NBT_DATA, Tag.TAG_COMPOUND)) return;

        var tags = componentsTag.getCompound(NBT_DATA);

        // from data, not all data is sent across network, to save bandwidth
        var fromNetwork = componentsTag.contains(NBT_FOR_NETWORK, Tag.TAG_BYTE) && componentsTag.getBoolean(NBT_FOR_NETWORK);
        // from item, components can opt in to not serializing to item stacks
        var fromItem = componentsTag.contains(NBT_FOR_ITEM, Tag.TAG_BYTE) && componentsTag.getBoolean(NBT_FOR_ITEM);

        for(var componentType : getComponentTypes())
        {
            if(!(componentType.get(this) instanceof BlockEntityComponent component)) continue;
            if(fromItem && !component.savesToItem()) continue;
            var registryType = componentType.registryName().toString();
            if(!tags.contains(registryType)) continue;
            component.deserialize(Objects.requireNonNull(tags.get(registryType)), fromNetwork);
        }
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void saveToItem(ItemStack stack)
    {
        var tag = new CompoundTag();
        serializeComponents(tag, false, true);
        BlockItem.setBlockEntityData(stack, getType(), tag);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public CompoundTag getUpdateTag()
    {
        var tag = super.getUpdateTag();
        serializeComponents(tag, true, false);
        return tag;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public boolean triggerEvent(int id, int type)
    {
        return blockEntityComponents().anyMatch(component -> component.triggerEvent(id, type));
    }
    // endregion

    @FunctionalInterface
    public interface Registrar extends ComponentHolder.Registrar<BlockEntity, BlockEntityComponentHolder>
    {
        default <T extends BlockEntityComponent> void register(BlockEntityComponentType<T> componentType, Consumer<T> consumer)
        {
            register((ComponentType<BlockEntity, BlockEntityComponentHolder, T>) componentType, consumer);
        }

        default void register(BlockEntityComponentType<?> componentType)
        {
            register((ComponentType<BlockEntity, BlockEntityComponentHolder, ?>) componentType);
        }

        default void register(BlockEntityComponentType<?>... componentTypes)
        {
            register((ComponentType<BlockEntity, BlockEntityComponentHolder, ?>[]) componentTypes);
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(BlockEntityComponentType, Consumer)} instead.
         */
        @Deprecated
        @Override
        <T extends Component<BlockEntity, BlockEntityComponentHolder>> void register(ComponentType<BlockEntity, BlockEntityComponentHolder, T> componentType, Consumer<T> consumer);

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(BlockEntityComponentType)} instead.
         */
        @Deprecated
        @Override
        default void register(ComponentType<BlockEntity, BlockEntityComponentHolder, ?> componentType)
        {
            ComponentHolder.Registrar.super.register(componentType);
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Use {@link #register(BlockEntityComponentType[])} instead.
         */
        @Deprecated
        @Override
        default void register(ComponentType<BlockEntity, BlockEntityComponentHolder, ?>... componentTypes)
        {
            ComponentHolder.Registrar.super.register(componentTypes);
        }
    }
}
