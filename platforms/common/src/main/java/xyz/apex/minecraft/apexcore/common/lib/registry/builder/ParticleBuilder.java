package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.ParticleHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryProviderListener;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.ParticleEntry;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderTypes;
import xyz.apex.minecraft.apexcore.common.lib.resgen.particle.ParticleDefinitionProvider;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Particle Builder implementation.
 * <p>
 * Used to build and register Particle entries.
 *
 * @param <O> Type of Registrar.
 * @param <T> Type of Particle [Entry].
 * @param <P> Type of Parent.
 */
public final class ParticleBuilder<O extends AbstractRegistrar<O>, S extends ParticleOptions, T extends ParticleType<S>, P> extends AbstractBuilder<O, P, ParticleType<?>, T, ParticleBuilder<O, S, T, P>, ParticleEntry<T>>
{
    private final Supplier<T> particleFactory;
    @Nullable private Supplier<Supplier<ParticleProvider<S>>> particleProviderFactory;
    @Nullable private Supplier<Supplier<ParticleProvider.Sprite<S>>> spriteParticleProviderFactory;
    @Nullable private Supplier<Supplier<Function<SpriteSet, ParticleProvider<S>>>> spriteSetParticleProviderFactory;

    @ApiStatus.Internal
    public ParticleBuilder(O registrar, P parent, String registrationName, Supplier<T> particleFactory)
    {
        super(registrar, parent, Registries.PARTICLE_TYPE, registrationName);

        this.particleFactory = particleFactory;
    }

    public ParticleBuilder<O, S, T, P> specialFactory(Supplier<Supplier<ParticleProvider<S>>> particleProviderFactory)
    {
        Validate.isTrue(spriteParticleProviderFactory == null && spriteSetParticleProviderFactory == null, "Only one particle factory type is supported!");
        this.particleProviderFactory = particleProviderFactory;
        return this;
    }

    public ParticleBuilder<O, S, T, P> spriteFactory(Supplier<Supplier<ParticleProvider.Sprite<S>>> spriteParticleProviderFactory)
    {
        Validate.isTrue(particleProviderFactory == null && spriteSetParticleProviderFactory == null, "Only one particle factory type is supported!");
        this.spriteParticleProviderFactory = spriteParticleProviderFactory;
        return this;
    }

    public ParticleBuilder<O, S, T, P> spriteSetFactory(Supplier<Supplier<Function<SpriteSet, ParticleProvider<S>>>> spriteSetParticleProviderFactory)
    {
        Validate.isTrue(particleProviderFactory == null && spriteParticleProviderFactory == null, "Only one particle factory type is supported!");
        this.spriteSetParticleProviderFactory = spriteSetParticleProviderFactory;
        return this;
    }

    /**
     * Set the ParticleDefinition generator for this Particle.
     *
     * @param listener Generator listener.
     * @return This Builder.
     */
    public ParticleBuilder<O, S, T, P> definition(RegistryProviderListener<ParticleDefinitionProvider, T, ParticleEntry<T>> listener)
    {
        return setProvider(ProviderTypes.PARTICLES, listener);
    }

    /**
     * Clears the currently registered ParticleDefinition generator.
     *
     * @return This Builder.
     */
    public ParticleBuilder<O, S, T, P> clearParticleProvider()
    {
        return clearProvider(ProviderTypes.PARTICLES);
    }

    @Override
    protected void onRegister(T entry)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> {
            if(particleProviderFactory != null)
                ParticleHooks.get().registerSpecial(entry, particleProviderFactory.get().get());
            if(spriteParticleProviderFactory != null)
                ParticleHooks.get().registerSprite(entry, spriteParticleProviderFactory.get().get());
            if(spriteSetParticleProviderFactory != null)
                ParticleHooks.get().registerSpriteSet(entry, spriteSetParticleProviderFactory.get().get());
        });
    }

    @Override
    protected ParticleEntry<T> createRegistryEntry()
    {
        return new ParticleEntry<>(registrar, registryKey);
    }

    @Override
    protected T createEntry()
    {
        return particleFactory.get();
    }

    @Override
    protected String getDescriptionId(ParticleEntry<T> entry)
    {
        return registryName().toLanguageKey("particle");
    }
}
