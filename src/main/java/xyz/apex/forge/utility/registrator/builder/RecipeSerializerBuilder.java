package xyz.apex.forge.utility.registrator.builder;

import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.entry.RecipeSerializerEntry;
import xyz.apex.forge.utility.registrator.factory.RecipeSerializerFactory;

public final class RecipeSerializerBuilder<OWNER extends AbstractRegistrator<OWNER>, RECIPE_TYPE extends RecipeSerializer<RECIPE>, RECIPE extends Recipe<INVENTORY>, INVENTORY extends Container, PARENT> extends RegistratorBuilder<OWNER, RecipeSerializer<?>, RECIPE_TYPE, PARENT, RecipeSerializerBuilder<OWNER, RECIPE_TYPE, RECIPE, INVENTORY, PARENT>, RecipeSerializerEntry<RECIPE_TYPE, RECIPE>>
{
	private final RecipeSerializerFactory<RECIPE_TYPE, RECIPE, INVENTORY> recipeSerializerFactory;

	public RecipeSerializerBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback, RecipeSerializerFactory<RECIPE_TYPE, RECIPE, INVENTORY> recipeSerializerFactory)
	{
		super(owner, parent, registryName, callback, RecipeSerializer.class, RecipeSerializerEntry::new, RecipeSerializerEntry::cast);

		this.recipeSerializerFactory = recipeSerializerFactory;
	}

	@Override
	protected @NonnullType RECIPE_TYPE createEntry()
	{
		return recipeSerializerFactory.create();
	}
}
