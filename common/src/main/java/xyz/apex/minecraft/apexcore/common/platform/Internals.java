package xyz.apex.minecraft.apexcore.common.platform;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.registry.DeferredRegister;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;

public interface Internals
{
    <T> DeferredRegister<T> deferredRegister(String ownerId, ResourceKey<? extends Registry<T>> registryType);

    void openMenu(ServerPlayer player, MenuEntry.ExtendedMenuProvider menuProvider);

    <T extends AbstractContainerMenu> MenuType<T> menuType(MenuEntry.MenuFactory<T> clientMenuConstructor);

    void registerPackRepository(PackRepository repository, RepositorySource source);

    @SideOnly(Side.CLIENT)
    void registerRenderType(Block block, RenderType renderType);
}
