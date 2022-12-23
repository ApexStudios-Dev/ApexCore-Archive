package xyz.apex.minecraft.testmod.forge.data;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;

public final class BlockStateGenerator extends BlockStateProvider
{
    BlockStateGenerator(GatherDataEvent event, PackOutput output)
    {
        super(output, TestMod.ID, event.getExistingFileHelper());
    }

    @Override
    protected void registerStatesAndModels()
    {
        simpleBlock(AllBlocks.LEAD_ORE.get());
        simpleBlock(AllBlocks.DEEPSLATE_LEAD_ORE.get());
        simpleBlock(AllBlocks.LEAD_BLOCK.get());
        simpleBlock(AllBlocks.RAW_LEAD_BLOCK.get());
    }
}
