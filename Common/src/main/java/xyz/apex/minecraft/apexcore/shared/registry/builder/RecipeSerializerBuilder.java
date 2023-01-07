package xyz.apex.minecraft.apexcore.shared.registry.builder;

import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RecipeEntry;

import java.util.function.Supplier;

public final class RecipeSerializerBuilder<R extends Recipe<?>, O extends AbstractRegistrar<O>, P> extends NoConfigBuilder<RecipeSerializer<?>, RecipeSerializer<R>, O, P>
{
    public RecipeSerializerBuilder(O owner, P parent, String registrationName, Supplier<RecipeSerializer<R>> factory)
    {
        super(owner, parent, Registries.RECIPE_SERIALIZER, registrationName, factory);
    }

    @Override
    protected RecipeEntry<R> createRegistryEntry(RegistrySupplier<RecipeSerializer<R>> delegate)
    {
        return new RecipeEntry<>(owner, delegate, registryKey);
    }

    @Override
    public RecipeEntry<R> register()
    {
        return (RecipeEntry<R>) super.register();
    }
}
