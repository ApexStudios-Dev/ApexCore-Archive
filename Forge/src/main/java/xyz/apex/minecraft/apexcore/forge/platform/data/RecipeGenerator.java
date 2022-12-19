package xyz.apex.minecraft.apexcore.forge.platform.data;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.data.providers.Recipe;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class RecipeGenerator extends DelegatedProvider<RecipeProvider> implements Recipe
{
    @Nullable private Consumer<FinishedRecipe> consumer;

    RecipeGenerator(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
    {
        super(packOutputSupplier, lookupProviderSupplier, modId, existingFileHelper);
    }

    @Override
    protected RecipeProvider createProvider(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
    {
        return new RecipeProvider(packOutputSupplier.get()) {
            @Override
            protected void buildRecipes(Consumer<FinishedRecipe> consumer)
            {
                RecipeGenerator.this.consumer = consumer;
                Generators.processDataGenerator(modId, ProviderTypes.RECIPES, RecipeGenerator.this);
                RecipeGenerator.this.consumer = null;
            }
        };
    }

    @Override
    public void accept(FinishedRecipe recipe)
    {
        Validate.notNull(consumer);
        consumer.accept(recipe);
    }
}
