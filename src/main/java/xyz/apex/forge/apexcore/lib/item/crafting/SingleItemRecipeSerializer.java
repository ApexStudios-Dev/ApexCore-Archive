package xyz.apex.forge.apexcore.lib.item.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.util.ResourceLocation;

public class SingleItemRecipeSerializer<T extends SingleItemRecipe> extends SingleItemRecipe.Serializer<T>
{
	public SingleItemRecipeSerializer(IRecipeFactory<T> recipeFactory)
	{
		super(recipeFactory);
	}

	public interface IRecipeFactory<T extends SingleItemRecipe> extends SingleItemRecipe.Serializer.IRecipeFactory<T>
	{
		T create(ResourceLocation recipeName, String group, Ingredient ingredient, ItemStack stack);
	}
}
