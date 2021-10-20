package xyz.apex.forge.apexcore.lib.data.template_pool;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import xyz.apex.forge.apexcore.lib.util.IMod;

import javax.annotation.Nullable;

public final class TemplatePools implements IStringSerializable, Comparable<TemplatePools>
{
	public static final TemplatePools EMPTY = of("empty");

	// region: Bastion
	public static final TemplatePools BASTION_STARTS = of("bastion/starts");

	// region: Blocks
	public static final TemplatePools BASTION_BLOCKS_GOLD = of("bastion/blocks/gold");
	// endregion

	// region: Bridge
	public static final TemplatePools BASTION_BRIDGE_BRIDGE_PIECES = of("bastion/bridge/bridge_pieces");
	public static final TemplatePools BASTION_BRIDGE_CONNECTORS = of("bastion/bridge/connectors");
	public static final TemplatePools BASTION_BRIDGE_LEGS = of("bastion/bridge/legs");
	public static final TemplatePools BASTION_BRIDGE_RAMPART_PLATES = of("bastion/bridge/rampart_plates");
	public static final TemplatePools BASTION_BRIDGE_RAMPARTS = of("bastion/bridge/ramparts");
	public static final TemplatePools BASTION_BRIDGE_STARTING_PIECES = of("bastion/bridge/starting_pieces");
	public static final TemplatePools BASTION_BRIDGE_WALLS = of("bastion/bridge/walls");
	// endregion

	// region: Hoglin Stable
	public static final TemplatePools BASTION_HOGLIN_STABLE_CONNECTORS = of("bastion/hoglin_stable/connectors");
	public static final TemplatePools BASTION_HOGLIN_STABLE_MIRRORED_STARTING_PIECES = of("bastion/hoglin_stable/mirrored_starting_pieces");
	public static final TemplatePools BASTION_HOGLIN_STABLE_POSTS = of("bastion/hoglin_stable/posts");
	public static final TemplatePools BASTION_HOGLIN_STABLE_RAMPARTS_PLATES = of("bastion/hoglin_stable/ramparts_plates");
	public static final TemplatePools BASTION_HOGLIN_STABLE_RAMPARTS = of("bastion/hoglin_stable/ramparts");
	public static final TemplatePools BASTION_HOGLIN_STABLE_STAIRS = of("bastion/hoglin_stable/stairs");
	public static final TemplatePools BASTION_HOGLIN_STABLE_STARTING_PIECES = of("bastion/hoglin_stable/starting_pieces");
	public static final TemplatePools BASTION_HOGLIN_STABLE_WALL_BASES = of("bastion/hoglin_stable/wall_bases");
	public static final TemplatePools BASTION_HOGLIN_STABLE_WALLS = of("bastion/hoglin_stable/walls");

	// region: Large Stables
	public static final TemplatePools BASTION_HOGLIN_STABLE_LARGE_STABLES_INNER = of("bastion/hoglin_stable/large_stables/inner");
	public static final TemplatePools BASTION_HOGLIN_STABLE_LARGE_STABLES_OUTER = of("bastion/hoglin_stable/large_stables/outer");
	// endregion

	// region Small Stables
	public static final TemplatePools BASTION_HOGLIN_STABLE_SMALL_STABLES_INNER = of("bastion/hoglin_stable/small_stables/inner");
	public static final TemplatePools BASTION_HOGLIN_STABLE_SMALL_STABLES_OUTER = of("bastion/hoglin_stable/small_stables/outer");
	// endregion
	// endregion

	// region: Mobs
	public static final TemplatePools BASTION_MOBS_HOGLIN = of("bastion/mobs/hoglin");
	public static final TemplatePools BASTION_MOBS_PIGLIN = of("bastion/mobs/piglin");
	public static final TemplatePools BASTION_MOBS_PIGLIN_MELEE = of("bastion/mobs/piglin_melee");
	// endregion

	// region: Treasure
	public static final TemplatePools BASTION_TREASURE_BASES = of("bastion/treasure/bases");
	public static final TemplatePools BASTION_TREASURE_BRAINS = of("bastion/treasure/brains");
	public static final TemplatePools BASTION_TREASURE_CONNECTORS = of("bastion/treasure/connectors");
	public static final TemplatePools BASTION_TREASURE_ENTRANCES = of("bastion/treasure/entrances");
	public static final TemplatePools BASTION_TREASURE_RAMPARTS = of("bastion/treasure/ramparts");
	public static final TemplatePools BASTION_TREASURE_ROOFS = of("bastion/treasure/roofs");
	public static final TemplatePools BASTION_TREASURE_STAIRS = of("bastion/treasure/stairs");
	public static final TemplatePools BASTION_TREASURE_WALLS = of("bastion/treasure/walls");

