package xyz.apex.minecraft.apexcore.shared;

import xyz.apex.minecraft.apexcore.shared.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.shared.util.ApexTags;

public interface ApexCore extends ModPlatform
{
    String ID = "apexcore";

    @Override
    default void initialize()
    {
        ApexTags.bootstrap();
    }
}
