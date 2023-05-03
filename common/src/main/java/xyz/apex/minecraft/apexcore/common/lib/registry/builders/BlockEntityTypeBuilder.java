package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.compress.utils.Lists;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.BlockEntityEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.BlockEntityFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * BlockEntityType builder used to construct new block entity type instances.
 *
 * @param <P> Type of parent element.
 * @param <T> Type of block entity.
 */
public final class BlockEntityTypeBuilder<P, T extends BlockEntity> extends AbstractBuilder<P, BlockEntityType<?>, BlockEntityType<T>, BlockEntityEntry<T>, BlockEntityTypeBuilder<P, T>>
{
    private final BlockEntityFactory<T> blockEntityFactory;
    private final List<Supplier<? extends Block>> validBlocks = Lists.newArrayList();

    BlockEntityTypeBuilder(P parent, BuilderManager builderManager, String registrationName, BlockEntityFactory<T> blockEntityFactory)
    {
        super(parent, builderManager, Registries.BLOCK_ENTITY_TYPE, registrationName, BlockEntityEntry::new);

        this.blockEntityFactory = blockEntityFactory;
    }

    // TODO: renderer

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected BlockEntityType<T> createObject()
    {
        var validBlocks = this.validBlocks.stream().map(Supplier::get).filter(Objects::nonNull).distinct().toArray(Block[]::new);
        return BlockEntityType.Builder.of((pos, blockState) -> blockEntityFactory.create(asSupplier().get(), pos, blockState), validBlocks)
                .build(Util.fetchChoiceType(References.BLOCK_ENTITY, getRegistryName().toString()));
    }

    /**
     * Mark given block as being valid for this block entity.
     *
     * @param validBlock Block to mark as being valid for this block entity.
     * @return This builder instance.
     */
    public BlockEntityTypeBuilder<P, T> validBlock(Supplier<? extends Block> validBlock)
    {
        validBlocks.add(validBlock);
        return self();
    }

    /**
     * Mark given blocks as being valid for this block entity.
     *
     * @param validBlocks Blocks to mark as being valid for this block entity.
     * @return This builder instance.
     */
    @SafeVarargs
    public final BlockEntityTypeBuilder<P, T> validBlocks(Supplier<? extends Block>... validBlocks)
    {
        Collections.addAll(this.validBlocks, validBlocks);
        return self();
    }
}
