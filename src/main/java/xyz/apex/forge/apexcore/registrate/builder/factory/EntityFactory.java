package xyz.apex.forge.apexcore.registrate.builder.factory;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface EntityFactory<ENTITY extends Entity> extends EntityType.EntityFactory<ENTITY>
{ }