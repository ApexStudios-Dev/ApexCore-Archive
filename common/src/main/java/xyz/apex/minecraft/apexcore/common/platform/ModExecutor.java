package xyz.apex.minecraft.apexcore.common.platform;

import com.google.common.util.concurrent.Runnables;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ModExecutor
{
    @SuppressWarnings("UnstableApiUsage")
    static void runIfLoaded(String modId, Supplier<Consumer<Mod>> toRun)
    {
        runIfLoadedOrElse(modId, toRun, Runnables::doNothing);
    }

    static void runIfLoadedOrElse(String modId, Supplier<Consumer<Mod>> toRun, Supplier<Runnable> emptyRunnable)
    {
        ModLoader.INSTANCE.getMod(modId).ifPresentOrElse(mod -> toRun.get().accept(mod), () -> emptyRunnable.get().run());
    }

    static <T> Optional<T> callIfLoaded(String modId, Supplier<Function<Mod, T>> toCall)
    {
        return ModLoader.INSTANCE.getMod(modId).map(mod -> toCall.get().apply(mod));
    }
}
