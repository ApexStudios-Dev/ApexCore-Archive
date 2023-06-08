package xyz.apex.minecraft.apexcore.fabric.lib.hook;

import com.google.common.base.Suppliers;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.hook.EntityHooks;

import java.util.function.Supplier;

final class EntityHooksImpl implements EntityHooks
{
    private final Supplier<Boolean> isApiPresent = Suppliers.memoize(() -> {
        try
        {
            Class.forName("net.fabricmc.fabric.api.entity.FakePlayer");
            return true;
        }
        catch(Throwable t)
        {
            return false;
        }
    });

    @Override
    public boolean isFakePlayer(@Nullable Entity entity)
    {
        // TODO: Change to just check instance of in 1.20
        //  Fake-Player-API added during a 1.19.4 build, not all of 1.19.4 will have the API
        if(isApiPresent.get()) return isFakePlayerApi(entity);
        return entity instanceof ServerPlayer && entity.getClass() != ServerPlayer.class;
    }

    @Override
    public void registerDefaultAttribute(Supplier<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier.Builder> defaultAttributes)
    {
        FabricDefaultAttributeRegistry.register(entityType.get(), defaultAttributes.get());
    }

    @Override
    public <T extends Mob> void registerSpawnPlacement(Supplier<EntityType<T>> entityType, SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        SpawnPlacements.register(entityType.get(), spawnPlacementType, heightmapType, spawnPredicate);
    }

    private boolean isFakePlayerApi(@Nullable Entity entity)
    {
        return entity instanceof FakePlayer;
    }
}
