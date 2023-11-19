package dev.apexstudios.apexcore.common.registry.holder;

import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DeferredBlockEntityType<T extends BlockEntity> extends DeferredHolder<BlockEntityType<?>, BlockEntityType<T>>
{
    protected DeferredBlockEntityType(ResourceKey<BlockEntityType<?>> valueKey)
    {
        super(valueKey);
    }

    @Nullable
    public T create(BlockPos pos, BlockState blockState)
    {
        return value().create(pos, blockState);
    }

    public OptionalLike<T> tryAndCreate(BlockPos pos, BlockState blockState)
    {
        return () -> create(pos, blockState);
    }

    public boolean isValid(BlockState blockState)
    {
        return value().isValid(blockState);
    }

    public boolean isValid(Block block)
    {
        return value().validBlocks.contains(block);
    }

    public boolean isValid(Holder<Block> holder)
    {
        return isValid(holder.value());
    }

    @Nullable
    public T get(BlockGetter level, BlockPos pos)
    {
        return value().getBlockEntity(level, pos);
    }

    public OptionalLike<T> getOptional(BlockGetter level, BlockPos pos)
    {
        return () -> get(level, pos);
    }

    public static <T extends BlockEntity> DeferredBlockEntityType<T> createBlockEntityType(ResourceLocation valueId)
    {
        return createBlockEntityType(ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, valueId));
    }

    public static <T extends BlockEntity> DeferredBlockEntityType<T> createBlockEntityType(ResourceKey<BlockEntityType<?>> valueKey)
    {
        return new DeferredBlockEntityType<>(valueKey);
    }
}
