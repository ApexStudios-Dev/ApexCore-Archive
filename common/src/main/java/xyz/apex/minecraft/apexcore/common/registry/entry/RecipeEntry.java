package xyz.apex.minecraft.apexcore.common.registry.entry;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.registry.RegistryManager;

public final class RecipeEntry<T extends Recipe<?>> extends RegistryEntry<RecipeSerializer<T>> implements RecipeSerializer<T>, RecipeType<T>
{
    private final RegistryEntry<RecipeType<T>> recipeType;

    private RecipeEntry(ResourceLocation registryName)
    {
        super(Registries.RECIPE_SERIALIZER, registryName);

        recipeType = RegistryManager.get(registryName.getNamespace()).getRegistry(Registries.RECIPE_TYPE).register(registryName.getPath(), () -> this);
    }

    public RegistryEntry<RecipeType<T>> asRecipeType()
    {
        return recipeType;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe)
    {
        return get().fromJson(recipeId, serializedRecipe);
    }

    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
    {
        return get().fromNetwork(recipeId, buffer);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe)
    {
        get().toNetwork(buffer, recipe);
    }
}
