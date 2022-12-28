package xyz.apex.minecraft.apexcore.shared.registry.builders;

import net.minecraft.world.level.block.entity.BlockEntity;

import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntityEntry;

public interface BlockEntityBuilders
{
    static <T extends BlockEntity> BlockEntityBuilder<T> builder(String modId, String registryName, BlockEntityBuilder.BlockEntityFactory<T> factory)
    {
        return new BlockEntityBuilder<>(modId, registryName, factory);
    }

    static <T extends BlockEntity> BlockEntityEntry<T> simple(String modId, String registryName, BlockEntityBuilder.BlockEntityFactory<T> factory)
    {
        return builder(modId, registryName, factory).register();
    }
}
