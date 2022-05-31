package xyz.apex.forge.utility.registrator.factory.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import xyz.apex.forge.utility.registrator.helper.ForgeSpawnEggItem;
import xyz.apex.java.utility.nullness.NonnullSupplier;

@SuppressWarnings("unchecked")
@FunctionalInterface
public interface SpawnEggItemFactory<ENTITY extends Entity, ITEM extends ForgeSpawnEggItem<ENTITY>>
{
	ITEM create(NonnullSupplier<EntityType<ENTITY>> entityTypeSupplier, int backgroundColor, int highlightColor, Item.Properties properties);

	static <ENTITY extends Entity, ITEM extends ForgeSpawnEggItem<ENTITY>> SpawnEggItemFactory<ENTITY, ITEM> forEntity()
	{
		return (entityTypeSupplier, backgroundColor, highlightColor, properties) -> (ITEM) new ForgeSpawnEggItem<>(entityTypeSupplier, backgroundColor, highlightColor, properties);
	}
}
