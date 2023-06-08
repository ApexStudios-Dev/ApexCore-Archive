package xyz.apex.minecraft.apexcore.common.lib.registry.entries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import xyz.apex.minecraft.apexcore.common.lib.registry.DelegatedRegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;

import java.util.function.Supplier;

/**
 * Registry entry for a recipe type and serializer.
 * <p>
 * Recipe serializer and type are registered with the same registration name and bound together.
 *
 * @param <T> Type of recipe.
 */
public final class RecipeEntry<T extends Recipe<?>> extends DelegatedRegistryEntry<RecipeType<T>> implements RecipeType<T>
{
    private final RegistryEntry<RecipeSerializer<T>> recipeSerializer;

    private RecipeEntry(RegistryEntry<RecipeType<T>> delegate, RegistryEntry<RecipeSerializer<T>> recipeSerializer)
    {
        super(delegate);

        this.recipeSerializer = recipeSerializer;
    }

    /**
     * @return Registry entry containing the recipe serializer.
     */
    public RegistryEntry<RecipeSerializer<T>> getRecipeSerializerEntry()
    {
        return recipeSerializer;
    }

    /**
     * @return The recipe serializer associated with this recipe type.
     */
    public RecipeSerializer<T> getRecipeSerializer()
    {
        return recipeSerializer.get();
    }

    /**
     * Registers a new recipe type and serializer.
     *
     * @param ownerId           Owner id to register for.
     * @param registrationName  Registration name to use for both recipe type and serializer.
     * @param serializerFactory Factory used to construct to recipe serializer.
     * @param <T>               Type of recipe.
     * @return Registry entry for a recipe type and serializer.
     */
    public static <T extends Recipe<?>> RecipeEntry<T> register(String ownerId, String registrationName, Supplier<RecipeSerializer<T>> serializerFactory)
    {
        var mgr = RegistrarManager.get(ownerId);
        var recipeType = mgr.get(Registries.RECIPE_TYPE).<RecipeType<T>>register(registrationName, RecipeTypeImpl::new);
        var recipeSerializer = mgr.get(Registries.RECIPE_SERIALIZER).register(registrationName, serializerFactory);
        return new RecipeEntry<>(recipeType, recipeSerializer);
    }

    private static final class RecipeTypeImpl<T extends Recipe<?>> implements RecipeType<T>
    {
    }
}
