package xyz.apex.forge.utility.registrator.entry;

import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullSupplier;

public final class ItemEntry<ITEM extends Item> extends ItemLikeEntry<ITEM> implements NonnullSupplier<ITEM>
{
	public ItemEntry(AbstractRegistrator<?> registrator, RegistryObject<ITEM> delegate)
	{
		super(registrator, delegate);
	}

	@Override
	public ITEM asItem()
	{
		return get();
	}

	public static <ITEM extends Item> ItemEntry<ITEM> cast(RegistryEntry<ITEM> registryEntry)
	{
		return cast(ItemEntry.class, registryEntry);
	}

	public static <ITEM extends Item> ItemEntry<ITEM> cast(com.tterrag.registrate.util.entry.RegistryEntry<ITEM> registryEntry)
	{
		return cast(ItemEntry.class, registryEntry);
	}
}
