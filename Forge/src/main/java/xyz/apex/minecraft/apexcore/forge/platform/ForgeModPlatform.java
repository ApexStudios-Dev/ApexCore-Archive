package xyz.apex.minecraft.apexcore.forge.platform;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import xyz.apex.minecraft.apexcore.shared.platform.AbstractModPlatform;

public class ForgeModPlatform extends AbstractModPlatform
{
    protected final IEventBus modBus;
    protected final String modId;
    protected final Logger logger;

    // manually instantiated
    public ForgeModPlatform(String modId)
    {
        this.modId = modId;
        logger = LogManager.getLogger("GamePlatform/Forge-%s".formatted(modId));
        modBus = setup();
    }

    // we are subclass of main mod entry point
    // should be able to access @Mod annotation
    protected ForgeModPlatform()
    {
        modId = getClass().getAnnotation(net.minecraftforge.fml.common.Mod.class).value();
        logger = LogManager.getLogger("GamePlatform/Forge-%s".formatted(modId));
        modBus = setup();
    }

    private IEventBus setup()
    {
        // should be called from mod entry point, thus this should never fail
        // this *HAS* to be true, in order to obtain the correct mod bus instance
        Validate.isTrue(ModLoadingContext.get().getActiveNamespace().equals(modId));
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(modId, modBus);
        modBus.addListener(EventPriority.HIGHEST, this::onClientSetup);
        modBus.addListener(EventPriority.HIGHEST, this::onDedicatedServerSetup);
        modBus.addListener(EventPriority.HIGHEST, this::onGatherData);
        initialize();
        return modBus;
    }

    @Override
    public final Logger getLogger()
    {
        return logger;
    }

    @Override
    public final String getModId()
    {
        return modId;
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        initializeSided(Env.CLIENT);
    }

    private void onDedicatedServerSetup(FMLDedicatedServerSetupEvent event)
    {
        initializeSided(Env.SERVER);
    }

    private void onGatherData(GatherDataEvent event)
    {
        initializeDataGen();
    }
}
