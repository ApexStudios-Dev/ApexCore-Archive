package xyz.apex.minecraft.apexcore.shared.platform;

import dev.architectury.platform.Mod;
import dev.architectury.utils.Env;

public interface ModPlatform extends GamePlatform
{
    String getModId();

    default Mod asMod()
    {
        return GamePlatform.INSTANCE.getMod(getModId());
    }

    @Override
    default void initialize()
    {
        GamePlatform.INSTANCE.initialize();
        GamePlatform.super.initialize();
    }

    @Override
    default void initializeSided(Env side)
    {
        GamePlatform.INSTANCE.initializeSided(side);
        GamePlatform.super.initializeSided(side);
    }

    @Override
    default void initializeDataGen()
    {
        GamePlatform.INSTANCE.initializeDataGen();
        GamePlatform.super.initializeDataGen();
    }

    @Override
    default ModLoader getModLoader()
    {
        return GamePlatform.INSTANCE.getModLoader();
    }

    @Override
    default String getModLoaderVersion()
    {
        return GamePlatform.INSTANCE.getModLoaderVersion();
    }
}
