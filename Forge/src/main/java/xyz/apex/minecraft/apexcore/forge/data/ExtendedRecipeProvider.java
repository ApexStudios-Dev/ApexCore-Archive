package xyz.apex.minecraft.apexcore.forge.data;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public abstract class ExtendedRecipeProvider extends RecipeProvider implements xyz.apex.minecraft.apexcore.shared.data.providers.RecipeProvider
{
    @Nullable private Consumer<FinishedRecipe> consumer;

    public ExtendedRecipeProvider(PackOutput output)
    {
        super(output);
    }

    protected abstract void registerRecipes();

    @Override
    protected final void buildRecipes(Consumer<FinishedRecipe> consumer)
    {
        this.consumer = consumer;
        registerRecipes();
        consumer = null;
    }

    @Override
    public final void accept(FinishedRecipe recipe)
    {
        Validate.notNull(consumer);
        consumer.accept(recipe);
    }
}
