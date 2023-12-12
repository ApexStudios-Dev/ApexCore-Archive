package dev.apexstudios.apexcore.common.generator.common;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.generator.AbstractResourceGenerator;
import dev.apexstudios.apexcore.common.generator.ProviderType;
import dev.apexstudios.apexcore.common.generator.ResourceGenerator;
import dev.apexstudios.apexcore.common.loader.Platform;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class PackMetaGenerator extends AbstractResourceGenerator<PackMetaGenerator>
{
    public static final ProviderType<PackMetaGenerator> PROVIDER_TYPE = ProviderType.register(ApexCore.ID, "pack_metadata", PackMetaGenerator::new);

    private final Map<String, Supplier<JsonElement>> elements = Maps.newHashMap();
    private UnaryOperator<PackBuilder> defaults = UnaryOperator.identity();

    private PackMetaGenerator(String ownerId, PackOutput output)
    {
        super(ownerId, output);

        elements.put(PackMetadataSection.TYPE.getMetadataSectionName(), () -> defaults.apply(new PackBuilder()).toJson());
    }

    public <T> PackMetaGenerator with(MetadataSectionType<T> sectionType, T section)
    {
        elements.put(sectionType.getMetadataSectionName(), () -> sectionType.toJson(section));
        return this;
    }

    public PackMetaGenerator with(UnaryOperator<PackBuilder> modifier)
    {
        this.defaults = modifier;
        return this;
    }

    @Override
    protected void generate(CachedOutput cache, HolderLookup.Provider registries)
    {
        var json = new JsonObject();
        elements.forEach((sectionName, sectionJson) -> json.add(sectionName, sectionJson.get()));
        ResourceGenerator.save(cache, json, output.getOutputFolder().resolve("pack.mcmeta"));
    }

    public final class PackBuilder
    {
        @Nullable private Component description = null;
        @Nullable private InclusiveRange<Integer> supportedFormats = null;
        private final Object2IntMap<PackType> packVersions = new Object2IntOpenHashMap<>();

        public PackBuilder description(String description)
        {
            return description("pack.%s.description".formatted(ownerId), description);
        }

        public PackBuilder description(String key, String value)
        {
            return description(Component.translatable(key, value));
        }

        public PackBuilder description(Component description)
        {
            this.description = description;
            return this;
        }

        public PackBuilder supportedFormats(InclusiveRange<Integer> supportedFormats)
        {
            this.supportedFormats = supportedFormats;
            return this;
        }

        public PackBuilder supportedFormats(int min, int max)
        {
            return supportedFormats(new InclusiveRange<>(min, max));
        }

        public PackBuilder supportedFormats(int value)
        {
            return supportedFormats(value, value);
        }

        public PackBuilder packVersion(PackType packType, int version)
        {
            packVersions.put(packType, version);
            return this;
        }

        private JsonElement toJson()
        {
            var root = new JsonObject();

            var description = this.description == null ? Component.translatable("pack.%s.description".formatted(ownerId)) : this.description;
            root.add("description", ResourceGenerator.encodeOrThrow(ComponentSerialization.CODEC, description));

            if(supportedFormats != null)
                root.add("supported_formats", ResourceGenerator.encodeOrThrow(InclusiveRange.INT, supportedFormats));

            var clientVersion = packVersions.getOrDefault(PackType.CLIENT_RESOURCES, Platform.resourceVersion(PackType.CLIENT_RESOURCES));
            root.addProperty("pack_format", clientVersion);

            var versionsJson = new JsonObject();

            for(var packType : PackType.values())
            {
                var version = packVersions.getOrDefault(packType, Platform.resourceVersion(packType));
                versionsJson.addProperty(packType.name().toLowerCase(), version);
            }

            var neoData = new JsonObject();
            neoData.add("versions", versionsJson);

            root.add("neoforge", neoData);

            return root;
        }
    }
}
