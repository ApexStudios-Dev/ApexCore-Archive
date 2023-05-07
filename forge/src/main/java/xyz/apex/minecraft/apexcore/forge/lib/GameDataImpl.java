package xyz.apex.minecraft.apexcore.forge.lib;

import net.minecraft.SharedConstants;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.GameData;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;

@ApiStatus.Internal
public final class GameDataImpl implements GameData
{
    @Override
    public PhysicalSide getPhysicalSide()
    {
        return switch(FMLEnvironment.dist)
                {
                    case CLIENT -> PhysicalSide.CLIENT;
                    case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
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
        return !FMLEnvironment.production;
    }

    @Override
    public boolean isDataGenActive()
    {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public boolean isForge()
    {
        return true;
    }

    @Override
    public boolean isFabric()
    {
        return false;
    }
}
