package dev.apexstudios.testmod.fabric.entrypoint;

import com.google.common.reflect.Reflection;
import dev.apexstudios.testmod.common.TestMod;
import net.fabricmc.api.ModInitializer;

public final class FabricModInitializer implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        Reflection.initialize(TestMod.class);
    }
}
