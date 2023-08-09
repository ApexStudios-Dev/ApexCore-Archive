package xyz.apex.minecraft.apexcore.common.lib.resgen.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LootTableProvider extends net.minecraft.data.loot.LootTableProvider
{
    public static final ProviderType<LootTableProvider> PROVIDER_TYPE = ProviderType.register(new ResourceLocation(ApexCore.ID, "loot_tables"), LootTableProvider::new);
    private static final Map<ResourceLocation, LootType<?>> LOOT_TYPES = Maps.newHashMap();

    private static final Set<Item> VANILLA_EXPLOSION_RESISTANT = Stream.of(
            Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL,
            Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD,
            Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX,
            Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX
    ).map(ItemLike::asItem).collect(Collectors.toUnmodifiableSet());

    private final ProviderType.ProviderContext context;
    private final Set<Item> explosionResistant = Sets.newHashSet();
    private final Map<LootType<?>, LootTableSubProvider> subProviders = Maps.newHashMap();

    private LootTableProvider(ProviderType.ProviderContext context)
    {
        super(context.packOutput(), Set.of(), List.of());

        this.context = context;
    }

    public LootTableProvider withExplosionResistant(ItemLike item)
    {
        explosionResistant.add(item.asItem());
        return this;
    }

    public boolean isExplosionResistant(ItemLike item)
    {
        var test = item.asItem();
        return VANILLA_EXPLOSION_RESISTANT.contains(test) || explosionResistant.contains(test);
    }

    @SuppressWarnings("unchecked")
    public <T extends LootTableSubProvider> T subProvider(LootType<T> lootType)
    {
        return (T) subProviders.computeIfAbsent(lootType, type -> type.createSubProvider(this));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        var lootTables = Maps.<ResourceLocation, LootTable>newHashMap();
        var lootTableSeeds = new Object2ObjectOpenHashMap<RandomSupport.Seed128bit, ResourceLocation>();
        var ownerId = context.ownerId();

        subProviders.forEach((lootType, subProvider) -> {
            var paramSet = lootType.paramSet();

            subProvider.generate(ownerId, (lootTableId, lootTable) -> {
                var existing = lootTableSeeds.put(RandomSequence.seedForKey(lootTableId), lootTableId);

                if(existing != null)
                    Util.logAndPauseIfInIde("Loot table random sequence seed collision on %s and %s".formatted(existing, lootTableId));

                var finalizedLootTable = lootTable
                        .setRandomSequence(lootTableId)
                        .setParamSet(paramSet)
                        .build();

                if(lootTables.put(lootTableId, finalizedLootTable) != null)
                    throw new IllegalStateException("Duplicate loot table %s".formatted(lootTableId));
            });
        });

        var problems = new ValidationContext(LootContextParamSets.ALL_PARAMS, new LootDataResolver() {
            @SuppressWarnings("unchecked")
            @Nullable
            @Override
            public <T> T getElement(LootDataId<T> lootDataId)
            {
                return lootDataId.type() == LootDataType.TABLE ? (T) lootTables.get(lootDataId.location()) : null;
            }
        }).getProblems();

        if(!problems.isEmpty())
        {
            problems.forEach((key, value) -> ApexCore.LOGGER.warn("Found validation problem in {}: {}", key, value));
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        }

        var pathProvider = context.packOutput().createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");

        return CompletableFuture.allOf(lootTables.entrySet().stream().map(entry -> DataProvider.saveStable(
                cache,
                LootTable.CODEC,
                entry.getValue(),
                pathProvider.json(entry.getKey())
        )).toArray(CompletableFuture[]::new));
    }

    public static <T extends LootTableSubProvider> LootType<T> registerLootType(LootContextParamSet paramSet, Function<LootTableProvider, T> subProviderFactory)
    {
        var lootTypeId = LootContextParamSets.REGISTRY.inverse().get(paramSet);
        var lootType = new LootTypeImpl<>(paramSet, lootTypeId, subProviderFactory);

        if(LOOT_TYPES.put(lootTypeId, lootType) != null)
            throw new IllegalStateException("Duplicate LootType registration: %s".formatted(lootTypeId));

        return lootType;
    }

    static final class LootTypeImpl<T extends LootTableSubProvider> implements LootType<T>
    {
        private final LootContextParamSet paramSet;
        private final ResourceLocation lootTypeName;
        private final Function<LootTableProvider, T> subProviderFactory;

        private LootTypeImpl(LootContextParamSet paramSet, ResourceLocation lootTypeName, Function<LootTableProvider, T> subProviderFactory)
        {
            this.paramSet = paramSet;
            this.lootTypeName = lootTypeName;
            this.subProviderFactory = subProviderFactory;
        }

        @Override
        public ResourceLocation lootTypeName()
        {
            return lootTypeName;
        }

        @Override
        public LootContextParamSet paramSet()
        {
            return paramSet;
        }

        @Override
        public T createSubProvider(LootTableProvider provider)
        {
            return subProviderFactory.apply(provider);
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this == obj)
                return true;
            if(!(obj instanceof LootType<?> other))
                return false;
            return paramSet == other.paramSet();
        }

        @Override
        public int hashCode()
        {
            return lootTypeName().hashCode();
        }

        @Override
        public String toString()
        {
            return "LootType{%s}".formatted(lootTypeName());
        }
    }
}
