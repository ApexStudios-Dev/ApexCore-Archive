package xyz.apex.minecraft.apexcore.shared;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.util.Tags;

public interface ApexCore
{
    String ID = "apexcore";

    static void bootstrap()
    {
        Tags.bootstrap();

        GamePlatform.events().register(ApexCore.ID);
    }
}
