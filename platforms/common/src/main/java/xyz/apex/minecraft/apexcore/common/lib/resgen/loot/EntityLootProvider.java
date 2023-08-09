package xyz.apex.minecraft.apexcore.common.lib.resgen.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class EntityLootProvider implements LootTableSubProvider
{
    public static final EntityPredicate.Builder ENTITY_ON_FIRE = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true));
    private static final Set<EntityType<?>> SPECIAL_LOOT_TABLE_TYPES = Set.of(EntityType.PLAYER, EntityType.ARMOR_STAND, EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.VILLAGER);

    private final Map<EntityType<?>, Map<ResourceLocation, LootTable.Builder>> lootTables = Maps.newHashMap();
    private final LootTableProvider provider;

    @ApiStatus.Internal
    EntityLootProvider(LootTableProvider provider)
    {
        this.provider = provider;
    }

    public void add(EntityType<?> entityType, LootTable.Builder builder)
    {
        add(entityType, entityType.getDefaultLootTable(), builder);
    }

    public void add(EntityType<?> entityType, ResourceLocation lootTableId, LootTable.Builder lootTable)
    {
        lootTables.computeIfAbsent(entityType, $ -> Maps.newHashMap()).put(lootTableId, lootTable);
    }

    @Override
    public void generate(String ownerId, BiConsumer<ResourceLocation, LootTable.Builder> consumer)
    {
        var generated = Sets.<ResourceLocation>newHashSet();

        BuiltInRegistries.ENTITY_TYPE.holders().forEach(holder -> {
            var entityType = holder.value();

            if(!entityType.isEnabled(FeatureFlags.VANILLA_SET))
                return;

            var map = lootTables.remove(entityType);

            if(map == null || map.isEmpty())
                return;

            var entityName = holder.key().location();

            if(!canHaveLootTable(entityType))
            {
                var keys = map.keySet().stream().map(ResourceLocation::toString).collect(Collectors.joining(","));
                throw new IllegalStateException("Weird LootTables '%s' for '%s', not a LivingEntity so should not have loot".formatted(keys, entityName));
            }

            var defaultLootTableId = entityType.getDefaultLootTable();

            if(defaultLootTableId.equals(BuiltInLootTables.EMPTY) || !map.containsKey(defaultLootTableId))
                throw new IllegalStateException("Missing LootTable '%s' for '%s'".formatted(defaultLootTableId, entityName));

            map.forEach((lootTableId, lootTable) -> {
                if(!generated.add(lootTableId))
                    throw new IllegalStateException("Duplicate LootTable '%s' for '%s'".formatted(lootTableId, entityName));

                consumer.accept(lootTableId, lootTable);
            });
        });
    }

    private static boolean canHaveLootTable(EntityType<?> entityType)
    {
        return SPECIAL_LOOT_TABLE_TYPES.contains(entityType) || entityType.getCategory() != MobCategory.MISC;
    }
}
