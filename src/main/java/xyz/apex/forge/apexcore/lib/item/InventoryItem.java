package xyz.apex.forge.apexcore.lib.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import xyz.apex.forge.apexcore.lib.container.ItemInventoryContainer;
import xyz.apex.forge.apexcore.lib.container.inventory.ItemInventory;

@Deprecated(forRemoval = true)
public abstract class InventoryItem<C extends ItemInventoryContainer> extends Item
{
	public InventoryItem(Properties properties)
	{
		super(properties);
	}

	protected abstract MenuType<C> getContainerType();
	protected abstract ItemInventory createInventory(ItemStack stack);
	protected abstract C createContainer(MenuType<C> containerType, int windowId, Inventory playerInventory, ItemInventory inventory);

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		var stack = player.getItemInHand(hand);

		if(player instanceof ServerPlayer serverPlayer)
		{
			if(!openContainerScreen(level, serverPlayer, hand, stack, stack.getHoverName()))
				return InteractionResultHolder.pass(stack);
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

	protected boolean openContainerScreen(Level level, ServerPlayer player, InteractionHand hand, ItemStack stack, Component titleComponent)
	{
		NetworkHooks.openGui(player, new SimpleMenuProvider((windowId, playerInventory, plr) -> createContainer(getContainerType(), windowId, playerInventory, createInventory(stack)), titleComponent));
		return true;
	}
}