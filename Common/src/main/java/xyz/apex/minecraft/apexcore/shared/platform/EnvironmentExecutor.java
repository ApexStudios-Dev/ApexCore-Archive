package xyz.apex.minecraft.apexcore.shared.platform;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class EnvironmentExecutor
{
    public static <T> T callWhenOn(Environment environment, Supplier<Callable<T>> toRun)
    {
        if(Platform.INSTANCE.isRunningOn(environment))
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

    public static <T> T callForClient(Supplier<Callable<T>> toRun)
    {
        return callWhenOn(Environment.CLIENT, toRun);
    }

    public static <T> T callForServer(Supplier<Callable<T>> toRun)
    {
        return callWhenOn(Environment.DEDICATED_SERVER, toRun);
    }

    public static void runWhenOn(Environment environment, Supplier<Runnable> toRun)
    {
        if(Platform.INSTANCE.isRunningOn(environment)) toRun.get().run();
    }

    public static void runForClient(Supplier<Runnable> toRun)
    {
        runWhenOn(Environment.CLIENT, toRun);
    }

    public static void runForServer(Supplier<Runnable> toRun)
    {
        runWhenOn(Environment.DEDICATED_SERVER, toRun);
    }

    public static <T> T runForPlatform(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget)
    {
        return switch(Platform.INSTANCE.getEnvironment()) {
            case CLIENT -> clientTarget.get().get();
            case DEDICATED_SERVER -> serverTarget.get().get();
        };
    }
}
