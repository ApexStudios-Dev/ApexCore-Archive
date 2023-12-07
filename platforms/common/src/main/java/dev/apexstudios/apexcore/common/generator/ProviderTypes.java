package dev.apexstudios.apexcore.common.generator;

import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.generator.tag.TagGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
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
    ProviderType<TagGenerator<BannerPattern>> TAG_BANNER_PATTERN = tag(ApexCore.ID, Registries.BANNER_PATTERN);
    ProviderType<TagGenerator<Biome>> TAG_BIOME = tag(ApexCore.ID, Registries.BIOME);
    ProviderType<TagGenerator<Block>> TAG_BLOCK = tag(ApexCore.ID, Registries.BLOCK);
    ProviderType<TagGenerator<CatVariant>> TAG_CAT_VARIANT = tag(ApexCore.ID, Registries.CAT_VARIANT);
    ProviderType<TagGenerator<DamageType>> TAG_DAMAGE_TYPE = tag(ApexCore.ID, Registries.DAMAGE_TYPE);
    ProviderType<TagGenerator<EntityType<?>>> TAG_ENTITY_TYPE = tag(ApexCore.ID, Registries.ENTITY_TYPE);
    ProviderType<TagGenerator<FlatLevelGeneratorPreset>> TAG_FLAT_LEVEL_GENERATOR_PRESET = tag(ApexCore.ID, Registries.FLAT_LEVEL_GENERATOR_PRESET);
    ProviderType<TagGenerator<Fluid>> TAG_FLUID = tag(ApexCore.ID, Registries.FLUID);
    ProviderType<TagGenerator<GameEvent>> TAG_GAME_EVENT = tag(ApexCore.ID, Registries.GAME_EVENT);
    ProviderType<TagGenerator<Instrument>> TAG_INSTRUMENT = tag(ApexCore.ID, Registries.INSTRUMENT);
    ProviderType<TagGenerator<Item>> TAG_ITEM = tag(ApexCore.ID, Registries.ITEM);
    ProviderType<TagGenerator<PaintingVariant>> TAG_PAINTING_VARIANT = tag(ApexCore.ID, Registries.PAINTING_VARIANT);
    ProviderType<TagGenerator<PoiType>> TAG_POINT_OF_INTEREST_TYPE = tag(ApexCore.ID, Registries.POINT_OF_INTEREST_TYPE);
    ProviderType<TagGenerator<Structure>> TAG_STRUCTURE = tag(ApexCore.ID, Registries.STRUCTURE);
    ProviderType<TagGenerator<WorldPreset>> TAG_WORLD_PRESET = tag(ApexCore.ID, Registries.WORLD_PRESET);
    ProviderType<TagGenerator<BlockEntityType<?>>> TAG_BLOCK_ENTITY_TYPE = tag(ApexCore.ID, Registries.BLOCK_ENTITY_TYPE);

    static <T extends ResourceGenerator> ProviderType<T> register(String ownerId, String providerName, ProviderType.Factory<T> providerFactory)
    {
        return ProviderType.register(ownerId, providerName, providerFactory);
    }

    static <T> ProviderType<TagGenerator<T>> tag(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        return TagGenerator.register(ownerId, registryType);
    }
}
