package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.ApexCore;
import net.minecraft.SharedConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.MenuConstructor;

import java.util.function.Consumer;

public interface Platform
{
    ModLoader modLoader();

    PhysicalSide physicalSide();

    default String gameVersion()
    {
        return SharedConstants.VERSION_STRING;
    }

    default int resourceVersion(PackType packType)
    {
        return switch(packType)
        {
            case CLIENT_RESOURCES -> SharedConstants.RESOURCE_PACK_FORMAT;
            case SERVER_DATA -> SharedConstants.DATA_PACK_FORMAT;
        };
    }

    default boolean isSnapshot()
    {
        return SharedConstants.SNAPSHOT;
    }

    default boolean isProduction()
    {
        return !SharedConstants.IS_RUNNING_IN_IDE;
    }

    boolean runningDataGen();

    PlatformFactory factory();

    // TODO: Find better location for this
    void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData);

    void openMenu(ServerPlayer player, Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData);

    MenuProvider menuProvider(Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData);

    MenuProvider wrapMenuProvider(MenuProvider provider, Consumer<FriendlyByteBuf> extraData);

    static Platform get()
    {
        return ApexCore.INSTANCE;
    }
}