	// region: Bases
	public static final TemplatePools BASTION_TREASURE_BASES_CENTERS = of("bastion/treasure/bases/centers");
	// endregion

	// region: Corners
	public static final TemplatePools BASTION_TREASURE_CORNERS_BOTTOM = of("bastion/treasure/corners/bottom");
	public static final TemplatePools BASTION_TREASURE_CORNERS_EDGES = of("bastion/treasure/corners/edges");
	public static final TemplatePools BASTION_TREASURE_CORNERS_MIDDLE = of("bastion/treasure/corners/middle");
	public static final TemplatePools BASTION_TREASURE_CORNERS_TOPS = of("bastion/treasure/corners/tops");
	// endregion

	// region: Extensions
	public static final TemplatePools BASTION_TREASURE_EXTENSIONS_HOUSES = of("bastion/treasure/extensions/houses");
	public static final TemplatePools BASTION_TREASURE_EXTENSIONS_LARGE_POOL = of("bastion/treasure/extensions/large_pool");
	public static final TemplatePools BASTION_TREASURE_EXTENSIONS_SMALL_POOL = of("bastion/treasure/extensions/small_pool");
	// endregion

	// region: Walls
	public static final TemplatePools BASTION_TREASURE_WALLS_BOTTOM = of("bastion/treasure/walls/bottom");
	public static final TemplatePools BASTION_TREASURE_WALLS_MID = of("bastion/treasure/walls/mid");
	public static final TemplatePools BASTION_TREASURE_WALLS_OUTER = of("bastion/treasure/walls/outer");
	public static final TemplatePools BASTION_TREASURE_WALLS_TOP = of("bastion/treasure/walls/top");
	// endregion
	// endregion

	// region: Units
	public static final TemplatePools BASTION_UNITS_CENTER_PIECES = of("bastion/units/center_pieces");
	public static final TemplatePools BASTION_UNITS_EDGE_WALL_UNITS = of("bastion/units/edge_wall_units");
	public static final TemplatePools BASTION_UNITS_EDGES = of("bastion/units/edges");
	public static final TemplatePools BASTION_UNITS_LARGE_RAMPARTS = of("bastion/units/large_ramparts");
	public static final TemplatePools BASTION_UNITS_PATHWAYS = of("bastion/units/pathways");
	public static final TemplatePools BASTION_UNITS_RAMPART_PLATES = of("bastion/units/rampart_plates");
	public static final TemplatePools BASTION_UNITS_RAMPARTS = of("bastion/units/ramparts");
	public static final TemplatePools BASTION_UNITS_WALL_UNITS = of("bastion/units/wall_units");

	// region: Fillers
	public static final TemplatePools BASTION_UNITS_FILLERS_STAGE_0 = of("bastion/units/fillers/stage_0");
	// endregion

	// region: Stages
	public static final TemplatePools BASTION_UNITS_STAGES_STAGE_0 = of("bastion/units/stages/stage_0");
	public static final TemplatePools BASTION_UNITS_STAGES_STAGE_1 = of("bastion/units/stages/stage_1");
	public static final TemplatePools BASTION_UNITS_STAGES_STAGE_2 = of("bastion/units/stages/stage_2");
	public static final TemplatePools BASTION_UNITS_STAGES_STAGE_3 = of("bastion/units/stages/stage_3");

	// region: Rot
	public static final TemplatePools BASTION_UNITS_STAGES_ROT_STAGE_1 = of("bastion/units/stages/rot/stage_1");
	// endregion
	// endregion

	// region: Walls
	public static final TemplatePools BASTION_UNITS_WALLS_WALL_BASES = of("bastion/units/walls/wall_bases");
	// endregion
	// endregion
	// endregion

	// region: Pillager Outpost
	public static final TemplatePools PILLAGER_OUTPOST_BASE_PLATES = of("pillager_outpost/base_plates");
	public static final TemplatePools PILLAGER_OUTPOST_FEATURE_PLATES = of("pillager_outpost/feature_plates");
	public static final TemplatePools PILLAGER_OUTPOST_FEATURES = of("pillager_outpost/features");
	public static final TemplatePools PILLAGER_OUTPOST_TOWERS = of("pillager_outpost/towers");
	// endregion

