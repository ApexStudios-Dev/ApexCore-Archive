package xyz.apex.minecraft.apexcore.shared.data.providers;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class LanguageProvider implements DataProvider
{
    private final Map<String, String> data = Maps.newTreeMap();
    private final PackOutput output;
    private final String modId;

    @ApiStatus.Internal
    public LanguageProvider(PackOutput output, String modId)
    {
        this.output = output;
        this.modId = modId;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        Generators.processDataGenerator(modId, ProviderTypes.LANGUAGE, this);

        var json = new JsonObject();
        // assets/<modId>/lang/en_us.json
        var outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(modId).resolve("lang").resolve("en_us.json");
        data.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, outputPath);
    }

    @Override
    public String getName()
    {
        return "Language: English-US";
    }

    // region: Helpers
    // region: Generic
    public LanguageProvider add(String key, String name)
    {
        if(data.put(key, name) != null) throw new IllegalStateException("Duplicate translation key: %s".formatted(key));
        return this;
    }
    // endregion

    // region: Block
    public LanguageProvider addBlock(Supplier<? extends Block> key, String name)
    {
        return add(key.get(), name);
    }

    public LanguageProvider add(Block key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    public LanguageProvider addBlock(Supplier<? extends Block> key)
    {
        return add(key.get());
    }

    public LanguageProvider add(Block key)
    {
        return add(key, getAutomaticName(key, Registries.BLOCK));
    }
    // endregion

    // region: Item
    public LanguageProvider addItem(Supplier<? extends Item> key, String name)
    {
        return add(key.get(), name);
    }

    public LanguageProvider add(Item key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    public LanguageProvider addItem(Supplier<? extends Item> key)
    {
        return add(key.get());
    }

    public LanguageProvider add(Item key)
    {
        return add(key, getAutomaticName(key, Registries.ITEM));
    }
    // endregion

    // region: ItemStack
    public LanguageProvider addItemStack(Supplier<ItemStack> key, String name)
    {
        return add(key.get(), name);
    }

    public LanguageProvider add(ItemStack key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    public LanguageProvider addItemStack(Supplier<ItemStack> key)
    {
        return add(key.get());
    }

    public LanguageProvider add(ItemStack key)
    {
        return add(key, getAutomaticName(key.getItem(), Registries.ITEM));
    }
    // endregion

    // region: Enchantment
    public LanguageProvider addEnchantment(Supplier<? extends Enchantment> key, String name)
    {
        return add(key.get(), name);
    }

    public LanguageProvider add(Enchantment key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    public LanguageProvider addEnchantment(Supplier<? extends Enchantment> key)
    {
        return add(key.get());
    }

    public LanguageProvider add(Enchantment key)
    {
        return add(key, getAutomaticName(key, Registries.ENCHANTMENT));
    }
    // endregion

    // region: MobEffect
    public LanguageProvider addMobEffect(Supplier<? extends MobEffect> key, String name)
    {
        return add(key.get(), name);
    }

    public LanguageProvider add(MobEffect key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    public LanguageProvider addMobEffect(Supplier<? extends MobEffect> key)
    {
        return add(key.get());
    }

    public LanguageProvider add(MobEffect key)
    {
        return add(key, getAutomaticName(key, Registries.MOB_EFFECT));
    }
    // endregion

    // region: EntityType
    public LanguageProvider addEntityType(Supplier<? extends EntityType<?>> key, String name)
    {
        return add(key.get(), name);
    }

    public LanguageProvider add(EntityType<?> key, String name)
    {
        return add(key.getDescriptionId(), name);
    }

    public LanguageProvider addEntityType(Supplier<? extends EntityType<?>> key)
    {
        return add(key.get());
    }

    public LanguageProvider add(EntityType<?> key)
    {
        return add(key, getAutomaticName(key, Registries.ENTITY_TYPE));
    }
    // endregion

    // region: ItemLike
    public LanguageProvider addItemLike(Supplier<? extends ItemLike> key, String name)
    {
        return add(key.get(), name);
    }

    public LanguageProvider add(ItemLike key, String name)
    {
        return add(key.asItem(), name);
    }

    public LanguageProvider addItemLike(Supplier<? extends ItemLike> key)
    {
        return add(key.get());
    }

    public LanguageProvider add(ItemLike key)
    {
        return add(key, getAutomaticName(key.asItem(), Registries.ITEM));
    }
    // endregion

    public <T> String getAutomaticName(Supplier<? extends T> supplier, ResourceKey<? extends Registry<T>> registryType)
    {
        return getAutomaticName(supplier.get(), registryType);
    }

    public <T> String getAutomaticName(T type, ResourceKey<? extends Registry<T>> registryType)
    {
        var registry = RegistryEntry.getRegistryOrThrow(registryType);
        var registryName = registry.getKey(type);
        Validate.notNull(registryName);
        return toEnglishName(registryName.getPath());
    }
    // endregion

    public static String toEnglishName(String internalName)
    {
        return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }
}
