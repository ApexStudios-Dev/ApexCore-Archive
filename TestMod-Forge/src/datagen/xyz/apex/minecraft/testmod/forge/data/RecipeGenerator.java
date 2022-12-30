package xyz.apex.minecraft.testmod.forge.data;

import com.google.common.collect.ImmutableList;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

import java.util.function.Consumer;

public final class RecipeGenerator extends RecipeProvider
{
    RecipeGenerator(PackOutput output)
    {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer)
    {
        var leadSmeltables = ImmutableList.<ItemLike>of(AllBlocks.LEAD_ORE, AllBlocks.DEEPSLATE_LEAD_ORE, AllItems.RAW_LEAD);

        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, AllItems.LEAD_AXE)
                .define('#', Items.STICK)
                .define('X', AllItems.LEAD_INGOT)
                .pattern("XX")
                .pattern("X#")
                .pattern(" #")
                .unlockedBy("has_lead_ingot", has(AllItems.LEAD_INGOT))
                .save(consumer)
        ;

        ShapedRecipeBuilder
                .shaped(RecipeCategory.COMBAT, AllItems.LEAD_BOOTS)
                .define('X', AllItems.LEAD_INGOT)
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_lead_ingot", has(AllItems.LEAD_INGOT))
                .save(consumer)
        ;

        ShapedRecipeBuilder
                .shaped(RecipeCategory.COMBAT, AllItems.LEAD_CHESTPLATE)
                .define('X', AllItems.LEAD_INGOT)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_lead_ingot", has(AllItems.LEAD_INGOT))
                .save(consumer)
        ;

        ShapedRecipeBuilder
                .shaped(RecipeCategory.COMBAT, AllItems.LEAD_HELMET)
                .define('X', AllItems.LEAD_INGOT)
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy("has_lead_ingot", has(AllItems.LEAD_INGOT))
                .save(consumer)
        ;

        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, AllItems.LEAD_HOE)
                .define('#', Items.STICK)
                .define('X', AllItems.LEAD_INGOT)
                .pattern("XX")
                .pattern(" #")
                .pattern(" #")
                .unlockedBy("has_lead_ingot", has(AllItems.LEAD_INGOT))
                .save(consumer)
        ;

        nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, RecipeCategory.MISC, AllItems.LEAD_INGOT, RecipeCategory.BUILDING_BLOCKS, AllBlocks.LEAD_BLOCK, "lead_ingot_from_lead_block", "lead_ingot");

        nineBlockStorageRecipesWithCustomPacking(consumer, RecipeCategory.MISC, AllItems.LEAD_NUGGET, RecipeCategory.MISC, AllItems.LEAD_INGOT, "lead_ingot_from_nuggets", "lead_ingot");

        ShapedRecipeBuilder
                .shaped(RecipeCategory.COMBAT, AllItems.LEAD_LEGGINGS)
                .define('X', AllItems.LEAD_INGOT)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_lead_ingot", has(AllItems.LEAD_INGOT))
                .save(consumer)
        ;

        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, AllItems.LEAD_PICKAXE)
                .define('#', Items.STICK)
                .define('X', AllItems.LEAD_INGOT)
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .unlockedBy("has_lead_ingot", has(AllItems.LEAD_INGOT))
                .save(consumer)
        ;

        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, AllItems.LEAD_SHOVEL)
                .define('#', Items.STICK)
                .define('X', AllItems.LEAD_INGOT)
                .pattern("X")
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_lead_ingot", has(AllItems.LEAD_INGOT))
                .save(consumer)
        ;

        ShapedRecipeBuilder
                .shaped(RecipeCategory.COMBAT, AllItems.LEAD_SWORD)
                .define('#', Items.STICK)
                .define('X', AllItems.LEAD_INGOT)
                .pattern("X")
                .pattern("X")
                .pattern("#")
                .unlockedBy("has_lead_ingot", has(AllItems.LEAD_INGOT))
                .save(consumer)
        ;

        oreSmelting(consumer, leadSmeltables, RecipeCategory.MISC, AllItems.LEAD_INGOT, .7F, 200, "lead_ingot");

        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, AllItems.RAW_LEAD, RecipeCategory.BUILDING_BLOCKS, AllBlocks.RAW_LEAD_BLOCK);

        SimpleCookingRecipeBuilder
                .smelting(Ingredient.of(AllItems.LEAD_PICKAXE, AllItems.LEAD_SHOVEL, AllItems.LEAD_AXE, AllItems.LEAD_HOE, AllItems.LEAD_SWORD, AllItems.LEAD_HELMET, AllItems.LEAD_CHESTPLATE, AllItems.LEAD_LEGGINGS, AllItems.LEAD_BOOTS, AllItems.LEAD_HORSE_ARMOR), RecipeCategory.MISC, AllItems.LEAD_NUGGET, .1F, 200)
                .unlockedBy("has_lead_pickaxe", has(AllItems.LEAD_PICKAXE))
                .unlockedBy("has_lead_shovel", has(AllItems.LEAD_SHOVEL))
                .unlockedBy("has_lead_axe", has(AllItems.LEAD_AXE))
                .unlockedBy("has_lead_hoe", has(AllItems.LEAD_HOE))
                .unlockedBy("has_lead_sword", has(AllItems.LEAD_SWORD))
                .unlockedBy("has_lead_helmet", has(AllItems.LEAD_HELMET))
                .unlockedBy("has_lead_chestplate", has(AllItems.LEAD_CHESTPLATE))
                .unlockedBy("has_lead_leggings", has(AllItems.LEAD_CHESTPLATE))
                .unlockedBy("has_lead_boots", has(AllItems.LEAD_BOOTS))
                .unlockedBy("has_lead_horse_armor", has(AllItems.LEAD_HORSE_ARMOR))
                .save(consumer, getSmeltingRecipeName(AllItems.LEAD_NUGGET))
        ;

        oreBlasting(consumer, leadSmeltables, RecipeCategory.MISC, AllItems.LEAD_INGOT, .7F, 100, "lead_ingot");

        SimpleCookingRecipeBuilder
                .blasting(Ingredient.of(AllItems.LEAD_PICKAXE, AllItems.LEAD_SHOVEL, AllItems.LEAD_AXE, AllItems.LEAD_HOE, AllItems.LEAD_SWORD, AllItems.LEAD_HELMET, AllItems.LEAD_CHESTPLATE, AllItems.LEAD_LEGGINGS, AllItems.LEAD_BOOTS, AllItems.LEAD_HORSE_ARMOR), RecipeCategory.MISC, AllItems.LEAD_NUGGET, .1F, 100)
                .unlockedBy("has_lead_pickaxe", has(AllItems.LEAD_PICKAXE))
                .unlockedBy("has_lead_shovel", has(AllItems.LEAD_SHOVEL))
                .unlockedBy("has_lead_axe", has(AllItems.LEAD_AXE))
                .unlockedBy("has_lead_hoe", has(AllItems.LEAD_HOE))
                .unlockedBy("has_lead_sword", has(AllItems.LEAD_SWORD))
                .unlockedBy("has_lead_helmet", has(AllItems.LEAD_HELMET))
                .unlockedBy("has_lead_chestplate", has(AllItems.LEAD_CHESTPLATE))
                .unlockedBy("has_lead_leggings", has(AllItems.LEAD_CHESTPLATE))
                .unlockedBy("has_lead_boots", has(AllItems.LEAD_BOOTS))
                .unlockedBy("has_lead_horse_armor", has(AllItems.LEAD_HORSE_ARMOR))
                .save(consumer, getBlastingRecipeName(AllItems.LEAD_NUGGET))
        ;
    }
}
