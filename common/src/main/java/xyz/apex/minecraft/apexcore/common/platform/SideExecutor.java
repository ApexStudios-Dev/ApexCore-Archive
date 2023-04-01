package xyz.apex.minecraft.apexcore.common.platform;

import java.util.Optional;
import java.util.function.Supplier;

public interface SideExecutor
{
    static void runWhenOn(Side side, Supplier<Runnable> toRun)
    {
        if(Platform.INSTANCE.getPhysicalSide().is(side)) toRun.get().run();
    }

    static void runForSide(Supplier<Runnable> clientTarget, Supplier<Runnable> serverTarget)
    {
        switch(Platform.INSTANCE.getPhysicalSide()) {
            case CLIENT -> clientTarget.get().run();
            case SERVER -> serverTarget.get().run();
        }
    }

    static <T> Optional<T> callWhenOn(Side side, Supplier<Supplier<T>> toCall)
    {
        if(Platform.INSTANCE.getPhysicalSide().is(side)) return Optional.ofNullable(toCall.get().get());
        return Optional.empty();
    }

    static <T> T callForSide(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget)
    {
        return switch(Platform.INSTANCE.getPhysicalSide()) {
            case CLIENT -> clientTarget.get().get();
            case SERVER -> serverTarget.get().get();
        };
    }
}
