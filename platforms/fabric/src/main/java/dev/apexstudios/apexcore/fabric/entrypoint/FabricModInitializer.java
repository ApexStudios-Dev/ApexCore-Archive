package dev.apexstudios.apexcore.fabric.entrypoint;

import com.google.common.reflect.Reflection;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.platform.Platform;
import dev.apexstudios.apexcore.fabric.platform.PlatformImpl;
import net.fabricmc.api.ModInitializer;

public final class FabricModInitializer implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        Reflection.initialize(ApexCore.class); // must be initialized before platform
        ((PlatformImpl) Platform.INSTANCE).onInitialize();
    }
}
