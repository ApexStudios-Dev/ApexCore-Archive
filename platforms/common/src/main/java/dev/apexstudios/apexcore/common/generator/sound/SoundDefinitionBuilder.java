package dev.apexstudios.apexcore.common.generator.sound;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.covers1624.quack.util.SneakyUtils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.IntStream;

public final class SoundDefinitionBuilder
{
    private static final boolean DEFAULT_REPLACE = false;

    private boolean replace = DEFAULT_REPLACE;
    @Nullable private String subtitle = null;
    private final List<SoundBuilder> sounds = Lists.newArrayList();

    SoundDefinitionBuilder()
    {
    }

    public SoundDefinitionBuilder replace(boolean replace)
    {
        this.replace = replace;
        return this;
    }

    public SoundDefinitionBuilder replace()
    {
        return replace(true);
    }

    public SoundDefinitionBuilder subtitle(@Nullable String subtitle)
    {
        this.subtitle = subtitle;
        return this;
    }

    public SoundDefinitionBuilder sound(ResourceLocation soundPath, Consumer<SoundBuilder> modifier)
    {
        var builder = new SoundBuilder(soundPath);
        modifier.accept(builder);
        sounds.add(builder);
        return this;
    }

    public SoundDefinitionBuilder sound(ResourceLocation soundPath)
    {
        return sound(soundPath, SneakyUtils.nullCons());
    }

    public SoundDefinitionBuilder sound(String ownerId, String soundPath, Consumer<SoundBuilder> modifier)
    {
        return sound(new ResourceLocation(ownerId, soundPath), modifier);
    }

    public SoundDefinitionBuilder sound(String ownerId, String soundPath)
    {
        return sound(ownerId, soundPath, SneakyUtils.nullCons());
    }

    public SoundDefinitionBuilder sound(String soundPath, Consumer<SoundBuilder> modifier)
    {
        return sound(new ResourceLocation(soundPath), modifier);
    }

    public SoundDefinitionBuilder sound(String soundPath)
    {
        return sound(soundPath, SneakyUtils.nullCons());
    }

    public SoundDefinitionBuilder sound(SoundEvent soundEvent, Consumer<SoundBuilder> modifier)
    {
        return sound(soundEvent.getLocation(), modifier);
    }

    public SoundDefinitionBuilder sound(SoundEvent soundEvent)
    {
        return sound(soundEvent, SneakyUtils.nullCons());
    }

    public SoundDefinitionBuilder sound(ResourceKey<SoundEvent> soundEvent, Consumer<SoundBuilder> modifier)
    {
        return sound(soundEvent.location(), modifier);
    }

    public SoundDefinitionBuilder sound(ResourceKey<SoundEvent> soundEvent)
    {
        return sound(soundEvent, SneakyUtils.nullCons());
    }

    public SoundDefinitionBuilder sound(Holder<SoundEvent> soundEvent, Consumer<SoundBuilder> modifier)
    {
        return sound(soundEvent.unwrapKey().orElseThrow(), modifier);
    }

    public SoundDefinitionBuilder sound(Holder<SoundEvent> soundEvent)
    {
        return sound(soundEvent, SneakyUtils.nullCons());
    }

    public SoundDefinitionBuilder sounds(String soundPath, int count, ObjIntConsumer<SoundBuilder> modifier)
    {
        IntStream.range(0, count).forEach(i -> sound(soundPath + i, builder -> modifier.accept(builder, i)));
        return this;
    }

    public SoundDefinitionBuilder sounds(String soundPath, int count, Consumer<SoundBuilder> modifier)
    {
        return sounds(soundPath, count, (builder, index) -> modifier.accept(builder));
    }

    public SoundDefinitionBuilder sounds(String soundPath, int count)
    {
        return sounds(soundPath, count, SneakyUtils.nullCons());
    }

