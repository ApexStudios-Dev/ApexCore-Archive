package xyz.apex.minecraft.apexcore.shared.platform;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class PlatformExecutor
{
    public static <T> T callWhenOn(Platform.Type platform, Supplier<Callable<T>> toRun)
    {
        if(GamePlatform.isRunningOn(platform))
        {
            try
            {
                return toRun.get().call();
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public static void runWhenOn(Platform.Type platform, Supplier<Runnable> toRun)
    {
        if(GamePlatform.isRunningOn(platform)) toRun.get().run();
    }

    public static <T> T runForPlatform(Platform.Type platform, Supplier<Supplier<T>> forgeTarget, Supplier<Supplier<T>> fabricTarget)
    {
        return switch(platform) {
            case FORGE -> forgeTarget.get().get();
            case FABRIC -> fabricTarget.get().get();
            default -> throw new IllegalArgumentException("Unknown Platform: %s".formatted(platform));
        };
    }
}
