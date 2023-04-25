package xyz.apex.minecraft.apexcore.fabric.hooks;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import xyz.apex.minecraft.apexcore.common.hooks.EntityHooks;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

public final class FabricEntityHooks extends FabricPlatformHolder implements EntityHooks
{
    FabricEntityHooks(FabricPlatform platform)
    {
        super(platform);
    }

    @Override
    public boolean isFakePlayer(Entity entity)
    {
        return entity instanceof ServerPlayer && entity.getClass() != ServerPlayer.class;
    }
}
