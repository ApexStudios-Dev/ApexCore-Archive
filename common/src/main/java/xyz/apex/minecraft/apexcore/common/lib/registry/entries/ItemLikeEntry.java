package xyz.apex.minecraft.apexcore.common.lib.registry.entries;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;

/**
 * Registry entry which can be associated with an Item instance.
 *
 * @param <T> Type of registry entry.
 */
public interface ItemLikeEntry<T extends ItemLike> extends RegistryEntry<T>, ItemLike
{
    /**
     * @return This registry entry mapped to the related Item instance.
     */
    @Override
    default Item asItem()
    {
        return map(ItemLike::asItem).orElse(Items.AIR);
    }

    /**
     * Returns true if ItemStack contains this registry entry.
     *
     * @param stack ItemStack to validate.
     * @return True if ItemStack contains this registry entry.
     */
    default boolean is(ItemStack stack)
    {
        return stack.is(asItem());
    }

    /**
     * Returns new ItemStack of given size for this registry entry.
     *
     * @param count ItemStack stack size.
     * @return New ItemStack of given size for this registry entry.
     */
    default ItemStack asStack(int count)
    {
        return map(value -> new ItemStack(value, count)).orElse(ItemStack.EMPTY);
    }

    /**
     * @return New ItemStack for this registry entry.
     */
    default ItemStack asStack()
    {
        return asStack(1);
    }
}
