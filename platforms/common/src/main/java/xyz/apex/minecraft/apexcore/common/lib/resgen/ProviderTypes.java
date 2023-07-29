package xyz.apex.minecraft.apexcore.common.lib.resgen;

import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.resgen.lang.LanguageProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.model.ModelProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.state.BlockStateProvider;
import xyz.apex.minecraft.apexcore.common.lib.resgen.tag.TagsProvider;

@ApiStatus.NonExtendable
public interface ProviderTypes
{
    // Client
    ProviderType<BlockStateProvider> BLOCK_STATES = BlockStateProvider.PROVIDER_TYPE;
    ProviderType<ModelProvider> MODELS = ModelProvider.PROVIDER_TYPE;
    ProviderType<LanguageProvider> LANGUAGES = LanguageProvider.PROVIDER_TYPE;

    // Server
    ProviderType<RecipeProvider> RECIPES = RecipeProvider.PROVIDER_TYPE;
    ProviderType<AdvancementProvider> ADVANCEMENTS = AdvancementProvider.PROVIDER_TYPE;

    ProviderType<TagsProvider<Block>> BLOCK_TAGS = TagsProvider.BLOCK;
    ProviderType<TagsProvider<Fluid>> FLUID_TAGS = TagsProvider.FLUID;
    ProviderType<TagsProvider<Item>> ITEM_TAGS = TagsProvider.ITEM;
    ProviderType<TagsProvider<Enchantment>> ENCHANTMENT_TAGS = TagsProvider.ENCHANTMENT;
    ProviderType<TagsProvider<EntityType<?>>> ENTITY_TYPE_TAGS = TagsProvider.ENTITY_TYPE;
    ProviderType<TagsProvider<BlockEntityType<?>>> BLOCK_ENTITY_TYPE_TAGS = TagsProvider.BLOCK_ENTITY_TYPE;
    ProviderType<TagsProvider<PaintingVariant>> PAINTING_VARIANT_TAGS = TagsProvider.PAINTING_VARIANT;
    ProviderType<TagsProvider<PoiType>> POINT_OF_INTEREST_TYPE_TAGS = TagsProvider.POINT_OF_INTEREST_TYPE;
    ProviderType<TagsProvider<Biome>> BIOME_TAGS = TagsProvider.BIOME;
    ProviderType<TagsProvider<BannerPattern>> BANNER_PATTERN_TAGS = TagsProvider.BANNER_PATTERN;
    ProviderType<TagsProvider<CatVariant>> CAT_VARIANT_TAGS = TagsProvider.CAT_VARIANT;
    ProviderType<TagsProvider<DamageType>> DAMAGE_TYPE_TAGS = TagsProvider.DAMAGE_TYPE;
    ProviderType<TagsProvider<FlatLevelGeneratorPreset>> FLAT_LEVEL_GENERATOR_PRESET_TAGS = TagsProvider.FLAT_LEVEL_GENERATOR_PRESET;
    ProviderType<TagsProvider<GameEvent>> GAME_EVENT_TAGS = TagsProvider.GAME_EVENT;
    ProviderType<TagsProvider<Instrument>> INSTRUMENT_TAGS = TagsProvider.INSTRUMENT;
    ProviderType<TagsProvider<Structure>> STRUCTURE_TAGS = TagsProvider.STRUCTURE;
    ProviderType<TagsProvider<WorldPreset>> WORLD_PRESET_TAGS = TagsProvider.WORLD_PRESET;

    @ApiStatus.Internal
    static void bootstrap()
    {
    }
}
