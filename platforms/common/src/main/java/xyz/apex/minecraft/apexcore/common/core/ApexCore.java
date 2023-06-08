package xyz.apex.minecraft.apexcore.common.core;

import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.hook.Hooks;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;

import java.util.Objects;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public abstract class ApexCore
{
    public static final String ID = "apexcore";
    public static final Logger LOGGER = LogManager.getLogger();

    @Nullable private static ApexCore INSTANCE = null;

    protected ApexCore()
    {
        Validate.isTrue(INSTANCE == null);
        INSTANCE = this;
        bootstrap();
    }

    protected void bootstrap()
    {
        // common check for every platform
        // all fake players *should* extend the ServerPlayer class
        // forge & fabric register their own listeners to check instanceof directly on their fake player classes
        //
        // platform listeners are registered before this one
        // meaning the platform specific check should happen first before this one
        EntityEvents.IS_FAKE_PLAYER.addListener(entity -> entity instanceof ServerPlayer && entity.getClass() != ServerPlayer.class);
        EventType.bootstrap();
        RegistrarManager.register(ID);
    }

    public abstract PhysicalSide physicalSide();

    public abstract ModLoader modLoader();

    public abstract Hooks hooks();

    public abstract NetworkManager createNetworkManager(String ownerId);

    public static ApexCore get()
    {
        return Objects.requireNonNull(INSTANCE);
    }
}
