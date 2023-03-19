package xyz.apex.minecraft.apexcore.forge.hooks;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;
import xyz.apex.minecraft.apexcore.common.hooks.RegistryHooks;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;
import xyz.apex.minecraft.apexcore.forge.platform.ForgeDeferredRegister;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatform;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatformHolder;

import java.util.Map;

public final class ForgeRegistryHooks extends ForgePlatformHolder implements RegistryHooks
{
    private final Map<String, Mod> mods = Maps.newHashMap();

    ForgeRegistryHooks(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public <T> DeferredRegister<T> deferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        return mods.computeIfAbsent(ownerId, Mod::new).deferredRegister(registryType);
    }

    @Override
    public void openMenu(ServerPlayer player, MenuEntry.ExtendedMenuProvider menuProvider)
    {
        NetworkHooks.openScreen(player, menuProvider, menuProvider::writeExtraData);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(MenuEntry.MenuFactory<T> clientMenuConstructor)
    {
        return IForgeMenuType.create((containerId, playerInventory, extraData) -> clientMenuConstructor.create(containerId, playerInventory, playerInventory.player, extraData));
    }

    private static final class Mod
    {
        private final String ownerId;
        private final Map<ResourceKey<? extends Registry<?>>, ForgeDeferredRegister<?>> registries = Maps.newHashMap();

        private Mod(String ownerId)
        {
            this.ownerId = ownerId;
        }

        @SuppressWarnings("unchecked")
        private <T> ForgeDeferredRegister<T> deferredRegister(ResourceKey<? extends Registry<T>> registryType)
        {
            return (ForgeDeferredRegister<T>) registries.computeIfAbsent(registryType, $ -> new ForgeDeferredRegister<>(ownerId, registryType));
        }
    }
}
