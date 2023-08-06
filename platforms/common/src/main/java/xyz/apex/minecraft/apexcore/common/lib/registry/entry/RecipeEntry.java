package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

public final class RecipeEntry<T extends Recipe<?>> extends BaseRegistryEntry<RecipeSerializer<T>> implements RecipeType<T>
{
    @ApiStatus.Internal
    public RecipeEntry(AbstractRegistrar<?> registrar, ResourceKey<RecipeSerializer<T>> registryKey)
    {
        super(registrar, registryKey);
    }

    public RegistryEntry<RecipeType<T>> asRecipeType()
    {
        return getSibling(Registries.RECIPE_TYPE);
    }

    public T fromJson(ResourceLocation recipeId, JsonObject json)
    {
        return value().fromJson(recipeId, json);
    }

    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
    {
        return value().fromNetwork(recipeId, buffer);
    }

    public void toNetwork(FriendlyByteBuf buffer, T recipe)
    {
        value().toNetwork(buffer, recipe);
    }

    public static <T extends Recipe<?>> RecipeEntry<T> cast(RegistryEntry<?> registryEntry)
    {
        return RegistryEntry.cast(RecipeEntry.class, registryEntry);
    }
}
