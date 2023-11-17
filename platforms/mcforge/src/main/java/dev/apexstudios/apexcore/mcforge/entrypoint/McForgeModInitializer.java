package dev.apexstudios.apexcore.mcforge.entrypoint;

import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.mcforge.loader.ModEvents;
import net.minecraftforge.fml.common.Mod;

@Mod(ApexCore.ID)
public final class McForgeModInitializer implements ApexCore
{
    public McForgeModInitializer()
    {
        bootstrap();
        ModEvents.registerForJavaFML();
    }
}
