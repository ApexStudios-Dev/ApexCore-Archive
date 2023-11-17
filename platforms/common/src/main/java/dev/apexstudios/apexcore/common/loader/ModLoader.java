package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.util.OptionalLike;
import dev.apexstudios.apexcore.common.util.Services;
import net.covers1624.quack.util.SneakyUtils;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackType;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

public interface ModLoader
{
    ModLoader INSTANCE = Services.singleton(ModLoader.class);

    String id();

    String displayName();

    String version();

    PhysicalSide physicalSide();

    default String gameVersion()
    {
        return SharedConstants.VERSION_STRING;
    }

    default int resourceVersion(PackType packType)
    {
        return switch(packType)
        {
            case CLIENT_RESOURCES -> SharedConstants.RESOURCE_PACK_FORMAT;
            case SERVER_DATA -> SharedConstants.DATA_PACK_FORMAT;
        };
    }

    default boolean isSnapshot()
    {
        return SharedConstants.SNAPSHOT;
    }

    default boolean isProduction()
    {
        return !SharedConstants.IS_RUNNING_IN_IDE;
    }

    boolean runningDataGen();

    boolean isNeo();

    boolean isForge();

    boolean isFabric();

    boolean isModLoaded(String id);

    Mod getMod(String id);

    Set<String> getLoadedModIds();

    Collection<Mod> getLoadedMods();

    ApexBootstrapper bootstrapper();

    default <T> OptionalLike<T> callIfLoaded(String id, Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> toCall)
    {
        if(isModLoaded(id))
            return () -> SneakyUtils.sneaky(toCall.get());

        return OptionalLike.empty();
    }

    default void runIfLoaded(String id, Supplier<SneakyUtils.ThrowingRunnable<Throwable>> toRun)
    {
        if(isModLoaded(id))
            SneakyUtils.sneaky(toRun.get());
    }

    static <T> OptionalLike<T> callIfNeo(Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> toCall)
    {
        if(INSTANCE.isNeo())
            return () -> SneakyUtils.sneaky(toCall.get());

        return OptionalLike.empty();
    }

    static <T> OptionalLike<T> callIfForge(Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> toCall)
    {
        if(INSTANCE.isForge())
            return () -> SneakyUtils.sneaky(toCall.get());

        return OptionalLike.empty();
    }

    static <T> OptionalLike<T> callIfFabric(Supplier<SneakyUtils.ThrowingSupplier<T, Throwable>> toCall)
    {
        if(INSTANCE.isFabric())
            return () -> SneakyUtils.sneaky(toCall.get());

        return OptionalLike.empty();
    }

    static void runIfNeo(Supplier<SneakyUtils.ThrowingRunnable<Throwable>> toRun)
    {
        if(INSTANCE.isNeo())
            SneakyUtils.sneaky(toRun.get());
    }

    static void runIfForge(Supplier<SneakyUtils.ThrowingRunnable<Throwable>> toRun)
    {
        if(INSTANCE.isForge())
            SneakyUtils.sneaky(toRun.get());
    }

    static void runIfFabric(Supplier<SneakyUtils.ThrowingRunnable<Throwable>> toRun)
    {
        if(INSTANCE.isFabric())
            SneakyUtils.sneaky(toRun.get());
    }
}
