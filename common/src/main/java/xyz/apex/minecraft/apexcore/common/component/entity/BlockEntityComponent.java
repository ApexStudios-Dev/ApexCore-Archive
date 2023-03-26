package xyz.apex.minecraft.apexcore.common.component.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public sealed interface BlockEntityComponent permits BaseBlockEntityComponent
{
    // region: Component
    @Nullable
    <T extends BlockEntityComponent> T getComponent(BlockEntityComponentType<T> componentType);

    <T extends BlockEntityComponent> Optional<T> getOptionalComponent(BlockEntityComponentType<T> componentType);

    <T extends BlockEntityComponent> T getRequiredComponent(BlockEntityComponentType<T> componentType);

    Set<BlockEntityComponentType<?>> getComponentTypes();

    Stream<BlockEntityComponent> components();

    <T extends BlockEntityComponent> boolean hasComponent(BlockEntityComponentType<T> componentType);

    BlockEntityComponentHolder getComponentHolder();

    BlockEntity toBlockEntity();

    BlockEntityType<? extends BlockEntity> toBlockEntityType();

    BlockPos toBlockPos();

    BlockState toBlockState();

    @Nullable
    Level getLevel();

    boolean hasLevel();

    void runForLevel(Consumer<Level> consumer);

    default void onRegistered(BlockEntityComponentHolder.Registrar registrar) {}
    // endregion

    // region: BlockEntity
    // unlike other BE methods, this one is not a delegate (called from BE)
    // your component may call this one as you please
    // to mark the block entity as dirty, and needing to be saved to disk
    void markDirty();

    default void onMarkedDirty() {}

    @Nullable
    default Tag writeNbt()
    {
        return null;
    }

    default void readNbt(Tag nbt) {}

    @Nullable
    default Tag writeUpdateNbt()
    {
        return writeNbt();
    }

    default void readUpdateNbt(Tag nbt)
    {
        readNbt(nbt);
    }

    default boolean triggerEvent(int eventId, int eventParam)
    {
        return false;
    }
    // endregion

    interface Listener
    {
        int listenerRadius();

        boolean handleGameEvent(ServerLevel level, GameEvent event, GameEvent.Context ctx, Vec3 pos);

        default GameEventListener.DeliveryMode deliveryMode()
        {
            return GameEventListener.DeliveryMode.UNSPECIFIED;
        }
    }

    interface Ticker
    {
        @Nullable
        Runnable createTicker(boolean isClientSide);
    }
}
