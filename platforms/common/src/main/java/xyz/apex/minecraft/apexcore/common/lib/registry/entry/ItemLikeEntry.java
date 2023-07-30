package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

/**
 * Base interface for all Item-like Registry Entries.
 *
 * @param <T> Type of Entry.
 */
public interface ItemLikeEntry<T extends ItemLike> extends RegistryEntry<T>, ItemLike
{
    /**
     * @return Item representation of underlying entry.
     */
    @ApiStatus.NonExtendable
    @Override
    default Item asItem()
    {
        return value().asItem();
    }

    /**
     * @return {@code true} if ItemStack contains {@link #asItem()}.
     */
    @ApiStatus.NonExtendable
    default boolean is(ItemStack stack)
    {
        return stack.is(asItem());
    }

    /**
     * @return ItemStack containing {@link #asItem()}, with given stack size.
     */
    @ApiStatus.NonExtendable
    default ItemStack asStack(int count)
    {
        return new ItemStack(this, count);
    }

    /**
     * @return ItemStack containing {@link #asItem()}.
     */
    @ApiStatus.NonExtendable
    default ItemStack asStack()
    {
        return asStack(1);
    }
}
