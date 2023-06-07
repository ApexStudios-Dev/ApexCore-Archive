package xyz.apex.minecraft.apexcore.common.lib;

import xyz.apex.minecraft.apexcore.common.core.ApexCore;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Enum representing the physical side the game is currently running under.
 */
public enum PhysicalSide
{
    /**
     * Running under the physical client.
     */
    CLIENT,

    /**
     * Running under the physical dedicated server.
     */
    DEDICATED_SERVER;

    /**
     * Returns optional containing the result after running the provided supplier if running on the correct side, otherwise empty.
     *
     * @param toCall Supplier to call if running on correct side.
     * @param <T>    Resultant type.
     * @return Optional containing the result after running the provided supplier if running on the correct side, otherwise empty.
     */
    public <T> Optional<T> callWhenOn(Supplier<Supplier<T>> toCall)
    {
        return callWhenOn(this, toCall);
    }

    /**
     * Invokes the provided runnable if running on the correct side.
     *
     * @param toRun Running to be invoked if running on the correct side.
     */
    public void runWhenOn(Supplier<Runnable> toRun)
    {
        runWhenOn(this, toRun);
    }

    /**
     * Invokes the provided runnable if running on the correct side or invokes the provided empty runnable if running on invalid side.
     *
     * @param toRun         Running to be invoked if running on the correct side.
     * @param emptyRunnable Running to be invoked if we are running on invalid side.
     */
    public void runWhenOnOrElse(Supplier<Runnable> toRun, Supplier<Runnable> emptyRunnable)
    {
        runWhenOnOrElse(this, toRun, emptyRunnable);
    }

    /**
     * Returns optional containing the result after running the provided supplier if running on the correct side, otherwise empty.
     *
     * @param side   Side to validate we are running on.
     * @param toCall Supplier to call if running on correct side.
     * @param <T>    Resultant type.
     * @return Optional containing the result after running the provided supplier if running on the correct side, otherwise empty.
     */
    public static <T> Optional<T> callWhenOn(PhysicalSide side, Supplier<Supplier<T>> toCall)
    {
        if(!isRunningOn(side)) return Optional.empty();
        return Optional.ofNullable(toCall.get().get());
    }

    /**
     * Invokes the provided suppliers and returns the result depending on what side we are running on.
     *
     * @param clientTarget Supplier to be invoked if running on the physical client.
     * @param serverTarget Supplier to be invoked if running on the dedicated server.
     * @param <T>          Resultant type.
     * @return Result after running the provided suppliers, depending on what side we are currently running on.
     */
    public static <T> T callForSide(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget)
    {
        return switch(getPhysicalSide())
                {
                    case CLIENT -> clientTarget.get().get();
                    case DEDICATED_SERVER -> serverTarget.get().get();
                };
    }

    /**
     * Invokes the provided runnable if running on the correct side.
     *
     * @param side  Side to validate we are running on.
     * @param toRun Running to be invoked if running on the correct side.
     */
    public static void runWhenOn(PhysicalSide side, Supplier<Runnable> toRun)
    {
        if(isRunningOn(side)) toRun.get().run();
    }

    /**
     * Invokes the provided runnable if running on the correct side or invokes the provided empty runnable if running on invalid side.
     *
     * @param side          Side to validate we are running on.
     * @param toRun         Running to be invoked if running on the correct side.
     * @param emptyRunnable Running to be invoked if we are running on invalid side.
     */
    public static void runWhenOnOrElse(PhysicalSide side, Supplier<Runnable> toRun, Supplier<Runnable> emptyRunnable)
    {
        if(isRunningOn(side)) toRun.get().run();
        else emptyRunnable.get().run();
    }

    /**
     * Invokes the provided runnables depending on what side we are running on.
     *
     * @param clientTarget Runnable to be invoked if running on the physical client.
     * @param serverTarget Runnable to be invoked if running on the dedicated server.
     */
    public static void runForSide(Supplier<Runnable> clientTarget, Supplier<Runnable> serverTarget)
    {
        switch(getPhysicalSide())
        {
            case CLIENT -> clientTarget.get().run();
            case DEDICATED_SERVER -> serverTarget.get().run();
        }
    }

    /**
     * @return The current side we are currently running on.
     */
    public static PhysicalSide getPhysicalSide()
    {
        return ApexCore.get().physicalSide();
    }

    /**
     * Returns true if we are currently running on the provided side.
     *
     * @param side Side to validate we are currently running on.
     * @return True if we are currently running on the provided side.
     */
    public static boolean isRunningOn(PhysicalSide side)
    {
        return getPhysicalSide() == side;
    }
}
