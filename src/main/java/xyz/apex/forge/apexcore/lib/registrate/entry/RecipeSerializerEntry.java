package xyz.apex.forge.apexcore.lib.registrate.entry;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import xyz.apex.forge.apexcore.lib.util.RegistryHelper;

public class RecipeSerializerEntry<T extends IRecipeSerializer<R>, R extends IRecipe<I>, I extends IInventory> extends RegistryEntry<T>
{
	private final IRecipeType<R> recipeType;

	public RecipeSerializerEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate)
	{
		super(owner, delegate);

		recipeType = RegistryHelper.registerRecipeType(delegate.getId().getNamespace(), delegate.getId().getPath());
	}

	public IRecipeType<R> getRecipeType()
	{
		return recipeType;
	}
}