	// region: Village
	// region: Common
	public static final TemplatePools VILLAGE_COMMON_ANIMALS = of("village/common/animals");
	public static final TemplatePools VILLAGE_COMMON_BUTCHER_ANIMALS = of("village/common/butcher_animals");
	public static final TemplatePools VILLAGE_COMMON_CATS = of("village/common/cats");
	public static final TemplatePools VILLAGE_COMMON_IRON_GOLEM = of("village/common/iron_golem");
	public static final TemplatePools VILLAGE_COMMON_SHEEP = of("village/common/sheep");
	public static final TemplatePools VILLAGE_COMMON_WELL_BOTTOMS = of("village/common/well_bottoms");
	// endregion

	// region: Desert
	public static final TemplatePools VILLAGE_DESERT_DECOR = of("village/desert/decor");
	public static final TemplatePools VILLAGE_DESERT_HOUSES = of("village/desert/houses");
	public static final TemplatePools VILLAGE_DESERT_STREETS = of("village/desert/streets");
	public static final TemplatePools VILLAGE_DESERT_TERMINATORS = of("village/desert/terminators");
	public static final TemplatePools VILLAGE_DESERT_TOWN_CENTERS = of("village/desert/town_centers");
	public static final TemplatePools VILLAGE_DESERT_VILLAGERS = of("village/desert/villagers");

	// region: Zombie
	public static final TemplatePools VILLAGE_DESERT_ZOMBIE_DECOR = of("village/desert/zombie/decor");
	public static final TemplatePools VILLAGE_DESERT_ZOMBIE_HOUSES = of("village/desert/zombie/houses");
	public static final TemplatePools VILLAGE_DESERT_ZOMBIE_STREETS = of("village/desert/zombie/streets");
	public static final TemplatePools VILLAGE_DESERT_ZOMBIE_TERMINATORS = of("village/desert/zombie/terminators");
	public static final TemplatePools VILLAGE_DESERT_ZOMBIE_VILLAGERS = of("village/desert/zombie/villagers");
	// endregion
	// endregion

	// region: Plains
	public static final TemplatePools VILLAGE_PLAINS_DECOR = of("village/plains/decor");
	public static final TemplatePools VILLAGE_PLAINS_HOUSES = of("village/plains/houses");
	public static final TemplatePools VILLAGE_PLAINS_STREETS = of("village/plains/streets");
	public static final TemplatePools VILLAGE_PLAINS_TERMINATORS = of("village/plains/terminators");
	public static final TemplatePools VILLAGE_PLAINS_TOWN_CENTERS = of("village/plains/town_centers");
	public static final TemplatePools VILLAGE_PLAINS_TREES = of("village/plains/trees");
	public static final TemplatePools VILLAGE_PLAINS_VILLAGERS = of("village/plains/villagers");

	// region: Zombie
	public static final TemplatePools VILLAGE_PLAINS_ZOMBIE_DECOR = of("village/plains/zombie/decor");
	public static final TemplatePools VILLAGE_PLAINS_ZOMBIE_HOUSES = of("village/plains/zombie/houses");
	public static final TemplatePools VILLAGE_PLAINS_ZOMBIE_STREETS = of("village/plains/zombie/streets");
	public static final TemplatePools VILLAGE_PLAINS_ZOMBIE_VILLAGERS = of("village/plains/zombie/villagers");
	// endregion
	// endregion

	// region: Savanna
	public static final TemplatePools VILLAGE_SAVANNA_DECOR = of("village/savanna/decor");
	public static final TemplatePools VILLAGE_SAVANNA_HOUSES = of("village/savanna/houses");
	public static final TemplatePools VILLAGE_SAVANNA_STREETS = of("village/savanna/streets");
	public static final TemplatePools VILLAGE_SAVANNA_TERMINATORS = of("village/savanna/terminators");
	public static final TemplatePools VILLAGE_SAVANNA_TOWN_CENTERS = of("village/savanna/town_centers");
	public static final TemplatePools VILLAGE_SAVANNA_TREES = of("village/savanna/trees");
	public static final TemplatePools VILLAGE_SAVANNA_VILLAGERS = of("village/savanna/villagers");

	// region: Zombie
	public static final TemplatePools VILLAGE_SAVANNA_ZOMBIE_DECOR = of("village/savanna/zombie/decor");
	public static final TemplatePools VILLAGE_SAVANNA_ZOMBIE_HOUSES = of("village/savanna/zombie/houses");
	public static final TemplatePools VILLAGE_SAVANNA_ZOMBIE_STREETS = of("village/savanna/zombie/streets");
	public static final TemplatePools VILLAGE_SAVANNA_ZOMBIE_VILLAGERS = of("village/savanna/zombie/villagers");
	// endregion
	// endregion

