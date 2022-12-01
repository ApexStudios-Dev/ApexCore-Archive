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
import xyz.apex.minecraft.apexcore.shared.registry.ModdedRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.RegistryEntryBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class BlockEntityBuilder<T extends BlockEntity> extends RegistryEntryBuilder<BlockEntityType<?>, BlockEntityType<T>, BlockEntityBuilder<T>>
{
    private final TriFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory;

    private final List<Supplier<Block>> validBlocks = Lists.newArrayList();

    private BlockEntityBuilder(ModdedRegistry<BlockEntityType<?>> registry, String name, TriFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory)
    {
        super(registry, name);

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
    @Override
    protected BlockEntityType<T> build()
    {
        var blocks = ImmutableList.copyOf(validBlocks);
        var ref = new AtomicReference<BlockEntityType<T>>();
        var modId = registry.getRegistryName().getNamespace();
        return (BlockEntityType<T>) Platform.registries().blockEntityType(modId, name, (pos, blockState) -> factory.apply(ref.get(), pos, blockState), blocks, ref::set);
    }

    public static <T extends BlockEntity> BlockEntityBuilder<T> create(ModdedRegistry<BlockEntityType<?>> registry, String name, TriFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory)
    {
        return new BlockEntityBuilder<>(registry, name, factory);
    }
}
