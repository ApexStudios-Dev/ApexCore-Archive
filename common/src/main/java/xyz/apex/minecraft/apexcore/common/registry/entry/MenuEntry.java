package xyz.apex.minecraft.apexcore.common.registry.entry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import xyz.apex.minecraft.apexcore.common.platform.Platform;
import xyz.apex.minecraft.apexcore.common.platform.Side;
import xyz.apex.minecraft.apexcore.common.platform.SideExecutor;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.registry.RegistryManager;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MenuEntry<T extends AbstractContainerMenu> extends RegistryEntry<MenuType<T>> implements FeatureElementEntry<MenuType<T>>
{
    private final ServerMenuConstructor<T> serverMenuConstructor;

    private MenuEntry(ResourceLocation registryName, ServerMenuConstructor<T> serverMenuConstructor, ClientMenuConstructor<T> clientMenuConstructor)
    {
        super(Registries.MENU, registryName);

        this.serverMenuConstructor = serverMenuConstructor;
    }

    public InteractionResult open(Player player, Supplier<Component> displayName, Consumer<FriendlyByteBuf> extraData)
    {
        if(player instanceof ServerPlayer serverPlayer) Platform.INSTANCE.internals().openMenu(serverPlayer, asMenuProvider(displayName), extraData);
        return InteractionResult.sidedSuccess(player.level.isClientSide);
    }

    public MenuProvider asMenuProvider(Supplier<Component> displayName)
    {
        return new SimpleMenuProvider(asMenuConstructor(), displayName.get());
    }

    public MenuConstructor asMenuConstructor()
    {
        return serverMenuConstructor::create;
    }

    public static <T extends AbstractContainerMenu> MenuEntry<T> register(String ownerId, String registrationName, ServerMenuConstructor<T> serverMenuConstructor, ClientMenuConstructor<T> clientMenuConstructor)
    {
        return RegistryManager.get(ownerId).getRegistry(Registries.MENU).register(
                registrationName,
                location -> new MenuEntry<>(location, serverMenuConstructor, clientMenuConstructor),
                () -> Platform.INSTANCE.internals().menuType(clientMenuConstructor)
        );
    }

    public static <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> MenuEntry<T> registerWithScreen(String ownerId, String registrationName, ServerMenuConstructor<T> serverMenuConstructor, ClientMenuConstructor<T> clientMenuConstructor, Supplier<MenuScreens.ScreenConstructor<T, S>> screenConstructor)
    {
        var registryEntry = register(ownerId, registrationName, serverMenuConstructor, clientMenuConstructor);
        SideExecutor.runWhenOn(Side.CLIENT, () -> () -> registryEntry.registerCallback(menuType -> MenuScreens.register(menuType, screenConstructor.get())));
        return registryEntry;
    }

    @FunctionalInterface
    public interface ServerMenuConstructor<T extends AbstractContainerMenu>
    {
        T create(int containerId, Inventory playerInventory, Player player);
    }

    @FunctionalInterface
    public interface ClientMenuConstructor<T extends AbstractContainerMenu>
    {
        T create(int containerId, Inventory playerInventory, Player player, FriendlyByteBuf extraData);
    }
}
