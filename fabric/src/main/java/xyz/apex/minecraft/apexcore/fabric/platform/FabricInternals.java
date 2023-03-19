package xyz.apex.minecraft.apexcore.fabric.platform;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.platform.Internals;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;

import java.util.Map;

final class FabricInternals implements Internals
{
    private final Map<String, FabricModInternals> modInternals = Maps.newHashMap();

    FabricInternals() {}

    @Override
    public <T> DeferredRegister<T> deferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        return modInternals.computeIfAbsent(ownerId, FabricModInternals::new).deferredRegister(registryType);
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

    @Override
    public void registerPackRepository(PackRepository repository, RepositorySource source)
    {
        if(repository.sources instanceof ImmutableSet) repository.sources = Sets.newHashSet(repository.sources);
        repository.sources.add(source);
    }

    @Override
    public void registerRenderType(Block block, RenderType renderType)
    {
        BlockRenderLayerMap.INSTANCE.putBlocks(renderType, block);
    }

    private static final class FabricModInternals
    {
        private final String ownerId;
        private final Map<ResourceKey<? extends Registry<?>>, FabricDeferredRegister<?>> registries = Maps.newHashMap();

        private FabricModInternals(String ownerId)
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
