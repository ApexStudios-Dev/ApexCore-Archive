package xyz.apex.forge.apexcore.registrate.entry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import xyz.apex.forge.apexcore.lib.util.RegistryHelper;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

public class ItemLikeEntry<ITEM extends ItemLike & IForgeRegistryEntry<? super ITEM>> extends RegistryEntry<ITEM> implements ItemLike
{
	protected ItemLikeEntry(CoreRegistrate<?> owner, RegistryObject<ITEM> delegate)
	{
		super(owner, delegate);
	}

	public final ItemStack asStack(int count)
	{
		return new ItemStack(this, count);
	}

	public final ItemStack asStack()
	{
		return new ItemStack(this);
	}

	public final boolean isIn(ItemStack stack)
	{
		return is(stack.getItem());
	}

	public final boolean is(@Nullable Item item)
	{
		return get() == item;
	}

	public final boolean hasItemTag(TagKey<Item> tag)
	{
		return RegistryHelper.hasTag(ForgeRegistries.ITEMS, tag, asItem());
	}

	@Override
	public Item asItem()
	{
		return get().asItem();
	}
}