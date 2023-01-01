package xyz.apex.minecraft.apexcore.fabric.platform;

import dev.architectury.utils.Env;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import xyz.apex.minecraft.apexcore.shared.platform.AbstractModPlatform;

public class FabricModPlatform extends AbstractModPlatform implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer, DataGeneratorEntrypoint
{
    protected final String modId;
    protected final Logger logger;

    public FabricModPlatform(String modId)
    {
        this.modId = modId;
        logger = LogManager.getLogger("GamePlatform/Fabric-%s".formatted(modId));
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
        initializeSided(Env.CLIENT);
    }

    @Override
    public final void onInitializeServer()
    {
        initializeSided(Env.SERVER);
    }

    @Override
    public final void onInitialize()
    {
        initialize();
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
}
