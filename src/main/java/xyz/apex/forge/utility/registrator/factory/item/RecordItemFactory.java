package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;

import xyz.apex.java.utility.nullness.NonnullSupplier;

@FunctionalInterface
public interface RecordItemFactory<ITEM extends RecordItem>
{
	RecordItemFactory<RecordItem> DEFAULT = RecordItem::new;

	ITEM create(int comparatorValue, NonnullSupplier<SoundEvent> sound, Item.Properties properties);
}
