package xyz.apex.minecraft.testmod.mcforge;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.mcforge.lib.EventBuses;
import xyz.apex.minecraft.testmod.common.TestMod;
import xyz.apex.minecraft.testmod.common.TestModClient;

@ApiStatus.Internal
public final class TestModImpl implements TestMod
{
    @Override
    public void bootstrap()
    {
        TestMod.super.bootstrap();
        EventBuses.registerForJavaFML();
        PhysicalSide.CLIENT.runWhenOn(() -> TestModClient.INSTANCE::bootstrap);
    }
}
