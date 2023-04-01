package xyz.apex.minecraft.apexcore.common.component.block;

import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public non-sealed class BaseBlockComponent implements BlockComponent
{
    protected final BlockComponentHolder holder;

    protected BaseBlockComponent(BlockComponentHolder holder)
    {
        this.holder = holder;
    }

    // region: Component
    @Nullable
    @Override
    public final <T extends BlockComponent> T getComponent(BlockComponentType<T> componentType)
    {
        return holder.getComponent(componentType);
    }

    @Override
    public final <T extends BlockComponent> Optional<T> getOptionalComponent(BlockComponentType<T> componentType)
    {
        return holder.getOptionalComponent(componentType);
    }

    @Override
    public final <T extends BlockComponent> T getRequiredComponent(BlockComponentType<T> componentType)
    {
        return holder.getRequiredComponent(componentType);
    }

    @Override
    public final Set<BlockComponentType<?>> getComponentTypes()
    {
        return holder.getComponentTypes();
    }

    @Override
    public final Stream<BlockComponent> components()
    {
        return holder.components();
    }

    @Override
    public final <T extends BlockComponent> boolean hasComponent(BlockComponentType<T> componentType)
    {
        return holder.hasComponent(componentType);
    }

    @Override
    public final BlockComponentHolder getComponentHolder()
    {
        return holder;
    }

    @Override
    public final Block toBlock()
    {
        return holder.toBlock();
    }
    // endregion
}
