package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.covers1624.quack.util.SneakyUtils;

import java.util.function.Supplier;

public enum PhysicalSide
{
    CLIENT,
    DEDICATED_SERVER;

    public boolean isClient()
    {
        return this == CLIENT;
    }

    public boolean isServer()
    {
        return this == DEDICATED_SERVER;
    }

    public boolean isRunningOn()
    {
        return current() == this;
    }

    public <T> OptionalLike<T> callWhenOn(Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> toCall)
    {
        if(isRunningOn())
            return () -> SneakyUtils.sneaky(toCall.get());

        return OptionalLike.empty();
    }

    public void runWhenOn(Supplier<SneakyUtils.ThrowingRunnable<Throwable>> toRun)
    {
        if(isRunningOn())
            SneakyUtils.sneaky(toRun.get());
    }

    public static <T> T callForSide(Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> clientTarget, Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> serverTarget)
    {
        var target = current().isClient() ? clientTarget : serverTarget;
        return SneakyUtils.sneaky(target.get());
    }

    public static void runForSide(Supplier<SneakyUtils.ThrowingRunnable<Throwable>> clientAction, Supplier<SneakyUtils.ThrowingRunnable<Throwable>> serverAction)
    {
        var target = current().isClient() ? clientAction : serverAction;
        SneakyUtils.sneaky(target.get());
    }

    public static PhysicalSide current()
    {
        return Platform.get().physicalSide();
    }
}
