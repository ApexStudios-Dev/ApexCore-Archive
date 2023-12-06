package dev.apexstudios.apexcore.fabric.loader;

import dev.apexstudios.apexcore.common.inventory.ItemHandlerProvider;
import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import dev.apexstudios.apexcore.common.util.MenuHelper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricPlatform implements Platform
{
    private final PhysicalSide physicalSide = switch(FabricLoader.getInstance().getEnvironmentType())
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new FabricModLoader();
    private final PlatformFactory factory = new FabricFactory();
    private final MenuHelper menuHelper = new FabricMenuHelper();

    public FabricPlatform()
    {
        ItemStorage.SIDED.registerFallback((level, pos, blockState, blockEntity, side) ->
                blockEntity instanceof ItemHandlerProvider.Directional provider ? provider.getItemHandler(side).map(FabricItemHandler::new).getRaw() : null
        );
    }

    @Override
    public ModLoader modLoader()
    {
        return modLoader;
    }

    @Override
    public PhysicalSide physicalSide()
    {
        return physicalSide;
    }

    @Override
    public boolean isProduction()
    {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean runningDataGen()
    {
        return FabricDataGenHelper.ENABLED;
    }

    @Override
    public PlatformFactory factory()
    {
        return factory;
    }

    @Override
    public MenuHelper menuHelper()
    {
        return menuHelper;
    }
}
