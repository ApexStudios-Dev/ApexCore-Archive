package dev.apexstudios.testmod.neoforge.entrypoint;

import dev.apexstudios.apexcore.neoforge.loader.ModEvents;
import dev.apexstudios.testmod.common.TestMod;
import net.neoforged.fml.common.Mod;

@Mod(TestMod.ID)
public final class NeoForgeModInitializer implements TestMod
{
    public NeoForgeModInitializer()
    {
        bootstrap();
        ModEvents.registerForJavaFML();
    }
}
