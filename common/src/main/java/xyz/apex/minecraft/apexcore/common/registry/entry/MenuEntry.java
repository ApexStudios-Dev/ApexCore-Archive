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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
    private MenuEntry(ResourceLocation registryName)
    {
        super(Registries.MENU, registryName);
    }

    public InteractionResult open(Player player, Component displayName, Consumer<FriendlyByteBuf> extraDataWriter)
    {
        if(player instanceof ServerPlayer serverPlayer) Platform.INSTANCE.internals().openMenu(serverPlayer, asMenuProvider(displayName, extraDataWriter));
        return InteractionResult.sidedSuccess(player.level.isClientSide);
    }

    public ExtendedMenuProvider asMenuProvider(Component displayName, Consumer<FriendlyByteBuf> extraDataWriter)
    {
        return new ExtendedMenuProvider() {
            @Override
            public void writeExtraData(FriendlyByteBuf extraData)
            {
                extraDataWriter.accept(extraData);
            }

            @Override
            public Component getDisplayName()
            {
                return displayName;
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
            {
                return get().create(containerId, playerInventory);
            }
        };
    }

    public static <T extends AbstractContainerMenu> MenuEntry<T> register(String ownerId, String registrationName, MenuFactory<T> menuFactory)
    {
        return RegistryManager.get(ownerId).getRegistry(Registries.MENU).register(
                registrationName,
                MenuEntry::new,
                () -> Platform.INSTANCE.internals().menuType(menuFactory)
        );
    }

    public static <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> MenuEntry<T> registerWithScreen(String ownerId, String registrationName, MenuFactory<T> menuFactory, Supplier<MenuScreens.ScreenConstructor<T, S>> screenConstructor)
    {
        var registryEntry = register(ownerId, registrationName, menuFactory);
        SideExecutor.runWhenOn(Side.CLIENT, () -> () -> registryEntry.registerCallback(menuType -> MenuScreens.register(menuType, screenConstructor.get())));
        return registryEntry;
    }

    public interface ExtendedMenuProvider extends MenuProvider
    {
        void writeExtraData(FriendlyByteBuf extraData);
    }

    @FunctionalInterface
    public interface MenuFactory<T extends AbstractContainerMenu>
    {
        T create(int containerId, Inventory playerInventory, Player player, FriendlyByteBuf extraData);
    }
}
