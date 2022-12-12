package xyz.apex.minecraft.testmod.shared.init;

import net.minecraft.world.item.Item;

import xyz.apex.minecraft.apexcore.shared.registry.ItemBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.entry.ItemEntry;
import xyz.apex.minecraft.testmod.shared.TestMod;

public interface AllItems
{
    ItemEntry<Item> LEAD_INGOT = ItemBuilder
            .item(TestMod.ID, "lead_ingot")
            .tag(
                    AllTags.Items.Forge.INGOTS_LEAD,
                    AllTags.Items.Fabric.LEAD_INGOT, AllTags.Items.Fabric.LEAD_INGOTS
            )
    .register();

    /*EnchancedTier LEAD_ITEM_TIER = TierBuilder.builder().level(2).uses(250).speed(6F).attackDamageBonus(2F).enchantmentValue(14).repairItem(AllItems.LEAD_INGOT).toolLevelTag(BlockTags.NEEDS_IRON_TOOL).build();
    ArmorMaterial LEAD_ARMOR_MATERIAL = ArmorMaterialBuilder.builder(TestMod.ID, "lead").durabilityFromModifier(15).defenseFromSlotProtections(2, 5, 6, 2).enchantmentValue(9).toughness(0F).knockbackResistance(0F).repairItem(AllItems.LEAD_INGOT).build();

    ItemRegistryEntry<BlockItem> LEAD_ORE = TestMod.Registries.ITEMS
            .blockBuilder(AllBlocks.LEAD_ORE)
            // .tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
    .register();

    ItemRegistryEntry<BlockItem> DEEPSLATE_LEAD_ORE = TestMod.Registries.ITEMS
            .blockBuilder(AllBlocks.DEEPSLATE_LEAD_ORE)
            // .tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
    .register();

    ItemRegistryEntry<BlockItem> LEAD_BLOCK = TestMod.Registries.ITEMS
            .blockBuilder(AllBlocks.LEAD_BLOCK)
            // .tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
    .register();

    ItemRegistryEntry<BlockItem> RAW_LEAD_BLOCK = TestMod.Registries.ITEMS
            .blockBuilder(AllBlocks.RAW_LEAD_BLOCK)
            // .tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
    .register();

    ItemRegistryEntry<SwordItem> LEAD_SWORD = TestMod.Registries.ITEMS
            .builders("lead_sword")
            .sword(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();

    ItemRegistryEntry<PickaxeItem> LEAD_PICKAXE = TestMod.Registries.ITEMS
            .builders("lead_pickaxe")
            .pickaxeItem(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_TOOL)
    .register();

    ItemRegistryEntry<AxeItem> LEAD_AXE = TestMod.Registries.ITEMS
            .builders("lead_axe")
            .axeItem(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_TOOL)
    .register();

    ItemRegistryEntry<ShovelItem> LEAD_SHOVEL = TestMod.Registries.ITEMS
            .builders("lead_shovel")
            .shovelItem(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_TOOL)
    .register();

    ItemRegistryEntry<HoeItem> LEAD_HOE = TestMod.Registries.ITEMS
            .builders("lead_hoe")
            .hoeItem(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_TOOL)
    .register();

    ItemRegistryEntry<ArmorItem> LEAD_HELMET = TestMod.Registries.ITEMS
            .builders("lead_helmet")
            .helmetItem(LEAD_ARMOR_MATERIAL)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();

    ItemRegistryEntry<ArmorItem> LEAD_CHESTPLATE = TestMod.Registries.ITEMS
            .builders("lead_chestplate")
            .chestplateItem(LEAD_ARMOR_MATERIAL)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();

    ItemRegistryEntry<ArmorItem> LEAD_LEGGINGS = TestMod.Registries.ITEMS
            .builders("lead_leggings")
            .leggingItem(LEAD_ARMOR_MATERIAL)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();

    ItemRegistryEntry<ArmorItem> LEAD_BOOTS = TestMod.Registries.ITEMS
            .builders("lead_boots")
            .bootsItem(LEAD_ARMOR_MATERIAL)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();

    ItemRegistryEntry<HorseArmorItem> LEAD_HORSE_ARMOR = TestMod.Registries.ITEMS
            .builders("lead_horse_armor")
            .horseArmor(5, "lead")
            .initialProperties(Properties.ITEM_COMBAT)
    .register();

    ItemRegistryEntry<Item> LEAD_INGOT = TestMod.Registries.ITEMS
            .builders("lead_ingot")
            .simpleItem()
            .initialProperties(Properties.ITEM_GENERIC)
    .register();

    ItemRegistryEntry<Item> LEAD_NUGGET = TestMod.Registries.ITEMS
            .builders("lead_nugget")
            .simpleItem()
            .initialProperties(Properties.ITEM_GENERIC)
    .register();

    ItemRegistryEntry<Item> RAW_LEAD = TestMod.Registries.ITEMS
            .builders("raw_lead")
            .simpleItem()
            .initialProperties(Properties.ITEM_GENERIC)
    .register();*/

    static void bootstrap()
    {
    }
}
