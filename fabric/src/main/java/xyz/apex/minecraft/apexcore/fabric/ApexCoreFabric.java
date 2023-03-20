package xyz.apex.minecraft.apexcore.fabric;

import net.fabricmc.api.ModInitializer;
import xyz.apex.minecraft.apexcore.common.ApexCore;

public final class ApexCoreFabric implements ApexCore, ModInitializer
{
    @Override
    public void onInitialize()
    {
        bootstrap();
    }
}
