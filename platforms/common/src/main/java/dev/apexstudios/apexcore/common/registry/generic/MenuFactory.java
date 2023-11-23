package dev.apexstudios.apexcore.common.registry.generic;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

@FunctionalInterface
public interface MenuFactory<T extends AbstractContainerMenu>
{
    T create(int windowId, Inventory inventory, FriendlyByteBuf buffer);
}
