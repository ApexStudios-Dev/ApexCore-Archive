package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.gson.JsonArray;
import org.jetbrains.annotations.ApiStatus;

public final class MultiPartBuilder
{
    private final StateBuilder parent;

    @ApiStatus.Internal
    MultiPartBuilder(StateBuilder parent)
    {
        this.parent = parent;
    }

    public StateBuilder end()
    {
        return parent;
    }

    @ApiStatus.Internal
    JsonArray toJson()
    {
        var json = new JsonArray();
        return json;
    }
}
