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

    boolean isProduction();

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

    static String gameVersion()
    {
        return SharedConstants.getCurrentVersion().getName();
    }

    static int resourceVersion(PackType packType)
    {
        return SharedConstants.getCurrentVersion().getPackVersion(packType);
    }

    static boolean isSnapshot()
    {
        return !SharedConstants.getCurrentVersion().isStable();
    }
}
