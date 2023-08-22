package xyz.apex.minecraft.apexcore.common.lib.component.block;

import com.google.common.util.concurrent.Runnables;
import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface BlockComponentHolder extends SimpleWaterloggedBlock, WorldlyContainerHolder permits BaseBlockComponentHolder
{
    @Nullable
    <C extends BlockComponent> C getComponent(BlockComponentType<C> componentType);

    <C extends BlockComponent> Optional<C> findComponent(BlockComponentType<C> componentType);

    <C extends BlockComponent> C getRequiredComponent(BlockComponentType<C> componentType);

    boolean hasComponent(BlockComponentType<?> componentType);

    Set<BlockComponentType<?>> getComponentTypes();

    Collection<BlockComponent> getComponents();

    Block getGameObject();

    @Nullable
    BlockEntity getBlockEntity(BlockGetter level, BlockPos pos, BlockState blockState);

    @Nullable
    <T extends BlockEntity> T getBlockEntity(BlockEntityType<T> blockEntityType, BlockGetter level, BlockPos pos, BlockState blockState);

    static <T extends BlockComponent, R> Optional<R> mapAsComponent(Block block, BlockComponentType<T> componentType, Function<T, R> mapper)
    {
        if(!(block instanceof BlockComponentHolder componentHolder))
            return Optional.empty();

        var component = componentHolder.getComponent(componentType);

        if(component == null)
            return Optional.empty();

        return Optional.ofNullable(mapper.apply(component));
    }

    static <T extends BlockComponent, R> Optional<R> mapAsComponent(BlockState blockState, BlockComponentType<T> componentType, Function<T, R> mapper)
    {
        return mapAsComponent(blockState.getBlock(), componentType, mapper);
    }

    static <T extends BlockComponent> void runAsComponent(Block block, BlockComponentType<T> componentType, Consumer<T> consumer, Runnable runnable)
    {
        if(!(block instanceof BlockComponentHolder componentHolder))
        {
            runnable.run();
            return;
        }

        var component = componentHolder.getComponent(componentType);

        if(component == null)
        {
            runnable.run();
            return;
        }

        consumer.accept(component);
    }

    static <T extends BlockComponent> void runAsComponent(Block block, BlockComponentType<T> componentType, Consumer<T> consumer)
    {
        runAsComponent(block, componentType, consumer, Runnables.doNothing());
    }

    static <T extends BlockComponent, R> void runAsComponent(BlockState blockState, BlockComponentType<T> componentType, Consumer<T> consumer, Runnable runnable)
    {
        runAsComponent(blockState.getBlock(), componentType, consumer, runnable);
    }

    static <T extends BlockComponent, R> void runAsComponent(BlockState blockState, BlockComponentType<T> componentType, Consumer<T> consumer)
    {
        runAsComponent(blockState.getBlock(), componentType, consumer);
    }
}
