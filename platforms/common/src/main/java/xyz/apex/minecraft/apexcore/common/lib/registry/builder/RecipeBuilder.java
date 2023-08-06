package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RecipeEntry;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Recipe Builder implementation.
 * <p>
 * Used to build and register Recipe entries.
 *
 * @param <O> Type of Registrar.
 * @param <T> Type of Recipe [Entry].
 * @param <P> Type of Parent.
 */
public final class RecipeBuilder<O extends AbstractRegistrar<O>, T extends Recipe<?>, P> extends AbstractBuilder<O, P, RecipeSerializer<?>, RecipeSerializer<T>, RecipeBuilder<O, T, P>, RecipeEntry<T>>
{
    private final BiFunction<ResourceLocation, JsonObject, T> fromJson;
    private final BiFunction<ResourceLocation, FriendlyByteBuf, T> fromNetwork;
    private final BiConsumer<FriendlyByteBuf, T> toNetwork;

    @ApiStatus.Internal
    public RecipeBuilder(O registrar, P parent, String registrationName, BiFunction<ResourceLocation, JsonObject, T> fromJson, BiFunction<ResourceLocation, FriendlyByteBuf, T> fromNetwork, BiConsumer<FriendlyByteBuf, T> toNetwork)
    {
        super(registrar, parent, Registries.RECIPE_SERIALIZER, registrationName);

        this.fromJson = fromJson;
        this.fromNetwork = fromNetwork;
        this.toNetwork = toNetwork;

        registrar.simple(Registries.RECIPE_TYPE, registrationName(), this);
    }

    @Override
    protected RecipeEntry<T> createRegistryEntry()
    {
        return new RecipeEntry<>(registrar, registryKey);
    }

    @Override
    protected RecipeSerializer<T> createEntry()
    {
        return new RecipeSerializerImpl<>(fromJson, fromNetwork, toNetwork);
    }

    @Override
    protected String getDescriptionId(RecipeEntry<T> entry)
    {
        return registryName().toLanguageKey("recipe_type");
    }

    private record RecipeSerializerImpl<T extends Recipe<?>>(
            BiFunction<ResourceLocation, JsonObject, T> fromJson,
            BiFunction<ResourceLocation, FriendlyByteBuf, T> fromNetwork,
            BiConsumer<FriendlyByteBuf, T> toNetwork
    ) implements RecipeSerializer<T>
    {
        @Override
        public T fromJson(ResourceLocation recipeId, JsonObject json)
        {
            return fromJson.apply(recipeId, json);
        }

        @Override
        public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            return fromNetwork.apply(recipeId, buffer);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe)
        {
            toNetwork.accept(buffer, recipe);
        }
    }
}
