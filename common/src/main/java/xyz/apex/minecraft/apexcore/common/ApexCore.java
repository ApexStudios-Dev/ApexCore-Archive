package xyz.apex.minecraft.apexcore.common;

import xyz.apex.minecraft.apexcore.common.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.common.util.ApexTags;

public interface ApexCore extends ModPlatform
{
    String ID = "apexcore";

    @Override
    default void initialize()
    {
        ApexTags.bootstrap();
    }
}
