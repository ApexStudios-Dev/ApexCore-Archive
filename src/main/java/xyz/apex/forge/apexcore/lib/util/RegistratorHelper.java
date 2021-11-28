package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;

import xyz.apex.repack.com.tterrag.registrate.providers.DataGenContext;
import xyz.apex.repack.com.tterrag.registrate.providers.RegistrateItemModelProvider;
import xyz.apex.repack.com.tterrag.registrate.providers.RegistrateRecipeProvider;

// TODO: Merge directly into registrator
public final class RegistratorHelper
{
	// region: Models
	public static <ITEM extends Item> void blockItemModel(DataGenContext<Item, ITEM> ctx, RegistrateItemModelProvider provider)
	{
		provider.blockItem(ctx);
	}

	public static <ITEM extends Item> void handheldModel(DataGenContext<Item, ITEM> ctx, RegistrateItemModelProvider provider)
	{
		provider.handheld(ctx);
	}
	// endregion

	// region: Recipes
	// region: Weapons
	// region: Sword
	public static <ITEM extends Item> void swordRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		swordRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void swordRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("I")
				.pattern("I")
				.pattern("S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion
	// endregion

	// region: Tools
	// region: Pickaxe
	public static <ITEM extends Item> void pickaxeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		pickaxeRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void pickaxeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("III")
				.pattern(" S ")
				.pattern(" S ")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion

	// region: Axe
	public static <ITEM extends Item> void axeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		axeRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void axeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("II")
				.pattern("IS")
				.pattern(" S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion

	// region: Shovel
	public static <ITEM extends Item> void shovelRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		shovelRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void shovelRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("I")
				.pattern("S")
				.pattern("S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion

	// region: Hoe
	public static <ITEM extends Item> void hoeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		hoeRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void hoeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot, ITag.INamedTag<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("II")
				.pattern(" S")
				.pattern(" S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion
	// endregion

	// region: Armor
	public static <ITEM extends Item> void helmetRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("III")
				.pattern("I I")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}

	public static <ITEM extends Item> void chestplateRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("I I")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}

	public static <ITEM extends Item> void leggingsRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("III")
				.pattern("I I")
				.pattern("I I")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}

	public static <ITEM extends Item> void bootsRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, ITag.INamedTag<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("I I")
				.pattern("I I")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.hasItem(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion
	// endregion
}