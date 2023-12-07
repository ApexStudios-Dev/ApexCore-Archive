package dev.apexstudios.apexcore.common.util;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class TagHelper<T>
{
    private static final Map<ResourceKey<? extends Registry<?>>, TagHelper<?>> HELPERS = Maps.newHashMap();

    public static final TagHelper<BannerPattern> BANNER_PATTERN = helper(Registries.BANNER_PATTERN);
    public static final TagHelper<Biome> BIOME = helper(Registries.BIOME);
    public static final TagHelper<Block> BLOCK = helper(Registries.BLOCK);
    public static final TagHelper<CatVariant> CAT_VARIANT = helper(Registries.CAT_VARIANT);
    public static final TagHelper<DamageType> DAMAGE_TYPE = helper(Registries.DAMAGE_TYPE);
    public static final TagHelper<EntityType<?>> ENTITY_TYPE = helper(Registries.ENTITY_TYPE);
    public static final TagHelper<FlatLevelGeneratorPreset> FLAT_LEVEL_GENERATOR_PRESET = helper(Registries.FLAT_LEVEL_GENERATOR_PRESET);
    public static final TagHelper<Fluid> FLUID = helper(Registries.FLUID);
    public static final TagHelper<GameEvent> GAME_EVENT = helper(Registries.GAME_EVENT);
    public static final TagHelper<Instrument> INSTRUMENT = helper(Registries.INSTRUMENT);
    public static final TagHelper<Item> ITEM = helper(Registries.ITEM);
    public static final TagHelper<PaintingVariant> PAINTING_VARIANT = helper(Registries.PAINTING_VARIANT);
    public static final TagHelper<PoiType> POINT_OF_INTEREST_TYPE = helper(Registries.POINT_OF_INTEREST_TYPE);
    public static final TagHelper<Structure> STRUCTURE = helper(Registries.STRUCTURE);
    public static final TagHelper<WorldPreset> WORLD_PRESET = helper(Registries.WORLD_PRESET);
    public static final TagHelper<BlockEntityType<?>> BLOCK_ENTITY_TYPE = helper(Registries.BLOCK_ENTITY_TYPE);

    private final ResourceKey<? extends Registry<T>> registryType;

    private TagHelper(ResourceKey<? extends Registry<T>> registryType)
    {
        this.registryType = registryType;
    }

    public TagKey<T> create(ResourceLocation tagName)
    {
        return TagKey.create(registryType, tagName);
    }

    public TagKey<T> create(String namespace, String tagName)
    {
        return create(new ResourceLocation(namespace, tagName));
    }

    public PlatformTag.Builder<T> platformBuilder(ResourceLocation defaultTagName)
    {
        return PlatformTag.builder(registryType, defaultTagName);
    }

    public PlatformTag.Builder<T> platformBuilder(String defaultTagNamespace, String defaultTagName)
    {
        return PlatformTag.builder(registryType, defaultTagNamespace, defaultTagName);
    }

    public PlatformTag.Builder<T> platformBuilder(String defaultTagName)
    {
        return PlatformTag.builder(registryType, defaultTagName);
    }

    public static <T> TagHelper<T> helper(ResourceKey<? extends Registry<T>> registryType)
    {
        return (TagHelper<T>) HELPERS.computeIfAbsent(registryType, $ -> new TagHelper<>(registryType));
    }

    public static <T> boolean isIn(TagKey<T> tag, @Nullable T element)
    {
        return isIn(null, tag, element);
    }

    public static <T> boolean isIn(@Nullable RegistryAccess registryAccess, TagKey<T> tag, @Nullable T element)
    {
        if(element == null)
            return false;

        var registryOpt = registryAccess == null ? BuiltInRegistries.REGISTRY.getOptional(tag.registry().registry()) : registryAccess.registry(tag.registry());

        if(registryOpt.isEmpty())
            return false;

        var registry = (Registry<T>) registryOpt.get();

        if(!tag.isFor(registry.key()))
            return false;

        return registry.getResourceKey(element).map(registryKey -> registry.getHolderOrThrow(registryKey).is(tag)).orElse(false);

    }
}
