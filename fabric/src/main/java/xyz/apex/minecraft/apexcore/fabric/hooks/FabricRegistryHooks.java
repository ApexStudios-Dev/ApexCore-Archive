package xyz.apex.minecraft.apexcore.fabric.hooks;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.hooks.RegistryHooks;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricDeferredRegister;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatform;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricPlatformHolder;

import java.util.Map;

public final class FabricRegistryHooks extends FabricPlatformHolder implements RegistryHooks
{
    private final Map<String, Mod> mods = Maps.newHashMap();

    FabricRegistryHooks(FabricPlatform platform)
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
        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf extraData)
            {
                menuProvider.writeExtraData(extraData);
            }

            @Override
            public Component getDisplayName()
            {
                return menuProvider.getDisplayName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
            {
                return menuProvider.createMenu(containerId, playerInventory, player);
            }
        });
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(MenuEntry.MenuFactory<T> clientMenuConstructor)
    {
        return new ExtendedScreenHandlerType<>((containerId, playerInventory, extraData) -> clientMenuConstructor.create(containerId, playerInventory, playerInventory.player, extraData));
    }

    private static final class Mod
    {
        private final String ownerId;
        private final Map<ResourceKey<? extends Registry<?>>, FabricDeferredRegister<?>> registries = Maps.newHashMap();

        private Mod(String ownerId)
        {
            this.ownerId = ownerId;
        }

        @SuppressWarnings("unchecked")
        private <T> FabricDeferredRegister<T> deferredRegister(ResourceKey<? extends Registry<T>> registryType)
        {
            return (FabricDeferredRegister<T>) registries.computeIfAbsent(registryType, $ -> new FabricDeferredRegister<>(ownerId, registryType));
        }
    }
}
