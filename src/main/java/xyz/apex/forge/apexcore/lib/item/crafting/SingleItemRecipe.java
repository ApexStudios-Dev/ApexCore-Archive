package xyz.apex.forge.apexcore.lib.item.crafting;

import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

// Copy of vanilla SingleItemRecipe
// Modified to make the factory interface public
// Was using AccessTransformers for this but GitHub work flows did not like that
public class SingleItemRecipe implements IRecipe<IInventory>
{
	public static final String JSON_GROUP = "group";
	public static final String JSON_INGREDIENT = "ingredient";
	public static final String JSON_RESULT = "result";
	public static final String JSON_COUNT = "count";

	protected final Ingredient recipeIngredient;
	protected final ItemStack recipeResult;
	protected final ResourceLocation recipeId;
	protected final String recipeGroup;
	private final IRecipeType<?> recipeType;
	private final IRecipeSerializer<?> recipeSerializer;

	public SingleItemRecipe(IRecipeType<?> recipeType, IRecipeSerializer<?> recipeSerializer, ResourceLocation recipeId, String recipeGroup, Ingredient recipeIngredient, ItemStack recipeResult)
	{
		this.recipeType = recipeType;
		this.recipeSerializer = recipeSerializer;
		this.recipeId = recipeId;
		this.recipeGroup = recipeGroup;
		this.recipeIngredient = recipeIngredient;
		this.recipeResult = recipeResult;
	}

	@Override
	public final IRecipeType<?> getType()
	{
		return recipeType;
	}

	@Override
	public final IRecipeSerializer<?> getSerializer()
	{
		return recipeSerializer;
	}

	@Override
	public final ResourceLocation getId()
	{
		return recipeId;
	}

	@Override
	public final String getGroup()
	{
		return recipeGroup;
	}

	@Override
	public final ItemStack getResultItem()
	{
		return recipeResult;
	}

	@Override
	public final NonNullList<Ingredient> getIngredients()
	{
		return NonNullList.of(recipeIngredient);
	}

	@Override
	public final boolean canCraftInDimensions(int width, int height)
	{
		// at least 1 slot must exist
		return width + height >= 1;
	}

	@Override
	public ItemStack assemble(IInventory inventory)
	{
		return recipeResult.copy();
	}

	@Override
	public boolean matches(IInventory inventory, World level)
	{
		return recipeIngredient.test(inventory.getItem(0));
	}

	public static final class Serializer<T extends SingleItemRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>
	{
		private final IRecipeFactory<T> recipeFactory;

		public Serializer(IRecipeFactory<T> recipeFactory)
		{
			this.recipeFactory = recipeFactory;
		}

		@Override
		public T fromJson(ResourceLocation recipeId, JsonObject json)
		{
			String recipeGroup = JSONUtils.getAsString(json, JSON_GROUP, "");
			Ingredient recipeIngredient;

			if(JSONUtils.isArrayNode(json, JSON_INGREDIENT))
				recipeIngredient = Ingredient.fromJson(JSONUtils.getAsJsonArray(json, JSON_INGREDIENT));
			else
				recipeIngredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, JSON_INGREDIENT));

			String recipeResultName = JSONUtils.getAsString(json, JSON_RESULT);
			int recipeResultCount = JSONUtils.getAsInt(json, JSON_COUNT);
			ItemStack recipeResult = new ItemStack(Registry.ITEM.get(new ResourceLocation(recipeResultName)), recipeResultCount);
			return recipeFactory.create(recipeId, recipeGroup, recipeIngredient, recipeResult);
		}

		@Override
		public T fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
		{
			String recipeGroup = buffer.readUtf(32767);
			Ingredient recipeIngredient = Ingredient.fromNetwork(buffer);
			ItemStack recipeResult = buffer.readItem();
			return recipeFactory.create(recipeId, recipeGroup, recipeIngredient, recipeResult);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, T recipe)
		{
			buffer.writeUtf(recipe.recipeGroup);
			recipe.recipeIngredient.toNetwork(buffer);
			buffer.writeItem(recipe.recipeResult);
		}

		@FunctionalInterface
		public interface IRecipeFactory<T extends SingleItemRecipe>
		{
			T create(ResourceLocation recipeId, String recipeGroup, Ingredient recipeIngredient, ItemStack recipeResult);
		}
	}
}
