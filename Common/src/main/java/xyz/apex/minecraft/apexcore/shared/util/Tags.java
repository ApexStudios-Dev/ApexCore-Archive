package xyz.apex.minecraft.apexcore.shared.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;

@SuppressWarnings({ "JavadocReference", "JavadocBlankLines" })
public interface Tags
{
    interface Blocks
    {
        interface Forge
        {
            TagKey<Block> BARRELS = forge("barrels");
            TagKey<Block> BARRELS_WOODEN = forge("barrels/wooden");
            TagKey<Block> BOOKSHELVES = forge("bookshelves");
            TagKey<Block> CHESTS = forge("chests");
            TagKey<Block> CHESTS_ENDER = forge("chests/ender");
            TagKey<Block> CHESTS_TRAPPED = forge("chests/trapped");
            TagKey<Block> CHESTS_WOODEN = forge("chests/wooden");
            TagKey<Block> COBBLESTONE = forge("cobblestone");
            TagKey<Block> COBBLESTONE_NORMAL = forge("cobblestone/normal");
            TagKey<Block> COBBLESTONE_INFESTED = forge("cobblestone/infested");
            TagKey<Block> COBBLESTONE_MOSSY = forge("cobblestone/mossy");
            TagKey<Block> COBBLESTONE_DEEPSLATE = forge("cobblestone/deepslate");
            TagKey<Block> END_STONES = forge("end_stones");
            TagKey<Block> ENDERMAN_PLACE_ON_BLACKLIST = forge("enderman_place_on_blacklist");
            TagKey<Block> FENCE_GATES = forge("fence_gates");
            TagKey<Block> FENCE_GATES_WOODEN = forge("fence_gates/wooden");
            TagKey<Block> FENCES = forge("fences");
            TagKey<Block> FENCES_NETHER_BRICK = forge("fences/nether_brick");
            TagKey<Block> FENCES_WOODEN = forge("fences/wooden");
            TagKey<Block> GLASS = forge("glass");
            TagKey<Block> GLASS_BLACK = forge("glass/black");
            TagKey<Block> GLASS_BLUE = forge("glass/blue");
            TagKey<Block> GLASS_BROWN = forge("glass/brown");
            TagKey<Block> GLASS_COLORLESS = forge("glass/colorless");
            TagKey<Block> GLASS_CYAN = forge("glass/cyan");
            TagKey<Block> GLASS_GRAY = forge("glass/gray");
            TagKey<Block> GLASS_GREEN = forge("glass/green");
            TagKey<Block> GLASS_LIGHT_BLUE = forge("glass/light_blue");
            TagKey<Block> GLASS_LIGHT_GRAY = forge("glass/light_gray");
            TagKey<Block> GLASS_LIME = forge("glass/lime");
            TagKey<Block> GLASS_MAGENTA = forge("glass/magenta");
            TagKey<Block> GLASS_ORANGE = forge("glass/orange");
            TagKey<Block> GLASS_PINK = forge("glass/pink");
            TagKey<Block> GLASS_PURPLE = forge("glass/purple");
            TagKey<Block> GLASS_RED = forge("glass/red");
            /**
             * Glass which is made from sand and only minor additional ingredients like dyes
             */
            TagKey<Block> GLASS_SILICA = forge("glass/silica");
            TagKey<Block> GLASS_TINTED = forge("glass/tinted");
            TagKey<Block> GLASS_WHITE = forge("glass/white");
            TagKey<Block> GLASS_YELLOW = forge("glass/yellow");
            TagKey<Block> GLASS_PANES = forge("glass_panes");
            TagKey<Block> GLASS_PANES_BLACK = forge("glass_panes/black");
            TagKey<Block> GLASS_PANES_BLUE = forge("glass_panes/blue");
            TagKey<Block> GLASS_PANES_BROWN = forge("glass_panes/brown");
            TagKey<Block> GLASS_PANES_COLORLESS = forge("glass_panes/colorless");
            TagKey<Block> GLASS_PANES_CYAN = forge("glass_panes/cyan");
            TagKey<Block> GLASS_PANES_GRAY = forge("glass_panes/gray");
            TagKey<Block> GLASS_PANES_GREEN = forge("glass_panes/green");
            TagKey<Block> GLASS_PANES_LIGHT_BLUE = forge("glass_panes/light_blue");
            TagKey<Block> GLASS_PANES_LIGHT_GRAY = forge("glass_panes/light_gray");
            TagKey<Block> GLASS_PANES_LIME = forge("glass_panes/lime");
            TagKey<Block> GLASS_PANES_MAGENTA = forge("glass_panes/magenta");
            TagKey<Block> GLASS_PANES_ORANGE = forge("glass_panes/orange");
            TagKey<Block> GLASS_PANES_PINK = forge("glass_panes/pink");
            TagKey<Block> GLASS_PANES_PURPLE = forge("glass_panes/purple");
            TagKey<Block> GLASS_PANES_RED = forge("glass_panes/red");
            TagKey<Block> GLASS_PANES_WHITE = forge("glass_panes/white");
            TagKey<Block> GLASS_PANES_YELLOW = forge("glass_panes/yellow");
            TagKey<Block> GRAVEL = forge("gravel");
            TagKey<Block> NETHERRACK = forge("netherrack");
            TagKey<Block> OBSIDIAN = forge("obsidian");
            /**
             * Blocks which are often replaced by deepslate ores, i.e. the ores in the tag {@link #ORES_IN_GROUND_DEEPSLATE}, during world generation
             */
            TagKey<Block> ORE_BEARING_GROUND_DEEPSLATE = forge("ore_bearing_ground/deepslate");
            /**
             * Blocks which are often replaced by netherrack ores, i.e. the ores in the tag {@link #ORES_IN_GROUND_NETHERRACK}, during world generation
             */
            TagKey<Block> ORE_BEARING_GROUND_NETHERRACK = forge("ore_bearing_ground/netherrack");
            /**
             * Blocks which are often replaced by stone ores, i.e. the ores in the tag {@link #ORES_IN_GROUND_STONE}, during world generation
             */
            TagKey<Block> ORE_BEARING_GROUND_STONE = forge("ore_bearing_ground/stone");
            /**
             * Ores which on average result in more than one resource worth of materials
             */
            TagKey<Block> ORE_RATES_DENSE = forge("ore_rates/dense");
            /**
             * Ores which on average result in one resource worth of materials
             */
            TagKey<Block> ORE_RATES_SINGULAR = forge("ore_rates/singular");
            /**
             * Ores which on average result in less than one resource worth of materials
             */
            TagKey<Block> ORE_RATES_SPARSE = forge("ore_rates/sparse");
            TagKey<Block> ORES = forge("ores");
            TagKey<Block> ORES_COAL = forge("ores/coal");
            TagKey<Block> ORES_COPPER = forge("ores/copper");
            TagKey<Block> ORES_DIAMOND = forge("ores/diamond");
            TagKey<Block> ORES_EMERALD = forge("ores/emerald");
            TagKey<Block> ORES_GOLD = forge("ores/gold");
            TagKey<Block> ORES_IRON = forge("ores/iron");
            TagKey<Block> ORES_LAPIS = forge("ores/lapis");
            TagKey<Block> ORES_NETHERITE_SCRAP = forge("ores/netherite_scrap");
            TagKey<Block> ORES_QUARTZ = forge("ores/quartz");
            TagKey<Block> ORES_REDSTONE = forge("ores/redstone");
            /**
             * Ores in deepslate (or in equivalent blocks in the tag {@link #ORE_BEARING_GROUND_DEEPSLATE}) which could logically use deepslate as recipe input or output
             */
            TagKey<Block> ORES_IN_GROUND_DEEPSLATE = forge("ores_in_ground/deepslate");
            /**
             * Ores in netherrack (or in equivalent blocks in the tag {@link #ORE_BEARING_GROUND_NETHERRACK}) which could logically use netherrack as recipe input or output
             */
            TagKey<Block> ORES_IN_GROUND_NETHERRACK = forge("ores_in_ground/netherrack");
            /**
             * Ores in stone (or in equivalent blocks in the tag {@link #ORE_BEARING_GROUND_STONE}) which could logically use stone as recipe input or output
             */
            TagKey<Block> ORES_IN_GROUND_STONE = forge("ores_in_ground/stone");
            TagKey<Block> SAND = forge("sand");
            TagKey<Block> SAND_COLORLESS = forge("sand/colorless");
            TagKey<Block> SAND_RED = forge("sand/red");
            TagKey<Block> SANDSTONE = forge("sandstone");
            TagKey<Block> STAINED_GLASS = forge("stained_glass");
            TagKey<Block> STAINED_GLASS_PANES = forge("stained_glass_panes");
            TagKey<Block> STONE = forge("stone");
            TagKey<Block> STORAGE_BLOCKS = forge("storage_blocks");
            TagKey<Block> STORAGE_BLOCKS_AMETHYST = forge("storage_blocks/amethyst");
            TagKey<Block> STORAGE_BLOCKS_COAL = forge("storage_blocks/coal");
            TagKey<Block> STORAGE_BLOCKS_COPPER = forge("storage_blocks/copper");
            TagKey<Block> STORAGE_BLOCKS_DIAMOND = forge("storage_blocks/diamond");
            TagKey<Block> STORAGE_BLOCKS_EMERALD = forge("storage_blocks/emerald");
            TagKey<Block> STORAGE_BLOCKS_GOLD = forge("storage_blocks/gold");
            TagKey<Block> STORAGE_BLOCKS_IRON = forge("storage_blocks/iron");
            TagKey<Block> STORAGE_BLOCKS_LAPIS = forge("storage_blocks/lapis");
            TagKey<Block> STORAGE_BLOCKS_NETHERITE = forge("storage_blocks/netherite");
            TagKey<Block> STORAGE_BLOCKS_QUARTZ = forge("storage_blocks/quartz");
            TagKey<Block> STORAGE_BLOCKS_RAW_COPPER = forge("storage_blocks/raw_copper");
            TagKey<Block> STORAGE_BLOCKS_RAW_GOLD = forge("storage_blocks/raw_gold");
            TagKey<Block> STORAGE_BLOCKS_RAW_IRON = forge("storage_blocks/raw_iron");
            TagKey<Block> STORAGE_BLOCKS_REDSTONE = forge("storage_blocks/redstone");
            TagKey<Block> NEEDS_WOOD_TOOL = forge("needs_wood_tool");
            TagKey<Block> NEEDS_GOLD_TOOL = forge("needs_gold_tool");
            TagKey<Block> NEEDS_NETHERITE_TOOL = forge("needs_netherite_tool");

