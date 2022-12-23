package xyz.apex.minecraft.testmod.forge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import xyz.apex.minecraft.apexcore.shared.util.Tags;
import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllTags;

public final class BlockTagGenerator extends BlockTagsProvider
{
    BlockTagGenerator(GatherDataEvent event, PackOutput output)
    {
        super(output, event.getLookupProvider(), TestMod.ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider)
    {
        var leadOre = AllBlocks.LEAD_ORE.get();
        var deepslateLeadOre = AllBlocks.DEEPSLATE_LEAD_ORE.get();
        var leadBlock = AllBlocks.LEAD_BLOCK.get();
        var rawLeadBlock = AllBlocks.RAW_LEAD_BLOCK.get();

        tag(Tags.Blocks.Forge.ORES).addTag(AllTags.Blocks.Forge.ORES_LEAD);
        tag(Tags.Blocks.Forge.STORAGE_BLOCKS).addTag(AllTags.Blocks.Forge.STORAGE_BLOCKS_LEAD);

        tag(AllTags.Blocks.Forge.ORES_LEAD).add(leadOre, deepslateLeadOre);
        tag(AllTags.Blocks.Fabric.LEAD_ORE).add(leadOre, deepslateLeadOre);
        tag(AllTags.Blocks.Fabric.LEAD_ORES).add(leadOre, deepslateLeadOre);

        tag(AllTags.Blocks.Forge.STORAGE_BLOCKS_LEAD).add(leadBlock, rawLeadBlock);
        tag(AllTags.Blocks.Fabric.LEAD_BLOCK).add(leadBlock);
        tag(AllTags.Blocks.Fabric.LEAD_BLOCKS).add(leadBlock);
        tag(AllTags.Blocks.Fabric.RAW_LEAD_BLOCK).add(rawLeadBlock);
        tag(AllTags.Blocks.Fabric.RAW_LEAD_BLOCKS).add(rawLeadBlock);
    }
}
