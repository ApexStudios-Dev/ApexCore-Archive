package xyz.apex.minecraft.apexcore.fabric.platform;

import dev.architectury.utils.Env;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import xyz.apex.minecraft.apexcore.shared.platform.AbstractModPlatform;
import xyz.apex.minecraft.apexcore.shared.registry.Registrar;

public class FabricModPlatform extends AbstractModPlatform implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer, DataGeneratorEntrypoint
{
    protected final String modId;
    protected final Logger logger;
    @Nullable protected final Registrar registrar;

    public FabricModPlatform(String modId, @Nullable Registrar registrar)
    {
        super();

        this.modId = modId;
        logger = LogManager.getLogger("ModPlatform/Fabric-%s".formatted(modId));

        this.registrar = registrar;
        if(registrar != null) registrar.setMod(this);
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

    @Override
    public final void onInitializeClient()
    {
        if(registrar != null) registrar.lateRegister();
        initializeSided(Env.CLIENT);
    }

    @Override
    public final void onInitializeServer()
    {
        if(registrar != null) registrar.lateRegister();
        initializeSided(Env.SERVER);
    }

    @Override
    public final void onInitialize()
    {
        initialize();
        if(registrar != null) registrar.register();
    }

    @Override
    public final void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        initializeDataGen();
    }

    @Override
    public final String getEffectiveModId()
    {
        return modId;
    }

    @Nullable
    @Override
    public final Registrar getRegistrar()
    {
        return registrar;
    }
}
