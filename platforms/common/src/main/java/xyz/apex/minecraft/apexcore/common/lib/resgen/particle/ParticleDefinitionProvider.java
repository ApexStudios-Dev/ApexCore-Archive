package xyz.apex.minecraft.apexcore.common.lib.resgen.particle;

import com.google.common.collect.Maps;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class ParticleDefinitionProvider implements DataProvider
{
    public static final ProviderType<ParticleDefinitionProvider> PROVIDER_TYPE = ProviderType.register(new ResourceLocation(ApexCore.ID, "particles"), ParticleDefinitionProvider::new);

    private final ProviderType.ProviderContext context;
    private final Map<ResourceLocation, ParticleDefinition> particleDefinitionMap = Maps.newHashMap();

    private ParticleDefinitionProvider(ProviderType.ProviderContext context)
    {
        this.context = context;
    }

    public ParticleDefinition particle(ParticleType<?> particleType)
    {
        var particleName = Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey(particleType));
        return particleDefinitionMap.computeIfAbsent(particleName, $ -> new ParticleDefinition());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        var pathProvider = context.packOutput().createPathProvider(PackOutput.Target.RESOURCE_PACK, "particles");

        return CompletableFuture.allOf(particleDefinitionMap
                .entrySet()
                .stream()
                .map(entry -> DataProvider.saveStable(output, entry.getValue().toJson(), pathProvider.json(entry.getKey())))
                .toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public String getName()
    {
        return "Particles";
    }
}
