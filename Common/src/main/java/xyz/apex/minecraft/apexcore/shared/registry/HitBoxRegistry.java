package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.Maps;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import xyz.apex.minecraft.apexcore.shared.util.function.Lazy;
import xyz.apex.minecraft.apexcore.shared.util.function.TriFunction;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class HitBoxRegistry
{
    private static final Map<Block, Entry<?>> ENTRY_MAP = Maps.newHashMap();

    public static void register(Block block, Supplier<VoxelShape> baseShape)
    {
        register(block, baseShape, (current, $, blockState) -> current);
    }

    public static <T extends Block> void register(T block, Supplier<VoxelShape> baseShape, TriFunction<VoxelShape, T, BlockState, VoxelShape> modifier)
    {
        ENTRY_MAP.put(block, new Entry<>(baseShape, modifier));
    }

    public static Optional<Entry<?>> findForBlock(Block block)
    {
        return Optional.ofNullable(ENTRY_MAP.get(block));
    }

    public static final class Entry<T extends Block>
    {
        private final Supplier<VoxelShape> baseShape;
        private final TriFunction<VoxelShape, T, BlockState, VoxelShape> modifier;
        private final Map<BlockState, VoxelShape> optimizedShapes = Maps.newHashMap();

        private Entry(Supplier<VoxelShape> baseShape, TriFunction<VoxelShape, T, BlockState, VoxelShape> modifier)
        {
            this.baseShape = Lazy.of(baseShape);
            this.modifier = modifier;
        }

        @SuppressWarnings("unchecked")
        public VoxelShape getShape(BlockState blockState)
        {
            return optimizedShapes.computeIfAbsent(blockState, $ -> modifier.apply(baseShape.get(), (T) blockState.getBlock(), $).optimize());
        }
    }
}
