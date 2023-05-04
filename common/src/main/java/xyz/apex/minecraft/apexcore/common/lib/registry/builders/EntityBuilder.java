package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.item.ExtendedSpawnEggItem;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.EntityEntry;

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
public final class EntityBuilder<P, T extends Entity> extends AbstractBuilder<P, EntityType<?>, EntityType<T>, EntityEntry<T>, EntityBuilder<P, T>> implements FeatureElementBuilder<P, EntityType<?>, EntityType<T>, EntityEntry<T>, EntityBuilder<P, T>>
{
    private Function<EntityType.Builder<T>, EntityType.Builder<T>> entityTypeModifier = Function.identity();
    private MobCategory mobCategory = MobCategory.MISC;
    private final EntityType.EntityFactory<T> entityFactory;

    EntityBuilder(P parent, BuilderManager builderManager, String registrationName, EntityType.EntityFactory<T> entityFactory)
    {
        super(parent, builderManager, Registries.ENTITY_TYPE, registrationName, EntityEntry::new);

        this.entityFactory = entityFactory;
    }

    @Override
    protected EntityType<T> createObject()
    {
        return entityTypeModifier.apply(EntityType.Builder.of(entityFactory, mobCategory)).build(getRegistryName().toString());
    }

    // TODO: attribute, spawn placement

    /**
     * Returns item builder to construct spawn egg item build to this entity.
     *
     * @param spawnEggItemFactory Factory used to construct spawn egg item.
     * @param backgroundColor     Background color of spawn egg item model.
     * @param highlightColor      Highlight color of spawn egg item model.
     * @param <I>                 Spawn egg item type.
     * @return Spawn egg item builder.
     */
    @SuppressWarnings("unchecked")
    public <I extends ExtendedSpawnEggItem> ItemBuilder<EntityBuilder<P, T>, I> spawnEgg(SpawnEggItemFactory<I> spawnEggItemFactory, int backgroundColor, int highlightColor)
    {
        return builderManager.item(self(), getRegistrationName(), properties -> spawnEggItemFactory.create(() -> (EntityType<? extends Mob>) asSupplier().get(), backgroundColor, highlightColor, properties))
                .withRegistrationNameSuffix("_spawn_egg")
                .addListener(ExtendedSpawnEggItem::registerSpawnEgg);
    }

    /**
     * Returns item builder to construct spawn egg item build to this entity.
     *
     * @param backgroundColor Background color of spawn egg item model.
     * @param highlightColor  Highlight color of spawn egg item model.
     * @return Spawn egg item builder.
     */
    public ItemBuilder<EntityBuilder<P, T>, ExtendedSpawnEggItem> spawnEgg(int backgroundColor, int highlightColor)
    {
        return spawnEgg(ExtendedSpawnEggItem::new, backgroundColor, highlightColor);
    }

    /**
     * Builds and registers default spawn egg item bound to this entity.
     *
     * @param backgroundColor Background color of spawn egg item model.
     * @param highlightColor  Highlight color of spawn egg item model.
     * @return This builder instance.
     */
    public EntityBuilder<P, T> defaultSpawnEgg(int backgroundColor, int highlightColor)
    {
        return spawnEgg(backgroundColor, highlightColor).end();
    }

    /**
     * Registers renderer for this entity.
     *
     * @param renderer Renderer to be registered.
     * @return This builder instance
     */
    public EntityBuilder<P, T> renderer(Supplier<EntityRendererProvider<T>> renderer)
    {
        return addListener(value -> PhysicalSide.CLIENT.runWhenOn(() -> () -> Services.HOOKS.registerRenderer().registerEntityRenderer(() -> value, renderer)));
    }

    /**
     * Set the category for this entity.
     *
     * @param mobCategory Category for this entity.
     * @return This builder instance.
     */
    public EntityBuilder<P, T> category(MobCategory mobCategory)
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
    public EntityBuilder<P, T> properties(UnaryOperator<EntityType.Builder<T>> entityTypeModifier)
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
    public EntityBuilder<P, T> sized(float width, float height)
    {
        return properties(properties -> properties.sized(width, height));
    }

    /**
     * Mark this entity as not being summonable by the summon command.
     *
     * @return This builder instance.
     */
    public EntityBuilder<P, T> noSummon()
    {
        return properties(EntityType.Builder::noSummon);
    }

    /**
     * Mark this entity as not being saved to disk.
     *
     * @return This builder instance.
     */
    public EntityBuilder<P, T> noSave()
    {
        return properties(EntityType.Builder::noSave);
    }

    /**
     * Mark this entity as being fire immune.
     *
     * @return This builder instance.
     */
    public EntityBuilder<P, T> fireImmune()
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
    public final EntityBuilder<P, T> immuneTo(Supplier<? extends Block>... blocks)
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
    public EntityBuilder<P, T> canSpawnFarFromPlayer()
    {
        return properties(EntityType.Builder::canSpawnFarFromPlayer);
    }

    /**
     * Set the client tracking range of this entity.
     *
     * @param clientTrackingRange Client tracking range for this entity.
     * @return This builder instance.
     */
    public EntityBuilder<P, T> clientTrackingRange(int clientTrackingRange)
    {
        return properties(properties -> properties.clientTrackingRange(clientTrackingRange));
    }

    /**
     * Set the updater interval of this entity.
     *
     * @param updateInterval Update interval for this entity.
     * @return This builder instance.
     */
    public EntityBuilder<P, T> updateInterval(int updateInterval)
    {
        return properties(properties -> properties.updateInterval(updateInterval));
    }

    @Override
    public EntityBuilder<P, T> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        if(getParent() instanceof FeatureElementBuilder<?, ?, ?, ?, ?> feature)
            feature.requiredFeatures(requiredFeatures);
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
    }

    @FunctionalInterface
    public interface SpawnEggItemFactory<I extends ExtendedSpawnEggItem>
    {
        I create(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties);
    }
}
