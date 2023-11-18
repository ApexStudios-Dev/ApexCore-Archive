package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.covers1624.quack.util.SneakyUtils;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

public interface ModLoader
{
    String id();

    String displayName();

    String version();

    boolean isNeo();

    boolean isForge();

    boolean isFabric();

    boolean isLoaded(String id);

    Mod get(String id);

    Set<String> getLoadedIds();

    Collection<Mod> getLoaded();

    static <T> OptionalLike<T> callIfLoaded(String id, Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> toCall)
    {
        if(get().isLoaded(id))
            return () -> SneakyUtils.sneaky(toCall.get());

        return OptionalLike.empty();
    }

    static void runIfLoaded(String id, Supplier<SneakyUtils.ThrowingRunnable<Throwable>> toRun)
    {
        if(get().isLoaded(id))
            SneakyUtils.sneaky(toRun.get());
    }

    static <T> OptionalLike<T> callIfNeo(Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> toCall)
    {
        if(get().isNeo())
            return () -> SneakyUtils.sneaky(toCall.get());

        return OptionalLike.empty();
    }

    static <T> OptionalLike<T> callIfForge(Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> toCall)
    {
        if(get().isForge())
            return () -> SneakyUtils.sneaky(toCall.get());

        return OptionalLike.empty();
    }

    static <T> OptionalLike<T> callIfFabric(Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> toCall)
    {
        if(get().isFabric())
            return () -> SneakyUtils.sneaky(toCall.get());

        return OptionalLike.empty();
    }

    static void runIfNeo(Supplier<SneakyUtils.ThrowingRunnable<Throwable>> toRun)
    {
        if(get().isNeo())
            SneakyUtils.sneaky(toRun.get());
    }

    static void runIfForge(Supplier<SneakyUtils.ThrowingRunnable<Throwable>> toRun)
    {
        if(get().isForge())
            SneakyUtils.sneaky(toRun.get());
    }

    static void runIfFabric(Supplier<SneakyUtils.ThrowingRunnable<Throwable>> toRun)
    {
        if(get().isFabric())
            SneakyUtils.sneaky(toRun.get());
    }

    static ModLoader get()
    {
        return Platform.get().modLoader();
    }
}
