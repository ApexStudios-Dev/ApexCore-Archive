package dev.apexstudios.apexcore.common.generator;

import dev.apexstudios.apexcore.common.generator.common.LanguageGenerator;
import dev.apexstudios.apexcore.common.generator.common.PackMetaGenerator;
import dev.apexstudios.apexcore.common.generator.sound.SoundGenerator;
import dev.apexstudios.apexcore.common.generator.tag.TagGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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

public interface ProviderTypes
{
    ProviderType<PackMetaGenerator> PACK_META = PackMetaGenerator.PROVIDER_TYPE;
    ProviderType<LanguageGenerator> LANGUAGE = LanguageGenerator.PROVIDER_TYPE;
    ProviderType<SoundGenerator> SOUND = SoundGenerator.PROVIDER_TYPE;

    ProviderType<TagGenerator<BannerPattern>> TAG_BANNER_PATTERN = tag(Registries.BANNER_PATTERN);
    ProviderType<TagGenerator<Biome>> TAG_BIOME = tag(Registries.BIOME);
    ProviderType<TagGenerator<Block>> TAG_BLOCK = tag(Registries.BLOCK);
    ProviderType<TagGenerator<CatVariant>> TAG_CAT_VARIANT = tag(Registries.CAT_VARIANT);
    ProviderType<TagGenerator<DamageType>> TAG_DAMAGE_TYPE = tag(Registries.DAMAGE_TYPE);
    ProviderType<TagGenerator<EntityType<?>>> TAG_ENTITY_TYPE = tag(Registries.ENTITY_TYPE);
    ProviderType<TagGenerator<FlatLevelGeneratorPreset>> TAG_FLAT_LEVEL_GENERATOR_PRESET = tag(Registries.FLAT_LEVEL_GENERATOR_PRESET);
    ProviderType<TagGenerator<Fluid>> TAG_FLUID = tag(Registries.FLUID);
    ProviderType<TagGenerator<GameEvent>> TAG_GAME_EVENT = tag(Registries.GAME_EVENT);
    ProviderType<TagGenerator<Instrument>> TAG_INSTRUMENT = tag(Registries.INSTRUMENT);
    ProviderType<TagGenerator<Item>> TAG_ITEM = tag(Registries.ITEM);
    ProviderType<TagGenerator<PaintingVariant>> TAG_PAINTING_VARIANT = tag(Registries.PAINTING_VARIANT);
    ProviderType<TagGenerator<PoiType>> TAG_POINT_OF_INTEREST_TYPE = tag(Registries.POINT_OF_INTEREST_TYPE);
    ProviderType<TagGenerator<Structure>> TAG_STRUCTURE = tag(Registries.STRUCTURE);
    ProviderType<TagGenerator<WorldPreset>> TAG_WORLD_PRESET = tag(Registries.WORLD_PRESET);
    ProviderType<TagGenerator<BlockEntityType<?>>> TAG_BLOCK_ENTITY_TYPE = tag(Registries.BLOCK_ENTITY_TYPE);

    static <T extends ResourceGenerator> ProviderType<T> register(String ownerId, String providerName, ProviderType.Factory<T> providerFactory)
    {
        return ProviderType.register(ownerId, providerName, providerFactory);
    }

    static <T> ProviderType<TagGenerator<T>> tag(ResourceKey<? extends Registry<T>> registryType)
    {
        return TagGenerator.getOrRegister(registryType);
    }

    static void addDefaultPackMetadata(String ownerId, String packName)
    {
        var description = ProviderTypes.addTranslation(ownerId, "pack.%s.description".formatted(ownerId), packName);
        ProviderTypes.PACK_META.addListener(ownerId, pack -> pack.with(builder -> builder.description(description)));
    }

    static void addDefaultPackMetadata(String ownerId)
    {
        addDefaultPackMetadata(ownerId, "Client/Server resources for mod: %s".formatted(ownerId));
    }

    static Component addTranslation(String ownerId, String key, String value)
    {
        LANGUAGE.addListener(ownerId, lang -> lang.with(key, value));
        return Component.translatable(key);
    }
}
