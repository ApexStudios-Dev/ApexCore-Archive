package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.Maps;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.util.Lazy;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class HitBoxRegistry
{
    private static final Map<ResourceLocation, Entry<?>> ENTRY_MAP = Maps.newHashMap();

    public static void register(ResourceLocation blockRegistryName, Supplier<VoxelShape> baseShape)
    {
        register(blockRegistryName, baseShape, Modifier.identity());
    }

    public static <T extends Block> void register(ResourceLocation blockRegistryName, Supplier<VoxelShape> baseShape, Modifier<T> modifier)
    {
        if(ENTRY_MAP.containsKey(blockRegistryName)) throw new IllegalStateException("Duplicate HitBox Registration: %s".formatted(blockRegistryName));
        Platform.INSTANCE.getLogger().debug("Registering Hitbox For Block: {}", blockRegistryName);
        ENTRY_MAP.put(blockRegistryName, new Entry<>(baseShape, modifier));
    }

    public static void register(String modId, String blockRegistryName, Supplier<VoxelShape> baseShape)
    {
        register(new ResourceLocation(modId, blockRegistryName), baseShape, Modifier.identity());
    }

    public static <T extends Block> void register(String modId, String blockRegistryName, Supplier<VoxelShape> baseShape, Modifier<T> modifier)
    {
        register(new ResourceLocation(modId, blockRegistryName), baseShape, modifier);
    }

    public static Optional<Entry<?>> findForBlock(Block block)
    {
        var registryName = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block));
        if(ENTRY_MAP.containsKey(registryName)) return Optional.of(ENTRY_MAP.get(registryName));
        return Optional.empty();
    }

    @FunctionalInterface
    public interface Modifier<T extends Block>
    {
        VoxelShape modify(VoxelShape current, T block, BlockState blockState);

        static <T extends Block> Modifier<T> identity()
        {

            return (current, block, blockState) -> current;
        }
    }

    public static final class Entry<T extends Block>
    {
        private final Supplier<VoxelShape> baseShape;
        private final Modifier<T> modifier;
        private final Map<BlockState, VoxelShape> optimizedShapes = Maps.newHashMap();

        private Entry(Supplier<VoxelShape> baseShape, Modifier<T> modifier)
        {
            this.baseShape = Lazy.of(baseShape);
            this.modifier = modifier;
        }

        @SuppressWarnings("unchecked")
        public VoxelShape getShape(BlockState blockState)
        {
            return optimizedShapes.computeIfAbsent(blockState, $ -> modifier.modify(baseShape.get(), (T) blockState.getBlock(), $).optimize());
        }
    }
}
