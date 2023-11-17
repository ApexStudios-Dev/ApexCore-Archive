package dev.apexstudios.testmod.mcforge.entrypoint;

import dev.apexstudios.apexcore.mcforge.loader.ModEvents;
import dev.apexstudios.testmod.common.TestMod;
import net.minecraftforge.fml.common.Mod;

@Mod(TestMod.ID)
public final class McForgeModInitializer implements TestMod
{
    public McForgeModInitializer()
    {
        bootstrap();
        ModEvents.registerForJavaFML();
    }
}
