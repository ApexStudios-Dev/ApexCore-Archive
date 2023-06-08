package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.EntityHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegistryHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RendererHooks;
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
 * @param <M> Type of builder manager.
 */
public final class EntityBuilder<P, T extends Entity, M extends BuilderManager<M>> extends AbstractBuilder<P, EntityType<?>, EntityType<T>, EntityEntry<T>, EntityBuilder<P, T, M>, M> implements FeatureElementBuilder<P, EntityType<?>, EntityType<T>, EntityEntry<T>, EntityBuilder<P, T, M>, M>
{
    private Function<EntityType.Builder<T>, EntityType.Builder<T>> entityTypeModifier = Function.identity();
    private MobCategory mobCategory = MobCategory.MISC;
    private final EntityType.EntityFactory<T> entityFactory;

    EntityBuilder(P parent, M builderManager, String registrationName, EntityType.EntityFactory<T> entityFactory)
    {
        super(parent, builderManager, Registries.ENTITY_TYPE, registrationName, EntityEntry::new);

        this.entityFactory = entityFactory;
    }

    @Override
    protected EntityType<T> createObject()
    {
        return entityTypeModifier.apply(EntityType.Builder.of(entityFactory, mobCategory)).build(getRegistryName().toString());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public EntityBuilder<P, T, M> spawnPlacement(SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightMapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        return addListener(value -> EntityHooks.get().registerSpawnPlacement(() -> (EntityType) value, spawnPlacementType, heightMapType, (SpawnPlacements.SpawnPredicate) spawnPredicate));
    }

    /**
     * Registers given default attributes for this entity.
     *
     * @param attributes Attributes to be registered.
     * @return This builder instance.
     */
    @SuppressWarnings("unchecked")
    public EntityBuilder<P, T, M> attributes(Supplier<AttributeSupplier.Builder> attributes)
    {
        return addListener(value -> EntityHooks.get().registerDefaultAttributes(() -> (EntityType<? extends LivingEntity>) value, attributes));
    }

    /**
     * Returns item builder to construct spawn egg item build to this entity.
     *
     * @param backgroundColor     Background color of spawn egg item model.
     * @param highlightColor      Highlight color of spawn egg item model.
     * @return Spawn egg item builder.
     */
    @SuppressWarnings("unchecked")
    public ItemBuilder<EntityBuilder<P, T, M>, SpawnEggItem, M> spawnEgg(int backgroundColor, int highlightColor)
    {
        return builderManager.item(self(), getRegistrationName(), properties -> RegistryHooks.get().createSpawnEgg(() -> (EntityType<? extends Mob>) asSupplier().get(), backgroundColor, highlightColor, properties))
                .withRegistrationNameSuffix("_spawn_egg");
    }

    /**
     * Builds and registers default spawn egg item bound to this entity.
     *
     * @param backgroundColor Background color of spawn egg item model.
     * @param highlightColor  Highlight color of spawn egg item model.
     * @return This builder instance.
     */
    public EntityBuilder<P, T, M> defaultSpawnEgg(int backgroundColor, int highlightColor)
    {
        return spawnEgg(backgroundColor, highlightColor).end();
    }

    /**
     * Registers renderer for this entity.
     *
     * @param renderer Renderer to be registered.
     * @return This builder instance
     */
    public EntityBuilder<P, T, M> renderer(Supplier<Supplier<EntityRendererProvider<T>>> renderer)
    {
        return addListener(value -> PhysicalSide.CLIENT.runWhenOn(() -> () -> RendererHooks.get().registerEntityRenderer(() -> value, renderer)));
    }

    /**
     * Set the category for this entity.
     *
     * @param mobCategory Category for this entity.
     * @return This builder instance.
     */
    public EntityBuilder<P, T, M> category(MobCategory mobCategory)
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
    public EntityBuilder<P, T, M> properties(UnaryOperator<EntityType.Builder<T>> entityTypeModifier)
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
    public EntityBuilder<P, T, M> sized(float width, float height)
    {
        return properties(properties -> properties.sized(width, height));
    }

    /**
     * Mark this entity as not being summonable by the summon command.
     *
     * @return This builder instance.
     */
    public EntityBuilder<P, T, M> noSummon()
    {
        return properties(EntityType.Builder::noSummon);
    }

    /**
     * Mark this entity as not being saved to disk.
     *
     * @return This builder instance.
     */
    public EntityBuilder<P, T, M> noSave()
    {
        return properties(EntityType.Builder::noSave);
    }

    /**
     * Mark this entity as being fire immune.
     *
     * @return This builder instance.
     */
    public EntityBuilder<P, T, M> fireImmune()
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
    public final EntityBuilder<P, T, M> immuneTo(Supplier<? extends Block>... blocks)
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
    public EntityBuilder<P, T, M> canSpawnFarFromPlayer()
    {
        return properties(EntityType.Builder::canSpawnFarFromPlayer);
    }

    /**
     * Set the client tracking range of this entity.
     *
     * @param clientTrackingRange Client tracking range for this entity.
     * @return This builder instance.
     */
    public EntityBuilder<P, T, M> clientTrackingRange(int clientTrackingRange)
    {
        return properties(properties -> properties.clientTrackingRange(clientTrackingRange));
    }

    /**
     * Set the updater interval of this entity.
     *
     * @param updateInterval Update interval for this entity.
     * @return This builder instance.
     */
    public EntityBuilder<P, T, M> updateInterval(int updateInterval)
    {
        return properties(properties -> properties.updateInterval(updateInterval));
    }

    @Override
    public EntityBuilder<P, T, M> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        if(getParent() instanceof FeatureElementBuilder<?, ?, ?, ?, ?, ?> feature)
            feature.requiredFeatures(requiredFeatures);
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
    }
}
