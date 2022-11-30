package xyz.apex.minecraft.apexcore.shared.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistry;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface PlatformRegistry extends PlatformHolder
{
    <T, R extends T> Supplier<R> register(Registry<T> vanilla, ModdedRegistry<T> modded, ResourceKey<T> key, Supplier<R> factory);

    <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(String namespace, String name, BiFunction<BlockPos, BlockState, T> factory, Collection<Supplier<Block>> validBlocks, Consumer<BlockEntityType<T>> consumer);
}
