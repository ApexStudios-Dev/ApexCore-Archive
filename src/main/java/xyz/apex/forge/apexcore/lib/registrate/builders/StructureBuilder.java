package xyz.apex.forge.apexcore.lib.registrate.builders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import xyz.apex.forge.apexcore.lib.registrate.entry.StructureEntry;

import java.util.Map;
import java.util.function.Supplier;

public class StructureBuilder<T extends Structure<C>, P, C extends IFeatureConfig> extends AbstractBuilder<Structure<?>, T, P, StructureBuilder<T, P, C>>
{
	private final StructureFactory<T, C> factory;
	private final NonNullSupplier<Codec<C>> codecSupplier;
	private final NonNullSupplier<C> configSupplier;

	private Supplier<StructureSeparationSettings> separationSettingsSupplier = () -> null;
	private boolean terraformTerrain = false;
	private Supplier<GenerationStage.Decoration> decorationStageSupplier = () -> GenerationStage.Decoration.SURFACE_STRUCTURES;
	private NonNullFunction<ServerWorld, Boolean> canDimensionGenerateStructure = world -> true;

	protected StructureBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, StructureFactory<T, C> factory, NonNullSupplier<Codec<C>> codecSupplier, NonNullSupplier<C> configSupplier)
	{
		super(owner, parent, name, callback, Structure.class);

		this.factory = factory;
		this.codecSupplier = codecSupplier;
		this.configSupplier = configSupplier;

		onRegister(this::finalizeRegistration);
	}

	private void finalizeRegistration(T structure)
	{
		// Adapted from https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/STStructures.java#L70
		Structure.STRUCTURES_REGISTRY.put(getName(), structure); // https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/STStructures.java#L75-L82

		// https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/STStructures.java#L84-L100
		if(terraformTerrain)
			Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure).build();

		// https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/STStructures.java#L102-L119
		StructureSeparationSettings separationSettings = separationSettingsSupplier.get();

		if(separationSettings != null)
		{
			DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.DEFAULTS).put(structure, separationSettings).build();

			// https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.16.3-Forge-jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/STStructures.java#L122-L146
			for(Map.Entry<RegistryKey<DimensionSettings>, DimensionSettings> registryKeyDimensionSettingsEntry : WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet())
			{
				DimensionStructuresSettings settings = registryKeyDimensionSettingsEntry.getValue().structureSettings();
				Map<Structure<?>, StructureSeparationSettings> settingsMap = settings.structureConfig();

				if(settingsMap instanceof ImmutableMap)
				{
					Map<Structure<?>, StructureSeparationSettings> map = Maps.newHashMap(settingsMap);
					map.put(structure, separationSettings);
					settings.structureConfig = map;
				}
				else
					settingsMap.put(structure, separationSettings);
			}
		}

		// below here are custom additions (not adapted from the repo)
		// fix having to override #step in your structure class
		// and also register to the underlying STEP map that vanilla assumes it should exist in
		GenerationStage.Decoration step = decorationStageSupplier.get();

		if(step != null)
			Structure.STEP.put(structure, step);
	}

	// region: SeparationSettings
	/**
	 * @param spacing average distance apart in chunks between spawn attempts
	 * @param separation minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE
	 * @param salt this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique.
	 */
	public StructureBuilder<T, P, C> separationSettings(int spacing, int separation, int salt)
	{
		return separationSettings(() -> new StructureSeparationSettings(spacing, separation, salt));
	}

	public StructureBuilder<T, P, C> separationSettings(NonNullSupplier<StructureSeparationSettings> separationSettingsSupplier)
	{
		this.separationSettingsSupplier = separationSettingsSupplier;
		return this;
	}
	// endregion

	// region: Terraform Terrain
	public StructureBuilder<T, P, C> terraformTerrain()
	{
		return terraformTerrain(true);
	}

	public StructureBuilder<T, P, C> terraformTerrain(boolean terraformTerrain)
	{
		this.terraformTerrain = terraformTerrain;
		return this;
	}
	// endregion

	public StructureBuilder<T, P, C> step(NonNullSupplier<GenerationStage.Decoration> decorationStageSupplier)
	{
		this.decorationStageSupplier = decorationStageSupplier;
		return this;
	}

	public StructureBuilder<T, P, C> canDimensionGenerate(NonNullFunction<ServerWorld, Boolean> canDimensionGenerateStructure)
	{
		this.canDimensionGenerateStructure = canDimensionGenerateStructure;
		return this;
	}

	@Override
	protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate)
	{
		return new StructureEntry<>(getOwner(), delegate, configSupplier, canDimensionGenerateStructure);
	}

	@Override
	protected @NonnullType T createEntry()
	{
		return factory.create(codecSupplier.get());
	}

	@Override
	public StructureEntry<T, C> register()
	{
		return (StructureEntry<T, C>) super.register();
	}

	public static <T extends Structure<C>, P, C extends IFeatureConfig> StructureBuilder<T, P, C> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, StructureFactory<T, C> factory, NonNullSupplier<Codec<C>> codecSupplier, NonNullSupplier<C> configSupplier)
	{
		return new StructureBuilder<>(owner, parent, name, callback, factory, codecSupplier, configSupplier);
	}

	@FunctionalInterface
	public interface StructureFactory<T extends Structure<C>, C extends IFeatureConfig>
	{
		T create(Codec<C> codec);
	}
}
