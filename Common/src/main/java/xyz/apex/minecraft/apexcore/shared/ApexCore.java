package xyz.apex.minecraft.apexcore.shared;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.util.Tags;

public interface ApexCore
{
    String ID = "apexcore";

    static void bootstrap()
    {
        Platform.INSTANCE.getLogger().debug("Bootstrapping common types...");
        Tags.bootstrap();
    }
}
