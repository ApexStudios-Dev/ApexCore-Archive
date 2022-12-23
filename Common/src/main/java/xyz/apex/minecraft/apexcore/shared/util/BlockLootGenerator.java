package xyz.apex.minecraft.testmod.forge.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

import java.util.Set;

public final class BlockLootGenerator extends BlockLootSubProvider
{
    BlockLootGenerator()
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

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
        return Platform.INSTANCE.registries().getAllKnown(Registries.BLOCK, TestMod.ID);
    }
}
