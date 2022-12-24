package xyz.apex.minecraft.testmod.forge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import xyz.apex.minecraft.apexcore.shared.util.Tags;
import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllItems;
import xyz.apex.minecraft.testmod.shared.init.AllTags;

public final class ItemTagGenerator extends ItemTagsProvider
{
    ItemTagGenerator(GatherDataEvent event, PackOutput output, BlockTagGenerator blockTags)
    {
        super(output, event.getLookupProvider(), blockTags, TestMod.ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider)
    {
        leadArmorTools();
        leadIngot();
        leadNugget();
        rawLeadOre();
        copyBlockTags();
    }

    private void leadArmorTools()
    {
        var leadSword = AllItems.LEAD_SWORD.get();
        var leadPickaxe = AllItems.LEAD_PICKAXE.get();
        var leadAxe = AllItems.LEAD_AXE.get();
        var leadShovel = AllItems.LEAD_SHOVEL.get();
        var leadHoe = AllItems.LEAD_HOE.get();

        tag(Tags.Items.Forge.TOOLS_SWORDS).add(leadSword);
        tag(Tags.Items.Forge.TOOLS_PICKAXES).add(leadPickaxe);
        tag(Tags.Items.Forge.TOOLS_AXES).add(leadAxe);
        tag(Tags.Items.Forge.TOOLS_SHOVELS).add(leadShovel);
        tag(Tags.Items.Forge.TOOLS_HOES).add(leadHoe);
        tag(Tags.Items.Forge.ARMORS_HELMETS).add(AllItems.LEAD_HELMET.get());
        tag(Tags.Items.Forge.ARMORS_CHESTPLATES).add(AllItems.LEAD_CHESTPLATE.get());
        tag(Tags.Items.Forge.ARMORS_LEGGINGS).add(AllItems.LEAD_LEGGINGS.get());
        tag(Tags.Items.Forge.ARMORS_BOOTS).add(AllItems.LEAD_BOOTS.get());
        tag(Tags.Items.Forge.ARMORS).add(AllItems.LEAD_HORSE_ARMOR.get());

        tag(Tags.Items.Fabric.SWORDS).add(leadSword);
        tag(Tags.Items.Fabric.PICKAXES).add(leadPickaxe);
        tag(Tags.Items.Fabric.AXES).add(leadAxe);
        tag(Tags.Items.Fabric.SHOVELS).add(leadShovel);
        tag(Tags.Items.Fabric.HOES).add(leadHoe);
    }

    private void leadIngot()
    {
        var leadIngot = AllItems.LEAD_INGOT.get();
        tag(Tags.Items.Forge.INGOTS).addTag(AllTags.Items.Forge.INGOTS_LEAD);
        tag(AllTags.Items.Forge.INGOTS_LEAD).add(leadIngot);
        tag(AllTags.Items.Fabric.LEAD_INGOT).add(leadIngot);
        tag(AllTags.Items.Fabric.LEAD_INGOTS).add(leadIngot);
    }

    private void leadNugget()
    {
        var leadNugget = AllItems.LEAD_NUGGET.get();
        tag(Tags.Items.Forge.NUGGETS).addTag(AllTags.Items.Forge.NUGGETS_LEAD);
        tag(AllTags.Items.Forge.NUGGETS_LEAD).add(leadNugget);
        tag(AllTags.Items.Fabric.LEAD_NUGGET).add(leadNugget);
        tag(AllTags.Items.Fabric.LEAD_NUGGETS).add(leadNugget);
    }

    private void rawLeadOre()
    {
        var rawLead = AllItems.RAW_LEAD.get();
        tag(AllTags.Items.Fabric.RAW_LEAD_ORE).add(rawLead);
        tag(AllTags.Items.Fabric.RAW_LEAD_ORES).add(rawLead);
    }

    private void copyBlockTags()
    {
        copy(Tags.Blocks.Forge.ORES, Tags.Items.Forge.ORES);
        copy(Tags.Blocks.Forge.STORAGE_BLOCKS, Tags.Items.Forge.STORAGE_BLOCKS);
        copy(Tags.Blocks.Fabric.ORES, Tags.Items.Fabric.ORES);

        copy(AllTags.Blocks.Forge.ORES_LEAD, AllTags.Items.Forge.ORES_LEAD);
        copy(AllTags.Blocks.Forge.STORAGE_BLOCKS_LEAD, AllTags.Items.Forge.STORAGE_BLOCKS_LEAD);

        copy(AllTags.Blocks.Fabric.LEAD_ORE, AllTags.Items.Fabric.LEAD_ORE);
        copy(AllTags.Blocks.Fabric.LEAD_ORES, AllTags.Items.Fabric.LEAD_ORES);
        copy(AllTags.Blocks.Fabric.LEAD_BLOCK, AllTags.Items.Fabric.LEAD_BLOCK);
        copy(AllTags.Blocks.Fabric.LEAD_BLOCKS, AllTags.Items.Fabric.LEAD_BLOCKS);
        copy(AllTags.Blocks.Fabric.RAW_LEAD_BLOCK, AllTags.Items.Fabric.RAW_LEAD_BLOCK);
        copy(AllTags.Blocks.Fabric.RAW_LEAD_BLOCKS, AllTags.Items.Fabric.RAW_LEAD_BLOCKS);
    }
}
