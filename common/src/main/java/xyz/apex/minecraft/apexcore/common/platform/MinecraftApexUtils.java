package xyz.apex.minecraft.apexcore.common.platform;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.event.EventFactory;
import xyz.apex.utils.core.ApexUtils;

import java.nio.file.Path;

@ApiStatus.Internal
public final class MinecraftApexUtils implements ApexUtils
{
    public void initMinecraft()
    {
        LOGGER.info("Initialized for Minecraft - {}!", Platform.INSTANCE.getMinecraftVersion());
        LOGGER.info("Game Directory: {}", INSTANCE.rootPath());
        EventFactory.bootstrap();
    }

    @Override
    public Path rootPath()
    {
        return Platform.INSTANCE.getGameDir();
    }

    @Override
    public Path configsDir()
    {
        return Platform.INSTANCE.getConfigDir();
    }
}
