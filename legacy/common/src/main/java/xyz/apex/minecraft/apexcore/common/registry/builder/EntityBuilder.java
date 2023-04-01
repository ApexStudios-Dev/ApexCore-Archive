package xyz.apex.minecraft.apexcore.common.registry.builder;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.common.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.registry.entry.EntityEntry;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unchecked")
public final class EntityBuilder<R extends Entity, O extends AbstractRegistrar<O>, P> extends AbstractBuilder<EntityType<?>, EntityType<R>, O, P, EntityBuilder<R, O, P>>
{
    private final Factory<R> factory;
    private Function<EntityType.Builder<R>, EntityType.Builder<R>> propertiesModifier = Function.identity();
    private MobCategory category = MobCategory.MISC;
    private Supplier<Supplier<EntityRendererProvider<R>>> rendererSupplier = () -> () -> null;
    @Nullable private Supplier<AttributeSupplier.Builder> attributesSupplier;
    @Nullable private ItemBuilder<ArchitecturySpawnEggItem, O, EntityBuilder<R, O, P>> spawnEggBuilder;

    public EntityBuilder(O owner, P parent, String registrationName, Factory<R> factory)
    {
        super(owner, parent, Registries.ENTITY_TYPE, registrationName);

        this.factory = factory;

        onRegister(entityType -> {
            if(attributesSupplier != null) EntityAttributeRegistry.register(() -> (EntityType<? extends LivingEntity>) entityType, attributesSupplier);

            EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
                var renderer = rendererSupplier.get().get();
                if(renderer != null) EntityRendererRegistry.register(() -> entityType, renderer);
            });
        });
    }

    public ItemBuilder<ArchitecturySpawnEggItem, O, EntityBuilder<R, O, P>> spawnEgg(int primaryColor, int secondaryColor)
    {
        this.spawnEggBuilder = new ItemBuilder<>(owner, this, getRegistrationName(), properties -> new ArchitecturySpawnEggItem(owner.getDelegate(Registries.ENTITY_TYPE, getRegistrationName()), primaryColor, secondaryColor, properties));
        return spawnEggBuilder;
    }

    public EntityBuilder<R, O, P> simpleSpawnEgg(int primaryColor, int secondaryColor)
    {
        return spawnEgg(primaryColor, secondaryColor).build();
    }

    public EntityBuilder<R, O, P> noSpawnEgg()
    {
        spawnEggBuilder = null;
        return this;
    }

    public EntityBuilder<R, O, P> category(MobCategory category)
    {
        this.category = category;
        return this;
    }

    public EntityBuilder<R, O, P> renderer(Supplier<Supplier<EntityRendererProvider<R>>> rendererSupplier)
    {
        this.rendererSupplier = rendererSupplier;
        return this;
    }

    public EntityBuilder<R, O, P> attributes(Supplier<AttributeSupplier.Builder> attributesSupplier)
    {
        this.attributesSupplier = attributesSupplier;
        return this;
    }

    public EntityBuilder<R, O, P> properties(UnaryOperator<EntityType.Builder<R>> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    public EntityBuilder<R, O, P> sized(float width, float height)
    {
        return properties(properties -> properties.sized(width, height));
    }

    public EntityBuilder<R, O, P> noSummon()
    {
        return properties(EntityType.Builder::noSummon);
    }

    public EntityBuilder<R, O, P> noSave()
    {
        return properties(EntityType.Builder::noSave);
    }

    public EntityBuilder<R, O, P> fireImmune()
    {
        return properties(EntityType.Builder::fireImmune);
    }

    public EntityBuilder<R, O, P> immuneTo(Block... blocks)
    {
        return properties(properties -> properties.immuneTo(blocks));
    }

    public EntityBuilder<R, O, P> canSpawnFarFromPlayer()
    {
        return properties(EntityType.Builder::canSpawnFarFromPlayer);
    }

    public EntityBuilder<R, O, P> clientTrackingRange(int clientTrackingRange)
    {
        return properties(properties -> properties.clientTrackingRange(clientTrackingRange));
    }

    public EntityBuilder<R, O, P> updateInterval(int updateInterval)
    {
        return properties(properties -> properties.updateInterval(updateInterval));
    }

    public EntityBuilder<R, O, P> requiredFeatures(FeatureFlag... flags)
    {
        return properties(properties -> properties.requiredFeatures(flags));
    }

    @Override
    protected EntityType<R> createEntry()
    {
        return propertiesModifier.apply(EntityType.Builder.of(factory::create, category)).build(getRegistryName().toString());
    }

    @Override
    protected EntityEntry<R> createRegistryEntry(RegistrySupplier<EntityType<R>> delegate)
    {
        return new EntityEntry<>(owner, delegate, registryKey);
    }

    @Override
    public EntityEntry<R> register()
    {
        var result = (EntityEntry<R>) super.register();
        if(spawnEggBuilder != null) spawnEggBuilder.register();
        return result;
    }

    @FunctionalInterface
    public interface Factory<T extends Entity>
    {
        T create(EntityType<T> entityType, Level level);
    }
}
