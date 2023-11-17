package dev.apexstudios.apexcore.fabric.entrypoint;

import dev.apexstudios.apexcore.common.ApexCore;
import net.fabricmc.api.ModInitializer;

public final class FabricModInitializer implements ModInitializer, ApexCore
{
    @Override
    public void onInitialize()
    {
        bootstrap();
    }
}
