package xyz.apex.forge.apexcore.lib.registrate;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.apex.forge.apexcore.lib.registrate.builders.*;
import xyz.apex.forge.apexcore.lib.util.IMod;

import java.util.function.IntSupplier;

public class CustomRegistrate<R extends CustomRegistrate<R>> extends AbstractRegistrate<R>
{
	private final RegistrateHelper<R> helper;
	private final IMod mod;

	protected CustomRegistrate(String modId)
	{
		this(IMod.createCaching(modId));
	}

	protected CustomRegistrate(IMod mod)
	{
		super(mod.getModId());

		helper = new RegistrateHelper<>(self());
		this.mod = mod;
	}

	public IMod getMod()
	{
		return mod;
	}

	public RegistrateHelper<R> helper()
	{
		return helper;
	}

	/*
		Structure Support
		Adapted from https://github.com/TelepathicGrunt/StructureTutorialMod/tree/1.16.3-Forge-jigsaw
	 */
	public <T extends Structure<NoFeatureConfig>> StructureBuilder<T, R, NoFeatureConfig> structure(StructureBuilder.StructureFactory<T, NoFeatureConfig> factory)
	{
		return structure(self(), currentName(), factory);
	}

	public <T extends Structure<NoFeatureConfig>> StructureBuilder<T, R, NoFeatureConfig> structure(String name, StructureBuilder.StructureFactory<T, NoFeatureConfig> factory)
	{
		return structure(self(), name, factory);
	}

	public <T extends Structure<NoFeatureConfig>, P> StructureBuilder<T, P, NoFeatureConfig> structure(P parent, StructureBuilder.StructureFactory<T, NoFeatureConfig> factory)
	{
		return structure(parent, currentName(), factory);
	}

	public <T extends Structure<NoFeatureConfig>, P> StructureBuilder<T, P, NoFeatureConfig> structure(P parent, String name, StructureBuilder.StructureFactory<T, NoFeatureConfig> factory)
	{
		return structure(parent, name, factory, () -> NoFeatureConfig.CODEC, () -> IFeatureConfig.NONE);
	}

	public <T extends Structure<C>, C extends IFeatureConfig> StructureBuilder<T, R, C> structure(StructureBuilder.StructureFactory<T, C> factory, NonNullSupplier<Codec<C>> codecSupplier, NonNullSupplier<C> configSupplier)
	{
		return structure(self(), currentName(), factory, codecSupplier, configSupplier);
	}

	public <T extends Structure<C>, C extends IFeatureConfig> StructureBuilder<T, R, C> structure(String name, StructureBuilder.StructureFactory<T, C> factory, NonNullSupplier<Codec<C>> codecSupplier, NonNullSupplier<C> configSupplier)
	{
		return structure(self(), name, factory, codecSupplier, configSupplier);
	}

	public <T extends Structure<C>, P, C extends IFeatureConfig> StructureBuilder<T, P, C> structure(P parent, StructureBuilder.StructureFactory<T, C> factory, NonNullSupplier<Codec<C>> codecSupplier, NonNullSupplier<C> configSupplier)
	{
		return structure(parent, currentName(), factory, codecSupplier, configSupplier);
	}

	public <T extends Structure<C>, P, C extends IFeatureConfig> StructureBuilder<T, P, C> structure(P parent, String name, StructureBuilder.StructureFactory<T, C> factory, NonNullSupplier<Codec<C>> codecSupplier, NonNullSupplier<C> configSupplier)
	{
		return entry(name, callback -> StructureBuilder.create(self(), parent, name, callback, factory, codecSupplier, configSupplier));
	}

	// Point of Interest Types
	public PointOfInterestTypeBuilder<PointOfInterestType, R> pointOfInterestType()
	{
		return pointOfInterestType(self(), currentName(), PointOfInterestType::new);
	}

	public <T extends PointOfInterestType> PointOfInterestTypeBuilder<T, R> pointOfInterestType(PointOfInterestTypeBuilder.PointOfInterestTypeFactory<T> factory)
	{
		return pointOfInterestType(self(), currentName(), factory);
	}

