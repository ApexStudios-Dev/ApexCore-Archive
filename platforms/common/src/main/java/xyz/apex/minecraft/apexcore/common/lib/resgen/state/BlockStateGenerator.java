package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.gson.JsonElement;
import net.minecraft.world.level.block.Block;

public interface BlockStateGenerator
{
    Block block();

    JsonElement toJson();
}
