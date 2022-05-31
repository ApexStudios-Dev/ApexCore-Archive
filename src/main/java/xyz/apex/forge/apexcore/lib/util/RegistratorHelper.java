package xyz.apex.forge.apexcore.lib.util;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;

import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

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
	public static <ITEM extends Item> void swordRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot)
	{
		swordRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void swordRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot, Tag.Named<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("I")
				.pattern("I")
				.pattern("S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.has(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion
	// endregion

	// region: Tools
	// region: Pickaxe
	public static <ITEM extends Item> void pickaxeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot)
	{
		pickaxeRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void pickaxeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot, Tag.Named<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("III")
				.pattern(" S ")
				.pattern(" S ")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.has(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion

	// region: Axe
	public static <ITEM extends Item> void axeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot)
	{
		axeRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void axeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot, Tag.Named<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("II")
				.pattern("IS")
				.pattern(" S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.has(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion

	// region: Shovel
	public static <ITEM extends Item> void shovelRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot)
	{
		shovelRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void shovelRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot, Tag.Named<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("I")
				.pattern("S")
				.pattern("S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.has(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion

	// region: Hoe
	public static <ITEM extends Item> void hoeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot)
	{
		hoeRecipe(ctx, provider, ingot, Tags.Items.RODS_WOODEN);
	}

	public static <ITEM extends Item> void hoeRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot, Tag.Named<Item> stick)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.define('S', stick)
				.pattern("II")
				.pattern(" S")
				.pattern(" S")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.has(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion
	// endregion

	// region: Armor
	public static <ITEM extends Item> void helmetRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("III")
				.pattern("I I")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.has(ingot))
				.save(provider);
		// @formatter:on
	}

	public static <ITEM extends Item> void chestplateRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("I I")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.has(ingot))
				.save(provider);
		// @formatter:on
	}

	public static <ITEM extends Item> void leggingsRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("III")
				.pattern("I I")
				.pattern("I I")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.has(ingot))
				.save(provider);
		// @formatter:on
	}

	public static <ITEM extends Item> void bootsRecipe(DataGenContext<Item, ITEM> ctx, RegistrateRecipeProvider provider, Tag.Named<Item> ingot)
	{
		// @formatter:off
		ShapedRecipeBuilder
				.shaped(ctx::get, 1)
				.define('I', ingot)
				.pattern("I I")
				.pattern("I I")
				.unlockedBy("has_" + provider.safeName(ingot.getName()), RegistrateRecipeProvider.has(ingot))
				.save(provider);
		// @formatter:on
	}
	// endregion
	// endregion
}
