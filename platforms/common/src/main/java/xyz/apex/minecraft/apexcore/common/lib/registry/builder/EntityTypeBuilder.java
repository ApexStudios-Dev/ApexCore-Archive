package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.EntityHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RendererHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryProviderListener;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.EntityTypeEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.EntityFactory;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderTypes;
import xyz.apex.minecraft.apexcore.common.lib.resgen.tag.TagsProvider;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * EntityType Builder implementation.
 * <p>
 * Used to build and register EntityType entries.
 *
 * @param <O> Type of Registrar.
 * @param <T> Type of Entity [Entry].
 * @param <P> Type of Parent.
 */
public final class EntityTypeBuilder<O extends AbstractRegistrar<O>, T extends Entity, P> extends AbstractBuilder<O, P, EntityType<?>, EntityType<T>, EntityTypeBuilder<O, T, P>, EntityTypeEntry<T>> implements FeaturedBuilder<O, P, EntityType<?>, EntityType<T>, EntityTypeBuilder<O, T, P>, EntityTypeEntry<T>>
{
    private final EntityFactory<T> entityFactory;
    private final MobCategory category;
    private Function<EntityType.Builder<T>, EntityType.Builder<T>> propertiesModifier = Function.identity();
    @Nullable private Triple<SpawnPlacements.Type, Heightmap.Types, SpawnPredicate<T>> spawnPlacementData = null;
    @Nullable private Supplier<AttributeSupplier.Builder> attributes = null;
    @Nullable private Supplier<Supplier<EntityRendererProvider<T>>> renderer = null;

