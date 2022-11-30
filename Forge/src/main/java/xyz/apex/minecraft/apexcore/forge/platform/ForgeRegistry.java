package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang3.Validate;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistry;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ForgeRegistry extends ForgeHolder implements PlatformRegistry
{
    private final Table<ResourceKey<? extends Registry<?>>, String, DeferredRegister<?>> modRegistries = HashBasedTable.create();

    ForgeRegistry(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public <T, R extends T> Supplier<R> register(Registry<T> vanilla, ModdedRegistry<T> modded, ResourceKey<T> key, Supplier<R> factory)
    {
        var registry = getOrCreateModRegistry(vanilla.key(), modded.getRegistryName().getNamespace());
        return registry.register(key.location().getPath(), factory);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> blockEntityType(String namespace, String name, BiFunction<BlockPos, BlockState, T> factory, Collection<Supplier<Block>> validBlocks, Consumer<BlockEntityType<T>> consumer)
    {
        return () -> {
            var blocks = validBlocks.stream().map(Supplier::get).toArray(Block[]::new);
            var result = BlockEntityType.Builder.of(factory::apply, blocks).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "%s:%s".formatted(namespace, name)));
            consumer.accept(result);
            return result;
        };
    }

    @SuppressWarnings({ "unchecked", "ConstantConditions" })
    private <T> DeferredRegister<T> getOrCreateModRegistry(ResourceKey<? extends Registry<T>> type, String modId)
    {
        if(modRegistries.contains(type, modId)) return (DeferredRegister<T>) modRegistries.get(type, modId);

        var modRegistry = DeferredRegister.create(type, modId);
        modRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        Validate.isTrue(ModLoadingContext.get().getActiveContainer().getModId().equals(modId), "ForgePlatform#getOrCreateModRegistry must be called during '%s' mod initialization", modId);
        return modRegistry;
    }
}
