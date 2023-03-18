package xyz.apex.minecraft.apexcore.common.registry.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;

import java.util.function.Consumer;

public final class EntityEntry<T extends Entity> extends RegistryEntry<EntityType<T>> implements FeatureElementEntry<EntityType<T>>, EntityTypeTest<Entity, T>
{
    public EntityEntry(ResourceLocation registryName)
    {
        super(Registries.ENTITY_TYPE, registryName);
    }

    @Nullable
    public T spawn(ServerLevel level, @Nullable ItemStack stack, @Nullable Player player, BlockPos pos, MobSpawnType mobSpawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return get().spawn(level, stack, player, pos, mobSpawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T spawn(ServerLevel level, BlockPos pos, MobSpawnType mobSpawnType)
    {
        return get().spawn(level, pos, mobSpawnType);
    }

    @Nullable
    public T spawn(ServerLevel level, @Nullable CompoundTag nbt, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType mobSpawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return get().spawn(level, nbt, consumer, pos, mobSpawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T create(ServerLevel level, @Nullable CompoundTag nbt, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType mobSpawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return get().create(level, nbt, consumer, pos, mobSpawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T create(Level level)
    {
        return get().create(level);
    }

    public boolean isBlockDangerous(BlockState blockState)
    {
        return get().isBlockDangerous(blockState);
    }

    @Nullable
    @Override
    public T tryCast(Entity entity)
    {
        return get().tryCast(entity);
    }

    @Override
    public Class<? extends Entity> getBaseClass()
    {
        return get().getBaseClass();
    }
}
