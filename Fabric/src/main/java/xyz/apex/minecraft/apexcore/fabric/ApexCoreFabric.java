package xyz.apex.minecraft.apexcore.fabric;

import net.fabricmc.api.ModInitializer;

import xyz.apex.minecraft.apexcore.shared.ApexCore;

public final class ApexCoreFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        ApexCore.bootstrap();
    }
}
