package xyz.apex.forge.utility.registrator.factory;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

@FunctionalInterface
public interface RecipeSerializerFactory<RECIPE_TYPE extends RecipeSerializer<RECIPE>, RECIPE extends Recipe<INVENTORY>, INVENTORY extends Container>
{
	RECIPE_TYPE create();
}
