package xyz.apex.minecraft.apexcore.shared.platform;

import dev.architectury.platform.Mod;
import dev.architectury.utils.Env;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractModPlatform implements ModPlatform
{
    @Override
    public final Mod asMod()
    {
        return ModPlatform.super.asMod();
    }

    @Override
    public final String getMinecraftVersion()
    {
        return ModPlatform.super.getMinecraftVersion();
    }

    @Override
    public final Path getGameFolder()
    {
        return ModPlatform.super.getGameFolder();
    }

    @Override
    public final Path getConfigFolder()
    {
        return ModPlatform.super.getConfigFolder();
    }

    @Override
    public final Path getModsFolder()
    {
        return ModPlatform.super.getModsFolder();
    }

    @Override
    public final Env getEnvironment()
    {
        return ModPlatform.super.getEnvironment();
    }

    @Override
    public final boolean isModLoaded(String modId)
    {
        return ModPlatform.super.isModLoaded(modId);
    }

    @Override
    public final Mod getMod(String modId)
    {
        return ModPlatform.super.getMod(modId);
    }

    @Override
    public final Optional<Mod> getOptionalMod(String modId)
    {
        return ModPlatform.super.getOptionalMod(modId);
    }

    @Override
    public final Collection<Mod> getMods()
    {
        return ModPlatform.super.getMods();
    }

    @Override
    public final Collection<String> getModIds()
    {
        return ModPlatform.super.getModIds();
    }

    @Override
    public final boolean isDevelopmentEnvironment()
    {
        return ModPlatform.super.isDevelopmentEnvironment();
    }

    @Override
    public final ModLoader getModLoader()
    {
        return ModPlatform.super.getModLoader();
    }

    @Override
    public final String getModLoaderVersion()
    {
        return ModPlatform.super.getModLoaderVersion();
    }

    @Override
    public final ResourceLocation id(String path)
    {
        return ModPlatform.super.id(path);
    }

    @Nullable
    @Override
    public AbstractRegistrar<?> getRegistrar()
    {
        return null;
    }
}
