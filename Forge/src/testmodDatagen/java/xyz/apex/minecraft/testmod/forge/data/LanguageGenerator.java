package xyz.apex.minecraft.testmod.forge.data;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

public final class LanguageGenerator extends LanguageProvider
{
    LanguageGenerator(PackOutput output)
    {
        super(output, TestMod.ID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        addBlock(AllBlocks.LEAD_ORE, "Lead Ore");
        addBlock(AllBlocks.DEEPSLATE_LEAD_ORE, "Deepslate Lead Ore");
        addBlock(AllBlocks.LEAD_BLOCK, "Lead Block");
        addBlock(AllBlocks.RAW_LEAD_BLOCK, "Block of Raw Lead");

        addItem(AllItems.LEAD_SWORD, "Lead Sword");
        addItem(AllItems.LEAD_PICKAXE, "Lead Pickaxe");
        addItem(AllItems.LEAD_AXE, "Lead Axe");
        addItem(AllItems.LEAD_SHOVEL, "Lead Shovel");
        addItem(AllItems.LEAD_HOE, "Lead Hoe");
        addItem(AllItems.LEAD_HELMET, "Lead Helmet");
        addItem(AllItems.LEAD_CHESTPLATE, "Lead Chestplate");
        addItem(AllItems.LEAD_LEGGINGS, "Lead Leggings");
        addItem(AllItems.LEAD_BOOTS, "Lead Boots");
        addItem(AllItems.LEAD_HORSE_ARMOR, "Lead Horse Armor");
        addItem(AllItems.LEAD_INGOT, "Lead Ingot");
        addItem(AllItems.LEAD_NUGGET, "Lead Nugget");
        addItem(AllItems.RAW_LEAD, "Raw Lead");
    }
}
