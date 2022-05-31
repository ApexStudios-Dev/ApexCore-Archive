package xyz.apex.forge.utility.registrator.entry;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullSupplier;

@SuppressWarnings("unused")
public abstract class ItemLikeEntry<ITEM extends IForgeRegistryEntry<? super ITEM> & ItemLike> extends RegistryEntry<ITEM> implements ItemLike, NonnullSupplier<ITEM>
{
	public ItemLikeEntry(AbstractRegistrator<?> registrator, RegistryObject<ITEM> delegate)
	{
		super(registrator, delegate);
	}

	public final ItemStack asItemStack(int stackSize)
	{
		return new ItemStack(this, stackSize);
	}

	public final ItemStack asItemStack()
	{
		return asItemStack(1);
	}

	public final boolean isInStack(ItemStack stack)
	{
		return isItem(stack.getItem());
	}

	public final boolean isInItemTag(Tag<Item> tag)
	{
		return tag.contains(asItem());
	}

	public final boolean isItem(Item item)
	{
		return asItem() == item;
	}

	// vanilla wrapper
	public final boolean isItem(ItemLike item)
	{
		return isItem(item.asItem());
	}

	@Override
	public Item asItem()
	{
		return get().asItem();
	}
}
