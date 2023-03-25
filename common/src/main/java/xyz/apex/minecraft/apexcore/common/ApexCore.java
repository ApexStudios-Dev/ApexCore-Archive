package xyz.apex.minecraft.apexcore.common;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentTypes;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentTypes;
import xyz.apex.minecraft.apexcore.common.platform.Platform;
import xyz.apex.minecraft.apexcore.common.util.TagHelper;

public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();

    default void bootstrap()
    {
        Platform.bootstrap();
        BlockTags.bootstrap();
        BlockComponentTypes.bootstrap();
        BlockEntityComponentTypes.bootstrap();
    }

    interface BlockTags
    {
        TagKey<Block> IMMOVABLE = TagHelper.apexBlockTag("immovable");

        private static void bootstrap() {}
    }
}
