package dev.apexstudios.testmod.common.ref;

import dev.apexstudios.apexcore.common.registry.holder.DeferredBlock;
import dev.apexstudios.testmod.common.TestMod;
import dev.apexstudios.testmod.common.block.BlockWithEntity;
import dev.apexstudios.testmod.common.block.entity.TestBlockEntity;
import net.minecraft.world.level.block.Block;

public interface TestBlocks
{
    DeferredBlock<Block> TEST_BLOCK = TestMod.REGISTER.object("test_block").block().color(() -> () -> (blockState, level, pos, tintIndex) -> -1).defaultItem().register();

    // @formatter:off
    DeferredBlock<BlockWithEntity> BLOCK_WITH_ENTITY = TestMod.REGISTER
            .object("block_with_entity")
            .block(BlockWithEntity::new)
            .defaultItem()
            .defaultBlockEntity(TestBlockEntity::new)
    .register();
    // @formatter:on
}