            private static void bootstrap() {}
        }

        interface Fabric
        {
            private static void bootstrap() {}
        }

        interface Vanilla
        {
            TagKey<Block> WOOL = BlockTags.WOOL;
            TagKey<Block> PLANKS = BlockTags.PLANKS;
            TagKey<Block> STONE_BRICKS = BlockTags.STONE_BRICKS;
            TagKey<Block> WOODEN_BUTTONS = BlockTags.WOODEN_BUTTONS;
            TagKey<Block> BUTTONS = BlockTags.BUTTONS;
            TagKey<Block> WOOL_CARPETS = BlockTags.WOOL_CARPETS;
            TagKey<Block> WOODEN_DOORS = BlockTags.WOODEN_DOORS;
            TagKey<Block> WOODEN_STAIRS = BlockTags.WOODEN_STAIRS;
            TagKey<Block> WOODEN_SLABS = BlockTags.WOODEN_SLABS;
            TagKey<Block> WOODEN_FENCES = BlockTags.WOODEN_FENCES;
            TagKey<Block> PRESSURE_PLATES = BlockTags.PRESSURE_PLATES;
            TagKey<Block> WOODEN_PRESSURE_PLATES = BlockTags.WOODEN_PRESSURE_PLATES;
            TagKey<Block> STONE_PRESSURE_PLATES = BlockTags.STONE_PRESSURE_PLATES;
            TagKey<Block> WOODEN_TRAPDOORS = BlockTags.WOODEN_TRAPDOORS;
            TagKey<Block> DOORS = BlockTags.DOORS;
            TagKey<Block> SAPLINGS = BlockTags.SAPLINGS;
            TagKey<Block> LOGS_THAT_BURN = BlockTags.LOGS_THAT_BURN;
            TagKey<Block> OVERWORLD_NATURAL_LOGS = BlockTags.OVERWORLD_NATURAL_LOGS;
            TagKey<Block> LOGS = BlockTags.LOGS;
            TagKey<Block> DARK_OAK_LOGS = BlockTags.DARK_OAK_LOGS;
            TagKey<Block> OAK_LOGS = BlockTags.OAK_LOGS;
            TagKey<Block> BIRCH_LOGS = BlockTags.BIRCH_LOGS;
            TagKey<Block> ACACIA_LOGS = BlockTags.ACACIA_LOGS;
            TagKey<Block> JUNGLE_LOGS = BlockTags.JUNGLE_LOGS;
            TagKey<Block> SPRUCE_LOGS = BlockTags.SPRUCE_LOGS;
            TagKey<Block> MANGROVE_LOGS = BlockTags.MANGROVE_LOGS;
            TagKey<Block> CRIMSON_STEMS = BlockTags.CRIMSON_STEMS;
            TagKey<Block> WARPED_STEMS = BlockTags.WARPED_STEMS;
            TagKey<Block> BAMBOO_BLOCKS = BlockTags.BAMBOO_BLOCKS;
            TagKey<Block> WART_BLOCKS = BlockTags.WART_BLOCKS;
            TagKey<Block> BANNERS = BlockTags.BANNERS;
            TagKey<Block> SAND = BlockTags.SAND;
            TagKey<Block> STAIRS = BlockTags.STAIRS;
            TagKey<Block> SLABS = BlockTags.SLABS;
            TagKey<Block> WALLS = BlockTags.WALLS;
            TagKey<Block> ANVIL = BlockTags.ANVIL;
            TagKey<Block> RAILS = BlockTags.RAILS;
            TagKey<Block> LEAVES = BlockTags.LEAVES;
            TagKey<Block> TRAPDOORS = BlockTags.TRAPDOORS;
            TagKey<Block> SMALL_FLOWERS = BlockTags.SMALL_FLOWERS;
            TagKey<Block> BEDS = BlockTags.BEDS;
            TagKey<Block> FENCES = BlockTags.FENCES;
            TagKey<Block> TALL_FLOWERS = BlockTags.TALL_FLOWERS;
            TagKey<Block> FLOWERS = BlockTags.FLOWERS;
            TagKey<Block> PIGLIN_REPELLENTS = BlockTags.PIGLIN_REPELLENTS;
            TagKey<Block> GOLD_ORES = BlockTags.GOLD_ORES;
            TagKey<Block> IRON_ORES = BlockTags.IRON_ORES;
            TagKey<Block> DIAMOND_ORES = BlockTags.DIAMOND_ORES;
            TagKey<Block> REDSTONE_ORES = BlockTags.REDSTONE_ORES;
            TagKey<Block> LAPIS_ORES = BlockTags.LAPIS_ORES;
            TagKey<Block> COAL_ORES = BlockTags.COAL_ORES;
            TagKey<Block> EMERALD_ORES = BlockTags.EMERALD_ORES;
            TagKey<Block> COPPER_ORES = BlockTags.COPPER_ORES;
            TagKey<Block> CANDLES = BlockTags.CANDLES;
            TagKey<Block> DIRT = BlockTags.DIRT;
            TagKey<Block> TERRACOTTA = BlockTags.TERRACOTTA;
            TagKey<Block> COMPLETES_FIND_TREE_TUTORIAL = BlockTags.COMPLETES_FIND_TREE_TUTORIAL;
            TagKey<Block> FLOWER_POTS = BlockTags.FLOWER_POTS;
            TagKey<Block> ENDERMAN_HOLDABLE = BlockTags.ENDERMAN_HOLDABLE;
            TagKey<Block> ICE = BlockTags.ICE;
            TagKey<Block> VALID_SPAWN = BlockTags.VALID_SPAWN;
            TagKey<Block> IMPERMEABLE = BlockTags.IMPERMEABLE;
            TagKey<Block> UNDERWATER_BONEMEALS = BlockTags.UNDERWATER_BONEMEALS;
            TagKey<Block> CORAL_BLOCKS = BlockTags.CORAL_BLOCKS;
            TagKey<Block> WALL_CORALS = BlockTags.WALL_CORALS;
            TagKey<Block> CORAL_PLANTS = BlockTags.CORAL_PLANTS;
            TagKey<Block> CORALS = BlockTags.CORALS;
            TagKey<Block> BAMBOO_PLANTABLE_ON = BlockTags.BAMBOO_PLANTABLE_ON;
            TagKey<Block> STANDING_SIGNS = BlockTags.STANDING_SIGNS;
            TagKey<Block> WALL_SIGNS = BlockTags.WALL_SIGNS;
            TagKey<Block> SIGNS = BlockTags.SIGNS;
            TagKey<Block> CEILING_HANGING_SIGNS = BlockTags.CEILING_HANGING_SIGNS;
            TagKey<Block> WALL_HANGING_SIGNS = BlockTags.WALL_HANGING_SIGNS;
            TagKey<Block> ALL_HANGING_SIGNS = BlockTags.ALL_HANGING_SIGNS;
            TagKey<Block> ALL_SIGNS = BlockTags.ALL_SIGNS;
            TagKey<Block> DRAGON_IMMUNE = BlockTags.DRAGON_IMMUNE;
            TagKey<Block> DRAGON_TRANSPARENT = BlockTags.DRAGON_TRANSPARENT;
            TagKey<Block> WITHER_IMMUNE = BlockTags.WITHER_IMMUNE;
            TagKey<Block> WITHER_SUMMON_BASE_BLOCKS = BlockTags.WITHER_SUMMON_BASE_BLOCKS;
            TagKey<Block> BEEHIVES = BlockTags.BEEHIVES;
            TagKey<Block> CROPS = BlockTags.CROPS;
            TagKey<Block> BEE_GROWABLES = BlockTags.BEE_GROWABLES;
            TagKey<Block> PORTALS = BlockTags.PORTALS;
            TagKey<Block> FIRE = BlockTags.FIRE;
            TagKey<Block> NYLIUM = BlockTags.NYLIUM;
            TagKey<Block> BEACON_BASE_BLOCKS = BlockTags.BEACON_BASE_BLOCKS;
            TagKey<Block> SOUL_SPEED_BLOCKS = BlockTags.SOUL_SPEED_BLOCKS;
            TagKey<Block> WALL_POST_OVERRIDE = BlockTags.WALL_POST_OVERRIDE;
            TagKey<Block> CLIMBABLE = BlockTags.CLIMBABLE;
            TagKey<Block> FALL_DAMAGE_RESETTING = BlockTags.FALL_DAMAGE_RESETTING;
            TagKey<Block> SHULKER_BOXES = BlockTags.SHULKER_BOXES;
            TagKey<Block> HOGLIN_REPELLENTS = BlockTags.HOGLIN_REPELLENTS;
            TagKey<Block> SOUL_FIRE_BASE_BLOCKS = BlockTags.SOUL_FIRE_BASE_BLOCKS;
            TagKey<Block> STRIDER_WARM_BLOCKS = BlockTags.STRIDER_WARM_BLOCKS;
            TagKey<Block> CAMPFIRES = BlockTags.CAMPFIRES;
            TagKey<Block> GUARDED_BY_PIGLINS = BlockTags.GUARDED_BY_PIGLINS;
            TagKey<Block> PREVENT_MOB_SPAWNING_INSIDE = BlockTags.PREVENT_MOB_SPAWNING_INSIDE;
            TagKey<Block> FENCE_GATES = BlockTags.FENCE_GATES;
            TagKey<Block> UNSTABLE_BOTTOM_CENTER = BlockTags.UNSTABLE_BOTTOM_CENTER;
            TagKey<Block> MUSHROOM_GROW_BLOCK = BlockTags.MUSHROOM_GROW_BLOCK;
            TagKey<Block> INFINIBURN_OVERWORLD = BlockTags.INFINIBURN_OVERWORLD;
            TagKey<Block> INFINIBURN_NETHER = BlockTags.INFINIBURN_NETHER;
            TagKey<Block> INFINIBURN_END = BlockTags.INFINIBURN_END;
            TagKey<Block> BASE_STONE_OVERWORLD = BlockTags.BASE_STONE_OVERWORLD;
            TagKey<Block> STONE_ORE_REPLACEABLES = BlockTags.STONE_ORE_REPLACEABLES;
            TagKey<Block> DEEPSLATE_ORE_REPLACEABLES = BlockTags.DEEPSLATE_ORE_REPLACEABLES;
            TagKey<Block> BASE_STONE_NETHER = BlockTags.BASE_STONE_NETHER;
            TagKey<Block> OVERWORLD_CARVER_REPLACEABLES = BlockTags.OVERWORLD_CARVER_REPLACEABLES;
            TagKey<Block> NETHER_CARVER_REPLACEABLES = BlockTags.NETHER_CARVER_REPLACEABLES;
            TagKey<Block> CANDLE_CAKES = BlockTags.CANDLE_CAKES;
            TagKey<Block> CAULDRONS = BlockTags.CAULDRONS;
            TagKey<Block> CRYSTAL_SOUND_BLOCKS = BlockTags.CRYSTAL_SOUND_BLOCKS;
            TagKey<Block> INSIDE_STEP_SOUND_BLOCKS = BlockTags.INSIDE_STEP_SOUND_BLOCKS;
            TagKey<Block> OCCLUDES_VIBRATION_SIGNALS = BlockTags.OCCLUDES_VIBRATION_SIGNALS;
            TagKey<Block> DAMPENS_VIBRATIONS = BlockTags.DAMPENS_VIBRATIONS;
            TagKey<Block> DRIPSTONE_REPLACEABLE = BlockTags.DRIPSTONE_REPLACEABLE;
            TagKey<Block> CAVE_VINES = BlockTags.CAVE_VINES;
            TagKey<Block> MOSS_REPLACEABLE = BlockTags.MOSS_REPLACEABLE;
            TagKey<Block> LUSH_GROUND_REPLACEABLE = BlockTags.LUSH_GROUND_REPLACEABLE;
            TagKey<Block> AZALEA_ROOT_REPLACEABLE = BlockTags.AZALEA_ROOT_REPLACEABLE;
            TagKey<Block> SMALL_DRIPLEAF_PLACEABLE = BlockTags.SMALL_DRIPLEAF_PLACEABLE;
            TagKey<Block> BIG_DRIPLEAF_PLACEABLE = BlockTags.BIG_DRIPLEAF_PLACEABLE;
            TagKey<Block> SNOW = BlockTags.SNOW;
            TagKey<Block> MINEABLE_WITH_AXE = BlockTags.MINEABLE_WITH_AXE;
            TagKey<Block> MINEABLE_WITH_HOE = BlockTags.MINEABLE_WITH_HOE;
            TagKey<Block> MINEABLE_WITH_PICKAXE = BlockTags.MINEABLE_WITH_PICKAXE;
            TagKey<Block> MINEABLE_WITH_SHOVEL = BlockTags.MINEABLE_WITH_SHOVEL;
            TagKey<Block> NEEDS_DIAMOND_TOOL = BlockTags.NEEDS_DIAMOND_TOOL;
            TagKey<Block> NEEDS_IRON_TOOL = BlockTags.NEEDS_IRON_TOOL;
            TagKey<Block> NEEDS_STONE_TOOL = BlockTags.NEEDS_STONE_TOOL;
            TagKey<Block> FEATURES_CANNOT_REPLACE = BlockTags.FEATURES_CANNOT_REPLACE;
            TagKey<Block> LAVA_POOL_STONE_CANNOT_REPLACE = BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE;
            TagKey<Block> GEODE_INVALID_BLOCKS = BlockTags.GEODE_INVALID_BLOCKS;
            TagKey<Block> FROG_PREFER_JUMP_TO = BlockTags.FROG_PREFER_JUMP_TO;
            TagKey<Block> SCULK_REPLACEABLE = BlockTags.SCULK_REPLACEABLE;
            TagKey<Block> SCULK_REPLACEABLE_WORLD_GEN = BlockTags.SCULK_REPLACEABLE_WORLD_GEN;
            TagKey<Block> ANCIENT_CITY_REPLACEABLE = BlockTags.ANCIENT_CITY_REPLACEABLE;
            TagKey<Block> ANIMALS_SPAWNABLE_ON = BlockTags.ANIMALS_SPAWNABLE_ON;
            TagKey<Block> AXOLOTLS_SPAWNABLE_ON = BlockTags.AXOLOTLS_SPAWNABLE_ON;
            TagKey<Block> GOATS_SPAWNABLE_ON = BlockTags.GOATS_SPAWNABLE_ON;
            TagKey<Block> MOOSHROOMS_SPAWNABLE_ON = BlockTags.MOOSHROOMS_SPAWNABLE_ON;
            TagKey<Block> PARROTS_SPAWNABLE_ON = BlockTags.PARROTS_SPAWNABLE_ON;
            TagKey<Block> POLAR_BEARS_SPAWNABLE_ON_ALTERNATE = BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE;
            TagKey<Block> RABBITS_SPAWNABLE_ON = BlockTags.RABBITS_SPAWNABLE_ON;
            TagKey<Block> FOXES_SPAWNABLE_ON = BlockTags.FOXES_SPAWNABLE_ON;
            TagKey<Block> WOLVES_SPAWNABLE_ON = BlockTags.WOLVES_SPAWNABLE_ON;
            TagKey<Block> FROGS_SPAWNABLE_ON = BlockTags.FROGS_SPAWNABLE_ON;
            TagKey<Block> AZALEA_GROWS_ON = BlockTags.AZALEA_GROWS_ON;
            TagKey<Block> REPLACEABLE_PLANTS = BlockTags.REPLACEABLE_PLANTS;
            TagKey<Block> CONVERTABLE_TO_MUD = BlockTags.CONVERTABLE_TO_MUD;
            TagKey<Block> MANGROVE_LOGS_CAN_GROW_THROUGH = BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH;
            TagKey<Block> MANGROVE_ROOTS_CAN_GROW_THROUGH = BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH;
            TagKey<Block> DEAD_BUSH_MAY_PLACE_ON = BlockTags.DEAD_BUSH_MAY_PLACE_ON;
            TagKey<Block> SNAPS_GOAT_HORN = BlockTags.SNAPS_GOAT_HORN;
            TagKey<Block> SNOW_LAYER_CANNOT_SURVIVE_ON = BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON;
            TagKey<Block> SNOW_LAYER_CAN_SURVIVE_ON = BlockTags.SNOW_LAYER_CAN_SURVIVE_ON;
            TagKey<Block> INVALID_SPAWN_INSIDE = BlockTags.INVALID_SPAWN_INSIDE;

