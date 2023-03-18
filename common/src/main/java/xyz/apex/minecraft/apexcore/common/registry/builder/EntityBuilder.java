package xyz.apex.minecraft.apexcore.common.registry.builder;

import com.google.common.collect.Streams;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.registry.entry.EntityEntry;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class EntityBuilder<T extends Entity> extends Builder<EntityType<?>, EntityType<T>, EntityEntry<T>, EntityBuilder<T>>
{
    private Function<EntityType.Builder<T>, EntityType.Builder<T>> propertiesModifier = Function.identity();
    private final EntityType.EntityFactory<T> entityFactory;
    private final MobCategory mobCategory;

    private EntityBuilder(String ownerId, String registrationName, MobCategory mobCategory, EntityType.EntityFactory<T> entityFactory)
    {
        super(Registries.ENTITY_TYPE, ownerId, registrationName);

        this.mobCategory = mobCategory;
        this.entityFactory = entityFactory;
    }

    // region: Properties
    public EntityBuilder<T> properties(UnaryOperator<EntityType.Builder<T>> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public EntityBuilder<T> sized(float width, float height)
    {
        return properties(properties -> properties.sized(width, height));
    }

    public EntityBuilder<T> noSummon()
    {
        return properties(EntityType.Builder::noSummon);
    }

    public EntityBuilder<T> noSave()
    {
        return properties(EntityType.Builder::noSave);
    }

    public EntityBuilder<T> fireImmune()
    {
        return properties(EntityType.Builder::fireImmune);
    }

    public EntityBuilder<T> immuneTo(Supplier<Block> block)
    {
        return properties(properties -> properties.immuneTo(block.get()));
    }

    @SafeVarargs
    public final EntityBuilder<T> immuneTo(Supplier<Block>... blocks)
    {
        return properties(properties -> {
            var resolved = Stream.of(blocks).map(Supplier::get).distinct().toArray(Block[]::new);
            return properties.immuneTo(resolved);
        });
    }

    public EntityBuilder<T> validBlocks(Collection<Supplier<Block>> blocks)
    {
        return properties(properties -> {
            var resolved = blocks.stream().map(Supplier::get).distinct().toArray(Block[]::new);
            return properties.immuneTo(resolved);
        });
    }

    public EntityBuilder<T> validBlocks(Iterable<Supplier<Block>> blocks)
    {
        return properties(properties -> {
            var resolved = Streams.stream(blocks).map(Supplier::get).distinct().toArray(Block[]::new);
            return properties.immuneTo(resolved);
        });
    }

    public EntityBuilder<T> canSpawnFarFromPlayer()
    {
        return properties(EntityType.Builder::canSpawnFarFromPlayer);
    }

    public EntityBuilder<T> clientTrackingRange(int clientTrackingRange)
    {
        return properties(properties -> properties.clientTrackingRange(clientTrackingRange));
    }

    public EntityBuilder<T> updateInterval(int updateInterval)
    {
        return properties(properties -> properties.updateInterval(updateInterval));
    }
    // endregion

    @Override
    protected EntityEntry<T> createRegistryEntry(ResourceLocation registryName)
    {
        return new EntityEntry<>(registryName);
    }

    @Override
    protected EntityType<T> create()
    {
        return propertiesModifier.apply(EntityType.Builder.of(entityFactory, mobCategory)).build(registryName.toString());
    }

    public static <T extends Entity> EntityBuilder<T> builder(String ownerId, String registrationName, MobCategory mobCategory, EntityType.EntityFactory<T> entityFactory)
    {
        return new EntityBuilder<>(ownerId, registrationName, mobCategory, entityFactory);
    }
}
