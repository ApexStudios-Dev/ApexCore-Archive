package xyz.apex.minecraft.apexcore.forge.platform;

import dev.architectury.utils.Env;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.versions.forge.ForgeVersion;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.platform.ModLoader;

import java.util.EnumSet;
import java.util.Set;

public final class ForgeGamePlatform implements GamePlatform
{
    private final Logger logger = LogManager.getLogger("GamePlatform/Forge");
    private boolean initialized = false;
    private final Set<Env> initializedSides = EnumSet.noneOf(Env.class);
    private boolean initializedDataGen = false;

    public ForgeGamePlatform()
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
        return ModLoader.FORGE;
    }

    @Override
    public Logger getLogger()
    {
        return logger;
    }

    @Override
    public String getModLoaderVersion()
    {
        return ForgeVersion.getVersion();
    }
}
