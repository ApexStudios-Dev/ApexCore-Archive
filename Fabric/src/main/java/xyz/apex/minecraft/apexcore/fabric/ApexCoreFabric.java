package xyz.apex.minecraft.apexcore.fabric;

import xyz.apex.minecraft.apexcore.fabric.platform.FabricModPlatform;
import xyz.apex.minecraft.apexcore.shared.ApexCore;

public final class ApexCoreFabric extends FabricModPlatform implements ApexCore
{
    public static final FabricModPlatform INSTANCE = new ApexCoreFabric();

    private ApexCoreFabric()
    {
        super(ID, null);
    }
}
