package dev.apexstudios.apexcore.neoforge.loader;

import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuConstructor;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.network.NetworkHooks;

import java.util.function.Consumer;

public final class NeoForgePlatform implements Platform
{
    private final PhysicalSide physicalSide = switch(FMLEnvironment.dist)
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new NeoForgeModLoader();
    private final PlatformFactory factory = new NeoForgeFactory();

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
        return FMLEnvironment.production;
    }

    @Override
    public boolean runningDataGen()
    {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public PlatformFactory factory()
    {
        return factory;
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        NetworkHooks.openScreen(player, provider, extraData);
    }

    @Override
    public void openMenu(ServerPlayer player, Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        player.openMenu(menuProvider(displayName, constructor, extraData));
    }

    @Override
    public MenuProvider menuProvider(Component displayName, MenuConstructor constructor, Consumer<FriendlyByteBuf> extraData)
    {
        return new SimpleMenuProvider(constructor, displayName);
    }

    @Override
    public MenuProvider wrapMenuProvider(MenuProvider provider, Consumer<FriendlyByteBuf> extraData)
    {
        return provider;
    }
}
