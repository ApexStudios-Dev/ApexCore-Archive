package dev.apexstudios.testmod.common.ref;

import dev.apexstudios.apexcore.common.registry.holder.DeferredBlockEntityType;
import dev.apexstudios.testmod.common.block.entity.TestBlockEntity;

public interface TestBlockEntities
{
    DeferredBlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY = DeferredBlockEntityType.createBlockEntityType(TestBlocks.BLOCK_WITH_ENTITY.registryName());
}
