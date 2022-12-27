package xyz.apex.minecraft.apexcore.shared.registry.builders;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.EnvironmentExecutor;
import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.registry.entry.EntityEntry;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EntityBuilder<T extends Entity> extends AbstractBuilder<EntityType<?>, EntityType<T>, EntityBuilder<T>, EntityEntry<T>>
{
    private final EntityFactory<T> entityFactory;
    private MobCategory mobCategory = MobCategory.MISC;
    private Function<EntityType.Builder<T>, EntityType.Builder<T>> propertiesModifier = Function.identity();
    @Nullable private Supplier<Function<EntityRendererProvider.Context, EntityRenderer<T>>> renderer = null;
    @Nullable private Supplier<AttributeSupplier.Builder> attributes = null;

    EntityBuilder(String modId, String registryName, EntityFactory<T> entityFactory)
    {
        super(Registries.ENTITY_TYPE, modId, registryName, EntityEntry::new);

        this.entityFactory = entityFactory;
    }

    @Override
    protected void onRegister(EntityType<T> value)
    {
        super.onRegister(value);

        var modId = getModId();
        var entityType = asSupplier();

        if(renderer != null) EnvironmentExecutor.runForClient(() -> () -> Platform.INSTANCE.registries().registerEntityRenderer(modId, entityType, renderer));
        if(attributes != null) Platform.INSTANCE.registries().registerEntityAttributes(modId, entityType, attributes);
    }

    public EntityBuilder<T> spawnEgg(int primaryColor, int secondaryColor, UnaryOperator<ItemBuilder<SpawnEggItem>> spawnEgg)
    {
        return child(Registries.ITEM, (modId, registryName) -> spawnEgg.apply(ItemBuilders.builder(modId, registryName, properties -> Platform.INSTANCE.registries().createSpawnEggItem(() -> asSupplier().get(), primaryColor, secondaryColor, properties))));
    }

    public EntityBuilder<T> spawnEgg(int primaryColor, int secondaryColor)
    {
        return spawnEgg(primaryColor, secondaryColor, UnaryOperator.identity());
    }

    public EntityBuilder<T> noSpawnEgg()
    {
        return removeChild(Registries.ITEM);
    }

    public EntityBuilder<T> renderer(Supplier<Function<EntityRendererProvider.Context, EntityRenderer<T>>> renderer)
    {
        this.renderer = renderer;
        return this;
    }

    public EntityBuilder<T> attributes(Supplier<AttributeSupplier.Builder> attributes)
    {
        this.attributes = attributes;
        return this;
    }

    public EntityBuilder<T> category(MobCategory mobCategory)
    {
        this.mobCategory = mobCategory;
        return this;
    }

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

    public EntityBuilder<T> immuneTo(Block... blocks)
    {
        return properties(properties -> properties.immuneTo(blocks));
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

    public EntityBuilder<T> requiredFeatures(FeatureFlag... flags)
    {
        return properties(properties -> properties.requiredFeatures(flags));
    }

    @Override
    protected EntityType<T> construct()
    {
        return propertiesModifier.apply(EntityType.Builder.of(entityFactory, mobCategory)).build(getRegistryName());
    }

    @FunctionalInterface public interface EntityFactory<T extends Entity> extends EntityType.EntityFactory<T> {}
}
