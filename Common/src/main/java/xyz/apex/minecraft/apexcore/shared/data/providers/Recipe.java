package xyz.apex.minecraft.apexcore.shared.data.providers;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@ApiStatus.NonExtendable
public interface Recipe extends DataProvider, Consumer<FinishedRecipe>
{
    default void oneToOneConversionRecipe(ItemLike pResult, ItemLike pIngredient, @Nullable String pGroup)
    {
        oneToOneConversionRecipe(pResult, pIngredient, pGroup, 1);
    }

    default void oneToOneConversionRecipe(ItemLike pResult, ItemLike pIngredient, @Nullable String pGroup, int pResultCount)
    {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pResult, pResultCount)
                              .requires(pIngredient)
                              .group(pGroup)
                              .unlockedBy(getHasName(pIngredient), has(pIngredient))
                              .save(this, getConversionRecipeName(pResult, pIngredient));
    }

    default void oreSmelting(List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup)
    {
        oreCooking(RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    default void oreBlasting(List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup)
    {
        oreCooking(RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    default void oreCooking(RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName)
    {
        pIngredients.forEach(itemlike -> SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
                                                                   .group(pGroup)
                                                                   .unlockedBy(getHasName(itemlike), has(itemlike))
                                                                   .save(this, "%s%s_%s".formatted(getItemName(pResult), pRecipeName, getItemName(itemlike))));
    }

    default void netheriteSmithing(Item pIngredientItem, RecipeCategory pCategory, Item pResultItem)
    {
        UpgradeRecipeBuilder.smithing(Ingredient.of(pIngredientItem), Ingredient.of(Items.NETHERITE_INGOT), pCategory, pResultItem)
                            .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                            .save(this, "%s_smithing".formatted(getItemName(pResultItem)));
    }

    default void twoByTwoPacker(RecipeCategory pCategory, ItemLike pPacked, ItemLike pUnpacked)
    {
        ShapedRecipeBuilder.shaped(pCategory, pPacked, 1)
                           .define('#', pUnpacked)
                           .pattern("##")
                           .pattern("##")
                           .unlockedBy(getHasName(pUnpacked), has(pUnpacked))
                           .save(this);
    }

    default void threeByThreePacker(RecipeCategory pCategory, ItemLike pPacked, ItemLike pUnpacked, String pCriterionName)
    {
        ShapelessRecipeBuilder.shapeless(pCategory, pPacked)
                              .requires(pUnpacked, 9)
                              .unlockedBy(pCriterionName, has(pUnpacked))
                              .save(this);
    }

    default void threeByThreePacker(RecipeCategory pCategory, ItemLike pPacked, ItemLike pUnpacked)
    {
        threeByThreePacker(pCategory, pPacked, pUnpacked, getHasName(pUnpacked));
    }

    default void planksFromLog(ItemLike pPlanks, TagKey<Item> pLogs, int pResultCount)
    {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pPlanks, pResultCount)
                              .requires(pLogs)
                              .group("planks")
                              .unlockedBy("has_log", has(pLogs))
                              .save(this);
    }

    default void planksFromLogs(ItemLike pPlanks, TagKey<Item> pLogs, int pResultCount)
    {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pPlanks, pResultCount)
                              .requires(pLogs)
                              .group("planks")
                              .unlockedBy("has_logs", has(pLogs))
                              .save(this);
    }

    default void woodFromLogs(ItemLike pWood, ItemLike pLog)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pWood, 3)
                           .define('#', pLog)
                           .pattern("##")
                           .pattern("##")
                           .group("bark")
                           .unlockedBy("has_log", has(pLog))
                           .save(this);
    }

    default void woodenBoat(ItemLike pBoat, ItemLike pMaterial)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, pBoat)
                           .define('#', pMaterial)
                           .pattern("# #")
                           .pattern("###")
                           .group("boat")
                           .unlockedBy("in_water", insideOf(Blocks.WATER))
                           .save(this);
    }

    default void chestBoat(ItemLike pBoat, ItemLike pMaterial)
    {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, pBoat)
                              .requires(Blocks.CHEST)
                              .requires(pMaterial)
                              .group("chest_boat")
                              .unlockedBy("has_boat", has(ItemTags.BOATS))
                              .save(this);
    }

    static RecipeBuilder buttonBuilder(ItemLike pButton, Ingredient pMaterial)
    {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, pButton).requires(pMaterial);
    }

    static RecipeBuilder doorBuilder(ItemLike pDoor, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pDoor, 3)
                                  .define('#', pMaterial)
                                  .pattern("##")
                                  .pattern("##")
                                  .pattern("##");
    }

    static RecipeBuilder fenceBuilder(ItemLike pFence, Ingredient pMaterial)
    {
        var i = pFence == Blocks.NETHER_BRICK_FENCE ? 6 : 3;
        var item = pFence == Blocks.NETHER_BRICK_FENCE ? Items.NETHER_BRICK : Items.STICK;

        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pFence, i)
                                  .define('W', pMaterial)
                                  .define('#', item)
                                  .pattern("W#W")
                                  .pattern("W#W");
    }

    static RecipeBuilder fenceGateBuilder(ItemLike pFenceGate, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pFenceGate)
                                  .define('#', Items.STICK)
                                  .define('W', pMaterial)
                                  .pattern("#W#")
                                  .pattern("#W#");
    }

    default void pressurePlate(ItemLike pPressurePlate, ItemLike pMaterial)
    {
        pressurePlateBuilder(RecipeCategory.REDSTONE, pPressurePlate, Ingredient.of(pMaterial)).unlockedBy(getHasName(pMaterial), has(pMaterial))
                                                                                               .save(this);
    }

    static RecipeBuilder pressurePlateBuilder(RecipeCategory pCategory, ItemLike pPressurePlate, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(pCategory, pPressurePlate).define('#', pMaterial).pattern("##");
    }

    default void slab(RecipeCategory pCategory, ItemLike pPressurePlate, ItemLike pMaterial)
    {
        slabBuilder(pCategory, pPressurePlate, Ingredient.of(pMaterial)).unlockedBy(getHasName(pMaterial), has(pMaterial))
                                                                        .save(this);
    }

    static RecipeBuilder slabBuilder(RecipeCategory pCategory, ItemLike pSlab, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(pCategory, pSlab, 6).define('#', pMaterial).pattern("###");
    }

    static RecipeBuilder stairBuilder(ItemLike pStairs, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pStairs, 4)
                                  .define('#', pMaterial)
                                  .pattern("#  ")
                                  .pattern("## ")
                                  .pattern("###");
    }

    static RecipeBuilder trapdoorBuilder(ItemLike pTrapdoor, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pTrapdoor, 2)
                                  .define('#', pMaterial)
                                  .pattern("###")
                                  .pattern("###");
    }

    static RecipeBuilder signBuilder(ItemLike pSign, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pSign, 3)
                                  .group("sign")
                                  .define('#', pMaterial)
                                  .define('X', Items.STICK)
                                  .pattern("###")
                                  .pattern("###")
                                  .pattern(" X ");
    }

    default void hangingSign(ItemLike pSign, ItemLike pMaterial)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pSign, 6)
                           .group("hanging_sign")
                           .define('#', pMaterial)
                           .define('X', Items.CHAIN)
                           .pattern("X X")
                           .pattern("###")
                           .pattern("###")
                           .unlockedBy("has_stripped_logs", has(pMaterial))
                           .save(this);
    }

    default void coloredWoolFromWhiteWoolAndDye(ItemLike pDyedWool, ItemLike pDye)
    {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pDyedWool)
                              .requires(pDye)
                              .requires(Blocks.WHITE_WOOL)
                              .group("wool")
                              .unlockedBy("has_white_wool", has(Blocks.WHITE_WOOL))
                              .save(this);
    }

    default void carpet(ItemLike pCarpet, ItemLike pMaterial)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pCarpet, 3)
                           .define('#', pMaterial)
                           .pattern("##")
                           .group("carpet")
                           .unlockedBy(getHasName(pMaterial), has(pMaterial))
                           .save(this);
    }

    default void coloredCarpetFromWhiteCarpetAndDye(ItemLike pDyedCarpet, ItemLike pDye)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pDyedCarpet, 8)
                           .define('#', Blocks.WHITE_CARPET)
                           .define('$', pDye)
                           .pattern("###")
                           .pattern("#$#")
                           .pattern("###")
                           .group("carpet")
                           .unlockedBy("has_white_carpet", has(Blocks.WHITE_CARPET))
                           .unlockedBy(getHasName(pDye), has(pDye))
                           .save(this, getConversionRecipeName(pDyedCarpet, Blocks.WHITE_CARPET));
    }

    default void bedFromPlanksAndWool(ItemLike pBed, ItemLike pWool)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pBed)
                           .define('#', pWool)
                           .define('X', ItemTags.PLANKS)
                           .pattern("###")
                           .pattern("XXX")
                           .group("bed")
                           .unlockedBy(getHasName(pWool), has(pWool))
                           .save(this);
    }

    default void bedFromWhiteBedAndDye(ItemLike pDyedBed, ItemLike pDye)
    {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, pDyedBed)
                              .requires(Items.WHITE_BED)
                              .requires(pDye)
                              .group("dyed_bed")
                              .unlockedBy("has_bed", has(Items.WHITE_BED))
                              .save(this, getConversionRecipeName(pDyedBed, Items.WHITE_BED));
    }

    default void banner(ItemLike pBanner, ItemLike pMaterial)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pBanner)
                           .define('#', pMaterial)
                           .define('|', Items.STICK)
                           .pattern("###")
                           .pattern("###")
                           .pattern(" | ")
                           .group("banner")
                           .unlockedBy(getHasName(pMaterial), has(pMaterial))
                           .save(this);
    }

    default void stainedGlassFromGlassAndDye(ItemLike pStainedGlass, ItemLike pDye)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pStainedGlass, 8)
                           .define('#', Blocks.GLASS)
                           .define('X', pDye)
                           .pattern("###")
                           .pattern("#X#")
                           .pattern("###")
                           .group("stained_glass")
                           .unlockedBy("has_glass", has(Blocks.GLASS))
                           .save(this);
    }

    default void stainedGlassPaneFromStainedGlass(ItemLike pStainedGlassPane, ItemLike pStainedGlass)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pStainedGlassPane, 16)
                           .define('#', pStainedGlass)
                           .pattern("###")
                           .pattern("###")
                           .group("stained_glass_pane")
                           .unlockedBy("has_glass", has(pStainedGlass))
                           .save(this);
    }

    default void stainedGlassPaneFromGlassPaneAndDye(ItemLike pStainedGlassPane, ItemLike pDye)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pStainedGlassPane, 8)
                           .define('#', Blocks.GLASS_PANE)
                           .define('$', pDye)
                           .pattern("###")
                           .pattern("#$#")
                           .pattern("###")
                           .group("stained_glass_pane")
                           .unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE))
                           .unlockedBy(getHasName(pDye), has(pDye))
                           .save(this, getConversionRecipeName(pStainedGlassPane, Blocks.GLASS_PANE));
    }

    default void coloredTerracottaFromTerracottaAndDye(ItemLike pColoredTerracotta, ItemLike pDye)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pColoredTerracotta, 8)
                           .define('#', Blocks.TERRACOTTA)
                           .define('X', pDye)
                           .pattern("###")
                           .pattern("#X#")
                           .pattern("###")
                           .group("stained_terracotta")
                           .unlockedBy("has_terracotta", has(Blocks.TERRACOTTA))
                           .save(this);
    }

    default void concretePowder(ItemLike pDyedConcretePowder, ItemLike pDye)
    {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pDyedConcretePowder, 8)
                              .requires(pDye)
                              .requires(Blocks.SAND, 4)
                              .requires(Blocks.GRAVEL, 4)
                              .group("concrete_powder")
                              .unlockedBy("has_sand", has(Blocks.SAND))
                              .unlockedBy("has_gravel", has(Blocks.GRAVEL))
                              .save(this);
    }

    default void candle(ItemLike pCandle, ItemLike pDye)
    {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, pCandle)
                              .requires(Blocks.CANDLE)
                              .requires(pDye)
                              .group("dyed_candle")
                              .unlockedBy(getHasName(pDye), has(pDye))
                              .save(this);
    }

    default void wall(RecipeCategory pCategory, ItemLike pWall, ItemLike pMaterial)
    {
        wallBuilder(pCategory, pWall, Ingredient.of(pMaterial)).unlockedBy(getHasName(pMaterial), has(pMaterial))
                                                               .save(this);
    }

    static RecipeBuilder wallBuilder(RecipeCategory pCategory, ItemLike pWall, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(pCategory, pWall, 6).define('#', pMaterial).pattern("###").pattern("###");
    }

    default void polished(RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial)
    {
        polishedBuilder(pCategory, pResult, Ingredient.of(pMaterial)).unlockedBy(getHasName(pMaterial), has(pMaterial))
                                                                     .save(this);
    }

    static RecipeBuilder polishedBuilder(RecipeCategory pCategory, ItemLike pResult, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(pCategory, pResult, 4).define('S', pMaterial).pattern("SS").pattern("SS");
    }

    default void cut(RecipeCategory pCategory, ItemLike pCutResult, ItemLike pMaterial)
    {
        cutBuilder(pCategory, pCutResult, Ingredient.of(pMaterial)).unlockedBy(getHasName(pMaterial), has(pMaterial))
                                                                   .save(this);
    }

    static ShapedRecipeBuilder cutBuilder(RecipeCategory pCategory, ItemLike pCutResult, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(pCategory, pCutResult, 4).define('#', pMaterial).pattern("##").pattern("##");
    }

    default void chiseled(RecipeCategory pCategory, ItemLike pChiseledResult, ItemLike pMaterial)
    {
        chiseledBuilder(pCategory, pChiseledResult, Ingredient.of(pMaterial)).unlockedBy(getHasName(pMaterial), has(pMaterial))
                                                                             .save(this);
    }

    default void mosaicBuilder(RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial)
    {
        ShapedRecipeBuilder.shaped(pCategory, pResult)
                           .define('#', pMaterial)
                           .pattern("#")
                           .pattern("#")
                           .unlockedBy(getHasName(pMaterial), has(pMaterial))
                           .save(this);
    }

    static ShapedRecipeBuilder chiseledBuilder(RecipeCategory pCategory, ItemLike pChiseledResult, Ingredient pMaterial)
    {
        return ShapedRecipeBuilder.shaped(pCategory, pChiseledResult).define('#', pMaterial).pattern("#").pattern("#");
    }

    default void stonecutterResultFromBase(RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial)
    {
        stonecutterResultFromBase(pCategory, pResult, pMaterial, 1);
    }

    default void stonecutterResultFromBase(RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial, int pResultCount)
    {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(pMaterial), pCategory, pResult, pResultCount)
                               .unlockedBy(getHasName(pMaterial), has(pMaterial))
                               .save(this, getConversionRecipeName(pResult, pMaterial) + "_stonecutting");
    }

    default void smeltingResultFromBase(ItemLike pResult, ItemLike pIngredient)
    {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(pIngredient), RecipeCategory.BUILDING_BLOCKS, pResult, 0.1F, 200)
                                  .unlockedBy(getHasName(pIngredient), has(pIngredient))
                                  .save(this);
    }

    default void nineBlockStorageRecipes(RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked)
    {
        nineBlockStorageRecipes(pUnpackedCategory, pUnpacked, pPackedCategory, pPacked, getSimpleRecipeName(pPacked), (String) null, getSimpleRecipeName(pUnpacked), (String) null);
    }

    default void nineBlockStorageRecipesWithCustomPacking(RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked, String pPackedName, String pPackedGroup)
    {
        nineBlockStorageRecipes(pUnpackedCategory, pUnpacked, pPackedCategory, pPacked, pPackedName, pPackedGroup, getSimpleRecipeName(pUnpacked), null);
    }

    default void nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked, String pUnpackedName, String pUnpackedGroup)
    {
        nineBlockStorageRecipes(pUnpackedCategory, pUnpacked, pPackedCategory, pPacked, getSimpleRecipeName(pPacked), (String) null, pUnpackedName, pUnpackedGroup);
    }

    default void nineBlockStorageRecipes(RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked, String pPackedName, @Nullable String pPackedGroup, String pUnpackedName, @Nullable String pUnpackedGroup)
    {
        ShapelessRecipeBuilder.shapeless(pUnpackedCategory, pUnpacked, 9)
                              .requires(pPacked)
                              .group(pUnpackedGroup)
                              .unlockedBy(getHasName(pPacked), has(pPacked))
                              .save(this, new ResourceLocation(pUnpackedName));
        ShapedRecipeBuilder.shaped(pPackedCategory, pPacked)
                           .define('#', pUnpacked)
                           .pattern("###")
                           .pattern("###")
                           .pattern("###")
                           .group(pPackedGroup)
                           .unlockedBy(getHasName(pUnpacked), has(pUnpacked))
                           .save(this, new ResourceLocation(pPackedName));
    }

    default void simpleCookingRecipe(String pCookingMethod, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, int pCookingTime, ItemLike pIngredient, ItemLike pResult, float pExperience)
    {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(pIngredient), RecipeCategory.FOOD, pResult, pExperience, pCookingTime, pCookingSerializer)
                                  .unlockedBy(getHasName(pIngredient), has(pIngredient))
                                  .save(this, "%s_from_%s".formatted(getItemName(pResult), pCookingMethod));
    }

    static EnterBlockTrigger.TriggerInstance insideOf(Block pBlock)
    {
        return new EnterBlockTrigger.TriggerInstance(EntityPredicate.Composite.ANY, pBlock, StatePropertiesPredicate.ANY);
    }

    static InventoryChangeTrigger.TriggerInstance has(MinMaxBounds.Ints pCount, ItemLike pItem)
    {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItem).withCount(pCount).build());
    }

    static InventoryChangeTrigger.TriggerInstance has(ItemLike pItemLike)
    {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
    }

    static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> pTag)
    {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pTag).build());
    }

    static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... pPredicates)
    {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, pPredicates);
    }

    static String getHasName(ItemLike pItemLike)
    {
        return "has_%s".formatted(getItemName(pItemLike));
    }

    static String getItemName(ItemLike pItemLike)
    {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem()).getPath();
    }

    static String getSimpleRecipeName(ItemLike pItemLike)
    {
        return getItemName(pItemLike);
    }

    static String getConversionRecipeName(ItemLike pResult, ItemLike pIngredient)
    {
        return "%s_from_%s".formatted(getItemName(pResult), getItemName(pIngredient));
    }

    static String getSmeltingRecipeName(ItemLike pItemLike)
    {
        return "%s_from_smelting".formatted(getItemName(pItemLike));
    }

    static String getBlastingRecipeName(ItemLike pItemLike)
    {
        return "%s_from_blasting".formatted(getItemName(pItemLike));
    }
}