    @ApiStatus.Internal
    public EntityTypeBuilder(O registrar, P parent, String registrationName, MobCategory category, EntityFactory<T> entityFactory)
    {
        super(registrar, parent, Registries.ENTITY_TYPE, registrationName);

        this.entityFactory = entityFactory;
        this.category = category;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void onRegister(EntityType<T> entry)
    {
        if(attributes != null)
            EntityHooks.get().registerDefaultAttributes(() -> (EntityType<? extends LivingEntity>) entry, attributes);
        if(spawnPlacementData != null)
            EntityHooks.get().registerSpawnPlacement(() -> (EntityType) entry, spawnPlacementData.getLeft(), spawnPlacementData.getMiddle(), (SpawnPlacements.SpawnPredicate<? extends Mob>) spawnPlacementData.getRight());

        PhysicalSide.CLIENT.runWhenOn(() -> () -> {
            if(renderer != null)
                RendererHooks.get().registerEntityRenderer(() -> entry, renderer);
        });
    }

    /**
     * Register spawn placement data and predicate for this Entity.
     *
     * @param spawnPlacementType Spawn placement restriction type.
     * @param heightMapType Height map type.
     * @param spawnPredicate Spawn placement predicate.
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> spawnPlacement(SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightMapType, SpawnPredicate<T> spawnPredicate)
    {
        spawnPlacementData = Triple.of(spawnPlacementType, heightMapType, spawnPredicate);
        return this;
    }

    /**
     * Set the Attribute builder for this Entity.
     * <p>
     * <b>NOTE</b>: This assumes your Entity is a LivingEntity subtype.
     *
     * @param attributes Attributes to be registered.
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> attributes(Supplier<AttributeSupplier.Builder> attributes)
    {
        this.attributes = attributes;
        return this;
    }

    /**
     * Returns new Item Builder constructing SpawnEgg Item bound to this Entity.
     * <p>
     * <b>NOTE</b>: This assumes your Entity is a Mob subtype.
     *
     * @param backgroundColor Background color of SpawnEgg Item model.
     * @param highlightColor Highlight color of SpawnEgg Item model.
     * @return New Builder constructing SpawnEgg Item bound to this Entity.
     */
    @SuppressWarnings("unchecked")
    public ItemBuilder<O, SpawnEggItem, EntityTypeBuilder<O, T, P>> spawnEgg(int backgroundColor, int highlightColor)
    {
        return registrar.item(this, "%s_spawn_egg".formatted(registrationName()), properties -> ApexCore.INSTANCE.createSpawnEgg(() -> (EntityType<? extends Mob>) getEntry(), backgroundColor, highlightColor, properties))
                        .model((provider, lookup, entry) -> provider.withParent(
                                entry.getRegistryName().withPrefix("item/"),
                                new ResourceLocation("item/template_spawn_egg")
                        ));
    }

    /**
     * Builds and registers default spawn egg item bound to this entity.
     * <p>
     * <b>NOTE</b>: This assumes your Entity is a Mob subtype.
     *
     * @param backgroundColor Background color of spawn egg item model.
     * @param highlightColor Highlight color of spawn egg item model.
     * @return This Builder
     */
    public EntityTypeBuilder<O, T, P> defaultSpawnEgg(int backgroundColor, int highlightColor)
    {
        return spawnEgg(backgroundColor, highlightColor).build();
    }

    /**
     * Registers a renderer for this entity.
     *
     * @param renderer Renderer to be registered.
     * @return This Builder
     */
    public EntityTypeBuilder<O, T, P> renderer(Supplier<Supplier<EntityRendererProvider<T>>> renderer)
    {
        this.renderer = renderer;
        return this;
    }

    /**
     * Adds a new property modifier, used to modify the Entity builder.
     *
     * @param propertiesModifier Modifier used to modify the Entity properties.
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> properties(UnaryOperator<EntityType.Builder<T>> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    /**
     * Set the bounding box size for this Entity.
     *
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> sized(float width, float height)
    {
        return properties(properties -> properties.sized(width, height));
    }

    /**
     * Mark this Entity as not being summonable via Commands.
     *
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> noSummon()
    {
        return properties(EntityType.Builder::noSummon);
    }

    /**
     * Mark this Entity as not saving to disk.
     *
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> noSave()
    {
        return properties(EntityType.Builder::noSave);
    }

    /**
     * Mark this EntityType as being fire-immune.
     *
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> fireImmune()
    {
        return properties(EntityType.Builder::fireImmune);
    }

    /**
     * Mark this Entity as being immune to the given Blocks.
     *
     * @return This Builder.
     */
    @SafeVarargs
    public final EntityTypeBuilder<O, T, P> immuneTo(Supplier<Block>... blocks)
    {
        return properties(properties -> {
            var resolvedBlocks = Stream.of(blocks).map(Supplier::get).toArray(Block[]::new);
            return properties.immuneTo(resolvedBlocks);
        });
    }

    /**
     * Mark this Entity as being able to spawn far from Players.
     *
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> canSpawnFarFromPlayer()
    {
        return properties(EntityType.Builder::canSpawnFarFromPlayer);
    }

    /**
     * Set the client tracking range for this Entity.
     *
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> clientTrackingRange(int clientTrackingRange)
    {
        return properties(properties -> properties.clientTrackingRange(clientTrackingRange));
    }

    /**
     * Set the update interval for this Entity.
     *
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> updateInterval(int updateInterval)
    {
        return properties(properties -> properties.updateInterval(updateInterval));
    }

    @Override
    public EntityTypeBuilder<O, T, P> requiredFeatures(FeatureFlag... requiredFeatures)
    {
        return properties(properties -> properties.requiredFeatures(requiredFeatures));
    }

    /**
     * Resister a set of Tags for this Entity.
     *
     * @param tags Tags to be set.
     * @return This Builder.
     */
    @SafeVarargs
    public final EntityTypeBuilder<O, T, P> tag(TagKey<EntityType<?>>... tags)
    {
        return tag((provider, lookup, entry) -> Stream.of(tags).map(provider::tag).forEach(tag -> tag.addElement(entry.value())));
    }

    /**
     * Set the Tag generator for this Entity.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> tag(RegistryProviderListener<TagsProvider<EntityType<?>>, EntityType<T>, EntityTypeEntry<T>> listener)
    {
        return setProvider(ProviderTypes.ENTITY_TYPE_TAGS, listener);
    }

    /**
     * Clears the currently registered Tag generator.
     *
     * @return This Builder.
     */
    public EntityTypeBuilder<O, T, P> noTags()
    {
        return clearProvider(ProviderTypes.ENTITY_TYPE_TAGS);
    }

    @Override
    protected EntityTypeEntry<T> createRegistryEntry()
    {
        return new EntityTypeEntry<>(registrar, registryKey);
    }

    @Override
    protected EntityType<T> createEntry()
    {
        return propertiesModifier.apply(EntityType.Builder.of(entityFactory, category)).build(registryName().toString());
    }

    @Override
    protected String getDescriptionId(EntityTypeEntry<T> entry)
    {
        return entry.value().getDescriptionId();
    }

    @FunctionalInterface
    public interface SpawnPredicate<T extends Entity> extends SpawnPlacements.SpawnPredicate<T>
    {
        @Override
        boolean test(EntityType<T> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource rng);
    }
}