	public PointOfInterestTypeBuilder<PointOfInterestType, R> pointOfInterestType(String name)
	{
		return pointOfInterestType(self(), name, PointOfInterestType::new);
	}

	public <T extends PointOfInterestType> PointOfInterestTypeBuilder<T, R> pointOfInterestType(String name, PointOfInterestTypeBuilder.PointOfInterestTypeFactory<T> factory)
	{
		return pointOfInterestType(self(), name, factory);
	}

	public <P> PointOfInterestTypeBuilder<PointOfInterestType, P> pointOfInterestType(P parent)
	{
		return pointOfInterestType(parent, currentName(), PointOfInterestType::new);
	}

	public <T extends PointOfInterestType, P> PointOfInterestTypeBuilder<T, P> pointOfInterestType(P parent, PointOfInterestTypeBuilder.PointOfInterestTypeFactory<T> factory)
	{
		return pointOfInterestType(parent, currentName(), factory);
	}

	public <P> PointOfInterestTypeBuilder<PointOfInterestType, P> pointOfInterestType(P parent, String name)
	{
		return pointOfInterestType(parent, name, PointOfInterestType::new);
	}

	public <T extends PointOfInterestType, P> PointOfInterestTypeBuilder<T, P> pointOfInterestType(P parent, String name, PointOfInterestTypeBuilder.PointOfInterestTypeFactory<T> factory)
	{
		return entry(name, callback -> PointOfInterestTypeBuilder.create(self(), parent, name, callback, factory));
	}

	// Villager Professions
	public VillagerProfessionBuilder<VillagerProfession, R> villagerProfession()
	{
		return villagerProfession(self(), currentName(), VillagerProfession::new);
	}

	public <T extends VillagerProfession> VillagerProfessionBuilder<T, R> villagerProfession(VillagerProfessionBuilder.VillagerProfessionFactory<T> factory)
	{
		return villagerProfession(self(), currentName(), factory);
	}

	public VillagerProfessionBuilder<VillagerProfession, R> villagerProfession(String name)
	{
		return villagerProfession(self(), name, VillagerProfession::new);
	}

	public <T extends VillagerProfession> VillagerProfessionBuilder<T, R> villagerProfession(String name, VillagerProfessionBuilder.VillagerProfessionFactory<T> factory)
	{
		return villagerProfession(self(), name, factory);
	}

	public <P> VillagerProfessionBuilder<VillagerProfession, P> villagerProfession(P parent)
	{
		return villagerProfession(parent, currentName(), VillagerProfession::new);
	}

	public <T extends VillagerProfession, P> VillagerProfessionBuilder<T, P> villagerProfession(P parent, VillagerProfessionBuilder.VillagerProfessionFactory<T> factory)
	{
		return villagerProfession(parent, currentName(), factory);
	}

	public <P> VillagerProfessionBuilder<VillagerProfession, P> villagerProfession(P parent, String name)
	{
		return villagerProfession(parent, name, VillagerProfession::new);
	}

	public <T extends VillagerProfession, P> VillagerProfessionBuilder<T, P> villagerProfession(P parent, String name, VillagerProfessionBuilder.VillagerProfessionFactory<T> factory)
	{
		return entry(name, callback -> VillagerProfessionBuilder.create(self(), parent, name, callback, factory));
	}

	// Recipe Serializers
	public <T extends IRecipeSerializer<RECIPE>, RECIPE extends IRecipe<I>, I extends IInventory> RecipeSerializerBuilder<T, R, RECIPE, I> recipeSerializer(RecipeSerializerBuilder.RecipeSerializerFactory<T, RECIPE, I> factory)
	{
		return recipeSerializer(self(), currentName(), factory);
	}

	public <T extends IRecipeSerializer<RECIPE>, P, RECIPE extends IRecipe<I>, I extends IInventory> RecipeSerializerBuilder<T, P, RECIPE, I> recipeSerializer(P parent, RecipeSerializerBuilder.RecipeSerializerFactory<T, RECIPE, I> factory)
	{
		return recipeSerializer(parent, currentName(), factory);
	}

