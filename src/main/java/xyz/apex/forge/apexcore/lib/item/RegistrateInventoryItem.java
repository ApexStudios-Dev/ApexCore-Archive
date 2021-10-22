package xyz.apex.forge.apexcore.lib.item;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.ContainerEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;
import xyz.apex.forge.apexcore.lib.container.ItemInventoryContainer;

public abstract class RegistrateInventoryItem<C extends ItemInventoryContainer> extends InventoryItem<C>
{
	private final NonNullLazyValue<ContainerEntry<C>> lazyContainerEntry;

	public RegistrateInventoryItem(Properties properties, AbstractRegistrate<?> registrate)
	{
		super(properties);

		lazyContainerEntry = new NonNullLazyValue<>(() -> obtainContainerEntry(registrate));
	}

	private ContainerEntry<C> obtainContainerEntry(AbstractRegistrate<?> registrate)
	{
		String name = Validate.notNull(getRegistryName()).getPath();
		RegistryEntry<ContainerType<C>> registryEntry = registrate.get(name, ContainerType.class);
		Validate.isInstanceOf(ContainerEntry.class, registryEntry);
		return (ContainerEntry<C>) registryEntry;
	}

	protected final ContainerEntry<C> getContainerEntry()
	{
		return lazyContainerEntry.get();
	}

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
