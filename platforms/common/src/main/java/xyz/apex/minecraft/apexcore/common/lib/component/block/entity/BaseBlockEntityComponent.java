package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import com.google.errorprone.annotations.DoNotCall;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.helper.InteractionResultHelper;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public non-sealed class BaseBlockEntityComponent implements BlockEntityComponent
{
    protected final BlockEntityComponentHolder componentHolder;
    private boolean registered = false;

    protected BaseBlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        this.componentHolder = componentHolder;
    }

    // region: Components
    @DoNotCall
    @ApiStatus.Internal
    void postRegistration()
    {
        registered = true;
    }

    @Nullable
    @Override
    public final <C extends BlockEntityComponent> C getComponent(BlockEntityComponentType<C> componentType)
    {
        return componentHolder.getComponent(componentType);
    }

    @Override
    public final <C extends BlockEntityComponent> Optional<C> findComponent(BlockEntityComponentType<C> componentType)
    {
        return componentHolder.findComponent(componentType);
    }

    @Override
    public final <C extends BlockEntityComponent> C getRequiredComponent(BlockEntityComponentType<C> componentType)
    {
        return componentHolder.getRequiredComponent(componentType);
    }

    @Override
    public final boolean hasComponent(BlockEntityComponentType<?> componentType)
    {
        return componentHolder.hasComponent(componentType);
    }

    @Override
    public final Set<BlockEntityComponentType<?>> getComponentTypes()
    {
        return componentHolder.getComponentTypes();
    }

    @Override
    public final Collection<BlockEntityComponent> getComponents()
    {
        return componentHolder.getComponents();
    }

    @Override
    public final BlockEntity getGameObject()
    {
        return componentHolder.getGameObject();
    }

    @Override
    public final BlockEntityComponentHolder getComponentHolder()
    {
        return componentHolder;
    }

    protected final boolean isRegistered()
    {
        return registered;
    }
    // endregion

    // region: Helpers
    @Override
    public final void setChanged()
    {
        getGameObject().setChanged();
    }
    // endregion

    // region: Events
    @Override
    public void serializeInto(CompoundTag tag, boolean forNetwork)
    {
    }

    @Override
    public void deserializeFrom(CompoundTag tag, boolean fromNetwork)
    {
    }

    @Override
    public boolean triggerEvent(int id, int type)
    {
        return false;
    }

    // region: Block Wrappers
    @Override
    public void playerDestroy(Level level, Player player, ItemStack tool)
    {
    }

    @Override
    public void setPlacedBy(Level level, @Nullable LivingEntity placer, ItemStack stack)
    {
    }

    @Override
    public void playerWillDestroy(Level level, Player player)
    {
    }

    @Override
    public void onPlace(Level level, BlockState oldBlockState)
    {
    }

    @Override
    public void onRemove(Level level, BlockState newBlockState)
    {
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return InteractionResultHelper.BlockUse.noActionTaken();
    }

    @Override
    public boolean isSignalSource()
    {
        return false;
    }

    @Override
    public boolean hasAnalogOutputSignal()
    {
        return false;
    }

    @Override
    public int getAnalogOutputSignal(Level level)
    {
        return 0;
    }

    @Override
    public int getSignal(BlockGetter level, Direction direction)
    {
        return 0;
    }
    // endregion
    // endregion
}
