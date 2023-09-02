package xyz.apex.minecraft.apexcore.common.lib.resgen;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class RecipeProvider implements DataProvider, RecipeOutput
{
    public static final ProviderType<RecipeProvider> PROVIDER_TYPE = ProviderType.register(new ResourceLocation(ApexCore.ID, "recipes"), RecipeProvider::new);

    private final ProviderType.ProviderContext context;
    private final List<FinishedRecipe> recipes = Lists.newArrayList();

    private RecipeProvider(ProviderType.ProviderContext context)
    {
        this.context = context;
    }

    public EnterBlockTrigger.TriggerInstance insideOf(Block block, StatePropertiesPredicate statePropertiesPredicate)
    {
        return new EnterBlockTrigger.TriggerInstance(Optional.empty(), block, Optional.of(statePropertiesPredicate));
    }

    public Criterion<EnterBlockTrigger.TriggerInstance> insideOf(Block block)
    {
        return EnterBlockTrigger.TriggerInstance.entersBlock(block);
    }

    public InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate.Builder... builders)
    {
        return inventoryTrigger(Stream.of(builders).map(ItemPredicate.Builder::build).toArray(ItemPredicate[]::new));
    }

    public InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... predicates)
    {
        return new InventoryChangeTrigger.TriggerInstance(Optional.empty(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, List.of(predicates));
    }

    public InventoryChangeTrigger.TriggerInstance has(MinMaxBounds.Ints count, ItemLike item)
    {
        return inventoryTrigger(ItemPredicate.Builder.item().of(item).withCount(count));
    }

    public InventoryChangeTrigger.TriggerInstance has(ItemLike item)
    {
        return inventoryTrigger(ItemPredicate.Builder.item().of(item));
    }

    public InventoryChangeTrigger.TriggerInstance has(TagKey<Item> tag)
    {
        return inventoryTrigger(ItemPredicate.Builder.item().of(tag));
    }

    @Override
    public void accept(FinishedRecipe recipe)
    {
        recipes.add(recipe);
    }

    @Override
    public Advancement.Builder advancement()
    {
        return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        var futures = Lists.<CompletableFuture<?>>newArrayList();
        var set = Sets.<ResourceLocation>newHashSet();

        var packOutput = context.packOutput();
        var recipePaths = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "recipes");
        var advancementPaths = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");

        recipes.forEach(recipe -> {
            var recipeId = recipe.id();

            if(!set.add(recipeId))
                throw new IllegalStateException("Duplicate recipe: %s".formatted(recipeId));

            futures.add(DataProvider.saveStable(cache, recipe.serializeRecipe(), recipePaths.json(recipeId)));

            var advancement = recipe.advancement();

            if(advancement != null)
                futures.add(DataProvider.saveStable(cache, advancement.value().serializeToJson(), advancementPaths.json(advancement.id())));
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName()
    {
        return "Recipes";
    }
}