            private static void bootstrap() {}
        }

        static TagKey<Block> tag(String namespace, String name)
        {
            return Tags.tag(Registries.BLOCK, namespace, name);
        }

        static TagKey<Block> forge(String name)
        {
            return Tags.forge(Registries.BLOCK, name);
        }

        static TagKey<Block> fabric(String name)
        {
            return Tags.fabric(Registries.BLOCK, name);
        }

        private static void bootstrap()
        {
            Biomes.Forge.bootstrap();
            Biomes.Fabric.bootstrap();
            Biomes.Vanilla.bootstrap();
        }
    }

    interface Items
    {
        interface Forge
        {
            TagKey<Item> BARRELS = forge("barrels");
            TagKey<Item> BARRELS_WOODEN = forge("barrels/wooden");
            TagKey<Item> BONES = forge("bones");
            TagKey<Item> BOOKSHELVES = forge("bookshelves");
            TagKey<Item> CHESTS = forge("chests");
            TagKey<Item> CHESTS_ENDER = forge("chests/ender");
            TagKey<Item> CHESTS_TRAPPED = forge("chests/trapped");
            TagKey<Item> CHESTS_WOODEN = forge("chests/wooden");
            TagKey<Item> COBBLESTONE = forge("cobblestone");
            TagKey<Item> COBBLESTONE_NORMAL = forge("cobblestone/normal");
            TagKey<Item> COBBLESTONE_INFESTED = forge("cobblestone/infested");
            TagKey<Item> COBBLESTONE_MOSSY = forge("cobblestone/mossy");
            TagKey<Item> COBBLESTONE_DEEPSLATE = forge("cobblestone/deepslate");
            TagKey<Item> CROPS = forge("crops");
            TagKey<Item> CROPS_BEETROOT = forge("crops/beetroot");
            TagKey<Item> CROPS_CARROT = forge("crops/carrot");
            TagKey<Item> CROPS_NETHER_WART = forge("crops/nether_wart");
            TagKey<Item> CROPS_POTATO = forge("crops/potato");
            TagKey<Item> CROPS_WHEAT = forge("crops/wheat");
            TagKey<Item> DUSTS = forge("dusts");
            TagKey<Item> DUSTS_PRISMARINE = forge("dusts/prismarine");
            TagKey<Item> DUSTS_REDSTONE = forge("dusts/redstone");
            TagKey<Item> DUSTS_GLOWSTONE = forge("dusts/glowstone");
            TagKey<Item> DYES = forge("dyes");
            TagKey<Item> DYES_BLACK = forge("dyes/black");
            TagKey<Item> DYES_RED = forge("dyes/red");
            TagKey<Item> DYES_GREEN = forge("dyes/green");
            TagKey<Item> DYES_BROWN = forge("dyes/brown");
            TagKey<Item> DYES_BLUE = forge("dyes/blue");
            TagKey<Item> DYES_PURPLE = forge("dyes/purple");
            TagKey<Item> DYES_CYAN = forge("dyes/cyan");
            TagKey<Item> DYES_LIGHT_GRAY = forge("dyes/light_gray");
            TagKey<Item> DYES_GRAY = forge("dyes/gray");
            TagKey<Item> DYES_PINK = forge("dyes/pink");
            TagKey<Item> DYES_LIME = forge("dyes/lime");
            TagKey<Item> DYES_YELLOW = forge("dyes/yellow");
            TagKey<Item> DYES_LIGHT_BLUE = forge("dyes/light_blue");
            TagKey<Item> DYES_MAGENTA = forge("dyes/magenta");
            TagKey<Item> DYES_ORANGE = forge("dyes/orange");
            TagKey<Item> DYES_WHITE = forge("dyes/white");
            TagKey<Item> EGGS = forge("eggs");
            /**
             * This tag defaults to {@link net.minecraft.world.item.Items#LAPIS_LAZULI} when not present in any datapacks, including forge client on vanilla server
             */
            TagKey<Item> ENCHANTING_FUELS = forge("enchanting_fuels");
            TagKey<Item> END_STONES = forge("end_stones");
            TagKey<Item> ENDER_PEARLS = forge("ender_pearls");
            TagKey<Item> FEATHERS = forge("feathers");
            TagKey<Item> FENCE_GATES = forge("fence_gates");
            TagKey<Item> FENCE_GATES_WOODEN = forge("fence_gates/wooden");
            TagKey<Item> FENCES = forge("fences");
            TagKey<Item> FENCES_NETHER_BRICK = forge("fences/nether_brick");
            TagKey<Item> FENCES_WOODEN = forge("fences/wooden");
            TagKey<Item> GEMS = forge("gems");
            TagKey<Item> GEMS_DIAMOND = forge("gems/diamond");
            TagKey<Item> GEMS_EMERALD = forge("gems/emerald");
            TagKey<Item> GEMS_AMETHYST = forge("gems/amethyst");
            TagKey<Item> GEMS_LAPIS = forge("gems/lapis");
            TagKey<Item> GEMS_PRISMARINE = forge("gems/prismarine");
            TagKey<Item> GEMS_QUARTZ = forge("gems/quartz");
            TagKey<Item> GLASS = forge("glass");
            TagKey<Item> GLASS_BLACK = forge("glass/black");
            TagKey<Item> GLASS_BLUE = forge("glass/blue");
            TagKey<Item> GLASS_BROWN = forge("glass/brown");
            TagKey<Item> GLASS_COLORLESS = forge("glass/colorless");
            TagKey<Item> GLASS_CYAN = forge("glass/cyan");
            TagKey<Item> GLASS_GRAY = forge("glass/gray");
            TagKey<Item> GLASS_GREEN = forge("glass/green");
            TagKey<Item> GLASS_LIGHT_BLUE = forge("glass/light_blue");
            TagKey<Item> GLASS_LIGHT_GRAY = forge("glass/light_gray");
            TagKey<Item> GLASS_LIME = forge("glass/lime");
            TagKey<Item> GLASS_MAGENTA = forge("glass/magenta");
            TagKey<Item> GLASS_ORANGE = forge("glass/orange");
            TagKey<Item> GLASS_PINK = forge("glass/pink");
            TagKey<Item> GLASS_PURPLE = forge("glass/purple");
            TagKey<Item> GLASS_RED = forge("glass/red");
            /**
             * Glass which is made from sand and only minor additional ingredients like dyes
             */
            TagKey<Item> GLASS_SILICA = forge("glass/silica");
            TagKey<Item> GLASS_TINTED = forge("glass/tinted");
            TagKey<Item> GLASS_WHITE = forge("glass/white");
            TagKey<Item> GLASS_YELLOW = forge("glass/yellow");
            TagKey<Item> GLASS_PANES = forge("glass_panes");
            TagKey<Item> GLASS_PANES_BLACK = forge("glass_panes/black");
            TagKey<Item> GLASS_PANES_BLUE = forge("glass_panes/blue");
            TagKey<Item> GLASS_PANES_BROWN = forge("glass_panes/brown");
            TagKey<Item> GLASS_PANES_COLORLESS = forge("glass_panes/colorless");
            TagKey<Item> GLASS_PANES_CYAN = forge("glass_panes/cyan");
            TagKey<Item> GLASS_PANES_GRAY = forge("glass_panes/gray");
            TagKey<Item> GLASS_PANES_GREEN = forge("glass_panes/green");
            TagKey<Item> GLASS_PANES_LIGHT_BLUE = forge("glass_panes/light_blue");
            TagKey<Item> GLASS_PANES_LIGHT_GRAY = forge("glass_panes/light_gray");
            TagKey<Item> GLASS_PANES_LIME = forge("glass_panes/lime");
            TagKey<Item> GLASS_PANES_MAGENTA = forge("glass_panes/magenta");
            TagKey<Item> GLASS_PANES_ORANGE = forge("glass_panes/orange");
            TagKey<Item> GLASS_PANES_PINK = forge("glass_panes/pink");
            TagKey<Item> GLASS_PANES_PURPLE = forge("glass_panes/purple");
            TagKey<Item> GLASS_PANES_RED = forge("glass_panes/red");
            TagKey<Item> GLASS_PANES_WHITE = forge("glass_panes/white");
            TagKey<Item> GLASS_PANES_YELLOW = forge("glass_panes/yellow");
            TagKey<Item> GRAVEL = forge("gravel");
            TagKey<Item> GUNPOWDER = forge("gunpowder");
            TagKey<Item> HEADS = forge("heads");
            TagKey<Item> INGOTS = forge("ingots");
            TagKey<Item> INGOTS_BRICK = forge("ingots/brick");
            TagKey<Item> INGOTS_COPPER = forge("ingots/copper");
            TagKey<Item> INGOTS_GOLD = forge("ingots/gold");
            TagKey<Item> INGOTS_IRON = forge("ingots/iron");
            TagKey<Item> INGOTS_NETHERITE = forge("ingots/netherite");
            TagKey<Item> INGOTS_NETHER_BRICK = forge("ingots/nether_brick");
            TagKey<Item> LEATHER = forge("leather");
            TagKey<Item> MUSHROOMS = forge("mushrooms");
            TagKey<Item> NETHER_STARS = forge("nether_stars");
            TagKey<Item> NETHERRACK = forge("netherrack");
            TagKey<Item> NUGGETS = forge("nuggets");
            TagKey<Item> NUGGETS_GOLD = forge("nuggets/gold");
            TagKey<Item> NUGGETS_IRON = forge("nuggets/iron");
            TagKey<Item> OBSIDIAN = forge("obsidian");
            /**
             * Blocks which are often replaced by deepslate ores, i.e. the ores in the tag {@link #ORES_IN_GROUND_DEEPSLATE}, during world generation
             */
            TagKey<Item> ORE_BEARING_GROUND_DEEPSLATE = forge("ore_bearing_ground/deepslate");
            /**
             * Blocks which are often replaced by netherrack ores, i.e. the ores in the tag {@link #ORES_IN_GROUND_NETHERRACK}, during world generation
             */
            TagKey<Item> ORE_BEARING_GROUND_NETHERRACK = forge("ore_bearing_ground/netherrack");
            /**
             * Blocks which are often replaced by stone ores, i.e. the ores in the tag {@link #ORES_IN_GROUND_STONE}, during world generation
             */
            TagKey<Item> ORE_BEARING_GROUND_STONE = forge("ore_bearing_ground/stone");
            /**
             * Ores which on average result in more than one resource worth of materials
             */
            TagKey<Item> ORE_RATES_DENSE = forge("ore_rates/dense");
            /**
             * Ores which on average result in one resource worth of materials
             */
            TagKey<Item> ORE_RATES_SINGULAR = forge("ore_rates/singular");
            /**
             * Ores which on average result in less than one resource worth of materials
             */
            TagKey<Item> ORE_RATES_SPARSE = forge("ore_rates/sparse");
            TagKey<Item> ORES = forge("ores");
            TagKey<Item> ORES_COAL = forge("ores/coal");
            TagKey<Item> ORES_COPPER = forge("ores/copper");
            TagKey<Item> ORES_DIAMOND = forge("ores/diamond");
            TagKey<Item> ORES_EMERALD = forge("ores/emerald");
            TagKey<Item> ORES_GOLD = forge("ores/gold");
            TagKey<Item> ORES_IRON = forge("ores/iron");
            TagKey<Item> ORES_LAPIS = forge("ores/lapis");
            TagKey<Item> ORES_NETHERITE_SCRAP = forge("ores/netherite_scrap");
            TagKey<Item> ORES_QUARTZ = forge("ores/quartz");
            TagKey<Item> ORES_REDSTONE = forge("ores/redstone");
            /**
             * Ores in deepslate (or in equivalent blocks in the tag {@link #ORE_BEARING_GROUND_DEEPSLATE}) which could logically use deepslate as recipe input or output
             */
            TagKey<Item> ORES_IN_GROUND_DEEPSLATE = forge("ores_in_ground/deepslate");
            /**
             * Ores in netherrack (or in equivalent blocks in the tag {@link #ORE_BEARING_GROUND_NETHERRACK}) which could logically use netherrack as recipe input or output
             */
            TagKey<Item> ORES_IN_GROUND_NETHERRACK = forge("ores_in_ground/netherrack");
            /**
             * Ores in stone (or in equivalent blocks in the tag {@link #ORE_BEARING_GROUND_STONE}) which could logically use stone as recipe input or output
             */
            TagKey<Item> ORES_IN_GROUND_STONE = forge("ores_in_ground/stone");
            TagKey<Item> RAW_MATERIALS = forge("raw_materials");
            TagKey<Item> RAW_MATERIALS_COPPER = forge("raw_materials/copper");
            TagKey<Item> RAW_MATERIALS_GOLD = forge("raw_materials/gold");
            TagKey<Item> RAW_MATERIALS_IRON = forge("raw_materials/iron");
            TagKey<Item> RODS = forge("rods");
            TagKey<Item> RODS_BLAZE = forge("rods/blaze");
            TagKey<Item> RODS_WOODEN = forge("rods/wooden");
            TagKey<Item> SAND = forge("sand");
            TagKey<Item> SAND_COLORLESS = forge("sand/colorless");
            TagKey<Item> SAND_RED = forge("sand/red");
            TagKey<Item> SANDSTONE = forge("sandstone");
            TagKey<Item> SEEDS = forge("seeds");
            TagKey<Item> SEEDS_BEETROOT = forge("seeds/beetroot");
            TagKey<Item> SEEDS_MELON = forge("seeds/melon");
            TagKey<Item> SEEDS_PUMPKIN = forge("seeds/pumpkin");
            TagKey<Item> SEEDS_WHEAT = forge("seeds/wheat");
            TagKey<Item> SHEARS = forge("shears");
            TagKey<Item> SLIMEBALLS = forge("slimeballs");
            TagKey<Item> STAINED_GLASS = forge("stained_glass");
            TagKey<Item> STAINED_GLASS_PANES = forge("stained_glass_panes");
            TagKey<Item> STONE = forge("stone");
            TagKey<Item> STORAGE_BLOCKS = forge("storage_blocks");
            TagKey<Item> STORAGE_BLOCKS_AMETHYST = forge("storage_blocks/amethyst");
            TagKey<Item> STORAGE_BLOCKS_COAL = forge("storage_blocks/coal");
            TagKey<Item> STORAGE_BLOCKS_COPPER = forge("storage_blocks/copper");
            TagKey<Item> STORAGE_BLOCKS_DIAMOND = forge("storage_blocks/diamond");
            TagKey<Item> STORAGE_BLOCKS_EMERALD = forge("storage_blocks/emerald");
            TagKey<Item> STORAGE_BLOCKS_GOLD = forge("storage_blocks/gold");
            TagKey<Item> STORAGE_BLOCKS_IRON = forge("storage_blocks/iron");
            TagKey<Item> STORAGE_BLOCKS_LAPIS = forge("storage_blocks/lapis");
            TagKey<Item> STORAGE_BLOCKS_NETHERITE = forge("storage_blocks/netherite");
            TagKey<Item> STORAGE_BLOCKS_QUARTZ = forge("storage_blocks/quartz");
            TagKey<Item> STORAGE_BLOCKS_RAW_COPPER = forge("storage_blocks/raw_copper");
            TagKey<Item> STORAGE_BLOCKS_RAW_GOLD = forge("storage_blocks/raw_gold");
            TagKey<Item> STORAGE_BLOCKS_RAW_IRON = forge("storage_blocks/raw_iron");
            TagKey<Item> STORAGE_BLOCKS_REDSTONE = forge("storage_blocks/redstone");
            TagKey<Item> STRING = forge("string");
            /**
             * A tag containing all existing tools.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS = forge("tools");
            /**
             * A tag containing all existing swords.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_SWORDS = forge("tools/swords");
            /**
             * A tag containing all existing axes.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_AXES = forge("tools/axes");
            /**
             * A tag containing all existing pickaxes.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_PICKAXES = forge("tools/pickaxes");
            /**
             * A tag containing all existing shovels.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_SHOVELS = forge("tools/shovels");
            /**
             * A tag containing all existing hoes.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_HOES = forge("tools/hoes");
            /**
             * A tag containing all existing shields.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_SHIELDS = forge("tools/shields");
            /**
             * A tag containing all existing bows.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_BOWS = forge("tools/bows");
            /**
             * A tag containing all existing crossbows.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_CROSSBOWS = forge("tools/crossbows");
            /**
             * A tag containing all existing fishing rods.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_FISHING_RODS = forge("tools/fishing_rods");
            /**
             * A tag containing all existing tridents.
             *
             * Note: This tag is not an alternative or a substitute to {@link net.minecraftforge.common.ToolActions}.
             *
             * @see net.minecraftforge.common.ToolAction
             * @see net.minecraftforge.common.ToolActions
             */
            TagKey<Item> TOOLS_TRIDENTS = forge("tools/tridents");
            /**
             * A tag containing all existing armors.
             */
            TagKey<Item> ARMORS = forge("armors");
            /**
             * A tag containing all existing helmets.
             */
            TagKey<Item> ARMORS_HELMETS = forge("armors/helmets");
            /**
             * A tag containing all chestplates.
             */
            TagKey<Item> ARMORS_CHESTPLATES = forge("armors/chestplates");
            /**
             * A tag containing all existing leggings.
             */
            TagKey<Item> ARMORS_LEGGINGS = forge("armors/leggings");
            /**
             * A tag containing all existing boots.
             */
            TagKey<Item> ARMORS_BOOTS = forge("armors/boots");

