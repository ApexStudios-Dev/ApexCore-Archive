package xyz.apex.forge.apexcore.lib.item.crafting;

import net.minecraft.item.crafting.SingleItemRecipe;

public class SingleItemRecipeSerializer<T extends SingleItemRecipe> extends SingleItemRecipe.Serializer<T>
{
	public SingleItemRecipeSerializer(IRecipeFactory<T> recipeFactory)
	{
		super(recipeFactory);
	}
}
