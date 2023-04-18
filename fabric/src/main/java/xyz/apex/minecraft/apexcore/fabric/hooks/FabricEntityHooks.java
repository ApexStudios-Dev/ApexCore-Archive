package xyz.apex.minecraft.apexcore.fabric.hooks;

import net.minecraft.world.entity.Entity;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.event.EventFactory;
import xyz.apex.minecraft.apexcore.common.hooks.EntityHooks;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

public final class FabricEntityHooks extends FabricPlatformHolder implements EntityHooks
{
    FabricEntityHooks(FabricPlatform platform)
    {
        super(platform);

        setupTechnici4nFakePlayerAPI();
    }

    @Override
    public boolean isFakePlayer(Entity entity)
    {
        return EventFactory.isFakePlayer(entity, false);
    }

    private void setupTechnici4nFakePlayerAPI()
    {
        try
        {
            var technici4nFakePlayerClass = Class.forName("net.fabricmc.fabric.api.entity.FakePlayer");
            ApexCore.LOGGER.debug("Found Technici4n FakePlayer class, This will be used to determine if Entity is FakePlayer or not.");

            EventFactory.FAKE_PLAYER.addListener(event -> {
                if(technici4nFakePlayerClass.isInstance(event.entity)) event.markAsFakePlayer();
            });
        }
        catch(ClassNotFoundException ignored)
        {
        }
    }
}
