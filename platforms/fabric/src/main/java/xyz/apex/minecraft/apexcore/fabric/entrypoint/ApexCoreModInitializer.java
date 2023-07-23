package xyz.apex.minecraft.apexcore.fabric.entrypoint;

import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

@ApiStatus.Internal
public final class ApexCoreModInitializer implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        ApexCore.INSTANCE.bootstrap();
    }
}
