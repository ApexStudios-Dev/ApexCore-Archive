package xyz.apex.minecraft.apexcore.common.lib.resgen.lang;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class LanguageBuilder
{
    private final LanguageProvider provider;
    private final String region;
    private final Map<String, String> translations = Maps.newTreeMap();

    LanguageBuilder(LanguageProvider provider, String region)
    {
        this.provider = provider;
        this.region = region;
    }

    public LanguageBuilder add(String key, String value)
    {
        if(key.isBlank() || value.isBlank())
            return this;

        translations.put(key, value);
        return this;
    }

    public LanguageBuilder add(String type, ResourceLocation id, String value)
    {
        return add(Util.makeDescriptionId(type, id), value);
    }

    public LanguageBuilder add(Block key, String value)
    {
        return add(key.getDescriptionId(), value);
    }

    public LanguageBuilder add(BlockState key, String value)
    {
        return add(key.getBlock(), value);
    }

    public LanguageBuilder add(Item key, String value)
    {
        return add(key.getDescriptionId(), value);
    }

    public LanguageBuilder add(ItemStack key, String value)
    {
        return add(key.getDescriptionId(), value);
    }

    public LanguageBuilder add(ResourceKey<CreativeModeTab> key, String value)
    {
        return add("itemGroup", key.location(), value);
    }

    public LanguageBuilder add(EntityType<?> key, String value)
    {
        return add(key.getDescriptionId(), value);
    }

    public LanguageBuilder add(Enchantment key, String value)
    {
        return add(key.getDescriptionId(), value);
    }

    public LanguageBuilder add(Attribute key, String value)
    {
        return add(key.getDescriptionId(), value);
    }

    public LanguageBuilder add(StatType<?> key, String value)
    {
        return add(key.getTranslationKey(), value);
    }

    public LanguageBuilder add(MobEffect key, String value)
    {
        return add(key.getDescriptionId(), value);
    }

    public LanguageBuilder add(MobEffectInstance key, String value)
    {
        return add(key.getDescriptionId(), value);
    }

    public LanguageBuilder add(GameRules.Key<?> key, String value)
    {
        return add(key.getDescriptionId(), value);
    }

    @Nullable
    public String get(String key)
    {
        return translations.get(key);
    }

    @Nullable
    public String get(String type, ResourceLocation id)
    {
        return get(Util.makeDescriptionId(type, id));
    }

    @Nullable
    public String get(Block key)
    {
        return get(key.getDescriptionId());
    }

    @Nullable
    public String get(BlockState key)
    {
        return get(key.getBlock());
    }

    @Nullable
    public String get(Item key)
    {
        return get(key.getDescriptionId());
    }

    @Nullable
    public String get(ItemStack key)
    {
        return get(key.getDescriptionId());
    }

    @Nullable
    public String get(ResourceKey<CreativeModeTab> key)
    {
        return get("itemGroup", key.location());
    }

    @Nullable
    public String get(EntityType<?> key)
    {
        return get(key.getDescriptionId());
    }

    @Nullable
    public String get(Enchantment key)
    {
        return get(key.getDescriptionId());
    }

    @Nullable
    public String get(Attribute key)
    {
        return get(key.getDescriptionId());
    }

    @Nullable
    public String get(StatType<?> key)
    {
        return get(key.getTranslationKey());
    }

    @Nullable
    public String get(MobEffect key)
    {
        return get(key.getDescriptionId());
    }

    @Nullable
    public String get(MobEffectInstance key)
    {
        return get(key.getDescriptionId());
    }

    @Nullable
    public String get(GameRules.Key<?> key)
    {
        return get(key.getDescriptionId());
    }

    public Set<String> keys()
    {
        return Collections.unmodifiableSet(translations.keySet());
    }

    public Collection<String> values()
    {
        return Collections.unmodifiableCollection(translations.values());
    }

    public LanguageBuilder copyFrom(String sourceRegion)
    {
        provider.copy(sourceRegion, region);
        return this;
    }

    public LanguageBuilder copyFrom(LanguageBuilder sourceRegion)
    {
        provider.copy(sourceRegion.region, region);
        return this;
    }

    public LanguageBuilder copyTo(String targetRegion)
    {
        provider.copy(region, targetRegion);
        return this;
    }

    public LanguageBuilder copyTo(LanguageBuilder targetRegion)
    {
        provider.copy(region, targetRegion.region);
        return this;
    }

    public String region()
    {
        return region;
    }

    public LanguageProvider end()
    {
        return provider;
    }
}
