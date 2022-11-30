package xyz.apex.minecraft.apexcore.shared.registry.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.function.TriFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistries;
import xyz.apex.minecraft.apexcore.shared.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.registry.RegistryKeys;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class BlockEntityBuilder<T extends BlockEntity>
{
    private final TriFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory;
    private final String name;
    private final String modId;

    private final List<Supplier<Block>> validBlocks = Lists.newArrayList();

    private BlockEntityBuilder(String modId, String name, TriFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory)
    {
        this.modId = modId;
        this.name = name;
        this.factory = factory;
    }

    public BlockEntityBuilder<T> validBlock(Supplier<Block> validBlock)
    {
        validBlocks.add(validBlock);
        return this;
    }

    @SafeVarargs
    public final BlockEntityBuilder<T> validBlocks(Supplier<Block>... validBlocks)
    {
        Collections.addAll(this.validBlocks, validBlocks);
        return this;
    }

    public BlockEntityBuilder<T> validBlocks(Collection<Supplier<Block>> validBlocks)
    {
        this.validBlocks.addAll(validBlocks);
        return this;
    }

    @SuppressWarnings("unchecked")
    public RegistryEntry<BlockEntityType<T>> register()
    {
        return ModdedRegistries.create(RegistryKeys.BLOCK_ENTITY_TYPE, modId).register(name, () -> {
            var blocks = ImmutableList.copyOf(validBlocks);
            var ref = new AtomicReference<BlockEntityType<T>>();
            return (BlockEntityType<T>) Platform.registries().blockEntityType(modId, name, (pos, blockState) -> factory.apply(ref.get(), pos, blockState), blocks, ref::set);
        });
    }

    public static <T extends BlockEntity> BlockEntityBuilder<T> create(String modId, String name, TriFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory)
    {
        return new BlockEntityBuilder<>(modId, name, factory);
    }
}
