package xyz.apex.minecraft.testmod.shared.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;

import xyz.apex.minecraft.apexcore.shared.multiblock.MultiBlockTypes;
import xyz.apex.minecraft.apexcore.shared.multiblock.SimpleMultiBlock;
import xyz.apex.minecraft.apexcore.shared.registry.builders.BlockBuilders;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntityEntry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntry;
import xyz.apex.minecraft.apexcore.shared.util.Properties;
import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.block.TestBlock;
import xyz.apex.minecraft.testmod.shared.block.entity.TestBlockEntity;

public interface AllBlocks
{
    BlockEntry<DropExperienceBlock> LEAD_ORE = BlockBuilders
            .builder(TestMod.ID, "lead_ore", DropExperienceBlock::new)
            .initialProperties(Properties.BLOCK_ORE)
    .register();

    BlockEntry<DropExperienceBlock> DEEPSLATE_LEAD_ORE = BlockBuilders
            .builder(TestMod.ID, "deepslate_lead_ore", DropExperienceBlock::new)
            .initialProperties(Properties.BLOCK_DEEPSLATE_ORE)
    .register();

    BlockEntry<Block> LEAD_BLOCK = BlockBuilders
            .builder(TestMod.ID, "lead_block")
            .initialProperties(Properties.BLOCK_STORAGE)
    .register();

    BlockEntry<Block> RAW_LEAD_BLOCK = BlockBuilders
            .builder(TestMod.ID, "raw_lead_block")
            .initialProperties(Properties.BLOCK_RAW_ORE)
    .register();

    BlockEntry<SimpleMultiBlock> MULTI_BLOCK = BlockBuilders
            .multiBlock(TestMod.ID, "multi_block", MultiBlockTypes.MB_1x2x1)
            .initialProperties(Properties.BLOCK_DIRT)
            .hitbox(() -> Block.box(0D, 0D, 0D, 16D, 32D, 16D), (current, block, blockState) -> {
                if(block.getMultiBlockType().isOrigin(blockState)) return current;
                return current.move(0D, -1D, 0D);
            })
    .register();

    BlockEntry<TestBlock> TEST_BLOCK = BlockBuilders
            .builder(TestMod.ID, "test_block", TestBlock::new)
            .initialProperties(Properties.BLOCK_PLANKS)
            .blockEntity(TestBlockEntity::new)
    .register();

    BlockEntityEntry<TestBlockEntity> TEST_BLOCK_ENTITY = TEST_BLOCK.asBlockEntity();

    static void bootstrap()
    {
    }
}
