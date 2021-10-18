package xyz.apex.forge.apexcore.lib.item;

import com.tterrag.registrate.util.entry.ContainerEntry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import xyz.apex.forge.apexcore.lib.container.ItemInventoryContainer;

public abstract class RegistrateInventoryItem<C extends ItemInventoryContainer> extends InventoryItem<C>
{
	public RegistrateInventoryItem(Properties properties)
	{
		super(properties);
	}

	protected abstract ContainerEntry<C> getContainerEntry();

	@Override
	protected ContainerType<C> getContainerType()
	{
		return getContainerEntry().get();
	}

	@Override
	protected boolean openContainerScreen(World level, ServerPlayerEntity player, Hand hand, ItemStack stack, ITextComponent titleComponent)
	{
		getContainerEntry().open(player, titleComponent);
		return true;
	}
}
