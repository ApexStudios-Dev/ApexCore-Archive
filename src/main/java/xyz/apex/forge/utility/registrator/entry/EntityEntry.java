package xyz.apex.forge.utility.registrator.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.RegistryObject;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public final class EntityEntry<ENTITY extends Entity> extends RegistryEntry<EntityType<ENTITY>> implements NonnullSupplier<EntityType<ENTITY>>
{
	public EntityEntry(AbstractRegistrator<?> registrator, RegistryObject<EntityType<ENTITY>> delegate)
	{
		super(registrator, delegate);
	}

	public boolean isInEntityTypeTag(Tag<EntityType<?>> tag)
	{
		return asEntityType().is(tag);
	}

	public boolean isEntityType(EntityType<?> entityType)
	{
		return asEntityType() == entityType;
	}

	@Nullable
	public ENTITY spawn(ServerLevel level, @Nullable ItemStack stack, @Nullable Player player, BlockPos pos, MobSpawnType spawnReason, boolean unk1, boolean unk2)
	{
		return spawn(level, stack == null ? null : stack.getTag(), stack != null && stack.hasCustomHoverName() ? stack.getHoverName() : null, player, pos, spawnReason, unk1, unk2);
	}

	@Nullable
	public ENTITY spawn(ServerLevel level, @Nullable CompoundTag entityTag, @Nullable Component displayName, @Nullable Player player, BlockPos pos, MobSpawnType spawnReason, boolean unk1, boolean unk2)
	{
		return asEntityType().spawn(level, entityTag, displayName, player, pos, spawnReason, unk1, unk2);
	}

	@Nullable
	public ENTITY create(ServerLevel level, @Nullable CompoundTag entityTag, @Nullable Component displayName, @Nullable Player player, BlockPos pos, MobSpawnType spawnReason, boolean unk1, boolean unk2)
	{
		return asEntityType().create(level, entityTag, displayName, player, pos, spawnReason, unk1, unk2);
	}

	@Nullable
	public ENTITY create(Level level)
	{
		return asEntityType().create(level);
	}

	public MobCategory getCategory()
	{
		return asEntityType().getCategory();
	}

	public ResourceLocation getDefaultLootTable()
	{
		return asEntityType().getDefaultLootTable();
	}

	public float getWidth()
	{
		return asEntityType().getWidth();
	}

	public float getHeight()
	{
		return asEntityType().getHeight();
	}

	public EntityDimensions getDimensions()
	{
		return asEntityType().getDimensions();
	}

	public EntityType<ENTITY> asEntityType()
	{
		return get();
	}

	public boolean hasType(Entity entity)
	{
		return isEntityType(entity.getType());
	}

	public static <ENTITY extends Entity> EntityEntry<ENTITY> cast(RegistryEntry<EntityType<ENTITY>> registryEntry)
	{
		return cast(EntityEntry.class, registryEntry);
	}

	public static <ENTITY extends Entity> EntityEntry<ENTITY> cast(com.tterrag.registrate.util.entry.RegistryEntry<EntityType<ENTITY>> registryEntry)
	{
		return cast(EntityEntry.class, registryEntry);
	}
}
