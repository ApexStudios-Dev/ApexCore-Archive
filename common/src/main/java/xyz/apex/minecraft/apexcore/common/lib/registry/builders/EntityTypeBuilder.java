package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.EntityTypeEntry;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * EntityType builder used to construct new entity type instances.
 *
 * @param <P> Type of parent element.
 * @param <T> Type of entity.
 */
public final class EntityTypeBuilder<P, T extends Entity> extends AbstractBuilder<P, EntityType<?>, EntityType<T>, EntityTypeEntry<T>, EntityTypeBuilder<P, T>> implements FeatureElementBuilder<P, EntityType<?>, EntityType<T>, EntityTypeEntry<T>, EntityTypeBuilder<P, T>>
{
    private Function<EntityType.Builder<T>, EntityType.Builder<T>> entityTypeModifier = Function.identity();
    private MobCategory mobCategory = MobCategory.MISC;
    private final EntityType.EntityFactory<T> entityFactory;

    EntityTypeBuilder(P parent, BuilderManager builderManager, String registrationName, EntityType.EntityFactory<T> entityFactory)
    {
        super(parent, builderManager, Registries.ENTITY_TYPE, registrationName, EntityTypeEntry::new);

        this.entityFactory = entityFactory;
    }

    @Override
    protected EntityType<T> createObject()
    {
        return entityTypeModifier.apply(EntityType.Builder.of(entityFactory, mobCategory)).build(getRegistryName().toString());
    }

    // TODO: spawn egg, renderer, attribute, spawn placement

    /**
     * Set the category for this entity.
     *
     * @param mobCategory Category for this entity.
     * @return This builder instance.
     */
    public EntityTypeBuilder<P, T> category(MobCategory mobCategory)
    {
        this.mobCategory = mobCategory;
        return self();
    }

    /**
     * Add a new property modifier to modify the finalized entity type.
     *
     * @param entityTypeModifier Modifier used to modify the finalized entity type.
     * @return This builder instance.
     */
    public EntityTypeBuilder<P, T> properties(UnaryOperator<EntityType.Builder<T>> entityTypeModifier)
    {
        this.entityTypeModifier = this.entityTypeModifier.andThen(entityTypeModifier);
        return self();
    }

    /**
     * Set the hibox size of this entity.
     *
     * @param width  Hitbox width.
     * @param height Hitbox height.
     * @return This builder instance.
     */
    public EntityTypeBuilder<P, T> sized(float width, float height)
    {
        return properties(properties -> properties.sized(width, height));
    }

    /**
     * Mark this entity as not being summonable by the summon command.
     *
     * @return This builder instance.
     */
    public EntityTypeBuilder<P, T> noSummon()
    {
        return properties(EntityType.Builder::noSummon);
    }

    /**
     * Mark this entity as not being saved to disk.
     *
     * @return This builder instance.
     */
    public EntityTypeBuilder<P, T> noSave()
    {
        return properties(EntityType.Builder::noSave);
    }

    /**
     * Mark this entity as being fire immune.
     *
     * @return This builder instance.
     */
    public EntityTypeBuilder<P, T> fireImmune()
    {
        return properties(EntityType.Builder::fireImmune);
    }

    /**
     * Mark this entity as being immune to the given blocks.
     *
     * @param blocks Blocks to mark this entity as being immune to.
     * @return This builder instance.
     */
    @SafeVarargs
    public final EntityTypeBuilder<P, T> immuneTo(Supplier<? extends Block>... blocks)
    {
        return properties(properties -> {
            var immuneBlocks = Stream.of(blocks).map(Supplier::get).toArray(Block[]::new);
            return properties.immuneTo(immuneBlocks);
        });
    }

    /**
     * Mark this entity as being allowed to spawn far from players.
     *
     * @return This builder instance.
     */
    public EntityTypeBuilder<P, T> canSpawnFarFromPlayer()
    {
        return properties(EntityType.Builder::canSpawnFarFromPlayer);
    }

    /**
     * Set the client tracking range of this entity.
     *
     * @param clientTrackingRange Client tracking range for this entity.
     * @return This builder instance.
     */
    public EntityTypeBuilder<P, T> clientTrackingRange(int clientTrackingRange)
    {
        return properties(properties -> properties.clientTrackingRange(clientTrackingRange));
    }

    /**
     * Set the updater interval of this entity.
     *
     * @param updateInterval Update interval for this entity.
     * @return This builder instance.
     */
    public EntityTypeBuilder<P, T> updateInterval(int updateInterval)
    {
        return properties(properties -> properties.updateInterval(updateInterval));
    }

    @Override
    public EntityTypeBuilder<P, T> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        if(getParent() instanceof FeatureElementBuilder<?, ?, ?, ?, ?> feature)
            feature.requiredFeatures(requiredFeatures);
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
    }

    @FunctionalInterface
    interface SpawnEggItemFactory<I extends Item, E extends Entity>
    {
        I create(Supplier<EntityType<E>> entityType, int backgroundColor, int highlightColor, Item.Properties properties);
    }
}
