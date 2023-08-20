package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreClient;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

import java.util.function.Function;

/**
 * Hooks for particle registrations.
 */
@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.NonExtendable
public interface ParticleHooks
{
    /**
     * <p>Registers a ParticleProvider for a non-json-based ParticleType.
     * These particles do not receive a list of texture sprites to use for rendering themselves.</p>
     *
     * <p>There must be <strong>no</strong> particle json with an ID matching the ParticleType,
     * or a redundant texture list error will occur when particle jsons load.</p>
     *
     * <p><a href="https://github.com/neoforged/NeoForge/blob/eb25ba6224acf22c58869e852f77578817f42b82/src/main/java/net/minecraftforge/client/event/RegisterParticleProvidersEvent.java#L42-L52">Docs provided by NeoForge.</a></p>
     *
     * @param <T> ParticleOptions used by the ParticleType and ParticleProvider.
     * @param particleType ParticleType to register a ParticleProvider for.
     * @param particleProviderFactory ParticleProvider function responsible for providing that ParticleType's particles.
     */
    <T extends ParticleOptions> void registerSpecial(ParticleType<T> particleType, ParticleProvider<T> particleProviderFactory);

    /**
     * <p>Registers a ParticleProvider for a json-based ParticleType with a single texture;
     * the resulting {@link TextureSheetParticle}s will use that texture when created.</p>
     *
     * <p>A particle json with an ID matching the ParticleType <strong>must exist</strong> in the <code>particles</code> asset folder,
     * or a missing texture list error will occur when particle jsons load.</p>
     *
     * <p><a href="https://github.com/neoforged/NeoForge/blob/eb25ba6224acf22c58869e852f77578817f42b82/src/main/java/net/minecraftforge/client/event/RegisterParticleProvidersEvent.java#L58-L68">Docs provided by NeoForge.</a></p>
     *
     * @param <T> ParticleOptions used by the ParticleType and Sprite function.
     * @param particleType ParticleType to register a ParticleProvider for.
     * @param particleProviderFactory Sprite function responsible for providing that ParticleType's particles.
     */
    <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleProvider.Sprite<T> particleProviderFactory);

    /**
     * <p>Registers a ParticleProvider for a json-based ParticleType.
     * Particle jsons define a list of texture sprites which the particle can use to render itself.</p>
     *
     * <p>A particle json with an ID matching the ParticleType <strong>must exist</strong> in the <code>particles</code> asset folder,
     * or a missing texture list error will occur when particle jsons load.</p>
     *
     * <p><a href="https://github.com/neoforged/NeoForge/blob/eb25ba6224acf22c58869e852f77578817f42b82/src/main/java/net/minecraftforge/client/event/RegisterParticleProvidersEvent.java#L75-L85">Docs provided by NeoForge.</a></p>
     *
     * @param <T> ParticleOptions used by the ParticleType and SpriteParticleRegistration function.
     * @param particleType ParticleType to register a particle provider for.
     * @param particleProviderFactory SpriteParticleRegistration function responsible for providing that ParticleType's particles.
     */
    <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> particleType, Function<SpriteSet, ParticleProvider<T>> particleProviderFactory);

    /**
     * @return Global instance.
     */
    static ParticleHooks get()
    {
        return ApexCoreClient.PARTICLE_HOOKS;
    }
}
