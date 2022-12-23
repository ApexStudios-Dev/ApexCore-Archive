package xyz.apex.minecraft.apexcore.shared.platform;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class PlatformExecutor
{
    public static <T> T callWhenOn(PlatformType platform, Supplier<Callable<T>> toRun)
    {
        if(Platform.INSTANCE.isRunningOn(platform))
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

    public static <T> T callForForge(Supplier<Callable<T>> toRun)
    {
        return callWhenOn(PlatformType.FORGE, toRun);
    }

    public static <T> T callForFabric(Supplier<Callable<T>> toRun)
    {
        return callWhenOn(PlatformType.FABRIC, toRun);
    }

    public static void runWhenOn(PlatformType platform, Supplier<Runnable> toRun)
    {
        if(Platform.INSTANCE.isRunningOn(platform)) toRun.get().run();
    }

    public static void runForForge(Supplier<Runnable> toRun)
    {
        runWhenOn(PlatformType.FORGE, toRun);
    }

    public static void runForFabric(Supplier<Runnable> toRun)
    {
        runWhenOn(PlatformType.FABRIC, toRun);
    }

    public static <T> T runForPlatform(Supplier<Supplier<T>> forgeTarget, Supplier<Supplier<T>> fabricTarget)
    {
        var platform = Platform.INSTANCE.getPlatformType();

        return switch(platform) {
            case FORGE -> forgeTarget.get().get();
            case FABRIC -> fabricTarget.get().get();
            default -> throw new IllegalArgumentException("Unknown Platform: %s".formatted(platform));
        };
    }
}
