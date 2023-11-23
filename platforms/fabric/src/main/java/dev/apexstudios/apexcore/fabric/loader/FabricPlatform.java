package dev.apexstudios.apexcore.fabric.loader;

import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class FabricPlatform implements Platform
{
    private final PhysicalSide physicalSide = switch(FabricLoader.getInstance().getEnvironmentType())
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new FabricModLoader();
    private final PlatformFactory factory = new FabricFactory();

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
    public void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        player.openMenu(wrapMenuProvider(provider, extraData));
    }

    @Override
    public void openMenu(ServerPlayer player, Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        player.openMenu(menuProvider(displayName, constructor, extraData));
    }

    @Override
    public MenuProvider menuProvider(Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        return new ExtendedScreenHandlerFactory()
        {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buffer)
            {
                extraData.accept(buffer);
            }

            @Override
            public Component getDisplayName()
            {
                return displayName;
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
            {
                return constructor.createMenu(windowId, inventory, player);
            }
        };
    }

    @Override
    public MenuProvider wrapMenuProvider(MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        return new ExtendedScreenHandlerFactory()
        {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buffer)
            {
                extraData.accept(buffer);
            }

            @Override
            public Component getDisplayName()
            {
                return provider.getDisplayName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
            {
                return provider.createMenu(windowId, inventory, player);
            }
        };
    }
}
