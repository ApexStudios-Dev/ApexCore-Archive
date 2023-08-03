package xyz.apex.minecraft.apexcore.common.lib.resgen.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class BlockLootProvider implements LootTableSubProvider
{
    public static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

    private final Map<ResourceLocation, LootTable.Builder> lootTables = Maps.newHashMap();
    private final LootTableProvider provider;

    @ApiStatus.Internal
    BlockLootProvider(LootTableProvider provider)
    {
        this.provider = provider;
    }

    public <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike item, FunctionUserBuilder<T> function)
    {
        return provider.isExplosionResistant(item) ? function.apply(ApplyExplosionDecay.explosionDecay()) : function.unwrap();
    }

    public <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike item, ConditionUserBuilder<T> condition)
    {
        return provider.isExplosionResistant(item) ? condition.when(ExplosionCondition.survivesExplosion()) : condition.unwrap();
    }

    public LootTable.Builder createSingleItemTable(ItemLike item)
    {
        return LootTable
                .lootTable()
                .withPool(applyExplosionCondition(
                        item,
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1F))
                                .add(LootItem.lootTableItem(item))
                ));
    }

    public LootTable.Builder createSingleItemTableWithSilkTouch(Block block, ItemLike item)
    {
        return createSilkTouchDispatchTable(
                block,
                applyExplosionCondition(
                        block,
                        LootItem.lootTableItem(item)
                )
        );
    }

    public LootTable.Builder createSingleItemTable(ItemLike item, NumberProvider count)
    {
        return LootTable
                .lootTable()
                .withPool(LootPool
                        .lootPool()
                        .setRolls(ConstantValue.exactly(1F))
                        .add(applyExplosionDecay(
                                item,
                                LootItem.lootTableItem(item)
                                        .apply(SetItemCountFunction.setCount(count))
                        ))
                );
    }

    public LootTable.Builder createSingleItemTableWithSilkTouch(Block block, ItemLike item, NumberProvider count)
    {
        return createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(count))
                )
        );
    }

    public LootTable.Builder createSlabItemTable(Block block)
    {
        return LootTable
                .lootTable()
                .withPool(LootPool
                        .lootPool()
                        .setRolls(ConstantValue.exactly(1F))
                        .add(applyExplosionDecay(
                                block,
                                LootItem.lootTableItem(block)
                                        .apply(SetItemCountFunction
                                                .setCount(ConstantValue.exactly(2F))
                                                .when(LootItemBlockStatePropertyCondition
                                                        .hasBlockStateProperties(block)
                                                        .setProperties(StatePropertiesPredicate.Builder
                                                                .properties()
                                                                .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)
                                                        )
                                                )
                                        )
                        ))
                );
    }

    public <T extends Comparable<T> & StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block block, Property<T> property, T value)
    {
        return LootTable
                .lootTable()
                .withPool(applyExplosionCondition(
                        block,
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1F))
                                .add(LootItem
                                        .lootTableItem(block)
                                        .when(LootItemBlockStatePropertyCondition
                                                .hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder
                                                        .properties()
                                                        .hasProperty(property, value)
                                                )
                                        )
                                )
                ));
    }

    public LootTable.Builder createNameableBlockEntityTable(Block block)
    {
        return LootTable
                .lootTable()
                .withPool(applyExplosionCondition(
                        block,
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1F))
                                .add(LootItem
                                        .lootTableItem(block)
                                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                )
                ));
    }

    public void dropOther(Block block, ItemLike drop)
    {
        add(block, createSingleItemTable(drop));
    }

    public void dropSelf(Block block)
    {
        dropOther(block, block);
    }

    public void noDrop(Block block)
    {
        add(block, LootTableSubProvider.noDrop());
    }

    public void add(Block block, Function<Block, LootTable.Builder> factory)
    {
        add(block, factory.apply(block));
    }

    public void add(Block block, LootTable.Builder builder)
    {
        var lootTable = block.getLootTable();

        if(lootTable != null && lootTable != BuiltInLootTables.EMPTY)
            lootTables.put(lootTable, builder);
    }

    @Override
    public void generate(String ownerId, BiConsumer<ResourceLocation, LootTable.Builder> consumer)
    {
        var generated = Sets.<ResourceLocation>newHashSet();

        for(var block : BuiltInRegistries.BLOCK)
        {
            var registryName = BuiltInRegistries.BLOCK.getKey(block);

            if(!registryName.getNamespace().equals(ownerId))
                continue;
            if(!block.isEnabled(FeatureFlags.VANILLA_SET))
                continue;

            var lootTableId = block.getLootTable();

            if(lootTableId == null || lootTableId == BuiltInLootTables.EMPTY)
                continue;
            if(!generated.add(lootTableId))
                continue;

            var lootTable = lootTables.remove(lootTableId);
            consumer.accept(lootTableId, lootTable);
        }

        if(!lootTables.isEmpty())
            throw new IllegalStateException("Created Block LootTables for non-blocks: %s".formatted(lootTables.keySet()));
    }

    public static LootTable.Builder createSelfDropDispatchTable(Block block, LootItemCondition.Builder conditionBuilder, LootPoolEntryContainer.Builder<?> alternativeBuilder)
    {
        return LootTable
                .lootTable()
                .withPool(LootPool
                        .lootPool()
                        .setRolls(ConstantValue.exactly(1F))
                        .add(LootItem
                                .lootTableItem(block)
                                .when(conditionBuilder)
                                .otherwise(alternativeBuilder)
                        )
                );
    }

    public static LootTable.Builder createSilkTouchDispatchTable(Block block, LootPoolEntryContainer.Builder<?> builder)
    {
        return createSelfDropDispatchTable(block, HAS_SILK_TOUCH, builder);
    }

    public static LootTable.Builder createSilkTouchOnlyTable(ItemLike item)
    {
        return LootTable
                .lootTable()
                .withPool(LootPool
                        .lootPool()
                        .when(HAS_SILK_TOUCH)
                        .setRolls(ConstantValue.exactly(1F))
                        .add(LootItem.lootTableItem(item))
                );
    }
}
