package xyz.apex.minecraft.apexcore.fabric.platform;

import dev.architectury.utils.Env;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.platform.ModLoader;

import java.util.EnumSet;
import java.util.Set;

public final class FabricGamePlatform implements GamePlatform
{
    private final Logger logger = LogManager.getLogger("GamePlatform/Fabric");
    private boolean initialized = false;
    private final Set<Env> initializedSides = EnumSet.noneOf(Env.class);
    private boolean initializedDataGen = false;

    public FabricGamePlatform()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        if(initialized) return;
        GamePlatform.super.initialize();
        initialized = true;
    }

    @Override
    public void initializeSided(Env side)
    {
        if(initializedSides.contains(side)) return;
        GamePlatform.super.initializeSided(side);
        initializedSides.add(side);
    }

    @Override
    public void initializeDataGen()
    {
        if(initializedDataGen) return;
        GamePlatform.super.initializeDataGen();
        initializedDataGen = true;
    }

    @Override
    public ModLoader getModLoader()
    {
        return ModLoader.FABRIC;
    }

    @Override
    public Logger getLogger()
    {
        return logger;
    }

    @Override
    public String getModLoaderVersion()
    {
        return FabricLoaderImpl.VERSION;
    }
}
