package xyz.apex.minecraft.apexcore.common.component.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public non-sealed class BaseBlockEntityComponent implements BlockEntityComponent
{
    protected final BlockEntityComponentHolder holder;

    protected BaseBlockEntityComponent(BlockEntityComponentHolder holder)
    {
        this.holder = holder;
    }

    // region: Component
    @Nullable
    @Override
    public final <T extends BlockEntityComponent> T getComponent(BlockEntityComponentType<T> componentType)
    {
        return holder.getComponent(componentType);
    }

    @Override
    public final <T extends BlockEntityComponent> Optional<T> getOptionalComponent(BlockEntityComponentType<T> componentType)
    {
        return holder.getOptionalComponent(componentType);
    }

    @Override
    public final <T extends BlockEntityComponent> T getRequiredComponent(BlockEntityComponentType<T> componentType)
    {
        return holder.getRequiredComponent(componentType);
    }

    @Override
    public final Set<BlockEntityComponentType<?>> getComponentTypes()
    {
        return holder.getComponentTypes();
    }

    @Override
    public final Stream<BlockEntityComponent> components()
    {
        return holder.components();
    }

    @Override
    public final <T extends BlockEntityComponent> boolean hasComponent(BlockEntityComponentType<T> componentType)
    {
        return holder.hasComponent(componentType);
    }

    @Override
    public final BlockEntityComponentHolder getComponentHolder()
    {
        return holder;
    }

    @Override
    public final BlockEntity toBlockEntity()
    {
        return holder.toBlockEntity();
    }

    @Override
    public final BlockEntityType<? extends BlockEntity> toBlockEntityType()
    {
        return holder.toBlockEntityType();
    }

    @Override
    public final BlockPos toBlockPos()
    {
        return holder.toBlockPos();
    }

    @Override
    public final BlockState toBlockState()
    {
        return holder.toBlockState();
    }

    @Nullable
    @Override
    public final Level getLevel()
    {
        return holder.getLevel();
    }

    @Override
    public final boolean hasLevel()
    {
        return holder.hasLevel();
    }

    @Override
    public final void runForLevel(Consumer<Level> consumer)
    {
        holder.runForLevel(consumer);
    }
    // endregion

    @Override
    public final void markDirty()
    {
        holder.markDirty();
    }
}
