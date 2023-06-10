package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.ForOverride;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

public sealed interface BlockEntityComponent permits BaseBlockEntityComponent
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

    BlockEntityComponentHolder getComponentHolder();
    // endregion

    // region: Helpers
    void setChanged();
    // endregion

    // region: Events
    @DoNotCall
    @ForOverride
    void serializeInto(CompoundTag tag, boolean forNetwork);

    @DoNotCall
    @ForOverride
    void deserializeFrom(CompoundTag tag, boolean fromNetwork);

    @DoNotCall
    @ForOverride
    boolean triggerEvent(int id, int type);

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
    // endregion
}
