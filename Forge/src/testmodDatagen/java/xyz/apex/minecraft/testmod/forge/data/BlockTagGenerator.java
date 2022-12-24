package xyz.apex.minecraft.testmod.forge.data;

import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import xyz.apex.minecraft.apexcore.shared.util.Tags;
import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllTags;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

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

        tag(Tags.Blocks.Forge.ORES, AllTags.Blocks.Forge.ORES_LEAD);
        tag(Tags.Blocks.Forge.STORAGE_BLOCKS, AllTags.Blocks.Forge.STORAGE_BLOCKS_LEAD);
        tag(Tags.Blocks.Fabric.ORES, AllTags.Blocks.Fabric.LEAD_ORE, AllTags.Blocks.Fabric.LEAD_ORES);

        tag(AllTags.Blocks.Forge.ORES_LEAD, leadOre, deepslateLeadOre);
        tag(AllTags.Blocks.Fabric.LEAD_ORE, leadOre, deepslateLeadOre);
        tag(AllTags.Blocks.Fabric.LEAD_ORES, leadOre, deepslateLeadOre);

        tag(AllTags.Blocks.Forge.STORAGE_BLOCKS_LEAD, leadBlock, rawLeadBlock);
        tag(AllTags.Blocks.Fabric.LEAD_BLOCK, leadBlock);
        tag(AllTags.Blocks.Fabric.LEAD_BLOCKS, leadBlock);
        tag(AllTags.Blocks.Fabric.RAW_LEAD_BLOCK, rawLeadBlock);
        tag(AllTags.Blocks.Fabric.RAW_LEAD_BLOCKS, rawLeadBlock);
    }

    private void tag(TagKey<Block> tag, @Nullable Object... values)
    {
        if(values == null || values.length == 0) return;
        var builder = tag(tag);
        Arrays.stream(values).filter(Objects::nonNull).forEach(value -> tag(builder, value));
    }

    private void tag(IntrinsicTagAppender<Block> builder, Object obj)
    {
        if(obj instanceof TagKey<?> valueTag)
        {
            if(valueTag.isFor(ForgeRegistries.Keys.BLOCKS)) builder.addOptionalTag(valueTag.location());
        }
        else if(obj instanceof Block block)
        {
            var registryName = ForgeRegistries.BLOCKS.getKey(block);
            if(registryName != null) builder.addOptional(registryName);
        }
        else if(obj instanceof Supplier<?> supplier) tag(builder, supplier.get()); // should wrap back around to Block instanceof branch
        else LogManager.getLogger().error("Unknown ObjectType: {}", obj.getClass().getCanonicalName());
    }
}
