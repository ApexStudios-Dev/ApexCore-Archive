package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public interface RegistryHelper
{
    String ownerId();

    void register(AbstractRegister<?> register);

    void registerColorHandler(ItemLike item, OptionalLike<OptionalLike<ItemColor>> colorHandler);

    void registerColorHandler(Block block, OptionalLike<OptionalLike<BlockColor>> colorHandler);

    void registerRenderType(Block block, OptionalLike<OptionalLike<RenderType>> renderType);

    void registerRenderType(Fluid fluid, OptionalLike<OptionalLike<RenderType>> renderType);

    <T extends Entity> void registerEntityAttributes(EntityType<T> entityType, OptionalLike<AttributeSupplier.Builder> attributes);

    <T extends Entity> void registerEntityRenderer(EntityType<T> entityType, OptionalLike<OptionalLike<EntityRendererProvider<T>>> rendererProvider);

    void registerItemDispenseBehavior(ItemLike item, OptionalLike<DispenseItemBehavior> dispenseBehavior);

    <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> blockEntityType, OptionalLike<OptionalLike<BlockEntityRendererProvider<T>>> rendererProvider);

    static <T extends Entity> void registerEntitySpawnPlacement(EntityType<T> entityType, @Nullable SpawnPlacements.Type spawnPlacement, @Nullable Heightmap.Types heightmap, @Nullable SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        // nothing to register, at least 1 must exist
        if(spawnPlacement == null && heightmap == null && spawnPredicate == null)
            return;

        // all must be none null, use defaults if provided null
        var spawnData = new SpawnPlacements.Data(
                heightmap == null ? Heightmap.Types.MOTION_BLOCKING_NO_LEAVES : heightmap,
                spawnPlacement == null ? SpawnPlacements.Type.NO_RESTRICTIONS : spawnPlacement,
                // default spawn predicate, true only if entity being spawned is of same type and enabled
                spawnPredicate == null ? (entityType1, level, spawnType, pos, random) -> entityType == entityType1 && entityType.isEnabled(level.enabledFeatures()) : spawnPredicate
        );

        // SpawnPlacements.register - this is bound to `Mob` for the entity type
        // but the backing function and lookup maps are unbounded or bound to `Entity`
        // SpawnPlacements.register(value, spawnPlacement, heightmap, spawnPredicate);
        // we register manually here rather than using the helper method, to not be bound to `Mob`
        // pretty much the exact same code but bounded to `Entity` rather than `Mob`
        if(SpawnPlacements.DATA_BY_TYPE.put(entityType, spawnData) != null)
            throw new IllegalStateException("Duplicate registration for type %s".formatted(BuiltInRegistries.ENTITY_TYPE.getKey(entityType)));
    }

    static <T extends Entity> void registerEntitySpawnPlacement(EntityType<T> entityType, @Nullable SpawnPlacements.Type spawnPlacement, @Nullable SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        registerEntitySpawnPlacement(entityType, spawnPlacement, null, spawnPredicate);
    }

    static void registerEntitySpawnPlacement(EntityType<?> entityType, @Nullable SpawnPlacements.Type spawnPlacement, @Nullable Heightmap.Types heightmap)
    {
        registerEntitySpawnPlacement(entityType, spawnPlacement, heightmap, null);
    }

    static void registerEntitySpawnPlacement(EntityType<?> entityType, @Nullable SpawnPlacements.Type spawnPlacement)
    {
        registerEntitySpawnPlacement(entityType, spawnPlacement, null, null);
    }

    static <T extends Entity> void registerEntitySpawnPlacement(EntityType<T> entityType, @Nullable Heightmap.Types heightmap, @Nullable SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        registerEntitySpawnPlacement(entityType, null, heightmap, spawnPredicate);
    }

    static void registerEntitySpawnPlacement(EntityType<?> entityType, @Nullable Heightmap.Types heightmap)
    {
        registerEntitySpawnPlacement(entityType, null, heightmap, null);
    }

    static <T extends Entity> void registerEntitySpawnPlacement(EntityType<T> entityType, @Nullable SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        registerEntitySpawnPlacement(entityType, null, null, spawnPredicate);
    }

    static <T extends Entity> boolean defaultSpawnPredicate(EntityType<T> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random)
    {
        return true;
    }

    static RegistryHelper get(String ownerId)
    {
        return Platform.get().registryHelper(ownerId);
    }
}
