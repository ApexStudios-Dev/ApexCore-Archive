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

public final class DeferredEntityType<T extends Entity> extends DeferredHolder<EntityType<?>, EntityType<T>> implements EntityTypeTest<Entity, T>
{
    private DeferredEntityType(String ownerId, ResourceKey<EntityType<?>> registryKey)
    {
        super(ownerId, registryKey);
    }

    @Nullable
    @Override
    public T tryCast(Entity entity)
    {
        return map(value -> value.tryCast(entity)).getRaw();
    }

    @Override
    public Class<? extends Entity> getBaseClass()
    {
        return value().getBaseClass();
    }

    @Nullable
    public T spawn(ServerLevel level, BlockPos pos, MobSpawnType spawnType)
    {
        return map(value -> value.spawn(level, pos, spawnType)).getRaw();
    }

    @Nullable
    public T spawn(ServerLevel level, @Nullable CompoundTag tag, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType spawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return map(value -> value.spawn(level, tag, consumer, pos, spawnType, shouldOffsetY, shouldOffsetYMore)).getRaw();
    }

    @Nullable
    public T create(ServerLevel level, @Nullable CompoundTag tag, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType spawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return map(value -> value.spawn(level, tag, consumer, pos, spawnType, shouldOffsetY, shouldOffsetYMore)).getRaw();
    }

    @Nullable
    public T create(Level level)
    {
        return map(value -> value.create(level)).getRaw();
    }

    public static ResourceKey<EntityType<?>> createRegistryKey(ResourceLocation registryName)
    {
        return ResourceKey.create(Registries.ENTITY_TYPE, registryName);
    }

    public static <T extends Entity> DeferredEntityType<T> createEntity(String ownerId, ResourceLocation registryName)
    {
        return createEntity(ownerId, createRegistryKey(registryName));
    }

    public static <T extends Entity> DeferredEntityType<T> createEntity(ResourceLocation registryName)
    {
        return createEntity(registryName.getNamespace(), registryName);
    }

    public static <T extends Entity> DeferredEntityType<T> createEntity(String ownerId, ResourceKey<EntityType<?>> registryKey)
    {
        return new DeferredEntityType<>(ownerId, registryKey);
    }

    public static <T extends Entity> DeferredEntityType<T> createEntity(ResourceKey<EntityType<?>> registryKey)
    {
        return new DeferredEntityType<>(registryKey.location().getNamespace(), registryKey);
    }
}