            private static void bootstrap() {}
        }

        interface Fabric
        {
            private static void bootstrap() {}
        }

        interface Vanilla
        {
            TagKey<Item> WOOL = ItemTags.WOOL;
            TagKey<Item> PLANKS = ItemTags.PLANKS;
            TagKey<Item> STONE_BRICKS = ItemTags.STONE_BRICKS;
            TagKey<Item> WOODEN_BUTTONS = ItemTags.WOODEN_BUTTONS;
            TagKey<Item> BUTTONS = ItemTags.BUTTONS;
            TagKey<Item> WOOL_CARPETS = ItemTags.WOOL_CARPETS;
            TagKey<Item> WOODEN_DOORS = ItemTags.WOODEN_DOORS;
            TagKey<Item> WOODEN_STAIRS = ItemTags.WOODEN_STAIRS;
            TagKey<Item> WOODEN_SLABS = ItemTags.WOODEN_SLABS;
            TagKey<Item> WOODEN_FENCES = ItemTags.WOODEN_FENCES;
            TagKey<Item> FENCE_GATES = ItemTags.FENCE_GATES;
            TagKey<Item> WOODEN_PRESSURE_PLATES = ItemTags.WOODEN_PRESSURE_PLATES;
            TagKey<Item> WOODEN_TRAPDOORS = ItemTags.WOODEN_TRAPDOORS;
            TagKey<Item> DOORS = ItemTags.DOORS;
            TagKey<Item> SAPLINGS = ItemTags.SAPLINGS;
            TagKey<Item> LOGS_THAT_BURN = ItemTags.LOGS_THAT_BURN;
            TagKey<Item> LOGS = ItemTags.LOGS;
            TagKey<Item> DARK_OAK_LOGS = ItemTags.DARK_OAK_LOGS;
            TagKey<Item> OAK_LOGS = ItemTags.OAK_LOGS;
            TagKey<Item> BIRCH_LOGS = ItemTags.BIRCH_LOGS;
            TagKey<Item> ACACIA_LOGS = ItemTags.ACACIA_LOGS;
            TagKey<Item> JUNGLE_LOGS = ItemTags.JUNGLE_LOGS;
            TagKey<Item> SPRUCE_LOGS = ItemTags.SPRUCE_LOGS;
            TagKey<Item> MANGROVE_LOGS = ItemTags.MANGROVE_LOGS;
            TagKey<Item> CRIMSON_STEMS = ItemTags.CRIMSON_STEMS;
            TagKey<Item> WARPED_STEMS = ItemTags.WARPED_STEMS;
            TagKey<Item> BAMBOO_BLOCKS = ItemTags.BAMBOO_BLOCKS;
            TagKey<Item> WART_BLOCKS = ItemTags.WART_BLOCKS;
            TagKey<Item> BANNERS = ItemTags.BANNERS;
            TagKey<Item> SAND = ItemTags.SAND;
            TagKey<Item> STAIRS = ItemTags.STAIRS;
            TagKey<Item> SLABS = ItemTags.SLABS;
            TagKey<Item> WALLS = ItemTags.WALLS;
            TagKey<Item> ANVIL = ItemTags.ANVIL;
            TagKey<Item> RAILS = ItemTags.RAILS;
            TagKey<Item> LEAVES = ItemTags.LEAVES;
            TagKey<Item> TRAPDOORS = ItemTags.TRAPDOORS;
            TagKey<Item> SMALL_FLOWERS = ItemTags.SMALL_FLOWERS;
            TagKey<Item> BEDS = ItemTags.BEDS;
            TagKey<Item> FENCES = ItemTags.FENCES;
            TagKey<Item> TALL_FLOWERS = ItemTags.TALL_FLOWERS;
            TagKey<Item> FLOWERS = ItemTags.FLOWERS;
            TagKey<Item> PIGLIN_REPELLENTS = ItemTags.PIGLIN_REPELLENTS;
            TagKey<Item> PIGLIN_LOVED = ItemTags.PIGLIN_LOVED;
            TagKey<Item> IGNORED_BY_PIGLIN_BABIES = ItemTags.IGNORED_BY_PIGLIN_BABIES;
            TagKey<Item> PIGLIN_FOOD = ItemTags.PIGLIN_FOOD;
            TagKey<Item> FOX_FOOD = ItemTags.FOX_FOOD;
            TagKey<Item> GOLD_ORES = ItemTags.GOLD_ORES;
            TagKey<Item> IRON_ORES = ItemTags.IRON_ORES;
            TagKey<Item> DIAMOND_ORES = ItemTags.DIAMOND_ORES;
            TagKey<Item> REDSTONE_ORES = ItemTags.REDSTONE_ORES;
            TagKey<Item> LAPIS_ORES = ItemTags.LAPIS_ORES;
            TagKey<Item> COAL_ORES = ItemTags.COAL_ORES;
            TagKey<Item> EMERALD_ORES = ItemTags.EMERALD_ORES;
            TagKey<Item> COPPER_ORES = ItemTags.COPPER_ORES;
            TagKey<Item> NON_FLAMMABLE_WOOD = ItemTags.NON_FLAMMABLE_WOOD;
            TagKey<Item> SOUL_FIRE_BASE_BLOCKS = ItemTags.SOUL_FIRE_BASE_BLOCKS;
            TagKey<Item> CANDLES = ItemTags.CANDLES;
            TagKey<Item> DIRT = ItemTags.DIRT;
            TagKey<Item> TERRACOTTA = ItemTags.TERRACOTTA;
            TagKey<Item> COMPLETES_FIND_TREE_TUTORIAL = ItemTags.COMPLETES_FIND_TREE_TUTORIAL;
            TagKey<Item> BOATS = ItemTags.BOATS;
            TagKey<Item> CHEST_BOATS = ItemTags.CHEST_BOATS;
            TagKey<Item> FISHES = ItemTags.FISHES;
            TagKey<Item> SIGNS = ItemTags.SIGNS;
            TagKey<Item> MUSIC_DISCS = ItemTags.MUSIC_DISCS;
            TagKey<Item> CREEPER_DROP_MUSIC_DISCS = ItemTags.CREEPER_DROP_MUSIC_DISCS;
            TagKey<Item> COALS = ItemTags.COALS;
            TagKey<Item> ARROWS = ItemTags.ARROWS;
            TagKey<Item> LECTERN_BOOKS = ItemTags.LECTERN_BOOKS;
            TagKey<Item> BOOKSHELF_BOOKS = ItemTags.BOOKSHELF_BOOKS;
            TagKey<Item> BEACON_PAYMENT_ITEMS = ItemTags.BEACON_PAYMENT_ITEMS;
            TagKey<Item> STONE_TOOL_MATERIALS = ItemTags.STONE_TOOL_MATERIALS;
            TagKey<Item> STONE_CRAFTING_MATERIALS = ItemTags.STONE_CRAFTING_MATERIALS;
            TagKey<Item> FREEZE_IMMUNE_WEARABLES = ItemTags.FREEZE_IMMUNE_WEARABLES;
            TagKey<Item> AXOLOTL_TEMPT_ITEMS = ItemTags.AXOLOTL_TEMPT_ITEMS;
            TagKey<Item> DAMPENS_VIBRATIONS = ItemTags.DAMPENS_VIBRATIONS;
            TagKey<Item> CLUSTER_MAX_HARVESTABLES = ItemTags.CLUSTER_MAX_HARVESTABLES;
            TagKey<Item> COMPASSES = ItemTags.COMPASSES;
            TagKey<Item> HANGING_SIGNS = ItemTags.HANGING_SIGNS;
            TagKey<Item> CREEPER_IGNITERS = ItemTags.CREEPER_IGNITERS;

