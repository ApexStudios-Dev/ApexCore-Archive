package xyz.apex.minecraft.apexcore.common.registry.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.registry.RegistryManager;

import java.util.function.Supplier;

public final class RecipeEntry<T extends Recipe<?>> extends RegistryEntry<RecipeSerializer<T>> implements RecipeType<T>
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

    public static <T extends Recipe<?>> RecipeEntry<T> register(String ownerId, String registrationName, Supplier<RecipeSerializer<T>> recipeSerializerFactory)
    {
        return RegistryManager.get(ownerId).getRegistry(Registries.RECIPE_SERIALIZER).register(registrationName, RecipeEntry::new, recipeSerializerFactory);
    }
}
