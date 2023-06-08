package xyz.apex.minecraft.apexcore.common.lib.registry.entries;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.registry.DelegatedRegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.BlockEntityBuilder;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Main RegistryEntry class for all BlockEntityType entries.
 * <p>
 * While the constructor is publicly visible, you should never invoke or create instance of this class yourself.
 * Instances of this class are provided when registered using the {@link BlockEntityBuilder} class.
 *
 * @param <T> Type of item.
 */
public final class BlockEntityEntry<T extends BlockEntity> extends DelegatedRegistryEntry<BlockEntityType<T>>
{
    /**
     * DO NOT MANUALLY CALL PUBLIC FOR INTERNAL USAGES ONLY
     */
    @ApiStatus.Internal
    public BlockEntityEntry(RegistryEntry<BlockEntityType<T>> delegate)
    {
        super(delegate);
    }

    /**
     * Returns true if the given block state is valid for this registry entry.
     *
     * @param blockState Block state to validate.
     * @return True if the given block state is valid for this registry entry.
     */
    public boolean isValid(BlockState blockState)
    {
        return map(value -> value.isValid(blockState)).orElse(false);
    }

    /**
     * Returns true if the given block is valid for this registry entry.
     *
     * @param block Block to validate.
     * @return True if the given block is valid for this registry entry.
     */
    public boolean isValid(Block block)
    {
        return map(value -> value.validBlocks.contains(block)).orElse(false);
    }

    /**
     * Returns new block entity instance for given block state and position.
     *
     * @param blockState Block state to construct block entity with.
     * @param pos        Block pos to construct block entity with.
     * @return New block entity instance for given block state and position.
     */
    @Nullable
    public T create(BlockState blockState, BlockPos pos)
    {
        return map(value -> value.create(pos, blockState)).orElse(null);
    }

    /**
     * Returns block entity in level at given position, returning null if no block entity present or block entity is of differing type.
     *
     * @param level Level to look up block entity in.
     * @param pos   Position to look up block entity at.
     * @return Block entity in level at given position, returning null if no block entity present or block entity is of differing type.
     */
    @Nullable
    public T get(BlockGetter level, BlockPos pos)
    {
        return find(level, pos).orElse(null);
    }

    /**
     * Returns block entity in level at given position, throwing exception if no block entity present or block entity is of differing type.
     *
     * @param level Level to look up block entity in.
     * @param pos   Position to look up block entity at.
     * @return Block entity in level at given position, returning null if no block entity present or block entity is of differing type.
     * @throws NoSuchElementException If no block entity could be found or found block entity is of differing type.
     */
    public T getOrThrow(BlockGetter level, BlockPos pos)
    {
        return find(level, pos).orElseThrow();
    }

    /**
     * Returns an optional containing block entity in level at given position, returning null if no block entity present or block entity is of differing type.
     *
     * @param level Level to look up block entity in.
     * @param pos   Position to look up block entity at.
     * @return An optional containing block entity in level at given position, returning null if no block entity present or block entity is of differing type.
     */
    public Optional<T> find(BlockGetter level, BlockPos pos)
    {
        return flatMap(value -> level.getBlockEntity(pos, value));
    }
}
