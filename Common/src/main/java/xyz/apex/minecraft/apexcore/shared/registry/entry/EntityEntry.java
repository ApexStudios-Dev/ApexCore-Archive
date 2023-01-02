package xyz.apex.minecraft.apexcore.shared.registry.entry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;

import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public final class EntityEntry<T extends Entity> extends RegistryEntry<EntityType<T>>
{
    public EntityEntry(AbstractRegistrar<?> owner, ResourceKey<? super EntityType<T>> registryKey)
    {
        super(owner, Registries.ENTITY_TYPE, registryKey);
    }

    public <I extends Item> ItemEntry<I> asItemEntry()
    {
        return getSibling(Registries.ITEM, ItemEntry.class);
    }

    public boolean is(@Nullable EntityType<?> other)
    {
        return isPresent() && get() == other;
    }

    public boolean hasSameType(@Nullable Entity other)
    {
        return other != null && is(other.getType());
    }

    public boolean hasEntityTypeTag(TagKey<EntityType<?>> tag)
    {
        return get().is(tag);
    }

    @Nullable
    public T spawn(ServerLevel level, BlockPos pos, MobSpawnType mobSpawnType)
    {
        return get().spawn(level, pos, mobSpawnType);
    }

    @Nullable
    public T spawn(ServerLevel level, @Nullable CompoundTag tag, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType mobSpawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return get().spawn(level, tag, consumer, pos, mobSpawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T create(ServerLevel level, @Nullable CompoundTag tag, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType mobSpawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return get().create(level, tag, consumer, pos, mobSpawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T create(Level level)
    {
        return get().create(level);
    }
}
