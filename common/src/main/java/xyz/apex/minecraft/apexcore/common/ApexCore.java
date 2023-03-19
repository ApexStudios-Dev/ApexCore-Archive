package xyz.apex.minecraft.apexcore.common;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.apex.minecraft.apexcore.common.util.TagHelper;

public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();

    interface BlockTags
    {
        TagKey<Block> IMMOVABLE = TagHelper.apexBlockTag("immovable");
    }
}
