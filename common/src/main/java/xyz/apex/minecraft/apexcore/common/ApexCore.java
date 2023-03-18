package xyz.apex.minecraft.apexcore.common;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.util.TagHelper;

public interface ApexCore
{
    String ID = "apexcore";

    interface BlockTags
    {
        TagKey<Block> IMMOVABLE = TagHelper.apexBlockTag("immovable");
    }
}
