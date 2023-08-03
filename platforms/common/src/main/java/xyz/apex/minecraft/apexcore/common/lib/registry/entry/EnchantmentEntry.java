package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

public final class EnchantmentEntry<T extends Enchantment> extends BaseRegistryEntry<T> implements ItemLike
{
    @ApiStatus.Internal
    public EnchantmentEntry(AbstractRegistrar<?> registrar, ResourceKey<T> registryKey)
    {
        super(registrar, registryKey);
    }

    /**
     * @return Item representation of underlying entry.
     */
    @Override
    public Item asItem()
    {
        return Items.ENCHANTED_BOOK;
    }

    /**
     * @return {@code true} if ItemStack contains this Enchantment.
     */
    public boolean has(ItemStack stack)
    {
        // because of course Mojang stores enchantments differently on enchanted books
        // than on all other items
        if(stack.is(Items.ENCHANTED_BOOK))
            return has(EnchantedBookItem.getEnchantments(stack));
        return has(stack.getEnchantmentTags());
    }

    private boolean has(ListTag enchantmentsTag)
    {
        for(var i = 0; i < enchantmentsTag.size(); i++)
        {
            var enchantmentTag = enchantmentsTag.getCompound(i);
            var enchantmentId = EnchantmentHelper.getEnchantmentId(enchantmentTag);

            if(enchantmentId != null && is(enchantmentId))
                return true;
        }

        return false;
    }

    /**
     * Enchants given Item with {@link #value()}.
     *
     * @param item Item to enchant.
     * @param count Enchanted ItemStack size.
     * @param level Level of enchantment.
     * @return Enchanted ItemStack.
     */
    public ItemStack with(ItemLike item, int count, int level)
    {
        var stack = new ItemStack(item);

        if(stack.is(Items.ENCHANTED_BOOK))
            EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(value(), level));
        else
            stack.enchant(value(), level);

        return stack;
    }

    /**
     * Enchants given Item with {@link #value()}.
     *
     * @param item Item to enchant.
     * @param level Level of enchantment.
     * @return Enchanted ItemStack.
     */
    public ItemStack with(ItemLike item, int level)
    {
        return with(item, 1, level);
    }

    /**
     * @return EnchantedBook containing {@link #value()}.
     */
    public ItemStack asStack(int count, int level)
    {
        return with(this, count, level);
    }

    /**
     * @return EnchantedBook containing {@link #value()}.
     */
    public ItemStack asStack(int level)
    {
        return with(this, 1, level);
    }

    /**
     * @return EnchantedBook containing {@link #value()}.
     */
    public ItemStack asStack()
    {
        return with(this, 1, 1);
    }

    public static <T extends Enchantment> EnchantmentEntry<T> cast(RegistryEntry<?> registryEntry)
    {
        return RegistryEntry.cast(EnchantmentEntry.class, registryEntry);
    }
}
