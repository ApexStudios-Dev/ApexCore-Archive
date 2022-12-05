package xyz.apex.minecraft.testmod.shared.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;

import xyz.apex.minecraft.apexcore.shared.registry.block.BlockRegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.Properties;
import xyz.apex.minecraft.testmod.shared.TestMod;

public interface AllBlocks
{
    BlockRegistryEntry<DropExperienceBlock> LEAD_ORE = TestMod.Registries.BLOCKS
            .builder("lead_ore", DropExperienceBlock::new)
            .initialProperties(Properties.BLOCK_ORE)
    .register();

    BlockRegistryEntry<DropExperienceBlock> DEEPSLATE_LEAD_ORE = TestMod.Registries.BLOCKS
            .builder("deepslate_lead_ore", DropExperienceBlock::new)
            .initialProperties(Properties.BLOCK_DEEPSLATE_ORE)
    .register();

    BlockRegistryEntry<Block> LEAD_BLOCK = TestMod.Registries.BLOCKS
            .generic("lead_block")
            .initialProperties(Properties.BLOCK_STORAGE)
    .register();

    BlockRegistryEntry<Block> RAW_LEAD_BLOCK = TestMod.Registries.BLOCKS
            .generic("raw_lead_block")
            .initialProperties(Properties.BLOCK_RAW_ORE)
    .register();

    static void bootstrap()
    {
    }
}
