package xyz.apex.minecraft.testmod.shared.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;

import xyz.apex.minecraft.apexcore.shared.item.CustomHorseArmorItem;
import xyz.apex.minecraft.apexcore.shared.registry.builders.ArmorMaterialBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.builders.ItemBuilders;
import xyz.apex.minecraft.apexcore.shared.registry.builders.TierBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.entry.ItemEntry;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;
import xyz.apex.minecraft.testmod.shared.TestMod;

public interface AllItems
{
    EnhancedTier LEAD_ITEM_TIER = TierBuilder
            .builder(TestMod.ID, "lead")
            .level(2)
            .uses(250)
            .speed(6F)
            .attackDamageBonus(2F)
            .enchantmentValue(14)
            .repairItem(AllItems.LEAD_INGOT)
            .toolLevelTag(BlockTags.NEEDS_IRON_TOOL)
    .register();

    ArmorMaterial LEAD_ARMOR_MATERIAL = ArmorMaterialBuilder
            .builder(TestMod.ID, "lead")
            .durabilityFromModifier(15)
            .defenseFromSlotProtections(2, 5, 6, 2)
            .enchantmentValue(9)
            .toughness(0F)
            .knockbackResistance(0F)
            .repairItem(AllItems.LEAD_INGOT)
    .register();

    ItemEntry<SwordItem> LEAD_SWORD = ItemBuilders.simpleSword(TestMod.ID, "lead_sword", LEAD_ITEM_TIER, 3, -2.4F);
    ItemEntry<PickaxeItem> LEAD_PICKAXE = ItemBuilders.simplePickaxe(TestMod.ID, "lead_pickaxe", LEAD_ITEM_TIER, 1, -2.8F);
    ItemEntry<AxeItem> LEAD_AXE = ItemBuilders.simpleAxe(TestMod.ID, "lead_axe", LEAD_ITEM_TIER, 6F, -3.1F);
    ItemEntry<ShovelItem> LEAD_SHOVEL = ItemBuilders.simpleShovel(TestMod.ID, "lead_shovel", LEAD_ITEM_TIER, 1.5F, -3F);
    ItemEntry<HoeItem> LEAD_HOE = ItemBuilders.simpleHoe(TestMod.ID, "lead_hoe", LEAD_ITEM_TIER, -2, -1F);
    ItemEntry<ArmorItem> LEAD_HELMET = ItemBuilders.simpleHelmet(TestMod.ID, "lead_helmet", LEAD_ARMOR_MATERIAL);
    ItemEntry<ArmorItem> LEAD_CHESTPLATE = ItemBuilders.simpleChestplate(TestMod.ID, "lead_chestplate", LEAD_ARMOR_MATERIAL);
    ItemEntry<ArmorItem> LEAD_LEGGINGS = ItemBuilders.simpleLeggings(TestMod.ID, "lead_leggings", LEAD_ARMOR_MATERIAL);
    ItemEntry<ArmorItem> LEAD_BOOTS = ItemBuilders.simpleBoots(TestMod.ID, "lead_boots", LEAD_ARMOR_MATERIAL);
    ItemEntry<CustomHorseArmorItem> LEAD_HORSE_ARMOR = ItemBuilders.simpleHorseArmor(TestMod.ID, "lead_horse_armor", 5);
    ItemEntry<Item> LEAD_INGOT = ItemBuilders.simple(TestMod.ID, "lead_ingot");
    ItemEntry<Item> LEAD_NUGGET = ItemBuilders.simple(TestMod.ID, "lead_nugget");
    ItemEntry<Item> RAW_LEAD = ItemBuilders.simple(TestMod.ID, "raw_lead");

    static void bootstrap()
    {
    }
}
