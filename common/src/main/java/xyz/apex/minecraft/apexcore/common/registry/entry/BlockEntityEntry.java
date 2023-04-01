package xyz.apex.minecraft.apexcore.common.registry.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;

import java.util.Optional;

public final class BlockEntityEntry<T extends BlockEntity> extends RegistryEntry<BlockEntityType<T>>
{
    public BlockEntityEntry(ResourceLocation registryName)
    {
        super(Registries.BLOCK_ENTITY_TYPE, registryName);
    }

    @Nullable
    public T create(BlockPos pos, BlockState blockState)
    {
        return get().create(pos, blockState);
    }

    public Optional<T> get(BlockGetter level, BlockPos pos)
    {
        return level.getBlockEntity(pos, get());
    }
}
