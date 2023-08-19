package xyz.apex.minecraft.testmod.fabric.entrypoint;

import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.testmod.common.TestModClient;

@ApiStatus.Internal
public final class TestModClientModInitializer implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        TestModClient.INSTANCE.bootstrap();
    }
}
