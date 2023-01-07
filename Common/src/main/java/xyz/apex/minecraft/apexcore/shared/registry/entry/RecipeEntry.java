package xyz.apex.minecraft.apexcore.shared.registry.entry;

import com.google.gson.JsonObject;
import dev.architectury.registry.registries.RegistrySupplier;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

public final class RecipeEntry<R extends Recipe<?>> extends RegistryEntry<RecipeSerializer<R>>
{
    private final RegistryEntry<RecipeType<R>> recipeType;

    public RecipeEntry(AbstractRegistrar<?> owner, RegistrySupplier<RecipeSerializer<R>> delegate, ResourceKey<? super RecipeSerializer<R>> registryKey)
    {
        super(owner, delegate, Registries.RECIPE_SERIALIZER, registryKey);

        recipeType = owner.simple(Registries.RECIPE_TYPE, getRegistrationName(), RecipeTypeImpl::new);
    }

    public RecipeType<R> asRecipeType()
    {
        return recipeType.get();
    }

    public boolean is(@Nullable RecipeType<?> other)
    {
        return recipeType.isPresent() && recipeType.get() == other;
    }

    public boolean is(@Nullable RecipeSerializer<?> other)
    {
        return isPresent() && get() == other;
    }

    public R fromJson(ResourceLocation recipeId, JsonObject json)
    {
        return get().fromJson(recipeId, json);
    }

    public R fromNetwork(ResourceLocation recipeId, FriendlyByteBuf data)
    {
        return get().fromNetwork(recipeId, data);
    }

    public void toNetwork(FriendlyByteBuf data, R recipe)
    {
        get().toNetwork(data, recipe);
    }

    private record RecipeTypeImpl<R extends Recipe<?>>() implements RecipeType<R> {}
}
