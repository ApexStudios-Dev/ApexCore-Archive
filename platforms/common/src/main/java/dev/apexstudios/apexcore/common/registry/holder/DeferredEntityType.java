package dev.apexstudios.apexcore.common.registry.holder;

import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DeferredEntityType<T extends Entity> extends DeferredHolder<EntityType<?>, EntityType<T>> implements EntityTypeTest<Entity, T>
{
    protected DeferredEntityType(ResourceKey<EntityType<?>> valueKey)
    {
        super(valueKey);
    }

    @Nullable
    @Override
    public T tryCast(Entity entity)
    {
        return value().tryCast(entity);
    }

    @Override
    public Class<? extends Entity> getBaseClass()
    {
        return value().getBaseClass();
    }

    @Nullable
    public T spawn(ServerLevel level, BlockPos pos, MobSpawnType spawnType)
    {
        return value().spawn(level, pos, spawnType);
    }

    @Nullable
    public T spawn(ServerLevel level, @Nullable CompoundTag tag, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType spawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return value().spawn(level, tag, consumer, pos, spawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T create(ServerLevel level, @Nullable CompoundTag tag, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType spawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return value().create(level, tag, consumer, pos, spawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T create(Level level)
    {
        return value().create(level);
    }

    public static <T extends Entity> DeferredEntityType<T> createEntity(ResourceLocation valueId)
    {
        return createEntity(ResourceKey.create(Registries.ENTITY_TYPE, valueId));
    }

    public static <T extends Entity> DeferredEntityType<T> createEntity(ResourceKey<EntityType<?>> valueKey)
    {
        return new DeferredEntityType<>(valueKey);
    }
}
