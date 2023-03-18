package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;
import xyz.apex.minecraft.apexcore.common.platform.Internals;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;

import java.util.Map;

final class ForgeInternals implements Internals
{
    private final Map<String, ModInternals> modInternals = Maps.newHashMap();

    @Override
    public <T> DeferredRegister<T> deferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType)
    {
        return modInternals.computeIfAbsent(ownerId, ModInternals::new).deferredRegister(registryType);
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

    @Override
    public void registerPackRepository(PackRepository repository, RepositorySource source)
    {
        repository.addPackFinder(source);
    }

    @Override
    public void registerRenderType(Block block, RenderType renderType)
    {
        ItemBlockRenderTypes.setRenderLayer(block, renderType);
    }

    private static final class ModInternals
    {
        private final String ownerId;
        private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registries = Maps.newHashMap();

        private ModInternals(String ownerId)
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