            private static void bootstrap() {}
        }

        static TagKey<Item> tag(String namespace, String name)
        {
            return Tags.tag(Registries.ITEM, namespace, name);
        }

        static TagKey<Item> forge(String name)
        {
            return Tags.forge(Registries.ITEM, name);
        }

        static TagKey<Item> fabric(String name)
        {
            return Tags.fabric(Registries.ITEM, name);
        }

        private static void bootstrap()
        {
            Biomes.Forge.bootstrap();
            Biomes.Fabric.bootstrap();
            Biomes.Vanilla.bootstrap();
        }
    }

    interface Fluids
    {
        interface Forge
        {
            /**
             * Holds all fluids related to milk.
             */
            TagKey<Fluid> MILK = forge("milk");
            /**
             * Holds all fluids that are gaseous at room temperature.
             */
            TagKey<Fluid> GASEOUS = forge("gaseous");

            private static void bootstrap() {}
        }

        interface Fabric
        {
            private static void bootstrap() {}
        }

        interface Vanilla
        {
            TagKey<Fluid> WATER = FluidTags.WATER;
            TagKey<Fluid> LAVA = FluidTags.LAVA;

            private static void bootstrap() {}
        }

        static TagKey<Fluid> tag(String namespace, String name)
        {
            return Tags.tag(Registries.FLUID, namespace, name);
        }

