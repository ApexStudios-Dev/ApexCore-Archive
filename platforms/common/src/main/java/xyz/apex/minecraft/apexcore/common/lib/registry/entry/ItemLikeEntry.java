package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

public interface ItemLikeEntry<T extends ItemLike> extends RegistryEntry<T>, ItemLike
{
    @ApiStatus.NonExtendable
    @Override
    default Item asItem()
    {
        return value().asItem();
    }

    @ApiStatus.NonExtendable
    default boolean is(ItemStack stack)
    {
        return stack.is(asItem());
    }

    @ApiStatus.NonExtendable
    default ItemStack asStack(int count)
    {
        return new ItemStack(this, count);
    }

    @ApiStatus.NonExtendable
    default ItemStack asStack()
    {
        return asStack(1);
    }
}
