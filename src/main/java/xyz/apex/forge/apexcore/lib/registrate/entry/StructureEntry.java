package xyz.apex.forge.apexcore.lib.registrate.entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.Validate;
import xyz.apex.forge.apexcore.lib.constants.Mods;

import java.lang.reflect.Method;
import java.util.Map;

public class StructureEntry<T extends Structure<C>, C extends IFeatureConfig> extends RegistryEntry<T>
{
	private static final Method getCodec_Method = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");

	private final NonNullLazyValue<StructureFeature<C, ? extends Structure<C>>> lazyStructureFeature;
	private final NonNullFunction<ServerWorld, Boolean> canDimensionGenerateStructure;

	public StructureEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate, NonNullSupplier<C> configSupplier, NonNullFunction<ServerWorld, Boolean> canDimensionGenerateStructure)
	{
		super(owner, delegate);

		this.canDimensionGenerateStructure = canDimensionGenerateStructure;

		lazyStructureFeature = new NonNullLazyValue<>(() -> get().configured(configSupplier.get()));

		FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.HIGH, false, FMLCommonSetupEvent.class, event -> event.enqueueWork(this::registerStructureFeature));
		MinecraftForge.EVENT_BUS.addListener(this::onLevelLoad);
	}

	public StructureFeature<C, ? extends Structure<C>> configured()
	{
		return lazyStructureFeature.get();
	}

	private void registerStructureFeature()
	{
		StructureFeature<C, ? extends Structure<C>> structureFeature = configured();

		// Configured features : https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/STConfiguredStructures.java#L23
		Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(getId().getNamespace(), "configured_" + getId().getPath()), structureFeature);

		// https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/STConfiguredStructures.java#L27-L42
		FlatGenerationSettings.STRUCTURE_FEATURES.put(get(), structureFeature);
	}

	// https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/StructureTutorialMain.java#L89-L98
	private void onLevelLoad(WorldEvent.Load event)
	{
		IWorld world = event.getWorld();

		if(world instanceof ServerWorld)
		{
			Structure<C> structure = get();
			ServerWorld level = (ServerWorld) world;

			// https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/StructureTutorialMain.java#L104-L117
			if(isTerraForged(level))
				return;
			// https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/StructureTutorialMain.java#L119-L127
			if(level.getChunkSource().getGenerator() instanceof FlatChunkGenerator && level.dimension() == World.OVERWORLD)
				return;

			// https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/StructureTutorialMain.java#L129-L139
			DimensionStructuresSettings settings = level.getChunkSource().getGenerator().getSettings();
			Map<Structure<?>, StructureSeparationSettings> settingsMap = settings.structureConfig();

			if(settingsMap instanceof ImmutableMap)
			{
				Map<Structure<?>, StructureSeparationSettings> map = Maps.newHashMap(settingsMap);
				settings.structureConfig = map;
				settingsMap = map;
			}

			if(canDimensionGenerateStructure.apply(level))
				settingsMap.putIfAbsent(structure, DimensionStructuresSettings.DEFAULTS.get(structure));
			else
				settingsMap.remove(structure);
		}
	}

	private static boolean isTerraForged(ServerWorld level)
	{
		Validate.notNull(getCodec_Method);

		try
		{
			Codec<? extends ChunkGenerator> codec = (Codec<? extends ChunkGenerator>) getCodec_Method.invoke(level.getChunkSource().getGenerator());
			ResourceLocation generatorName = Registry.CHUNK_GENERATOR.getKey(codec);

			if(generatorName != null && generatorName.getNamespace().equals(Mods.TERRAFORGED.modId))
				return true;
		}
		catch(Exception e)
		{
			return false;
		}

		return false;
	}
}
