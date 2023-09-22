package xyz.apex.minecraft.apexcore.mcforge.lib.hook;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.ParticleHooks;
import xyz.apex.minecraft.apexcore.mcforge.core.ModEvents;

import java.util.function.Function;

@ApiStatus.Internal
public final class ParticleHooksImpl implements ParticleHooks
{
    @Override
    public <T extends ParticleOptions> void registerSpecial(ParticleType<T> particleType, ParticleProvider<T> particleProviderFactory)
    {
        ModEvents.active().addListener(RegisterParticleProvidersEvent.class, event -> event.registerSpecial(particleType, particleProviderFactory));
    }

    @Override
    public <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleProvider.Sprite<T> particleProviderFactory)
    {
        ModEvents.active().addListener(RegisterParticleProvidersEvent.class, event -> event.registerSprite(particleType, particleProviderFactory));
    }

    @Override
    public <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> particleType, Function<SpriteSet, ParticleProvider<T>> particleProviderFactory)
    {
        ModEvents.active().addListener(RegisterParticleProvidersEvent.class, event -> event.registerSpriteSet(particleType, particleProviderFactory::apply));
    }
}
