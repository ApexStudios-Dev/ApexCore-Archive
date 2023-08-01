package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

import java.util.Optional;
import java.util.function.Consumer;

public final class EntityEntry<T extends Entity> extends BaseRegistryEntry<EntityType<T>> implements FeaturedEntry<EntityType<T>>, EntityTypeTest<Entity, T>, ItemLike
{
    @ApiStatus.Internal
    public EntityEntry(AbstractRegistrar<?> registrar, ResourceKey<EntityType<T>> registryKey)
    {
        super(registrar, registryKey);
    }

    public Optional<ItemEntry<SpawnEggItem>> asSpawnEgg()
    {
        return registrar.getOptional(Registries.ITEM, "%s_spawn_egg".formatted(getRegistryName().getPath())).map(ItemEntry::cast);
    }

    public boolean hasSpawnEgg()
    {
        return asSpawnEgg().isPresent();
    }

    @Override
    public Item asItem()
    {
        return asSpawnEgg().map(ItemLikeEntry::asItem).orElse(Items.AIR);
    }

    public boolean is(ItemStack stack)
    {
        return asSpawnEgg().map(entry -> entry.is(stack)).orElse(false);
    }

    public ItemStack asStack(int count)
    {
        return asSpawnEgg().map(entry -> entry.asStack(count)).orElse(ItemStack.EMPTY);
    }

    public ItemStack asStack()
    {
        return asStack(1);
    }

    @Nullable
    public T spawn(ServerLevel level, @Nullable ItemStack stack, @Nullable Player player, BlockPos pos, MobSpawnType spawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return value().spawn(level, stack, player, pos, spawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T spawn(ServerLevel level, BlockPos pos, MobSpawnType spawnType)
    {
        return value().spawn(level, pos, spawnType);
    }

    @Nullable
    public T spawn(ServerLevel level, @Nullable CompoundTag compound, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType spawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return value().spawn(level, compound, consumer, pos, spawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T create(ServerLevel level, @Nullable CompoundTag nbt, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType spawnType, boolean shouldOffsetY, boolean shouldOffsetYMore)
    {
        return value().create(level, nbt, consumer, pos, spawnType, shouldOffsetY, shouldOffsetYMore);
    }

    @Nullable
    public T create(Level level)
    {
        return value().create(level);
    }

    public boolean isBlockDangerous(BlockState blockState)
    {
        return value().isBlockDangerous(blockState);
    }

    public boolean is(Entity other)
    {
        return is(other.getType());
    }

    public boolean is(EntityType<?> other)
    {
        return value() == other;
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

    public static <T extends Entity> EntityEntry<T> cast(RegistryEntry<?> registryEntry)
    {
        return RegistryEntry.cast(EntityEntry.class, registryEntry);
    }
}
