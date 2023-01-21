package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
    private static final Map<ResourceLocation, Entry<?>> ENTRY_MAP = Maps.newHashMap();

    public static void register(ResourceLocation blockRegistryName, Supplier<VoxelShape> baseShape)
    {
        register(blockRegistryName, baseShape, (current, $, blockState) -> current);
    }

    public static <T extends Block> void register(ResourceLocation blockRegistryName, Supplier<VoxelShape> baseShape, TriFunction<VoxelShape, T, BlockState, VoxelShape> modifier)
    {
        ENTRY_MAP.put(blockRegistryName, new Entry<>(baseShape, modifier));
    }

    public static Optional<Entry<?>> findForBlock(Block block)
    {
        var blockRegistryName = BuiltInRegistries.BLOCK.getKey(block);
        return Optional.ofNullable(ENTRY_MAP.get(blockRegistryName));
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
            LogManager.getLogger().info("get shape");
            return optimizedShapes.computeIfAbsent(blockState, $ -> modifier.apply(baseShape.get(), (T) blockState.getBlock(), $).optimize());
        }
    }
}