        static TagKey<Fluid> forge(String name)
        {
            return Tags.forge(Registries.FLUID, name);
        }

        static TagKey<Fluid> fabric(String name)
        {
            return Tags.fabric(Registries.FLUID, name);
        }

        private static void bootstrap()
        {
            Biomes.Forge.bootstrap();
            Biomes.Fabric.bootstrap();
            Biomes.Vanilla.bootstrap();
        }
    }

    interface EntityTypes
    {
        interface Forge
        {
            TagKey<EntityType<?>> BOSSES = forge("bosses");

            private static void bootstrap() {}
        }

        interface Fabric
        {
            private static void bootstrap() {}
        }

        interface Vanilla
        {
            TagKey<EntityType<?>> SKELETONS = EntityTypeTags.SKELETONS;
            TagKey<EntityType<?>> RAIDERS = EntityTypeTags.RAIDERS;
            TagKey<EntityType<?>> BEEHIVE_INHABITORS = EntityTypeTags.BEEHIVE_INHABITORS;
            TagKey<EntityType<?>> ARROWS = EntityTypeTags.ARROWS;
            TagKey<EntityType<?>> IMPACT_PROJECTILES = EntityTypeTags.IMPACT_PROJECTILES;
            TagKey<EntityType<?>> POWDER_SNOW_WALKABLE_MOBS = EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS;
            TagKey<EntityType<?>> AXOLOTL_ALWAYS_HOSTILES = EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES;
            TagKey<EntityType<?>> AXOLOTL_HUNT_TARGETS = EntityTypeTags.AXOLOTL_HUNT_TARGETS;
            TagKey<EntityType<?>> FREEZE_IMMUNE_ENTITY_TYPES = EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES;
            TagKey<EntityType<?>> FREEZE_HURTS_EXTRA_TYPES = EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES;
            TagKey<EntityType<?>> FROG_FOOD = EntityTypeTags.FROG_FOOD;

