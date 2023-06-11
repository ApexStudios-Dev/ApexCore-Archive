package xyz.apex.minecraft.apexcore.common.core;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.helper.TagHelper;

@ApiStatus.NonExtendable
public interface ApexTags
{
    @ApiStatus.NonExtendable
    interface Blocks
    {
        TagKey<Block> PLACEMENT_VISUALIZER = TagHelper.apexBlockTag("placement_visualizer");

        private static void bootstrap()
        {
        }
    }

    @DoNotCall
    @ApiStatus.Internal
    static void bootstrap()
    {
    }
}
