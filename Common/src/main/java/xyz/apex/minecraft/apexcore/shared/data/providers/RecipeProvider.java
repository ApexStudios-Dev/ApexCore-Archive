package xyz.apex.minecraft.apexcore.shared.data.providers;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;

import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;

import java.util.function.Consumer;

public final class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements Consumer<FinishedRecipe>
{
    private Consumer<FinishedRecipe> consumer;
    private final String modId;

    @ApiStatus.Internal
    public RecipeProvider(PackOutput output, String modId)
    {
        super(output);

        this.modId = modId;
    }

    @Override // protected in common/forge | public in fabric | public here to match visiblity across all platforms
    public void buildRecipes(Consumer<FinishedRecipe> consumer)
    {
        this.consumer = consumer;
        Generators.processDataGenerator(modId, ProviderTypes.RECIPES, this);
        consumer = null;
    }

    @Override
    public void accept(FinishedRecipe recipe)
    {
        Validate.notNull(consumer);
        consumer.accept(recipe);
    }
}
