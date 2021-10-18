package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

public final class RegistryHelper
{
	public static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> registerFeature(ResourceLocation featureName, ConfiguredFeature<FC, F> configuredFeature)
	{
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, featureName, configuredFeature);
	}

	public static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> registerFeature(String featureNamespace, String featureName, ConfiguredFeature<FC, F> configuredFeature)
	{
		return registerFeature(new ResourceLocation(featureNamespace, featureName), configuredFeature);
	}

	public static LootFunctionType registerLootFunction(ResourceLocation lootFunctionName, ILootSerializer<? extends ILootFunction> lootSerializer)
	{
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, lootFunctionName, new LootFunctionType(lootSerializer));
	}

	public static LootFunctionType registerLootFunction(String lootFunctionNamespace, String lootFunctionName, ILootSerializer<? extends ILootFunction> lootSerializer)
	{
		return registerLootFunction(new ResourceLocation(lootFunctionNamespace, lootFunctionName), lootSerializer);
	}

	public static <T extends IRecipe<?>> IRecipeType<T> registerRecipeType(String recipeTypeNamespace, String recipeTypeName)
	{
		return IRecipeType.register(recipeTypeNamespace + ":" + recipeTypeName);
	}
}
