package xyz.apex.minecraft.apexcore.common.lib.resgen.tag;

import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
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
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegistryHooks;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class TagsProvider<T> implements DataProvider
{
    public static final ProviderType<TagsProvider<Block>> BLOCK = register(new ResourceLocation(ApexCore.ID, "tags/block"), Registries.BLOCK, block -> block.builtInRegistryHolder().key());
    public static final ProviderType<TagsProvider<Fluid>> FLUID = register(new ResourceLocation(ApexCore.ID, "tags/fluid"), Registries.FLUID, fluid -> fluid.builtInRegistryHolder().key(), BLOCK);
    public static final ProviderType<TagsProvider<Item>> ITEM = register(new ResourceLocation(ApexCore.ID, "tags/item"), Registries.ITEM, item -> item.builtInRegistryHolder().key(), BLOCK);
    public static final ProviderType<TagsProvider<Enchantment>> ENCHANTMENT = register(new ResourceLocation(ApexCore.ID, "tags/enchantment"), Registries.ENCHANTMENT, ITEM);
    public static final ProviderType<TagsProvider<EntityType<?>>> ENTITY_TYPE = register(new ResourceLocation(ApexCore.ID, "tags/entity_type"), Registries.ENTITY_TYPE, entityType -> entityType.builtInRegistryHolder().key(), ITEM);
    public static final ProviderType<TagsProvider<BlockEntityType<?>>> BLOCK_ENTITY_TYPE = register(new ResourceLocation(ApexCore.ID, "tags/block_entity_type"), Registries.BLOCK_ENTITY_TYPE, BLOCK);
    public static final ProviderType<TagsProvider<PaintingVariant>> PAINTING_VARIANT = register(new ResourceLocation(ApexCore.ID, "tags/painting_variant"), Registries.PAINTING_VARIANT);
    public static final ProviderType<TagsProvider<PoiType>> POINT_OF_INTEREST_TYPE = register(new ResourceLocation(ApexCore.ID, "tags/point_of_interest_type"), Registries.POINT_OF_INTEREST_TYPE, BLOCK);
    public static final ProviderType<TagsProvider<Biome>> BIOME = register(new ResourceLocation(ApexCore.ID, "tags/biome"), Registries.BIOME);
    public static final ProviderType<TagsProvider<BannerPattern>> BANNER_PATTERN = register(new ResourceLocation(ApexCore.ID, "tags/banner_pattern"), Registries.BANNER_PATTERN);
    public static final ProviderType<TagsProvider<CatVariant>> CAT_VARIANT = register(new ResourceLocation(ApexCore.ID, "tags/cat_variant"), Registries.CAT_VARIANT);
    public static final ProviderType<TagsProvider<DamageType>> DAMAGE_TYPE = register(new ResourceLocation(ApexCore.ID, "tags/damage_type"), Registries.DAMAGE_TYPE);
    public static final ProviderType<TagsProvider<FlatLevelGeneratorPreset>> FLAT_LEVEL_GENERATOR_PRESET = register(new ResourceLocation(ApexCore.ID, "tags/flat_level_generator_preset"), Registries.FLAT_LEVEL_GENERATOR_PRESET);
    public static final ProviderType<TagsProvider<GameEvent>> GAME_EVENT = register(new ResourceLocation(ApexCore.ID, "tags/game_event"), Registries.GAME_EVENT, gameEvent -> gameEvent.builtInRegistryHolder().key());
    public static final ProviderType<TagsProvider<Instrument>> INSTRUMENT = register(new ResourceLocation(ApexCore.ID, "tags/instrument"), Registries.INSTRUMENT);
    public static final ProviderType<TagsProvider<Structure>> STRUCTURE = register(new ResourceLocation(ApexCore.ID, "tags/structure"), Registries.STRUCTURE);
    public static final ProviderType<TagsProvider<WorldPreset>> WORLD_PRESET = register(new ResourceLocation(ApexCore.ID, "tags/world_preset"), Registries.WORLD_PRESET);

    private final ProviderType.ProviderContext context;
    private final ResourceKey<? extends Registry<T>> registryType;
    private final Map<ResourceLocation, TagBuilder<T>> builders = Maps.newLinkedHashMap();
    @Nullable private final Function<T, ResourceKey<T>> intrinsicKeyExtractor;
    private final Supplier<Registry<T>> registry;

    private TagsProvider(ProviderType.ProviderContext context, ResourceKey<? extends Registry<T>> registryType, @Nullable Function<T, ResourceKey<T>> intrinsicKeyExtractor)
    {
        this.context = context;
        this.registryType = registryType;
        this.intrinsicKeyExtractor = intrinsicKeyExtractor;

        registry = Suppliers.memoize(() -> RegistryHooks.findVanillaRegistry(registryType).orElseThrow());
    }

    public TagBuilder<T> tag(TagKey<T> tag)
    {
        return builders.computeIfAbsent(tag.location(), tagName -> new TagBuilder<>(tagName, this::getKey));
    }

    public ResourceKey<T> getKey(T element)
    {
        if(intrinsicKeyExtractor != null)
            return intrinsicKeyExtractor.apply(element);

        return registry.get().getResourceKey(element).orElseThrow();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        var pathProvider = context.packOutput().createPathProvider(PackOutput.Target.DATA_PACK, TagManager.getTagDir(registryType));

        return context.registries()
                      .thenApply(registries -> registries.lookupOrThrow(registryType))
                      .thenCompose(registryLookup -> CompletableFuture.allOf(
                              builders.values().stream().map(builder -> generate(cache, pathProvider, registryLookup, builder)).toArray(CompletableFuture[]::new)
                      ));
    }

    private CompletableFuture<?> generate(CachedOutput cache, PackOutput.PathProvider pathProvider, HolderLookup.RegistryLookup<T> registryLookup, TagBuilder<T> builder)
    {
        var missingTags = builder.entries().stream().filter(Predicate.not(t -> t.verifyIfPresent(
                registryName -> registryLookup.get(ResourceKey.create(registryType, registryName)).isPresent(),
                builders::containsKey
        ))).toList();

        if(!missingTags.isEmpty())
        {
            var missingTagNames = missingTags.stream().map(TagEntry::toString).collect(Collectors.joining(","));
            throw new IllegalArgumentException("Couldn't define tag %s as it is missing the following references: %s".formatted(builder.tagName(), missingTagNames));
        }

        return DataProvider.saveStable(
                cache,
                builder.toJson().getOrThrow(false, ApexCore.LOGGER::error),
                pathProvider.json(builder.tagName())
        );
    }

    @Override
    public String getName()
    {
        return "Tags for %s".formatted(registryType.location());
    }

    public static <T> ProviderType<TagsProvider<T>> register(ResourceLocation providerName, ResourceKey<? extends Registry<T>> registryType, Function<T, ResourceKey<T>> intrinsicKeyExtractor, ProviderType<?>... parents)
    {
        return ProviderType.register(providerName, context -> new TagsProvider<>(context, registryType, intrinsicKeyExtractor), parents);
    }

    public static <T> ProviderType<TagsProvider<T>> register(ResourceLocation providerName, ResourceKey<? extends Registry<T>> registryType, ProviderType<?>... parents)
    {
        return ProviderType.register(providerName, context -> new TagsProvider<>(context, registryType, null), parents);
    }
}