	public <T extends IRecipeSerializer<RECIPE>, RECIPE extends IRecipe<I>, I extends IInventory> RecipeSerializerBuilder<T, R, RECIPE, I> recipeSerializer(String name, RecipeSerializerBuilder.RecipeSerializerFactory<T, RECIPE, I> factory)
	{
		return recipeSerializer(self(), name, factory);
	}

	public <T extends IRecipeSerializer<RECIPE>, P, RECIPE extends IRecipe<I>, I extends IInventory> RecipeSerializerBuilder<T, P, RECIPE, I> recipeSerializer(P parent, String name, RecipeSerializerBuilder.RecipeSerializerFactory<T, RECIPE, I> factory)
	{
		return entry(name, callback -> RecipeSerializerBuilder.create(self(), parent, name, callback, factory));
	}

	// Painting Types
	public PaintingTypeBuilder<PaintingType, R> painting(int width, int height)
	{
		return painting(self(), currentName(), PaintingType::new, () -> width, () -> height);
	}

	public PaintingTypeBuilder<PaintingType, R> painting(String name, int width, int height)
	{
		return painting(self(), name, PaintingType::new, () -> width, () -> height);
	}

	public <P> PaintingTypeBuilder<PaintingType, P> painting(P parent, int width, int height)
	{
		return painting(parent, currentName(), PaintingType::new, () -> width, () -> height);
	}

	public <P> PaintingTypeBuilder<PaintingType, P> painting(P parent, String name, int width, int height)
	{
		return painting(parent, name, PaintingType::new, () -> width, () -> height);
	}

	public PaintingTypeBuilder<PaintingType, R> painting(IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		return painting(self(), currentName(), PaintingType::new, widthSupplier, heightSupplier);
	}

	public PaintingTypeBuilder<PaintingType, R> painting(String name, IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		return painting(self(), name, PaintingType::new, widthSupplier, heightSupplier);
	}

	public <P> PaintingTypeBuilder<PaintingType, P> painting(P parent, IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		return painting(parent, currentName(), PaintingType::new, widthSupplier, heightSupplier);
	}

	public <P> PaintingTypeBuilder<PaintingType, P> painting(P parent, String name, IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		return painting(parent, name, PaintingType::new, widthSupplier, heightSupplier);
	}

	public <T extends PaintingType> PaintingTypeBuilder<T, R> painting(PaintingTypeBuilder.PaintingTypeFactory<T> factory, IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		return painting(self(), currentName(), factory, widthSupplier, heightSupplier);
	}

	public <T extends PaintingType> PaintingTypeBuilder<T, R> painting(String name, PaintingTypeBuilder.PaintingTypeFactory<T> factory, IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		return painting(self(), name, factory, widthSupplier, heightSupplier);
	}

	public <T extends PaintingType, P> PaintingTypeBuilder<T, P> painting(P parent, PaintingTypeBuilder.PaintingTypeFactory<T> factory, IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		return painting(parent, currentName(), factory, widthSupplier, heightSupplier);
	}

	public <T extends PaintingType, P> PaintingTypeBuilder<T, P> painting(P parent, String name, PaintingTypeBuilder.PaintingTypeFactory<T> factory, IntSupplier widthSupplier, IntSupplier heightSupplier)
	{
		return entry(name, callback -> PaintingTypeBuilder.create(self(), parent, name, callback, factory, widthSupplier, heightSupplier));
	}

	public static <R extends CustomRegistrate<R>> NonNullLazyValue<R> create(String modId, NonNullFunction<String, R> builder)
	{
		return new NonNullLazyValue<>(() -> builder.apply(modId).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus()));
	}

	public static <R extends CustomRegistrate<R>> NonNullLazyValue<R> create(IMod mod, NonNullFunction<IMod, R> builder)
	{
		return new NonNullLazyValue<>(() -> builder.apply(mod).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus()));
	}
}
