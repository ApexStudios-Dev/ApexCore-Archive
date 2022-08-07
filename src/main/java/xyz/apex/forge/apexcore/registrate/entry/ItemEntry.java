package xyz.apex.forge.apexcore.registrate.entry;

import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

public final class ItemEntry<ITEM extends Item> extends ItemLikeEntry<ITEM>
{
	public ItemEntry(CoreRegistrate<?> owner, RegistryObject<ITEM> delegate)
	{
		super(owner, delegate);
	}

	public static <ITEM extends Item> ItemEntry<ITEM> cast(RegistryEntry<ITEM> entry)
	{
		return cast(ItemEntry.class, entry);
	}
}