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

public final class DeferredBlockEntityType<T extends BlockEntity> extends DeferredHolder<BlockEntityType<?>, BlockEntityType<T>>
{
    private DeferredBlockEntityType(String ownerId, ResourceKey<BlockEntityType<?>> registryKey)
    {
        super(ownerId, registryKey);
    }

    @Nullable
    public T create(BlockPos pos, BlockState blockState)
    {
        return tryAndCreate(pos, blockState).getRaw();
    }

    public OptionalLike<T> tryAndCreate(BlockPos pos, BlockState blockState)
    {
        return map(value -> value.create(pos, blockState));
    }

    public boolean isValid(BlockState blockState)
    {
        return map(value -> value.isValid(blockState)).orElse(false);
    }

    public boolean isValid(Block block)
    {
        return map(value -> value.validBlocks.contains(block)).orElse(false);
    }

    public boolean isValid(Holder<Block> holder)
    {
        return isValid(holder.value());
    }

    @Nullable
    public T get(BlockGetter level, BlockPos pos)
    {
        return getOptional(level, pos).getRaw();
    }

    public OptionalLike<T> getOptional(BlockGetter level, BlockPos pos)
    {
        return map(value -> value.getBlockEntity(level, pos));
    }

    public static ResourceKey<BlockEntityType<?>> createRegistryKey(ResourceLocation registryName)
    {
        return ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, registryName);
    }

    public static <T extends BlockEntity> DeferredBlockEntityType<T> createBlockEntityType(String ownerId, ResourceLocation registryName)
    {
        return createBlockEntityType(ownerId, createRegistryKey(registryName));
    }

    public static <T extends BlockEntity> DeferredBlockEntityType<T> createBlockEntityType(ResourceLocation registryName)
    {
        return createBlockEntityType(registryName.getNamespace(), registryName);
    }

    public static <T extends BlockEntity> DeferredBlockEntityType<T> createBlockEntityType(String ownerId, ResourceKey<BlockEntityType<?>> registryKey)
    {
        return new DeferredBlockEntityType<>(ownerId, registryKey);
    }

    public static <T extends BlockEntity> DeferredBlockEntityType<T> createBlockEntityType(ResourceKey<BlockEntityType<?>> registryKey)
    {
        return new DeferredBlockEntityType<>(registryKey.location().getNamespace(), registryKey);
    }
}
