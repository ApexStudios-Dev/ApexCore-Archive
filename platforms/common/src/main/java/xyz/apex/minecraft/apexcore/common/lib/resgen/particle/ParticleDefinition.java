package xyz.apex.minecraft.apexcore.common.lib.resgen.particle;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public final class ParticleDefinition
{
    private final Set<ResourceLocation> textures = Sets.newHashSet();

    public ResourceLocation getKey(ParticleType<?> particleType)
    {
        return Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey(particleType));
    }

    public ParticleDefinition texture(ParticleType<?>... particleTypes)
    {
        Stream.of(particleTypes).map(this::getKey).forEach(this.textures::add);
        return this;
    }

    public ParticleDefinition texture(ResourceLocation... textures)
    {
        Collections.addAll(this.textures, textures);
        return this;
    }

    @SafeVarargs
    public final ParticleDefinition texture(ResourceKey<ParticleType<?>>... textures)
    {
        Stream.of(textures).map(ResourceKey::location).forEach(this.textures::add);
        return this;
    }

    public ParticleDefinition texture(String... textures)
    {
        Stream.of(textures).map(ResourceLocation::new).forEach(this.textures::add);
        return this;
    }

    public ParticleDefinition textures(Iterable<ResourceLocation> textures)
    {
        textures.forEach(this.textures::add);
        return this;
    }

    public Set<ResourceLocation> textures()
    {
        return Set.copyOf(textures);
    }

    @ApiStatus.Internal
    JsonElement toJson()
    {
        var texturesJson = new JsonArray();
        textures.stream().map(ResourceLocation::toString).forEach(texturesJson::add);

        var root = new JsonObject();
        root.add("textures", texturesJson);
        return root;
    }
}
