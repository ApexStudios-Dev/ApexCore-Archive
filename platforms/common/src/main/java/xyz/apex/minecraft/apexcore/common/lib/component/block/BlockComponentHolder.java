package xyz.apex.minecraft.apexcore.common.lib.component.block;

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
}
