package xyz.apex.minecraft.apexcore.common.lib.component.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Simple entity block implementation for component based blocks.
 *
 * @param <T> Type of block entity.
 */
public class EntityBlockComponentHolder<T extends BlockEntity> extends BlockComponentHolder implements EntityBlock
{
    private final Supplier<BlockEntityType<T>> blockEntityType;

    public EntityBlockComponentHolder(Supplier<BlockEntityType<T>> blockEntityType, Properties properties)
    {
        super(properties);

        this.blockEntityType = blockEntityType;
    }

    /**
     * @return Block entity type for this block.
     */
    public final BlockEntityType<T> getBlockEntityType()
    {
        return blockEntityType.get();
    }

    /**
     * Looks up block entity at given position.
     *
     * @param level Level to lookup block entity in.
     * @param pos   Position to lookup block entity at.
     * @return Looked up block entity or empty.
     */
    public final Optional<T> findBlockEntity(BlockGetter level, BlockPos pos)
    {
        return level.getBlockEntity(pos, getBlockEntityType());
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos pos, int id, int param)
    {
        return findBlockEntity(level, pos).map(blockEntity -> blockEntity.triggerEvent(id, param)).orElse(false);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
    {
        return getBlockEntityType().create(pos, blockState);
    }
}
