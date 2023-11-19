package dev.apexstudios.apexcore.common.registry.holder;

import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class DeferredBlock<T extends Block> extends DeferredHolder<Block, T> implements ItemLike
{
    private DeferredBlock(String ownerId, ResourceKey<Block> registryKey)
    {
        super(ownerId, registryKey);
    }

    public BlockState defaultBlockState()
    {
        return map(Block::defaultBlockState).orElseGet(Blocks.AIR::defaultBlockState);
    }

    public boolean is(BlockState blockState)
    {
        return map(blockState::is).orElse(false);
    }

    @Override
    public Item asItem()
    {
        return map(Block::asItem).orElse(Items.AIR);
    }

    public static ResourceKey<Block> createRegistryKey(ResourceLocation registryName)
    {
        return ResourceKey.create(Registries.BLOCK, registryName);
    }

    public static <T extends Block> DeferredBlock<T> createBlock(String ownerId, ResourceLocation registryName)
    {
        return createBlock(ownerId, createRegistryKey(registryName));
    }

    public static <T extends Block> DeferredBlock<T> createBlock(ResourceLocation registryName)
    {
        return createBlock(registryName.getNamespace(), registryName);
    }

    public static <T extends Block> DeferredBlock<T> createBlock(String ownerId, ResourceKey<Block> registryKey)
    {
        return new DeferredBlock<>(ownerId, registryKey);
    }

    public static <T extends Block> DeferredBlock<T> createBlock(ResourceKey<Block> registryKey)
    {
        return createBlock(registryKey.location().getNamespace(), registryKey);
    }
}
