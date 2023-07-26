package xyz.apex.minecraft.apexcore.common.lib.resgen;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ResourceTypes
{
    ResourceType CLIENT_MODEL = new ResourceType.Singleton("models/", ".json", PackType.CLIENT_RESOURCES);
    ResourceType CLIENT_MODEL_BLOCK = new ResourceType.Parented("block/", CLIENT_MODEL);
    ResourceType CLIENT_MODEL_ITEM = new ResourceType.Parented("item/", CLIENT_MODEL);

    @ApiStatus.Internal
    @DoNotCall
    static void bootstrap()
    {
    }
}
