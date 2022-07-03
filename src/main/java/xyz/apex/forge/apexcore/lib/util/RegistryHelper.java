package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

@Deprecated(forRemoval = true)
public final class RegistryHelper
{
	public static LootItemFunctionType registerLootFunction(ResourceLocation lootFunctionName, Serializer<? extends LootItemFunction> lootSerializer)
	{
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, lootFunctionName, new LootItemFunctionType(lootSerializer));
	}

	public static LootItemFunctionType registerLootFunction(String lootFunctionNamespace, String lootFunctionName, Serializer<? extends LootItemFunction> lootSerializer)
	{
		return registerLootFunction(new ResourceLocation(lootFunctionNamespace, lootFunctionName), lootSerializer);
	}

	public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String recipeTypeNamespace, String recipeTypeName)
	{
		return RecipeType.register(recipeTypeNamespace + ":" + recipeTypeName);
	}
}