package xyz.apex.minecraft.apexcore.shared.data.providers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface LanguageProvider extends DataProvider
{
    void addBlock(Supplier<? extends Block> key, String name);
    void add(Block key, String name);
    void addItem(Supplier<? extends Item> key, String name);
    void add(Item key, String name);
    void addItemStack(Supplier<ItemStack> key, String name);
    void add(ItemStack key, String name);
    void addEnchantment(Supplier<? extends Enchantment> key, String name);
    void add(Enchantment key, String name);
    void addEffect(Supplier<? extends MobEffect> key, String name);
    void add(MobEffect key, String name);
    void addEntityType(Supplier<? extends EntityType<?>> key, String name);
    void add(EntityType<?> key, String name);
    void add(String key, String value);

    default void addItemLike(Supplier<? extends ItemLike> key, String name)
    {
        add(key.get(), name);
    }

    default void add(ItemLike key, String name)
    {
        add(key.asItem(), name);
    }

    default void addBlock(Supplier<? extends Block> key)
    {
        addBlock(key, getAutomaticName(key, Registries.BLOCK));
    }

    default void addItem(Supplier<? extends Item> key)
    {
        addItem(key, getAutomaticName(key, Registries.ITEM));
    }

    default void addEnchantment(Supplier<? extends Enchantment> key)
    {
        addEnchantment(key, getAutomaticName(key, Registries.ENCHANTMENT));
    }

    default void addEffect(Supplier<? extends MobEffect> key)
    {
        addEffect(key, getAutomaticName(key, Registries.MOB_EFFECT));
    }

    default void addEntityType(Supplier<? extends EntityType<?>> key)
    {
        addEntityType(key, getAutomaticName(key, Registries.ENTITY_TYPE));
    }

    default void add(Block key)
    {
        add(key, getAutomaticName(key, Registries.BLOCK));
    }

    default void add(Item key)
    {
        add(key, getAutomaticName(key, Registries.ITEM));
    }

    default void add(Enchantment key)
    {
        add(key, getAutomaticName(key, Registries.ENCHANTMENT));
    }

    default void add(MobEffect key)
    {
        add(key, getAutomaticName(key, Registries.MOB_EFFECT));
    }

    default void add(EntityType<?> key)
    {
        add(key, getAutomaticName(key, Registries.ENTITY_TYPE));
    }

    default <T> String getAutomaticName(Supplier<? extends T> supplier, ResourceKey<? extends Registry<T>> registryType)
    {
        return getAutomaticName(supplier.get(), registryType);
    }

    default <T> String getAutomaticName(T type, ResourceKey<? extends Registry<T>> registryType)
    {
        var registry = RegistryEntry.getRegistryOrThrow(registryType);
        var registryName = registry.getKey(type);
        Validate.notNull(registryName);
        return toEnglishName(registryName.getPath());
    }

    static String toEnglishName(String internalName)
    {
        return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }
}
