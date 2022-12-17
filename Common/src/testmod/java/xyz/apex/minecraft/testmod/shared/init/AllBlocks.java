package xyz.apex.minecraft.testmod.shared.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;

import xyz.apex.minecraft.apexcore.shared.registry.BlockBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntry;
import xyz.apex.minecraft.testmod.shared.TestMod;

public interface AllBlocks
{
    BlockEntry<DropExperienceBlock> LEAD_ORE = builder("lead_ore", DropExperienceBlock::new)
            .copyPropertiesFrom(() -> Blocks.IRON_ORE)
            .simpleItem()
//                .tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
            .build()
    .register();

    BlockEntry<DropExperienceBlock> DEEPSLATE_LEAD_ORE = builder("deepslate_lead_ore", DropExperienceBlock::new)
            .copyPropertiesFrom(() -> Blocks.DEEPSLATE_IRON_ORE)
            .simpleItem()
//                .tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
            .build()
    .register();

    BlockEntry<Block> LEAD_BLOCK = builder("lead_block")
            .copyPropertiesFrom(() -> Blocks.IRON_BLOCK)
            .simpleItem()
//                .tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
            .build()
    .register();

    BlockEntry<Block> RAW_LEAD_BLOCK = builder("raw_lead_block")
            .copyPropertiesFrom(() -> Blocks.RAW_IRON_BLOCK)
            .simpleItem()
//                .tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
            .build()
    .register();

    static void bootstrap()
    {
    }

    private static <T extends Block, P> BlockBuilder<T, P> builder(String name, P parent, BlockBuilder.BlockFactory<T> itemFactory)
    {
        return BlockBuilder.builder(TestMod.ID, name, parent, itemFactory);
    }

    private static <T extends Block> BlockBuilder<T, Object> builder(String name, BlockBuilder.BlockFactory<T> itemFactory)
    {
        return BlockBuilder.builder(TestMod.ID, name, itemFactory);
    }

    private static BlockBuilder<Block, Object> builder(String name)
    {
        return BlockBuilder.block(TestMod.ID, name);
    }

    private static <P> BlockBuilder<Block, P> builder(String name, P parent)
    {
        return BlockBuilder.block(TestMod.ID, name, parent);
    }
}
