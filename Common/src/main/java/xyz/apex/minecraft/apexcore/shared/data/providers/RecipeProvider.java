package xyz.apex.minecraft.apexcore.shared.data.providers;

import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public interface RecipeProvider extends DataProvider, Consumer<FinishedRecipe>
{}
