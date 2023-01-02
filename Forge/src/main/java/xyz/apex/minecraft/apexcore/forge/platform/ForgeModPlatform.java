package xyz.apex.minecraft.apexcore.forge.platform;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import xyz.apex.minecraft.apexcore.shared.platform.AbstractModPlatform;
import xyz.apex.minecraft.apexcore.shared.registry.Registrar;

public class ForgeModPlatform extends AbstractModPlatform
{
    protected final IEventBus modBus;
    protected final String modId;
    protected final Logger logger;
    @Nullable protected final Registrar registrar;

    // manually instantiated
    public ForgeModPlatform(String modId, @Nullable Registrar registrar)
    {
        super();

        this.modId = modId;
        this.registrar = registrar;
        logger = LogManager.getLogger("ModPlatform/Forge-%s".formatted(modId));
        modBus = setup();
    }

    // we are subclass of main mod entry point
    // should be able to access @Mod annotation
    protected ForgeModPlatform(@Nullable Registrar registrar)
    {
        super();

        modId = getClass().getAnnotation(net.minecraftforge.fml.common.Mod.class).value();
        this.registrar = registrar;
        logger = LogManager.getLogger("ModPlatform/Forge-%s".formatted(modId));
        modBus = setup();
    }

    private IEventBus setup()
    {
        if(registrar != null) registrar.setMod(this);
        // should be called from mod entry point, thus this should never fail
        // this *HAS* to be true, in order to obtain the correct mod bus instance
        Validate.isTrue(ModLoadingContext.get().getActiveNamespace().equals(modId));
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(modId, modBus);
        modBus.addListener(EventPriority.HIGHEST, this::onClientSetup);
        modBus.addListener(EventPriority.HIGHEST, this::onDedicatedServerSetup);
        modBus.addListener(EventPriority.HIGHEST, this::onGatherData);
        modBus.addListener(EventPriority.HIGHEST, this::onRegister);
        modBus.addListener(EventPriority.LOWEST, this::onLateRegister);
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

    private void onRegister(RegisterEvent event)
    {
        if(registrar == null) return;

        var vanillaRegistry = event.getVanillaRegistry();
        var forgeRegistry = event.getForgeRegistry();

        if(forgeRegistry != null) registrar.register(forgeRegistry.getRegistryKey());
        else if(vanillaRegistry != null) registrar.register(vanillaRegistry.key());
        else logger.fatal("UNKNOWN Registry Type: {}, Please report this to the mod author ASAP!", event.getRegistryKey());
    }

    private void onLateRegister(RegisterEvent event)
    {
        if(registrar == null) return;

        var vanillaRegistry = event.getVanillaRegistry();
        var forgeRegistry = event.getForgeRegistry();

        if(forgeRegistry != null) registrar.lateRegister(forgeRegistry.getRegistryKey());
        else if(vanillaRegistry != null) registrar.lateRegister(vanillaRegistry.key());
        else logger.fatal("UNKNOWN Registry Type: {}, Please report this to the mod author ASAP!", event.getRegistryKey());
    }

    @Nullable
    @Override
    public final Registrar getRegistrar()
    {
        return registrar;
    }
}
