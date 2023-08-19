package xyz.apex.minecraft.testmod.fabric.entrypoint;

import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.testmod.common.TestMod;

@ApiStatus.Internal
public final class TestModModInitializer implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        TestMod.INSTANCE.bootstrap();
    }
}
