package xyz.apex.minecraft.apexcore.fabric.entrypoint;

import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;

@ApiStatus.Internal
public final class ApexCoreClientModInitializer implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ApexCoreClient.INSTANCE.bootstrap();
    }
}
