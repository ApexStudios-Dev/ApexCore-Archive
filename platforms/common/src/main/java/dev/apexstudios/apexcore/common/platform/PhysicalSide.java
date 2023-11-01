package dev.apexstudios.apexcore.common.platform;

import org.jetbrains.annotations.UnknownNullability;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public enum PhysicalSide
{
    CLIENT,
    DEDICATED_SERVER;

    public <T> Optional<T> callWhenOn(Supplier<Callable<T>> toCall)
    {
        return isOn(this) ? Optional.ofNullable(call(toCall)) : Optional.empty();
    }

    @UnknownNullability
    public <T> T callWhenOnOrElse(Supplier<Callable<T>> toCall, Supplier<Callable<T>> emptyAction)
    {
        return isOn(this) ? call(toCall) : call(emptyAction);
    }

    public void runWhenOn(Supplier<Invokable> toRun)
    {
        if(isOn(this))
            run(toRun);
    }

    public void runWhenOnOrElse(Supplier<Invokable> toRun, Supplier<Invokable> elseRunnable)
    {
        if(isOn(this))
            run(toRun);
        else
            run(elseRunnable);
    }

    public static <T> T callForSide(Supplier<Callable<T>> clientTarget, Supplier<Callable<T>> serverTarget)
    {
        return switch (current())
        {
            case CLIENT -> call(clientTarget);
            case DEDICATED_SERVER -> call(serverTarget);
        };
    }

    public static void runForSide(Supplier<Invokable> clientTarget, Supplier<Invokable> serverTarget)
    {
        switch (current())
        {
            case CLIENT -> run(clientTarget);
            case DEDICATED_SERVER -> run(serverTarget);
        }
    }

    public static PhysicalSide current()
    {
        return Platform.INSTANCE.physicalSide();
    }

    public static boolean isOn(PhysicalSide side)
    {
        return current() == side;
    }

    @UnknownNullability
    private static <T> T call(Supplier<Callable<T>> toCall)
    {
        try
        {
            return toCall.get().call();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void run(Supplier<Invokable> toRun)
    {
        try
        {
            toRun.get().run();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface Invokable
    {
        void run() throws Exception;
    }
}
