package xyz.apex.minecraft.testmod.shared;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class TestMenu extends AbstractContainerMenu
{
    public final BlockPos pos;
    public final ResourceKey<Level> dimensionType;
    public final Player player;

    public TestMenu(MenuType<TestMenu> menuType, int containerId, Inventory inventory, FriendlyByteBuf data)
    {
        super(menuType, containerId);

        player = inventory.player;
        pos = data.readBlockPos();
        dimensionType = data.readResourceKey(Registries.DIMENSION);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player other)
    {
        if(!other.getUUID().equals(player.getUUID())) return false;
        if(!player.level.dimension().equals(dimensionType)) return false;

        var blockState = player.level.getBlockState(pos);
        return TestMod.TEST_BLOCK.hasBlockState(blockState);
    }
}
