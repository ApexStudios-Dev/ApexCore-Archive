package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Hooks for various entity related things.
 */
@ApiStatus.NonExtendable
public interface EntityHooks
{
    /**
     * Returns true if given entity is a fake player.
     *
     * @param entity Entity to validate.
     * @return True if given entity is a fake player.
     */
    boolean isFakePlayer(@Nullable Entity entity);
}
