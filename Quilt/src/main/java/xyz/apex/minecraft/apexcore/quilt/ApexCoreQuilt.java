package xyz.apex.minecraft.apexcore.quilt;

import xyz.apex.minecraft.apexcore.quilt.platform.QuiltModPlatform;
import xyz.apex.minecraft.apexcore.shared.ApexCore;

public final class ApexCoreQuilt extends QuiltModPlatform implements ApexCore
{
    public static final QuiltModPlatform INSTANCE = new ApexCoreQuilt();

    private ApexCoreQuilt()
    {
        super(ID, null);
    }
}
