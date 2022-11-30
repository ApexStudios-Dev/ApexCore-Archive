package xyz.apex.minecraft.apexcore.fabric.platform;

import com.google.common.base.Suppliers;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistry;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class FabricRegistry extends FabricHolder implements PlatformRegistry
{
    FabricRegistry(FabricPlatform platform)
    {
        super(platform);
    }

    @Override
    public <T, R extends T> Supplier<R> register(Registry<T> vanilla, ModdedRegistry<T> modded, ResourceKey<T> key, Supplier<R> factory)
    {
        var value = Registry.register(vanilla, key, factory.get());
        return Suppliers.memoize(() -> value);
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(String namespace, String name, BiFunction<BlockPos, BlockState, T> factory, Collection<Supplier<Block>> validBlocks, Consumer<BlockEntityType<T>> consumer)
    {
        return () -> {
            var blockEntityBuilder = FabricBlockEntityTypeBuilder.create(factory::apply);
            validBlocks.stream().map(Supplier::get).forEach(blockEntityBuilder::addBlock);
            var result = blockEntityBuilder.build(Util.fetchChoiceType(References.BLOCK_ENTITY, "%s:%s".formatted(namespace, name)));
            consumer.accept(result);
            return result;
        };
    }
}
