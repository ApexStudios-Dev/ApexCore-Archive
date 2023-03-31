package xyz.apex.minecraft.apexcore.common.registry.builder;

import com.google.common.collect.Iterables;
import net.minecraft.Util;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentHolderFactory;
import xyz.apex.minecraft.apexcore.common.hooks.RendererHooks;
import xyz.apex.minecraft.apexcore.common.registry.entry.BlockEntityEntry;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class BlockEntityBuilder<T extends BlockEntity> extends Builder<BlockEntityType<?>, BlockEntityType<T>, BlockEntityEntry<T>, BlockEntityBuilder<T>>
{
    private final List<Supplier<? extends Block>> validBlocks = Lists.newArrayList();
    private final BlockEntityType.BlockEntitySupplier<T> blockEntityFactory;
    @Nullable private Supplier<BlockEntityRendererProvider<T>> blockEntityRendererProvider = null;
    private Predicate<Block> blockValidator = block -> true;

    private BlockEntityBuilder(String ownerId, String registrationName, BlockEntityType.BlockEntitySupplier<T> blockEntityFactory)
    {
        super(Registries.BLOCK_ENTITY_TYPE, ownerId, registrationName);

        this.blockEntityFactory = blockEntityFactory;

        onRegister(blockEntityType -> {
           if(blockEntityRendererProvider != null) RendererHooks.getInstance().registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider);
        });
    }

    public BlockEntityBuilder<T> renderer(Supplier<BlockEntityRendererProvider<T>> blockEntityRendererProvider)
    {
        this.blockEntityRendererProvider = blockEntityRendererProvider;
        return this;
    }

    public BlockEntityBuilder<T> blockValidator(Predicate<Block> blockValidator)
    {
        this.blockValidator = blockValidator;
        return this;
    }

    public BlockEntityBuilder<T> validBlock(Supplier<? extends Block> validBlock)
    {
        validBlocks.add(validBlock);
        return this;
    }

    @SafeVarargs
    public final BlockEntityBuilder<T> validBlocks(Supplier<? extends Block>... validBlocks)
    {
        Collections.addAll(this.validBlocks, validBlocks);
        return this;
    }

    public BlockEntityBuilder<T> validBlocks(Collection<Supplier<? extends Block>> validBlocks)
    {
        this.validBlocks.addAll(validBlocks);
        return this;
    }

    public BlockEntityBuilder<T> validBlocks(Iterable<Supplier<? extends Block>> validBlocks)
    {
        Iterables.addAll(this.validBlocks, validBlocks);
        return this;
    }

    @Override
    protected BlockEntityEntry<T> createRegistryEntry(ResourceLocation registryName)
    {
        return new BlockEntityEntry<>(registryName);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected BlockEntityType<T> create()
    {
        var resolvedValidBlocks = validBlocks.stream().map(Supplier::get).filter(blockValidator).distinct().toArray(Block[]::new);
        return BlockEntityType.Builder.of(blockEntityFactory, resolvedValidBlocks).build(Util.fetchChoiceType(References.BLOCK_ENTITY, registryName.toString()));
    }

    public static <T extends BlockEntity> BlockEntityBuilder<T> builder(String ownerId, String registrationName, BlockEntityType.BlockEntitySupplier<T> blockEntityFactory)
    {
        return new BlockEntityBuilder<>(ownerId, registrationName, blockEntityFactory);
    }

    public static <T extends BlockEntity & BlockEntityComponentHolder> BlockEntityBuilder<T> builderWithComponents(String ownerId, String registrationName, Consumer<BlockEntityComponentHolder.Registrar> componentRegistrar, BlockEntityComponentHolderFactory<T> blockFactory)
    {
        return builder(ownerId, registrationName, (pos, blockState) -> blockFactory.create(componentRegistrar, pos, blockState));
    }
}
