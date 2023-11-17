package dev.apexstudios.testmod.fabric.entrypoint;

import dev.apexstudios.testmod.common.TestMod;
import net.fabricmc.api.ModInitializer;

public final class FabricModInitializer implements ModInitializer, TestMod
{
    @Override
    public void onInitialize()
    {
        bootstrap();
    }
}
