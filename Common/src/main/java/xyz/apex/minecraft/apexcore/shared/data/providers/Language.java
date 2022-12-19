package xyz.apex.minecraft.apexcore.shared.data.providers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApiStatus.NonExtendable
public interface Language extends DataProvider
{
    // region: Supplier
    // region: General
    default Language addBlock(Supplier<? extends Block> key, String name)
    {
        return add(key.get(), name);
    }

    default Language addItem(Supplier<? extends Item> key, String name)
    {
        return add(key.get(), name);
    }

    default Language addItemLike(Supplier<? extends ItemLike> key, String name)
    {
        return add(key.get(), name);
    }

    default Language addItemStack(Supplier<ItemStack> key, String name)
    {
        return add(key.get(), name);
    }

    default Language addEnchantment(Supplier<? extends Enchantment> key, String name)
    {
        return add(key.get(), name);
    }

    default Language addEffect(Supplier<? extends MobEffect> key, String name)
    {
        return add(key.get(), name);
    }

    default Language addEntityType(Supplier<? extends EntityType<?>> key, String name)
    {
        return add(key.get(), name);
    }

    default Language addCreativeModeTab(Supplier<? extends CreativeModeTab> key, String name)
    {
        return add(key.get(), name);
    }
    // endregion

    // region: ToEnglishName
    default Language addBlock(Supplier<? extends Block> key)
    {
        return add(key.get());
    }

    default Language addItem(Supplier<? extends Item> key)
    {
        return add(key.get());
    }

    default Language addItemLike(Supplier<? extends ItemLike> key)
    {
        return add(key.get());
    }

    default Language addItemStack(Supplier<ItemStack> key)
    {
        return add(key.get());
    }

    default Language addEnchantment(Supplier<? extends Enchantment> key)
    {
        return add(key.get());
    }

    default Language addEffect(Supplier<? extends MobEffect> key)
    {
        return add(key.get());
    }

    default Language addEntityType(Supplier<? extends EntityType<?>> key)
    {
        return add(key.get());
    }

    default Language addCreativeModeTab(Supplier<? extends CreativeModeTab> key)
    {
        return add(key.get());
    }
    // endregion
    // endregion

    // region: General
    default Language add(Block key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    default Language add(Item key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    default Language add(ItemLike key, String name)
    {
        return add(key.asItem(), name);
    }

    default Language add(ItemStack key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    default Language add(Enchantment key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    default Language add(MobEffect key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    default Language add(EntityType<?> key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    default Language add(CreativeModeTab key, String name)
    {
        if(key.getDisplayName() instanceof TranslatableContents translatable) return add(translatable.getKey(), name);
        return this;
    }

    Language add(String key, String name);
    // endregion

    // region: ToEnglishName
    default Language add(Block key)
    {
        return add(key, toEnglishName(key.getDescriptionId()));
    }

    default Language add(Item key)
    {
        return add(key, toEnglishName(key.getDescriptionId()));
    }

    default Language add(ItemLike key)
    {
        return add(key.asItem());
    }

    default Language add(ItemStack key)
    {
        return add(key, toEnglishName(key.getDescriptionId()));
    }

    default Language add(Enchantment key)
    {
        return add(key, toEnglishName(key.getDescriptionId()));
    }

    default Language add(MobEffect key)
    {
        return add(key, toEnglishName(key.getDescriptionId()));
    }

    default Language add(EntityType<?> key)
    {
        return add(key, toEnglishName(key.getDescriptionId()));
    }

    default Language add(CreativeModeTab key)
    {
        if(key.getDisplayName() instanceof TranslatableContents translatable) return add(translatable.getKey(), toEnglishName(translatable.getKey()));
        return this;
    }

    default Language add(String key)
    {
        return add(key, toEnglishName(key));
    }
    // endregion

    static String toEnglishName(String internalName)
    {
        return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    static <T> String getAutomaticName(Registry<T> registry, T entry)
    {
        var registryName = registry.getKey(entry);
        Validate.notNull(registryName);
        return toEnglishName(registryName.getPath());
    }
}
