package xyz.apex.forge.apexcore.registrate.holder;

import net.minecraft.world.entity.Entity;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.EntityBuilder;
import xyz.apex.forge.apexcore.registrate.builder.factory.EntityFactory;

@SuppressWarnings("unchecked")
public interface EntityHolder<OWNER extends CoreRegistrate<OWNER> & EntityHolder<OWNER>> extends ItemHolder<OWNER>
{
	private OWNER self()
	{
		return (OWNER) this;
	}

	default  <ENTITY extends Entity> EntityBuilder<OWNER, ENTITY, OWNER> entity(EntityFactory<ENTITY> factory)
	{
		return entity(self(), factory);
	}

	default <ENTITY extends Entity> EntityBuilder<OWNER, ENTITY, OWNER> entity(String name, EntityFactory<ENTITY> factory)
	{
		return entity(self(), name, factory);
	}

	default <ENTITY extends Entity, PARENT> EntityBuilder<OWNER, ENTITY, PARENT> entity(PARENT parent, EntityFactory<ENTITY> factory)
	{
		return entity(parent, self().currentName(), factory);
	}

	default <ENTITY extends Entity, PARENT> EntityBuilder<OWNER, ENTITY, PARENT> entity(PARENT parent, String name, EntityFactory<ENTITY> factory)
	{
		return self().entry(name, callback -> new EntityBuilder<>(self(), parent, name, callback, factory));
	}
}