    JsonElement toJson()
    {
        Validate.notEmpty(sounds, "SoundDefinitions must have at least 1 sound entry");

        var root = new JsonObject();

        root.add("sounds", sounds
                .stream()
                .map(SoundBuilder::toJson)
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll)
        );

        if(replace != DEFAULT_REPLACE)
            root.addProperty("replace", replace);
        if(subtitle != null)
            root.addProperty("subtitle", subtitle);

        return root;
    }

    public static final class SoundBuilder
    {
        private static final float DEFAULT_VOLUME = 1F;
        private static final float DEFAULT_PITCH = 1F;
        private static final int DEFAULT_WEIGHT = 1;
        private static final Type DEFAULT_TYPE = Type.FILE;
        private static final boolean DEFAULT_STREAM = false;
        private static final boolean DEFAULT_PRELOAD = false;
        private static final int DEFAULT_ATTENUATION_DISTANCE = 16;

        private float volume = DEFAULT_VOLUME;
        private float pitch = DEFAULT_PITCH;
        private int weight = DEFAULT_WEIGHT;
        private Type type = DEFAULT_TYPE;
        private boolean stream = DEFAULT_STREAM;
        private boolean preload = DEFAULT_PRELOAD;
        private int attenuationDistance = DEFAULT_ATTENUATION_DISTANCE;

        private final ResourceLocation soundPath;

        private SoundBuilder(ResourceLocation soundPath)
        {
            this.soundPath = soundPath;
        }

        public SoundBuilder volume(float volume)
        {
            Validate.inclusiveBetween(0F, 1F, volume, "Volume must be between 0-1 inclusive: %f".formatted(volume));
            this.volume = volume;
            return this;
        }

        public SoundBuilder pitch(float pitch)
        {
            Validate.inclusiveBetween(0F, 1F, pitch, "Pitch must be between 0-1 inclusive: %f".formatted(pitch));
            this.pitch = pitch;
            return this;
        }

        public SoundBuilder weight(int weight)
        {
            Validate.isTrue(weight >= 0, "Weight must to be a positive Integer: %d".formatted(weight));
            this.weight = weight;
            return this;
        }

        public SoundBuilder type(Type type)
        {
            this.type = type;
            return this;
        }

        public SoundBuilder stream(boolean stream)
        {
            this.stream = stream;
            return this;
        }

        public SoundBuilder stream()
        {
            return stream(true);
        }

        public SoundBuilder preload(boolean preload)
        {
            this.preload = preload;
            return this;
        }

        public SoundBuilder preload()
        {
            return preload(true);
        }

        public SoundBuilder attenuationDistance(int attenuationDistance)
        {
            Validate.isTrue(attenuationDistance >= 0, "Attenuation Distance must to be a positive Integer: %d".formatted(attenuationDistance));
            this.attenuationDistance = attenuationDistance;
            return this;
        }

        private JsonElement toJson()
        {
            var soundPath = this.soundPath.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE) ? this.soundPath.getPath() : this.soundPath.toString();

            var root = new JsonObject();
            root.addProperty("name", soundPath);

            if(volume != DEFAULT_VOLUME)
                root.addProperty("volume", volume);
            if(pitch != DEFAULT_PITCH)
                root.addProperty("pitch", pitch);
            if(weight != DEFAULT_WEIGHT)
                root.addProperty("weight", weight);
            if(type != DEFAULT_TYPE)
                root.addProperty("type", type.serializedName);
            if(stream != DEFAULT_STREAM)
                root.addProperty("stream", stream);
            if(preload != DEFAULT_PRELOAD)
                root.addProperty("preload", preload);
            if(attenuationDistance != DEFAULT_ATTENUATION_DISTANCE)
                root.addProperty("attenuation_distance", attenuationDistance);

            if(root.size() != 1)
                return root;

            return new JsonPrimitive(soundPath);
        }
    }

    public enum Type
    {
        FILE("file"),
        EVENT("event");

        private final String serializedName;

        Type(String serializedName)
        {
            this.serializedName = serializedName;
        }
    }
}
