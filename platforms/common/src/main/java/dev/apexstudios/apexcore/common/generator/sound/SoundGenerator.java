package dev.apexstudios.apexcore.common.generator.sound;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.generator.AbstractResourceGenerator;
import dev.apexstudios.apexcore.common.generator.ProviderType;
import dev.apexstudios.apexcore.common.generator.ResourceGenerator;
import net.covers1624.quack.util.SneakyUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.function.Consumer;

public final class SoundGenerator extends AbstractResourceGenerator<SoundGenerator>
{
    public static final ProviderType<SoundGenerator> PROVIDER_TYPE = ProviderType.register(ApexCore.ID, "sounds", SoundGenerator::new);

    private final Map<String, SoundDefinitionBuilder> sounds = Maps.newHashMap();

    private SoundGenerator(String ownerId, PackOutput output)
    {
        super(ownerId, output);
    }

    @Override
    protected void generate(CachedOutput cache, HolderLookup.Provider registries)
    {
        if(sounds.isEmpty())
            return;

        var json = new JsonObject();
        sounds.forEach((soundName, builder) -> json.add(soundName, builder.toJson()));
        var path = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(ownerId).resolve("sounds.json");
        ResourceGenerator.save(cache, json, path);
    }

    public SoundGenerator sound(String soundName, Consumer<SoundDefinitionBuilder> modifier)
    {
        modifier.accept(sounds.computeIfAbsent(soundName, $ -> new SoundDefinitionBuilder()));
        return this;
    }

    public SoundGenerator sound(String soundName)
    {
        return sound(soundName, SneakyUtils.nullCons());
    }

    private SoundGenerator sound(ResourceLocation soundName, Consumer<SoundDefinitionBuilder> modifier)
    {
        // when sounds are loaded they are all given the same namespace as the pack they are contained within
        Validate.isTrue(soundName.getNamespace().equals(ownerId), "Can only generate sounds for your own namespace: %s".formatted(ownerId));
        return sound(soundName.getPath(), modifier);
    }

    public SoundGenerator sound(SoundEvent soundEvent, Consumer<SoundDefinitionBuilder> modifier)
    {
        return sound(soundEvent.getLocation(), modifier);
    }

    public SoundGenerator sound(SoundEvent soundEvent)
    {
        return sound(soundEvent, SneakyUtils.nullCons());
    }

    public SoundGenerator sound(ResourceKey<SoundEvent> soundEvent, Consumer<SoundDefinitionBuilder> modifier)
    {
        return sound(soundEvent.location(), modifier);
    }

    public SoundGenerator sound(ResourceKey<SoundEvent> soundEvent)
    {
        return sound(soundEvent, SneakyUtils.nullCons());
    }

    public SoundGenerator sound(Holder<SoundEvent> soundEvent, Consumer<SoundDefinitionBuilder> modifier)
    {
        return sound(soundEvent.unwrapKey().orElseThrow(), modifier);
    }

    public SoundGenerator sound(Holder<SoundEvent> soundEvent)
    {
        return sound(soundEvent, SneakyUtils.nullCons());
    }
}
