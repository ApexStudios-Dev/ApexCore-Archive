package xyz.apex.forge.utility.registrator.factory;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

@FunctionalInterface
public interface ParticleFactory<PARTICLE extends ParticleOptions>
{
	ParticleType<PARTICLE> create();
}
