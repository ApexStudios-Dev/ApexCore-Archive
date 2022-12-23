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
        tag(Tags.Items.Forge.TOOLS_SWORDS).add(AllItems.LEAD_SWORD.get());
        tag(Tags.Items.Forge.TOOLS_PICKAXES).add(AllItems.LEAD_PICKAXE.get());
        tag(Tags.Items.Forge.TOOLS_AXES).add(AllItems.LEAD_AXE.get());
        tag(Tags.Items.Forge.TOOLS_SHOVELS).add(AllItems.LEAD_SHOVEL.get());
        tag(Tags.Items.Forge.TOOLS_HOES).add(AllItems.LEAD_HOE.get());
        tag(Tags.Items.Forge.ARMORS_HELMETS).add(AllItems.LEAD_HELMET.get());
        tag(Tags.Items.Forge.ARMORS_CHESTPLATES).add(AllItems.LEAD_CHESTPLATE.get());
        tag(Tags.Items.Forge.ARMORS_LEGGINGS).add(AllItems.LEAD_LEGGINGS.get());
        tag(Tags.Items.Forge.ARMORS_BOOTS).add(AllItems.LEAD_BOOTS.get());
        tag(Tags.Items.Forge.ARMORS).add(AllItems.LEAD_HORSE_ARMOR.get());
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