            private static void bootstrap() {}
        }

        static TagKey<EntityType<?>> tag(String namespace, String name)
        {
            return Tags.tag(Registries.ENTITY_TYPE, namespace, name);
        }

        static TagKey<EntityType<?>> forge(String name)
        {
            return Tags.forge(Registries.ENTITY_TYPE, name);
        }

        static TagKey<EntityType<?>> fabric(String name)
        {
            return Tags.fabric(Registries.ENTITY_TYPE, name);
        }

        private static void bootstrap()
        {
            Biomes.Forge.bootstrap();
            Biomes.Fabric.bootstrap();
            Biomes.Vanilla.bootstrap();
        }
    }

    interface Biomes
    {
        interface Forge
        {
            TagKey<Biome> IS_HOT = forge("is_hot");
            TagKey<Biome> IS_HOT_OVERWORLD = forge("is_hot/overworld");
            TagKey<Biome> IS_HOT_NETHER = forge("is_hot/nether");
            TagKey<Biome> IS_HOT_END = forge("is_hot/end");
            TagKey<Biome> IS_COLD = forge("is_cold");
            TagKey<Biome> IS_COLD_OVERWORLD = forge("is_cold/overworld");
            TagKey<Biome> IS_COLD_NETHER = forge("is_cold/nether");
            TagKey<Biome> IS_COLD_END = forge("is_cold/end");
            TagKey<Biome> IS_SPARSE = forge("is_sparse");
            TagKey<Biome> IS_SPARSE_OVERWORLD = forge("is_sparse/overworld");
            TagKey<Biome> IS_SPARSE_NETHER = forge("is_sparse/nether");
            TagKey<Biome> IS_SPARSE_END = forge("is_sparse/end");
            TagKey<Biome> IS_DENSE = forge("is_dense");
            TagKey<Biome> IS_DENSE_OVERWORLD = forge("is_dense/overworld");
            TagKey<Biome> IS_DENSE_NETHER = forge("is_dense/nether");
            TagKey<Biome> IS_DENSE_END = forge("is_dense/end");
            TagKey<Biome> IS_WET = forge("is_wet");
            TagKey<Biome> IS_WET_OVERWORLD = forge("is_wet/overworld");
            TagKey<Biome> IS_WET_NETHER = forge("is_wet/nether");
            TagKey<Biome> IS_WET_END = forge("is_wet/end");
            TagKey<Biome> IS_DRY = forge("is_dry");
            TagKey<Biome> IS_DRY_OVERWORLD = forge("is_dry/overworld");
            TagKey<Biome> IS_DRY_NETHER = forge("is_dry/nether");
            TagKey<Biome> IS_DRY_END = forge("is_dry/end");
            TagKey<Biome> IS_CONIFEROUS = forge("is_coniferous");
            TagKey<Biome> IS_SPOOKY = forge("is_spooky");
            TagKey<Biome> IS_DEAD = forge("is_dead");
            TagKey<Biome> IS_LUSH = forge("is_lush");
            TagKey<Biome> IS_MUSHROOM = forge("is_mushroom");
            TagKey<Biome> IS_MAGICAL = forge("is_magical");
            TagKey<Biome> IS_RARE = forge("is_rare");
            TagKey<Biome> IS_PLATEAU = forge("is_plateau");
            TagKey<Biome> IS_MODIFIED = forge("is_modified");
            TagKey<Biome> IS_WATER = forge("is_water");
            TagKey<Biome> IS_DESERT = forge("is_desert");
            TagKey<Biome> IS_PLAINS = forge("is_plains");
            TagKey<Biome> IS_SWAMP = forge("is_swamp");
            TagKey<Biome> IS_SANDY = forge("is_sandy");
            TagKey<Biome> IS_SNOWY = forge("is_snowy");
            TagKey<Biome> IS_WASTELAND = forge("is_wasteland");
            TagKey<Biome> IS_VOID = forge("is_void");
            TagKey<Biome> IS_UNDERGROUND = forge("is_underground");
            TagKey<Biome> IS_CAVE = forge("is_cave");
            TagKey<Biome> IS_PEAK = forge("is_peak");
            TagKey<Biome> IS_SLOPE = forge("is_slope");
            TagKey<Biome> IS_MOUNTAIN = forge("is_mountain");

            private static void bootstrap() {}
        }

        interface Fabric
        {
            private static void bootstrap() {}
        }

