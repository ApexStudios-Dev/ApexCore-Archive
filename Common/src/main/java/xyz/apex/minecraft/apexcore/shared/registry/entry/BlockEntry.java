package xyz.apex.minecraft.apexcore.shared.registry.entry;

import dev.architectury.registry.registries.RegistrySupplier;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

@SuppressWarnings({ "unchecked", "deprecation" })
public final class BlockEntry<T extends Block> extends ItemLikeEntry<T>
{
    public BlockEntry(AbstractRegistrar<?> owner, RegistrySupplier<T> delegate, ResourceKey<? super T> registryKey)
    {
        super(owner, delegate, Registries.BLOCK, registryKey);
    }

    public <I extends Item> ItemEntry<I> asItemEntry()
    {
        return getSibling(Registries.ITEM, ItemEntry.class);
    }

    public <B extends BlockEntity> BlockEntityEntry<B> asBlockEntityEntry()
    {
        return getSibling(Registries.BLOCK_ENTITY_TYPE, BlockEntityEntry.class);
    }

    public <M extends AbstractContainerMenu> MenuEntry<M> asMenuEntry()
    {
        return getSibling(Registries.MENU, MenuEntry.class);
    }

    public boolean is(@Nullable Block other)
    {
        return isPresent() && get() == other;
    }

    public boolean hasBlockTag(TagKey<Block> tag)
    {
        return get().builtInRegistryHolder().is(tag);
    }

    public boolean hasBlockState(BlockState blockState)
    {
        return is(blockState.getBlock());
    }

    public BlockState defaultBlockState()
    {
        return get().defaultBlockState();
    }
}
