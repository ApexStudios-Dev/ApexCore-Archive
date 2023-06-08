package xyz.apex.minecraft.apexcore.common.lib.helper;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;

@ApiStatus.NonExtendable
public interface EntityHelper
{
    /**
     * Returns true if given entity is a fake player.
     *
     * @param entity Entity to validate.
     * @return True if given entity is a fake player.
     */
    static boolean isFakePlayer(@Nullable Entity entity)
    {
        return entity != null && EntityEvents.IS_FAKE_PLAYER.post().handle(entity);
    }
}
