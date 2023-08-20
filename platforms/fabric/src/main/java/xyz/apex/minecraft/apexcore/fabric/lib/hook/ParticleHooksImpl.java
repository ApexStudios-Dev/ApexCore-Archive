package xyz.apex.minecraft.apexcore.fabric.lib.hook;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.hook.ParticleHooks;

import java.util.function.Function;

@ApiStatus.Internal
@SideOnly(PhysicalSide.CLIENT)
public final class ParticleHooksImpl implements ParticleHooks
{
    @Override
    public <T extends ParticleOptions> void registerSpecial(ParticleType<T> particleType, ParticleProvider<T> particleProviderFactory)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ParticleFactoryRegistry.getInstance().register(particleType, particleProviderFactory);
    }

    @Override
    public <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleProvider.Sprite<T> particleProviderFactory)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));

        // registerSpecial(particleType, particleProviderFactory::createParticle);
        // ParticleFactoryRegistry.getInstance().register(particleType, particleProviderFactory::createParticle);

        registerSpriteSet(particleType, provider -> particleProviderFactory::createParticle);
        // ParticleFactoryRegistry.getInstance().register(particleType, provider -> particleProviderFactory::createParticle);
    }

    @Override
    public <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> particleType, Function<SpriteSet, ParticleProvider<T>> particleProviderFactory)
    {
        Validate.isTrue(PhysicalSide.isRunningOn(PhysicalSide.CLIENT));
        ParticleFactoryRegistry.getInstance().register(particleType, particleProviderFactory::apply);
    }
}
