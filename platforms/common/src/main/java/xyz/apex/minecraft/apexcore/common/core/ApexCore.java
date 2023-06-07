package xyz.apex.minecraft.apexcore.common.core;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;

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
        EventType.bootstrap();
    }

    public abstract PhysicalSide getPhysicalSide();

    public abstract ModLoader getModLoader();

    public abstract NetworkManager createNetworkManager(String ownerId);

    public static ApexCore get()
    {
        return Objects.requireNonNull(INSTANCE);
    }
}
