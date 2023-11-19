package dev.apexstudios.apexcore.common.registry.builder;

import com.google.common.collect.Sets;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.holder.DeferredBlockEntityType;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.Util;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public final class BlockEntityTypeBuilder<O extends AbstractRegister<O>, P, T extends BlockEntity> extends AbstractBuilder<O, P, BlockEntityType<?>, BlockEntityType<T>, DeferredBlockEntityType<T>, BlockEntityTypeBuilder<O, P, T>>
{
    private final Set<OptionalLike<Block>> validBlocks = Sets.newHashSet();
    private final Factory<T> blockEntityFactory;
    private OptionalLike<OptionalLike<BlockEntityRendererProvider<T>>> renderer = () -> null;

    @ApiStatus.Internal
    public BlockEntityTypeBuilder(O owner, P parent, String valueName, BuilderHelper helper, Factory<T> blockEntityFactory)
    {
        super(owner, parent, Registries.BLOCK_ENTITY_TYPE, valueName, DeferredBlockEntityType::createBlockEntityType, helper);

        this.blockEntityFactory = blockEntityFactory;
    }

    public BlockEntityTypeBuilder<O, P, T> renderer(OptionalLike<OptionalLike<BlockEntityRendererProvider<T>>> renderer)
    {
        this.renderer = renderer;
        return this;
    }

    public BlockEntityTypeBuilder<O, P, T> validBlock(OptionalLike<Block> validBlock)
    {
        validBlocks.add(validBlock);
        return this;
    }

    public BlockEntityTypeBuilder<O, P, T> validBlock(Holder<Block> validBlock)
    {
        return validBlock(OptionalLike.of(validBlock));
    }

    public BlockEntityTypeBuilder<O, P, T> validBlock(@Nullable Block validBlock)
    {
        return validBlock(() -> validBlock);
    }

    @SafeVarargs
    public final BlockEntityTypeBuilder<O, P, T> validBlocks(Holder<Block>... validBlocks)
    {
        Arrays.stream(validBlocks).map(OptionalLike::of).forEach(this.validBlocks::add);
        return this;
    }

    public BlockEntityTypeBuilder<O, P, T> validBlocks(@Nullable Block... validBlocks)
    {
        Stream.of(validBlocks).map(OptionalLike::of).forEach(this.validBlocks::add);
        return this;
    }

    @Override
    protected BlockEntityType<T> createValue()
    {
        var validBlocks = this.validBlocks.stream().filter(OptionalLike::isPresent).map(OptionalLike::get).toArray(Block[]::new);
        var dfuType = Util.fetchChoiceType(References.BLOCK_ENTITY, getValueName().toString());
        return BlockEntityType.Builder.of(blockEntityFactory.toVanilla(asHolder()), validBlocks).build(dfuType);
    }

    @Override
    protected void onRegister(BlockEntityType<T> value)
    {
        owner.registerBlockEntityRenderer(value, renderer);
    }

    @FunctionalInterface
    public interface Factory<T extends BlockEntity>
    {
        T create(BlockEntityType<T> blockEntityType, BlockPos pos, BlockState blockState);

        @ApiStatus.Internal
        default BlockEntityType.BlockEntitySupplier<T> toVanilla(DeferredBlockEntityType<T> blockEntityType)
        {
            return (pos, blockState) -> create(blockEntityType.value(), pos, blockState);
        }

        static <T extends BlockEntity> Factory<T> fromVanilla(BlockEntityType.BlockEntitySupplier<T> vanilla)
        {
            return new Factory<>()
            {
                @Override
                public T create(BlockEntityType<T> blockEntityType, BlockPos pos, BlockState blockState)
                {
                    return vanilla.create(pos, blockState);
                }

                @Override
                public BlockEntityType.BlockEntitySupplier<T> toVanilla(DeferredBlockEntityType<T> blockEntityType)
                {
                    return vanilla;
                }
            };
        }
    }
}
