package dev.apexstudios.apexcore.neoforge.entrypoint;

import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.neoforge.loader.ModEvents;
import net.neoforged.fml.common.Mod;

@Mod(ApexCore.ID)
public final class NeoForgeModInitializer implements ApexCore
{
    public NeoForgeModInitializer()
    {
        bootstrap();
        ModEvents.registerForJavaFML();
    }
}
