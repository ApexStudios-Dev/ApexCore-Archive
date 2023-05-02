package xyz.apex.minecraft.apexcore.fabric.core;

import net.fabricmc.api.ModInitializer;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

public final class ApexCoreFabric implements ApexCore, ModInitializer
{
    @Override
    public void onInitialize()
    {
        bootstrap();
    }
}
