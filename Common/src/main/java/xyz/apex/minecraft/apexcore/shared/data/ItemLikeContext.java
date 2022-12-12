package xyz.apex.minecraft.apexcore.shared.data;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.shared.registry.entry.ItemLikeEntry;

public class ItemLikeContext<R extends ItemLike, E extends ItemLikeEntry<R>> extends DataContext<R, E> implements ItemLike
{
    public ItemLikeContext(E entry)
    {
        super(entry);
    }

    public ItemLikeContext(DataContext<R, E> ctx)
    {
        this(ctx.getEntry());
    }

    @Override
    public Item asItem()
    {
        return get().asItem();
    }
}
