package dev.apexstudios.apexcore.common.util;

import dev.apexstudios.apexcore.common.ApexCore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface ApexTags
{
    PlatformTag<Block> BLOCK_IMMOVABLE = TagHelper.BLOCK.platformBuilder(ApexCore.ID, "immovable").withFabric("movement_restricted").withForgeLike("relocation_not_supported").create();
    PlatformTag<BlockEntityType<?>> BLOCK_ENTITY_IMMOVABLE = TagHelper.BLOCK_ENTITY_TYPE.platformBuilder(ApexCore.ID, "immovable").withFabric("movement_restricted").withForgeLike("relocation_not_supported").create();
}