        interface Vanilla
        {
            TagKey<Biome> IS_DEEP_OCEAN = BiomeTags.IS_DEEP_OCEAN;
            TagKey<Biome> IS_OCEAN = BiomeTags.IS_OCEAN;
            TagKey<Biome> IS_BEACH = BiomeTags.IS_BEACH;
            TagKey<Biome> IS_RIVER = BiomeTags.IS_RIVER;
            TagKey<Biome> IS_MOUNTAIN = BiomeTags.IS_MOUNTAIN;
            TagKey<Biome> IS_BADLANDS = BiomeTags.IS_BADLANDS;
            TagKey<Biome> IS_HILL = BiomeTags.IS_HILL;
            TagKey<Biome> IS_TAIGA = BiomeTags.IS_TAIGA;
            TagKey<Biome> IS_JUNGLE = BiomeTags.IS_JUNGLE;
            TagKey<Biome> IS_FOREST = BiomeTags.IS_FOREST;
            TagKey<Biome> IS_SAVANNA = BiomeTags.IS_SAVANNA;
            TagKey<Biome> IS_OVERWORLD = BiomeTags.IS_OVERWORLD;
            TagKey<Biome> IS_NETHER = BiomeTags.IS_NETHER;
            TagKey<Biome> IS_END = BiomeTags.IS_END;
            TagKey<Biome> STRONGHOLD_BIASED_TO = BiomeTags.STRONGHOLD_BIASED_TO;
            TagKey<Biome> HAS_BURIED_TREASURE = BiomeTags.HAS_BURIED_TREASURE;
            TagKey<Biome> HAS_DESERT_PYRAMID = BiomeTags.HAS_DESERT_PYRAMID;
            TagKey<Biome> HAS_IGLOO = BiomeTags.HAS_IGLOO;
            TagKey<Biome> HAS_JUNGLE_TEMPLE = BiomeTags.HAS_JUNGLE_TEMPLE;
            TagKey<Biome> HAS_MINESHAFT = BiomeTags.HAS_MINESHAFT;
            TagKey<Biome> HAS_MINESHAFT_MESA = BiomeTags.HAS_MINESHAFT_MESA;
            TagKey<Biome> HAS_OCEAN_MONUMENT = BiomeTags.HAS_OCEAN_MONUMENT;
            TagKey<Biome> HAS_OCEAN_RUIN_COLD = BiomeTags.HAS_OCEAN_RUIN_COLD;
            TagKey<Biome> HAS_OCEAN_RUIN_WARM = BiomeTags.HAS_OCEAN_RUIN_WARM;
            TagKey<Biome> HAS_PILLAGER_OUTPOST = BiomeTags.HAS_PILLAGER_OUTPOST;
            TagKey<Biome> HAS_RUINED_PORTAL_DESERT = BiomeTags.HAS_RUINED_PORTAL_DESERT;
            TagKey<Biome> HAS_RUINED_PORTAL_JUNGLE = BiomeTags.HAS_RUINED_PORTAL_JUNGLE;
            TagKey<Biome> HAS_RUINED_PORTAL_OCEAN = BiomeTags.HAS_RUINED_PORTAL_OCEAN;
            TagKey<Biome> HAS_RUINED_PORTAL_SWAMP = BiomeTags.HAS_RUINED_PORTAL_SWAMP;
            TagKey<Biome> HAS_RUINED_PORTAL_MOUNTAIN = BiomeTags.HAS_RUINED_PORTAL_MOUNTAIN;
            TagKey<Biome> HAS_RUINED_PORTAL_STANDARD = BiomeTags.HAS_RUINED_PORTAL_STANDARD;
            TagKey<Biome> HAS_SHIPWRECK_BEACHED = BiomeTags.HAS_SHIPWRECK_BEACHED;
            TagKey<Biome> HAS_SHIPWRECK = BiomeTags.HAS_SHIPWRECK;
            TagKey<Biome> HAS_STRONGHOLD = BiomeTags.HAS_STRONGHOLD;
            TagKey<Biome> HAS_SWAMP_HUT = BiomeTags.HAS_SWAMP_HUT;
            TagKey<Biome> HAS_VILLAGE_DESERT = BiomeTags.HAS_VILLAGE_DESERT;
            TagKey<Biome> HAS_VILLAGE_PLAINS = BiomeTags.HAS_VILLAGE_PLAINS;
            TagKey<Biome> HAS_VILLAGE_SAVANNA = BiomeTags.HAS_VILLAGE_SAVANNA;
            TagKey<Biome> HAS_VILLAGE_SNOWY = BiomeTags.HAS_VILLAGE_SNOWY;
            TagKey<Biome> HAS_VILLAGE_TAIGA = BiomeTags.HAS_VILLAGE_TAIGA;
            TagKey<Biome> HAS_WOODLAND_MANSION = BiomeTags.HAS_WOODLAND_MANSION;
            TagKey<Biome> HAS_NETHER_FORTRESS = BiomeTags.HAS_NETHER_FORTRESS;
            TagKey<Biome> HAS_NETHER_FOSSIL = BiomeTags.HAS_NETHER_FOSSIL;
            TagKey<Biome> HAS_BASTION_REMNANT = BiomeTags.HAS_BASTION_REMNANT;
            TagKey<Biome> HAS_ANCIENT_CITY = BiomeTags.HAS_ANCIENT_CITY;
            TagKey<Biome> HAS_RUINED_PORTAL_NETHER = BiomeTags.HAS_RUINED_PORTAL_NETHER;
            TagKey<Biome> HAS_END_CITY = BiomeTags.HAS_END_CITY;
            TagKey<Biome> REQUIRED_OCEAN_MONUMENT_SURROUNDING = BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING;
            TagKey<Biome> MINESHAFT_BLOCKING = BiomeTags.MINESHAFT_BLOCKING;
            TagKey<Biome> PLAYS_UNDERWATER_MUSIC = BiomeTags.PLAYS_UNDERWATER_MUSIC;
            TagKey<Biome> HAS_CLOSER_WATER_FOG = BiomeTags.HAS_CLOSER_WATER_FOG;
            TagKey<Biome> WATER_ON_MAP_OUTLINES = BiomeTags.WATER_ON_MAP_OUTLINES;
            TagKey<Biome> PRODUCES_CORALS_FROM_BONEMEAL = BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL;
            TagKey<Biome> WITHOUT_ZOMBIE_SIEGES = BiomeTags.WITHOUT_ZOMBIE_SIEGES;
            TagKey<Biome> WITHOUT_PATROL_SPAWNS = BiomeTags.WITHOUT_PATROL_SPAWNS;
            TagKey<Biome> WITHOUT_WANDERING_TRADER_SPAWNS = BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS;
            TagKey<Biome> SPAWNS_COLD_VARIANT_FROGS = BiomeTags.SPAWNS_COLD_VARIANT_FROGS;
            TagKey<Biome> SPAWNS_WARM_VARIANT_FROGS = BiomeTags.SPAWNS_WARM_VARIANT_FROGS;
            TagKey<Biome> ONLY_ALLOWS_SNOW_AND_GOLD_RABBITS = BiomeTags.ONLY_ALLOWS_SNOW_AND_GOLD_RABBITS;
            TagKey<Biome> REDUCED_WATER_AMBIENT_SPAWNS = BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS;
            TagKey<Biome> ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT = BiomeTags.ALLOWS_TROPICAL_FISH_SPAWNS_AT_ANY_HEIGHT;
            TagKey<Biome> POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS = BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS;
            TagKey<Biome> MORE_FREQUENT_DROWNED_SPAWNS = BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS;
            TagKey<Biome> ALLOWS_SURFACE_SLIME_SPAWNS = BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS;

            private static void bootstrap() {}
        }

        static TagKey<Biome> tag(String namespace, String name)
        {
            return Tags.tag(Registries.BIOME, namespace, name);
        }

        static TagKey<Biome> forge(String name)
        {
            return Tags.forge(Registries.BIOME, name);
        }

        static TagKey<Biome> fabric(String name)
        {
            return Tags.fabric(Registries.BIOME, name);
        }

        private static void bootstrap()
        {
            Forge.bootstrap();
            Fabric.bootstrap();
            Vanilla.bootstrap();
        }
    }

    static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String namespace, String name)
    {
        return TagKey.create(registryType, new ResourceLocation(namespace, name));
    }

    static <T> TagKey<T> forge(ResourceKey<? extends Registry<T>> registryType, String name)
    {
        return Platform.Type.FORGE.tag(registryType, name);
    }

    static <T> TagKey<T> fabric(ResourceKey<? extends Registry<T>> registryType, String name)
    {
        return Platform.Type.FABRIC.tag(registryType, name);
    }

    static void bootstrap()
    {
    }
}