	// region: Snowy
	public static final TemplatePools VILLAGE_SNOWY_DECOR = of("village/snowy/decor");
	public static final TemplatePools VILLAGE_SNOWY_HOUSES = of("village/snowy/houses");
	public static final TemplatePools VILLAGE_SNOWY_STREETS = of("village/snowy/streets");
	public static final TemplatePools VILLAGE_SNOWY_TERMINATORS = of("village/snowy/terminators");
	public static final TemplatePools VILLAGE_SNOWY_TOWN_CENTERS = of("village/snowy/town_centers");
	public static final TemplatePools VILLAGE_SNOWY_TREES = of("village/snowy/trees");
	public static final TemplatePools VILLAGE_SNOWY_VILLAGERS = of("village/snowy/villagers");

	// region: Zombie
	public static final TemplatePools VILLAGE_SNOWY_ZOMBIE_DECOR = of("village/snowy/zombie/decor");
	public static final TemplatePools VILLAGE_SNOWY_ZOMBIE_HOUSES = of("village/snowy/zombie/houses");
	public static final TemplatePools VILLAGE_SNOWY_ZOMBIE_STREETS = of("village/snowy/zombie/streets");
	public static final TemplatePools VILLAGE_SNOWY_ZOMBIE_VILLAGERS = of("village/snowy/zombie/villagers");
	// endregion
	// endregion

	// region: Taiga
	public static final TemplatePools VILLAGE_TAIGA_DECOR = of("village/taiga/decor");
	public static final TemplatePools VILLAGE_TAIGA_HOUSES = of("village/taiga/houses");
	public static final TemplatePools VILLAGE_TAIGA_STREETS = of("village/taiga/streets");
	public static final TemplatePools VILLAGE_TAIGA_TERMINATORS = of("village/taiga/terminators");
	public static final TemplatePools VILLAGE_TAIGA_TOWN_CENTERS = of("village/taiga/town_centers");
	public static final TemplatePools VILLAGE_TAIGA_VILLAGERS = of("village/taiga/villagers");

	// region: Zombie
	public static final TemplatePools VILLAGE_TAIGA_ZOMBIE_DECOR = of("village/taiga/zombie/decor");
	public static final TemplatePools VILLAGE_TAIGA_ZOMBIE_HOUSES = of("village/taiga/zombie/houses");
	public static final TemplatePools VILLAGE_TAIGA_ZOMBIE_STREETS = of("village/taiga/zombie/streets");
	public static final TemplatePools VILLAGE_TAIGA_ZOMBIE_VILLAGERS = of("village/taiga/zombie/villagers");
	// endregion
	// endregion
	// endregion

	public final ResourceLocation poolName;

	private TemplatePools(ResourceLocation poolName)
	{
		this.poolName = poolName;
	}

	public boolean is(@Nullable TemplatePools other)
	{
		return equals(other);
	}

	public boolean isEmpty()
	{
		return is(EMPTY);
	}

	// region: Getters
	public String getNamespace()
	{
		return poolName.getNamespace();
	}

	public String getPath()
	{
		return poolName.getPath();
	}

	public ResourceLocation getPoolName()
	{
		return poolName;
	}

	@Override
	public String getSerializedName()
	{
		return poolName.toString();
	}
	// endregion

	// region: Comparable
	@Override
	public int compareTo(TemplatePools other)
	{
		return poolName.compareTo(other.poolName);
	}

	public int compareToNamespaceFirst(TemplatePools other)
	{
		return poolName.compareNamespaced(other.poolName);
	}
	// endregion

	// region: Object
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj instanceof TemplatePools)
			return equals((TemplatePools) obj);
		return false;
	}

	public boolean equals(@Nullable TemplatePools other)
	{
		return other != null && poolName.equals(other.poolName);
	}

	@Override
	public int hashCode()
	{
		return poolName.hashCode();
	}

	@Override
	public String toString()
	{
		return poolName.toString();
	}
	// endregion

	public static TemplatePools of(ResourceLocation poolName)
	{
		return new TemplatePools(poolName);
	}

	public static TemplatePools of(String poolNamespace, String poolPath)
	{
		return of(new ResourceLocation(poolNamespace, poolPath));
	}

	private static TemplatePools of(String poolPath)
	{
		return of(IMod.MINECRAFT.id(poolPath));
	}
}
