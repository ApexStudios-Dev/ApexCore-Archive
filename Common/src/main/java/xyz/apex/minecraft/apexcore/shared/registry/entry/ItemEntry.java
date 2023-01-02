package xyz.apex.minecraft.apexcore.shared.registry.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

@SuppressWarnings("unchecked")
public final class ItemEntry<T extends Item> extends ItemLikeEntry<T>
{
    public ItemEntry(AbstractRegistrar<?> owner, ResourceKey<? super T> registryKey)
    {
        super(owner, Registries.ITEM, registryKey);
    }

    public <B extends Block> BlockEntry<B> asBlockEntry()
    {
        return getSibling(Registries.BLOCK, BlockEntry.class);
    }

    public <M extends AbstractContainerMenu> MenuEntry<M> asMenuEntry()
    {
        return getSibling(Registries.MENU, MenuEntry.class);
    }
}
