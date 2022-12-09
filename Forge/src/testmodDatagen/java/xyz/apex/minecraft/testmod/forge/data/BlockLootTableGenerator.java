package xyz.apex.minecraft.testmod.forge.data;

import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

public final class BlockLootTableGenerator extends VanillaBlockLoot
{
    @Override
    protected void generate()
    {
        add(AllBlocks.LEAD_ORE.get(), block -> createOreDrop(block, AllItems.RAW_LEAD.get()));
        add(AllBlocks.DEEPSLATE_LEAD_ORE.get(), block -> createOreDrop(block, AllItems.RAW_LEAD.get()));

        dropSelf(AllBlocks.LEAD_BLOCK.get());
        dropSelf(AllBlocks.RAW_LEAD_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return TestMod.Registries.BLOCKS.values();
    }
}
