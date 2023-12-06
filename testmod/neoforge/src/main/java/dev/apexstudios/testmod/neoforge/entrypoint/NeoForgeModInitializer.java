package dev.apexstudios.testmod.neoforge.entrypoint;

import dev.apexstudios.apexcore.neoforge.loader.ModEvents;
import dev.apexstudios.apexcore.neoforge.loader.NeoForgeItemHandler;
import dev.apexstudios.testmod.common.TestMod;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod(TestMod.ID)
public final class NeoForgeModInitializer implements TestMod
{
    public NeoForgeModInitializer()
    {
        bootstrap();
        ModEvents.registerForJavaFML()
                 // must be done for every block/item that has a item handler
                 // stupid and i dont like it, but there is no fallback registration like fabric
                 .addListener(RegisterCapabilitiesEvent.class, event -> NeoForgeItemHandler.registerForBlockEntity(event, TEST_BLOCK_ENTITY.value()));
    }
}
