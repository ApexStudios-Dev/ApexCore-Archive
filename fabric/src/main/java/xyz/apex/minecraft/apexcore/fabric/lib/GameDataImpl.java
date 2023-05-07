package xyz.apex.minecraft.apexcore.fabric.lib;

import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.GameData;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;

@ApiStatus.Internal
public final class GameDataImpl implements GameData
{
    @Override
    public PhysicalSide getPhysicalSide()
    {
        return switch(FabricLoader.getInstance().getEnvironmentType())
                {
                    case CLIENT -> PhysicalSide.CLIENT;
                    case SERVER -> PhysicalSide.DEDICATED_SERVER;
                };
    }

    @Override
    public String getVersion()
    {
        return SharedConstants.VERSION_STRING;
    }

    @Override
    public boolean isSnapshot()
    {
        return SharedConstants.SNAPSHOT;
    }

    @Override
    public boolean isDevelopment()
    {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isDataGenActive()
    {
        return FabricDataGenHelper.ENABLED;
    }

    @Override
    public boolean isForge()
    {
        return false;
    }

    @Override
    public boolean isFabric()
    {
        return true;
    }
}
