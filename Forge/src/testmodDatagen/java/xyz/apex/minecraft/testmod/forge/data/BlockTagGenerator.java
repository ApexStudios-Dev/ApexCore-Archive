package xyz.apex.minecraft.testmod.forge.data;

import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.versions.forge.ForgeVersion;

import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;

public final class BlockTagGenerator extends BlockTagsProvider
{
    public static final TagKey<Block> LEAD_ORE = BlockTags.create(new ResourceLocation(ForgeVersion.MOD_ID, "ores/lead"));
    public static final TagKey<Block> LEAD_BLOCK = BlockTags.create(new ResourceLocation(ForgeVersion.MOD_ID, "storage_blocks/lead"));

    BlockTagGenerator(GatherDataEvent event)
    {
        super(event.getGenerator(), TestMod.ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags()
    {
        var leadOre = AllBlocks.LEAD_ORE.get();
        var deepslateLeadOre = AllBlocks.DEEPSLATE_LEAD_ORE.get();
        var leadBlock = AllBlocks.LEAD_BLOCK.get();
        var rawLeadBlock = AllBlocks.RAW_LEAD_BLOCK.get();

        tag(Tags.Blocks.ORES).addTag(LEAD_ORE);// forge:ores + forge:ores/lead
        tag(LEAD_ORE).add(leadOre, deepslateLeadOre);
        tag(BlockTags.create(new ResourceLocation("c", "lead_ore"))).add(leadOre, deepslateLeadOre);
        tag(BlockTags.create(new ResourceLocation("c", "lead_ores"))).add(leadOre, deepslateLeadOre);
        tag(Tags.Blocks.STORAGE_BLOCKS).addTag(LEAD_BLOCK); // forge:storage_blocks + forge:storage_blocks/lead
        tag(LEAD_BLOCK).add(leadBlock, rawLeadBlock);
        tag(BlockTags.create(new ResourceLocation("c", "lead_block"))).add(leadBlock);
        tag(BlockTags.create(new ResourceLocation("c", "lead_blocks"))).add(leadBlock);
        tag(BlockTags.create(new ResourceLocation("c", "raw_lead_block"))).add(rawLeadBlock);
        tag(BlockTags.create(new ResourceLocation("c", "raw_lead_blocks"))).add(rawLeadBlock);
    }
}
