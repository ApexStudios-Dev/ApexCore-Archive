package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.world.entity.Entity;
import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;

public interface EntityHooks extends PlatformHolder
{
    boolean isFakePlayer(Entity entity);

    static EntityHooks getInstance()
    {
        return Hooks.getInstance().entity();
    }
}
