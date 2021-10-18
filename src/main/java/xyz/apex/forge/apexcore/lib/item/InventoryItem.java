package xyz.apex.forge.apexcore.lib.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.apex.forge.apexcore.lib.container.ItemInventoryContainer;
import xyz.apex.forge.apexcore.lib.container.inventory.ItemInventory;

public abstract class InventoryItem<C extends ItemInventoryContainer> extends Item
{
	public InventoryItem(Properties properties)
	{
		super(properties);
	}

	protected abstract ContainerType<C> getContainerType();
	protected abstract ItemInventory createInventory(ItemStack stack);
	protected abstract C createContainer(ContainerType<C> containerType, int windowId, PlayerInventory playerInventory, ItemInventory inventory);

	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);

		if(player instanceof ServerPlayerEntity)
		{
			if(!openContainerScreen(level, (ServerPlayerEntity) player, hand, stack, stack.getHoverName()))
				return ActionResult.pass(stack);
		}

		return ActionResult.sidedSuccess(stack, level.isClientSide);
	}

	protected boolean openContainerScreen(World level, ServerPlayerEntity player, Hand hand, ItemStack stack, ITextComponent titleComponent)
	{
		NetworkHooks.openGui(player, new SimpleNamedContainerProvider((windowId, playerInventory, plr) -> createContainer(getContainerType(), windowId, playerInventory, createInventory(stack)), titleComponent));
		return true;
	}
}
