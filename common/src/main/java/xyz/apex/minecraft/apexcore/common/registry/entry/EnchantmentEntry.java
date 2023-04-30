package xyz.apex.minecraft.apexcore.common.registry.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;

import java.util.Map;

public final class EnchantmentEntry<T extends Enchantment> extends RegistryEntry<T> implements ItemLikeEntry<EnchantedBookItem>
{
    public EnchantmentEntry(ResourceLocation registryName)
    {
        super(Registries.ENCHANTMENT, registryName);
    }

    public EnchantmentInstance asEnchantmentInstance(int level)
    {
        return new EnchantmentInstance(get(), level);
    }

    public boolean is(EnchantmentInstance enchantment)
    {
        return isPresent() && get() == enchantment.enchantment;
    }

    public Map<EquipmentSlot, ItemStack> getSlotItems(LivingEntity entity)
    {
        return get().getSlotItems(entity);
    }

    public Enchantment.Rarity getRarity()
    {
        return get().getRarity();
    }

    public int getMinLevel()
    {
        return get().getMinLevel();
    }

    public int getMaxLevel()
    {
        return get().getMaxLevel();
    }

    public int getMinCost(int level)
    {
        return get().getMinCost(level);
    }

    public int getMaxCost(int level)
    {
        return get().getMaxCost(level);
    }

    public int getDamageProtection(int level, DamageSource damageSource)
    {
        return get().getDamageProtection(level, damageSource);
    }

    public float getDamageBonus(int level, MobType mobType)
    {
        return get().getDamageBonus(level, mobType);
    }

    public boolean isCompatibleWith(Enchantment other)
    {
        return get().isCompatibleWith(other);
    }

    public boolean canEnchant(ItemStack stack)
    {
        return get().canEnchant(stack);
    }

    public boolean isTreasureOnly()
    {
        return get().isTreasureOnly();
    }

    public boolean isCurse()
    {
        return get().isCurse();
    }

    public boolean isTradeable()
    {
        return get().isTradeable();
    }

    public boolean isDiscoverable()
    {
        return get().isDiscoverable();
    }

    @Override
    public Item asItem()
    {
        return Items.ENCHANTED_BOOK;
    }

    @Override
    public ItemStack asStack()
    {
        return asStack(1, 1);
    }

    @Override
    public ItemStack asStack(int count)
    {
        return asStack(count, 1);
    }

    public ItemStack asStack(int count, int level)
    {
        var stack = new ItemStack(this, count);
        EnchantedBookItem.addEnchantment(stack, asEnchantmentInstance(level));
        return stack;
    }
}
