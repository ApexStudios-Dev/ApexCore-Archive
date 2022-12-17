package xyz.apex.minecraft.testmod.shared.init;

import net.minecraft.world.item.Item;

import xyz.apex.minecraft.apexcore.shared.registry.ItemBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.entry.ItemEntry;
import xyz.apex.minecraft.testmod.shared.TestMod;

public interface AllItems
{
    // EnchancedTier LEAD_ITEM_TIER = TierBuilder.builder().level(2).uses(250).speed(6F).attackDamageBonus(2F).enchantmentValue(14).repairItem(AllItems.LEAD_INGOT).toolLevelTag(BlockTags.NEEDS_IRON_TOOL).build();
    // ArmorMaterial LEAD_ARMOR_MATERIAL = ArmorMaterialBuilder.builder(TestMod.ID, "lead").durabilityFromModifier(15).defenseFromSlotProtections(2, 5, 6, 2).enchantmentValue(9).toughness(0F).knockbackResistance(0F).repairItem(AllItems.LEAD_INGOT).build();

    /*ItemEntry<SwordItem> LEAD_SWORD = builder("lead_sword")
            .sword(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();*/

    /*ItemEntry<PickaxeItem> LEAD_PICKAXE = builder("lead_pickaxe")
            .pickaxeItem(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_TOOL)
    .register();*/

    /*ItemEntry<AxeItem> LEAD_AXE = builder("lead_axe")
            .axeItem(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_TOOL)
    .register();*/

    /*ItemEntry<ShovelItem> LEAD_SHOVEL = builder("lead_shovel")
            .shovelItem(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_TOOL)
    .register();*/

    /*ItemEntry<HoeItem> LEAD_HOE = builder("lead_hoe")
            .hoeItem(LEAD_ITEM_TIER)
            .initialProperties(Properties.ITEM_TOOL)
    .register();*/

    /*ItemEntry<ArmorItem> LEAD_HELMET = builder("lead_helmet")
            .helmetItem(LEAD_ARMOR_MATERIAL)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();*/

    /*ItemEntry<ArmorItem> LEAD_CHESTPLATE = builder("lead_chestplate")
            .chestplateItem(LEAD_ARMOR_MATERIAL)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();*/

    /*ItemEntry<ArmorItem> LEAD_LEGGINGS = builder("lead_leggings")
            .leggingItem(LEAD_ARMOR_MATERIAL)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();*/

    /*ItemEntry<ArmorItem> LEAD_BOOTS = builder("lead_boots")
            .bootsItem(LEAD_ARMOR_MATERIAL)
            .initialProperties(Properties.ITEM_COMBAT)
    .register();*/

    /*ItemEntry<HorseArmorItem> LEAD_HORSE_ARMOR = builder("lead_horse_armor")
            .horseArmor(5, "lead")
            .initialProperties(Properties.ITEM_COMBAT)
    .register();*/

    ItemEntry<Item> LEAD_INGOT = builder("lead_ingot")
            .tag(
                    AllTags.Items.Forge.INGOTS_LEAD,
                    AllTags.Items.Fabric.LEAD_INGOT, AllTags.Items.Fabric.LEAD_INGOTS
            )
    .register();

    ItemEntry<Item> LEAD_NUGGET = builder("lead_nugget")
            .tag(
                    AllTags.Items.Forge.NUGGETS_LEAD,
                    AllTags.Items.Fabric.LEAD_NUGGET, AllTags.Items.Fabric.LEAD_NUGGETS
            )
    .register();

    ItemEntry<Item> RAW_LEAD = builder("raw_lead")
            .tag(AllTags.Items.Fabric.RAW_LEAD_ORE, AllTags.Items.Fabric.RAW_LEAD_ORES)
    .register();

    static void bootstrap()
    {
    }

    private static <T extends Item, P> ItemBuilder<T, P> builder(String name, P parent, ItemBuilder.ItemFactory<T> itemFactory)
    {
        return ItemBuilder.builder(TestMod.ID, name, parent, itemFactory);
    }

    private static <T extends Item> ItemBuilder<T, Object> builder(String name, ItemBuilder.ItemFactory<T> itemFactory)
    {
        return ItemBuilder.builder(TestMod.ID, name, itemFactory);
    }

    private static ItemBuilder<Item, Object> builder(String name)
    {
        return ItemBuilder.item(TestMod.ID, name);
    }

    private static <P> ItemBuilder<Item, P> builder(String name, P parent)
    {
        return ItemBuilder.item(TestMod.ID, name, parent);
    }
}
