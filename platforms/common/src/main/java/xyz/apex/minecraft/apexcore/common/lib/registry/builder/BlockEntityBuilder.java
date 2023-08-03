package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.Util;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.RendererHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.BlockEntityEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.BlockEntityFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * BlockEntityType Builder implementation.
 * <p>
 * Used to build and register BlockEntityType entries.
 *
 * @param <O> Type of Registrar.
 * @param <T> Type of BlockEntity [Entry].
 * @param <P> Type of Parent.
 */
public final class BlockEntityBuilder<O extends AbstractRegistrar<O>, T extends BlockEntity, P> extends AbstractBuilder<O, P, BlockEntityType<?>, BlockEntityType<T>, BlockEntityBuilder<O, T, P>, BlockEntityEntry<T>>
{
    private final BlockEntityFactory<T> blockEntityFactory;
    private final List<Supplier<? extends Block>> validBlocks = Lists.newArrayList();
    @Nullable private Supplier<Supplier<BlockEntityRendererProvider<T>>> renderer = null;

    @ApiStatus.Internal
    public BlockEntityBuilder(O registrar, P parent, String registrationName, BlockEntityFactory<T> blockEntityFactory)
    {
        super(registrar, parent, Registries.BLOCK_ENTITY_TYPE, registrationName);

        this.blockEntityFactory = blockEntityFactory;
    }

    @Override
    protected void onRegister(BlockEntityType<T> entry)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> {
            if(renderer != null)
                RendererHooks.get().registerBlockEntityRenderer(() -> entry, renderer);
        });
    }

    /**
     * Registers renderer for this BlockEntity.
     *
     * @param renderer Renderer to be registered.
     * @return This Builder.
     */
    public BlockEntityBuilder<O, T, P> renderer(Supplier<Supplier<BlockEntityRendererProvider<T>>> renderer)
    {
        this.renderer = renderer;
        return this;
    }

    /**
     * Mark given Block as being valid for this BlockEntity.
     *
     * @param validBlock Block to mark as being valid for this BlockEntity.
     * @return This Builder.
     */
    public BlockEntityBuilder<O, T, P> validBlock(Supplier<? extends Block> validBlock)
    {
        validBlocks.add(validBlock);
        return self();
    }

    /**
     * Mark given Blocks as being valid for this BlockEntity.
     *
     * @param validBlocks Blocks to mark as being valid for this BlockEntity.
     * @return This Builder.
     */
    @SafeVarargs
    public final BlockEntityBuilder<O, T, P> validBlocks(Supplier<? extends Block>... validBlocks)
    {
        Collections.addAll(this.validBlocks, validBlocks);
        return self();
    }

    @Override
    protected BlockEntityEntry<T> createRegistryEntry()
    {
        return new BlockEntityEntry<>(registrar, registryKey);
    }

    @Override
    protected BlockEntityType<T> createEntry()
    {
        return new BlockEntityType<>(
                (pos, blockState) -> blockEntityFactory.create(getEntry(), pos, blockState),
                validBlocks.stream().map(Supplier::get).collect(Collectors.toUnmodifiableSet()),
                Util.fetchChoiceType(References.BLOCK_ENTITY, registryName().toString())
        );
    }

    @Override
    protected String getDescriptionId(BlockEntityEntry<T> entry)
    {
        return registryName().toLanguageKey("block_entity");
    }
}
