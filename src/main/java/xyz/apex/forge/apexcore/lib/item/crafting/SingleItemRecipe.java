package xyz.apex.forge.apexcore.lib.item.crafting;

import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

// Copy of vanilla SingleItemRecipe
// Modified to make the factory interface public
// Was using AccessTransformers for this but GitHub work flows did not like that
public class SingleItemRecipe implements Recipe<Container>
{
	public static final String JSON_GROUP = "group";
	public static final String JSON_INGREDIENT = "ingredient";
	public static final String JSON_RESULT = "result";
	public static final String JSON_COUNT = "count";

	protected final Ingredient recipeIngredient;
	protected final ItemStack recipeResult;
	protected final ResourceLocation recipeId;
	protected final String recipeGroup;
	private final RecipeType<?> recipeType;
	private final RecipeSerializer<?> recipeSerializer;

	public SingleItemRecipe(RecipeType<?> recipeType, RecipeSerializer<?> recipeSerializer, ResourceLocation recipeId, String recipeGroup, Ingredient recipeIngredient, ItemStack recipeResult)
	{
		this.recipeType = recipeType;
		this.recipeSerializer = recipeSerializer;
		this.recipeId = recipeId;
		this.recipeGroup = recipeGroup;
		this.recipeIngredient = recipeIngredient;
		this.recipeResult = recipeResult;
	}

	@Override
	public final RecipeType<?> getType()
	{
		return recipeType;
	}

	@Override
	public final RecipeSerializer<?> getSerializer()
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
	public ItemStack assemble(Container inventory)
	{
		return recipeResult.copy();
	}

	@Override
	public boolean matches(Container inventory, Level level)
	{
		return recipeIngredient.test(inventory.getItem(0));
	}

	public static final class Serializer<T extends SingleItemRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T>
	{
		private final IRecipeFactory<T> recipeFactory;

		public Serializer(IRecipeFactory<T> recipeFactory)
		{
			this.recipeFactory = recipeFactory;
		}

		@Override
		public T fromJson(ResourceLocation recipeId, JsonObject json)
		{
			var recipeGroup = GsonHelper.getAsString(json, JSON_GROUP, "");
			Ingredient recipeIngredient;

			if(GsonHelper.isArrayNode(json, JSON_INGREDIENT))
				recipeIngredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, JSON_INGREDIENT));
			else
				recipeIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, JSON_INGREDIENT));

			var recipeResultName = GsonHelper.getAsString(json, JSON_RESULT);
			var recipeResultCount = GsonHelper.getAsInt(json, JSON_COUNT);
			var recipeResult = new ItemStack(Registry.ITEM.get(new ResourceLocation(recipeResultName)), recipeResultCount);
			return recipeFactory.create(recipeId, recipeGroup, recipeIngredient, recipeResult);
		}

		@Override
		public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
		{
			var recipeGroup = buffer.readUtf(32767);
			var recipeIngredient = Ingredient.fromNetwork(buffer);
			var recipeResult = buffer.readItem();
			return recipeFactory.create(recipeId, recipeGroup, recipeIngredient, recipeResult);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, T recipe)
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
