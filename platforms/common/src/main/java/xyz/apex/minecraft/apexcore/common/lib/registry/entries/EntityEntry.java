package xyz.apex.minecraft.apexcore.common.lib.registry.entries;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.registry.DelegatedRegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.EntityBuilder;

/**
 * Main RegistryEntry class for all EntityType entries.
 * <p>
 * While the constructor is publicly visible, you should never invoke or create instance of this class yourself.
 * Instances of this class are provided when registered using the {@link EntityBuilder} class.
 *
 * @param <T> Type of entity type.
 */
public final class EntityEntry<T extends Entity> extends DelegatedRegistryEntry<EntityType<T>> implements FeatureElementEntry<EntityType<T>>, EntityTypeTest<Entity, T>
{
    /**
     * DO NOT MANUALLY CALL PUBLIC FOR INTERNAL USAGES ONLY
     */
    @ApiStatus.Internal
    public EntityEntry(RegistryEntry<EntityType<T>> delegate)
    {
        super(delegate);
    }

    @Nullable
    @Override
    public T tryCast(Entity entity)
    {
        return map(value -> value.tryCast(entity)).orElse(null);
    }

    @Override
    public Class<? extends Entity> getBaseClass()
    {
        // this didn't like being in a map() call
        var value = getNullable();
        return value == null ? Entity.class : value.getBaseClass();
    }
}
