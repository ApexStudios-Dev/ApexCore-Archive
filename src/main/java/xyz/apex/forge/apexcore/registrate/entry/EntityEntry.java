package xyz.apex.forge.apexcore.registrate.entry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

public final class EntityEntry<ENTITY extends Entity> extends RegistryEntry<EntityType<ENTITY>>
{
	public EntityEntry(CoreRegistrate<?> owner, RegistryObject<EntityType<ENTITY>> delegate)
	{
		super(owner, delegate);
	}

	@Nullable
	public ENTITY create(Level level)
	{
		return get().create(level);
	}

	public boolean isIn(@Nullable EntityType<?> entityType)
	{
		return entityType != null && entityType == get();
	}

	public boolean is(@Nullable Entity entity)
	{
		return entity != null && is(entity.getType());
	}

	public boolean hasBlockTag(TagKey<EntityType<?>> tag)
	{
		return get().is(tag);
	}

	public static <ENTITY extends Entity> EntityEntry<ENTITY> cast(com.tterrag.registrate.util.entry.RegistryEntry<EntityType<ENTITY>> entry)
	{
		return cast(EntityEntry.class, entry);
	}
}