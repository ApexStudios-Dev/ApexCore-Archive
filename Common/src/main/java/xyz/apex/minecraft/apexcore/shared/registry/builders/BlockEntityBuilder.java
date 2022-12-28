package xyz.apex.minecraft.apexcore.shared.registry.builders;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.Nullable;

import net.minecraft.Util;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.platform.EnvironmentExecutor;
import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntityEntry;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockEntityBuilder<T extends BlockEntity> extends AbstractBuilder<BlockEntityType<?>, BlockEntityType<T>, BlockEntityBuilder<T>, BlockEntityEntry<T>>
{
    private final BlockEntityFactory<T> factory;
    private final Set<Supplier<? extends Block>> validBlocks = Sets.newHashSet();
    @Nullable private Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>>> renderer = null;

    BlockEntityBuilder(String modId, String registryName, BlockEntityFactory<T> factory)
    {
        super(Registries.BLOCK_ENTITY_TYPE, modId, registryName, BlockEntityEntry::new);

         this.factory = factory;
    }

    @Override
    protected void onRegister(BlockEntityType<T> value)
    {
        super.onRegister(value);

        if(renderer != null) EnvironmentExecutor.runForClient(() -> () -> Platform.INSTANCE.registries().registerBlockEntityRenderer(getModId(), asSupplier(), renderer));
    }

    public BlockEntityBuilder<T> validBlock(Supplier<? extends Block> block)
    {
        validBlocks.add(block);
        return this;
    }

    public BlockEntityBuilder<T> validBlocks(Supplier<? extends Block>... blocks)
    {
        Collections.addAll(validBlocks, blocks);
        return this;
    }

    public BlockEntityBuilder<T> renderer(Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>>> renderer)
    {
        this.renderer = renderer;
        return this;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected BlockEntityType<T> construct()
    {
        var ref = new AtomicReference<BlockEntityType<T>>();

        var result = BlockEntityType.Builder.of(
                (pos, blockState) -> factory.create(ref.get(), pos, blockState),
                validBlocks.stream().map(Supplier::get).toArray(Block[]::new)
        ).build(Util.fetchChoiceType(References.BLOCK_ENTITY, getRegistryName()));

        ref.set(result);
        return result;
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity>
    {
        T create(BlockEntityType<T> blockEntityType, BlockPos pos, BlockState blockState);
    }
}
