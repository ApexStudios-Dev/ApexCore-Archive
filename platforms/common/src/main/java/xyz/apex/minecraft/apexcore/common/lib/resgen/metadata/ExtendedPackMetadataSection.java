package xyz.apex.minecraft.apexcore.common.lib.resgen.metadata;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.DetectedVersion;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.GsonHelper;

import java.util.Locale;
import java.util.stream.Stream;

// Mostly used for resource generation purposes
public final class ExtendedPackMetadataSection extends PackMetadataSection
{
    public static final MetadataSectionType<ExtendedPackMetadataSection> SECTION_TYPE = new Serializer();

    private final Object2IntMap<PackType> packTypeFormats;

    public ExtendedPackMetadataSection(Component description, int packFormat, Object2IntMap<PackType> packTypeFormats)
    {
        super(description, packFormat);

        this.packTypeFormats = Object2IntMaps.unmodifiable(packTypeFormats);
    }

    public int getPackFormat(PackType packType)
    {
        return packTypeFormats.getOrDefault(packType, getPackFormat());
    }

    public static ExtendedPackMetadataSection detected(Component description)
    {
        var packFormatTypes = new Object2IntOpenHashMap<PackType>();
        Stream.of(PackType.values()).forEach(packType -> packFormatTypes.put(packType, DetectedVersion.BUILT_IN.getPackVersion(packType)));

        return new ExtendedPackMetadataSection(
                description,
                DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
                packFormatTypes
        );
    }

    private static final class Serializer implements MetadataSectionType<ExtendedPackMetadataSection>
    {
        @Override
        public JsonObject toJson(ExtendedPackMetadataSection metadata)
        {
            var json = new JsonObject();
            var packFormat = metadata.getPackFormat();

            json.add("description", Component.Serializer.toJsonTree(metadata.getDescription()));
            json.addProperty("pack_format", packFormat);

            for(var packType : PackType.values())
            {
                var packTypeFormat = metadata.getPackFormat(packType);

                if(packTypeFormat != packFormat)
                    json.addProperty(makePackFormatKey(packType), packTypeFormat);
            }

            return json;
        }

        @Override
        public String getMetadataSectionName()
        {
            return "pack";
        }

        @Override
        public ExtendedPackMetadataSection fromJson(JsonObject json)
        {
            var description = Component.Serializer.fromJson(json.get("description"));

            if(description == null)
                throw new JsonParseException("Invalid/missing description!");

            var packFormat = GsonHelper.getAsInt(json, "pack_format");
            var packTypeFormats = new Object2IntOpenHashMap<PackType>();

            for(var packType : PackType.values())
            {
                var key = makePackFormatKey(packType);

                if(json.has(key))
                    packTypeFormats.put(packType, GsonHelper.getAsInt(json, key));
            }

            return new ExtendedPackMetadataSection(description, packFormat, packTypeFormats);
        }

        private String makePackFormatKey(PackType packType)
        {
            // currently only forge provides additional pack format keys
            // expand this out to check for current platform, if fabric introduces their own
            return "forge:%s_pack_format".formatted(packType.name().toLowerCase(Locale.ROOT));
        }
    }
}
