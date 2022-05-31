package xyz.apex.forge.utility.registrator.factory;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface EntityFactory<ENTITY extends Entity>
{
	ENTITY create(EntityType<ENTITY> entityType, Level level);
}
