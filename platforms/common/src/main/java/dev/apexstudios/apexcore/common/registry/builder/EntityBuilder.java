package dev.apexstudios.apexcore.common.registry.builder;

import dev.apexstudios.apexcore.common.loader.RegistryHelper;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.generic.DeferredSpawnEggItem;
import dev.apexstudios.apexcore.common.registry.holder.DeferredEntity;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class EntityBuilder<O extends AbstractRegister<O>, P, T extends Entity> extends AbstractBuilder<O, P, EntityType<?>, EntityType<T>, DeferredEntity<T>, EntityBuilder<O, P, T>>
{
    private final MobCategory category;
    private final EntityType.EntityFactory<T> entityFactory;
    private Function<EntityType.Builder<T>, EntityType.Builder<T>> propertiesModifier = Function.identity();
    private OptionalLike<OptionalLike<EntityRendererProvider<T>>> renderer = () -> null;
    private OptionalLike<AttributeSupplier.Builder> attributes = () -> null;
    private OptionalLike<SpawnPlacements.Type> spawnPlacement = () -> null;
    private OptionalLike<Heightmap.Types> heightmap = () -> null;
    private OptionalLike<SpawnPlacements.SpawnPredicate<T>> spawnPredicate = () -> null;

    @ApiStatus.Internal
    public EntityBuilder(O owner, P parent, String valueName, BuilderHelper helper, MobCategory category, EntityType.EntityFactory<T> entityFactory)
    {
        super(owner, parent, Registries.ENTITY_TYPE, valueName, DeferredEntity::createEntity, helper);

        this.category = category;
        this.entityFactory = entityFactory;
    }

    public EntityBuilder<O, P, T> renderer(OptionalLike<OptionalLike<EntityRendererProvider<T>>> renderer)
    {
        this.renderer = renderer;
        return this;
    }

    public EntityBuilder<O, P, T> attributes(OptionalLike<AttributeSupplier.Builder> attributes)
    {
        this.attributes = attributes;
        return this;
    }

    public EntityBuilder<O, P, T> spawnPlacement(@Nullable SpawnPlacements.Type spawnPlacement)
    {
        this.spawnPlacement = () -> spawnPlacement;
        return this;
    }

    public EntityBuilder<O, P, T> spawnHeightmap(@Nullable Heightmap.Types heightmap)
    {
        this.heightmap = () -> heightmap;
        return this;
    }

    public EntityBuilder<O, P, T> spawnPredicate(SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        this.spawnPredicate = () -> spawnPredicate;
        return this;
    }

    public EntityBuilder<O, P, T> properties(UnaryOperator<EntityType.Builder<T>> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public <I extends DeferredSpawnEggItem> ItemBuilder<O, EntityBuilder<O, P, T>, I> spawnEgg(DeferredSpawnEggItem.Factory<I> itemFactory, int backgroundColor, int highlightColor)
    {
        return owner.item(this, getValueName().getPath(), properties -> itemFactory.create(asHolder(), backgroundColor, highlightColor, properties))
                    .color(() -> () -> (stack, tintIndex) -> ((DeferredSpawnEggItem) stack.getItem()).getColor(tintIndex))
                    .dispenseBehavior(DeferredSpawnEggItem.DEFAULT_DISPENSE_BEHAVIOR)
                    .onRegisterAfter(Registries.ENTITY_TYPE, DeferredSpawnEggItem::injectSpawnEggEntry);
    }

    public ItemBuilder<O, EntityBuilder<O, P, T>, DeferredSpawnEggItem> spawnEgg(int backgroundColor, int highlightColor)
    {
        return spawnEgg(DeferredSpawnEggItem::new, backgroundColor, highlightColor);
    }

    public EntityBuilder<O, P, T> defaultSpawnEgg(DeferredSpawnEggItem.Factory<?> itemFactory, int backgroundColor, int highlightColor)
    {
        return spawnEgg(itemFactory, backgroundColor, highlightColor).build();
    }

    public EntityBuilder<O, P, T> defaultSpawnEgg(int backgroundColor, int highlightColor)
    {
        return spawnEgg(backgroundColor, highlightColor).build();
    }

    @Override
    protected EntityType<T> createValue()
    {
        var builder = EntityType.Builder.of(entityFactory, category);
        builder = propertiesModifier.apply(builder);
        return builder.build(getValueName().toString());
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
