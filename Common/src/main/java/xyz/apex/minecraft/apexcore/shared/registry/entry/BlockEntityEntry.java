package xyz.apex.minecraft.apexcore.shared.registry.entry;

import dev.architectury.registry.registries.RegistrySupplier;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

import java.util.Optional;

@SuppressWarnings("unchecked")
public final class BlockEntityEntry<T extends BlockEntity> extends RegistryEntry<BlockEntityType<T>>
{
    public BlockEntityEntry(AbstractRegistrar<?> owner, RegistrySupplier<BlockEntityType<T>> delegate, ResourceKey<? super BlockEntityType<T>> registryKey)
    {
        super(owner, delegate, Registries.BLOCK_ENTITY_TYPE, registryKey);
    }

    public <M extends AbstractContainerMenu> MenuEntry<M> asMenuEntry()
    {
        return getSibling(Registries.MENU, MenuEntry.class);
    }

    public <B extends Block> BlockEntry<B> asBlockEntry()
    {
        return getSibling(Registries.BLOCK, BlockEntry.class);
    }

    public <I extends Item> ItemEntry<I> asItemEntry()
    {
        return getSibling(Registries.ITEM, ItemEntry.class);
    }

    public boolean is(@Nullable BlockEntityType<?> other)
    {
        return isPresent() && get() == other;
    }

    @Nullable
    public T create(BlockPos pos, BlockState blockState)
    {
        return get().create(pos, blockState);
    }

    @Nullable
    public T get(BlockGetter level, BlockPos pos)
    {
        return get().getBlockEntity(level, pos);
    }

    public Optional<T> getOptional(BlockGetter level, BlockPos pos)
    {
        return Optional.ofNullable(get(level, pos));
    }

    public boolean isValid(BlockState blockState)
    {
        return get().isValid(blockState);
    }

    public boolean isValid(Block block)
    {
        return get().validBlocks.contains(block);
    }
}
