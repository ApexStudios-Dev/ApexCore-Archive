package xyz.apex.minecraft.apexcore.common.lib.registry.entries;

import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.DelegatedRegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.EnchantmentBuilder;

/**
 * Main RegistryEntry class for all Enchantment entries.
 * <p>
 * While the constructor is publicly visible, you should never invoke or create instance of this class yourself.
 * Instances of this class are provided when registered using the {@link EnchantmentBuilder} class.
 *
 * @param <T> Type of enchantment.
 */
public final class EnchantmentEntry<T extends Enchantment> extends DelegatedRegistryEntry<T> implements ItemLike
{
    /**
     * DO NOT MANUALLY CALL PUBLIC FOR INTERNAL USAGES ONLY
     */
    @ApiStatus.Internal
    public EnchantmentEntry(RegistryEntry<T> delegate)
    {
        super(delegate);
    }

    // Custom ItemLikeEntry implementation cause Enchantment does not implement ItemLike
    // meaning we cant implement ItemLikeEntry onto this class

    /**
     * @return This registry entry mapped to the related Item instance.
     */
    @Override
    public Item asItem()
    {
        return Items.ENCHANTED_BOOK;
    }

    /**
     * Returns true if ItemStack contains this registry entry.
     *
     * @param stack ItemStack to validate.
     * @return True if ItemStack contains this registry entry.
     */
    public boolean is(ItemStack stack)
    {
        return stack.is(Items.ENCHANTED_BOOK);
    }

    /**
     * Returns new ItemStack of given size for this registry entry.
     *
     * @param count ItemStack stack size.
     * @param level Enchantment level.
     * @return New ItemStack of given size for this registry entry.
     */
    public ItemStack asStack(int count, int level)
    {
        return map(value -> {
            var stack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(value, level));
            stack.setCount(count);
            return stack;
        }).orElse(ItemStack.EMPTY);
    }

    /**
     * Returns new ItemStack of given size for this registry entry.
     *
     * @param level Enchantment level.
     * @return New ItemStack of given size for this registry entry.
     */
    public ItemStack asStack(int level)
    {
        return asStack(1, level);
    }

    /**
     * @return New ItemStack for this registry entry.
     */
    public ItemStack asStack()
    {
        return asStack(1, 1);
    }
}
