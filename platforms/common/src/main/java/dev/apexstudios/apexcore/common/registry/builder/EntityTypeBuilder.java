package dev.apexstudios.apexcore.common.registry.builder;

import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.generic.DeferredSpawnEggItem;
import dev.apexstudios.apexcore.common.registry.holder.DeferredEntityType;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class EntityTypeBuilder<O extends AbstractRegister<O>, P, T extends Entity> extends AbstractBuilder<O, P, EntityType<?>, EntityType<T>, DeferredEntityType<T>, EntityTypeBuilder<O, P, T>>
{
    private final MobCategory category;
    private final EntityType.EntityFactory<T> entityFactory;
    private Function<EntityType.Builder<T>, EntityType.Builder<T>> propertiesModifier = Function.identity();
    private OptionalLike<OptionalLike<EntityRendererProvider<T>>> renderer = OptionalLike.empty();
    private OptionalLike<AttributeSupplier.Builder> attributes = OptionalLike.empty();
    private OptionalLike<SpawnPlacements.Type> spawnPlacement = OptionalLike.empty();
    private OptionalLike<Heightmap.Types> heightmap = OptionalLike.empty();
    private OptionalLike<SpawnPlacements.SpawnPredicate<T>> spawnPredicate = OptionalLike.empty();

    @ApiStatus.Internal
    public EntityTypeBuilder(O owner, P parent, String entityName, BuilderHelper helper, MobCategory category, EntityType.EntityFactory<T> entityFactory)
    {
        super(owner, parent, Registries.ENTITY_TYPE, entityName, DeferredEntityType::createEntity, helper);

        this.category = category;
        this.entityFactory = entityFactory;
    }

    public EntityTypeBuilder<O, P, T> renderer(OptionalLike<OptionalLike<EntityRendererProvider<T>>> renderer)
    {
        this.renderer = renderer;
        return this;
    }

    public EntityTypeBuilder<O, P, T> attributes(OptionalLike<AttributeSupplier.Builder> attributes)
    {
        this.attributes = attributes;
        return this;
    }

    public EntityTypeBuilder<O, P, T> spawnPlacement(@Nullable SpawnPlacements.Type spawnPlacement)
    {
        this.spawnPlacement = OptionalLike.of(spawnPlacement);
        return this;
    }

    public EntityTypeBuilder<O, P, T> spawnHeightmap(@Nullable Heightmap.Types heightmap)
    {
        this.heightmap = OptionalLike.of(heightmap);
        return this;
    }

    public EntityTypeBuilder<O, P, T> spawnPredicate(SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        this.spawnPredicate = () -> spawnPredicate;
        return this;
    }

    public EntityTypeBuilder<O, P, T> properties(UnaryOperator<EntityType.Builder<T>> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public <I extends DeferredSpawnEggItem> ItemBuilder<O, EntityTypeBuilder<O, P, T>, I> spawnEgg(DeferredSpawnEggItem.Factory<I> itemFactory, int backgroundColor, int highlightColor)
    {
        return owner.item(this, registrationName(), properties -> itemFactory.create(holder(), backgroundColor, highlightColor, properties))
                    .color(() -> () -> (stack, tintIndex) -> ((DeferredSpawnEggItem) stack.getItem()).getColor(tintIndex))
                    .dispenseBehavior(DeferredSpawnEggItem.DEFAULT_DISPENSE_BEHAVIOR)
                    .creativeModeTab(CreativeModeTabs.SPAWN_EGGS)
                    .onRegisterAfter(Registries.ENTITY_TYPE, DeferredSpawnEggItem::injectSpawnEggEntry);
    }

    public ItemBuilder<O, EntityTypeBuilder<O, P, T>, DeferredSpawnEggItem> spawnEgg(int backgroundColor, int highlightColor)
    {
        return spawnEgg(DeferredSpawnEggItem::new, backgroundColor, highlightColor);
    }

    public EntityTypeBuilder<O, P, T> defaultSpawnEgg(DeferredSpawnEggItem.Factory<?> itemFactory, int backgroundColor, int highlightColor)
    {
        return spawnEgg(itemFactory, backgroundColor, highlightColor).build();
    }

    public EntityTypeBuilder<O, P, T> defaultSpawnEgg(int backgroundColor, int highlightColor)
    {
        return spawnEgg(backgroundColor, highlightColor).build();
    }

    @Override
    protected EntityType<T> createValue()
    {
        var builder = EntityType.Builder.of(entityFactory, category);
        builder = propertiesModifier.apply(builder);
        return builder.build(registryName().toString());
    }

    @Override
    protected void onRegister(EntityType<T> value)
    {
        if(spawnPlacement.isPresent() || heightmap.isPresent() || spawnPredicate.isPresent())
            RegistryHelper.registerEntitySpawnPlacement(value, spawnPlacement.getRaw(), heightmap.getRaw(), spawnPredicate.getRaw());

        owner.registerEntityAttributes(value, attributes);
        owner.registerEntityRenderer(value, renderer);
    }
}
