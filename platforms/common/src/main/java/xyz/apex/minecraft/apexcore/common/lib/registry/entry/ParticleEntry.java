package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

public final class ParticleEntry<T extends ParticleType<?>> extends BaseRegistryEntry<T>
{
    @ApiStatus.Internal
    public ParticleEntry(AbstractRegistrar<?> registrar, ResourceKey<T> registryKey)
    {
        super(registrar, registryKey);
    }

    public static <T extends ParticleType<?>> ParticleEntry<T> cast(RegistryEntry<?> registryEntry)
    {
        return RegistryEntry.cast(ParticleEntry.class, registryEntry);
    }
}
