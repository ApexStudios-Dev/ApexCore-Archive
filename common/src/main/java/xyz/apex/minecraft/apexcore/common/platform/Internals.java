package xyz.apex.minecraft.apexcore.common.platform;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;

import java.util.function.Consumer;

public interface Internals
{
    <T> DeferredRegister<T> deferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType);

    void openMenu(ServerPlayer player, MenuProvider constructor, Consumer<FriendlyByteBuf> extraData);

    <T extends AbstractContainerMenu> MenuType<T> menuType(MenuEntry.ClientMenuConstructor<T> clientMenuConstructor);
}
