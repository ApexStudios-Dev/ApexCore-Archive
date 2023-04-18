package xyz.apex.minecraft.apexcore.forge.hooks;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.FakePlayer;
import xyz.apex.minecraft.apexcore.common.event.EventFactory;
import xyz.apex.minecraft.apexcore.common.hooks.EntityHooks;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatform;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatformHolder;

public final class ForgeEntityHooks extends ForgePlatformHolder implements EntityHooks
{
    ForgeEntityHooks(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public boolean isFakePlayer(Entity entity)
    {
        var isForgeFakePlayer = entity instanceof FakePlayer;
        return EventFactory.isFakePlayer(entity, isForgeFakePlayer);
    }
}
