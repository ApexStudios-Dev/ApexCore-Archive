package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.core.Direction;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.*;
import xyz.apex.minecraft.apexcore.common.lib.PlatformOnly;
import xyz.apex.minecraft.apexcore.common.lib.resgen.JsonHelper;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public final class ElementBuilder
{
    private final ModelBuilder parent;
    private final Vector3f from = new Vector3f(0F);
    private final Vector3f to = new Vector3f(16F);
    private boolean shade = true;
    @Nullable private RotationBuilder rotation = null;
    private final Map<Direction, FaceBuilder> faceBuilders = Maps.newEnumMap(Direction.class);
    @PlatformOnly(PlatformOnly.FORGE) private int blockLight = 0;
    @PlatformOnly(PlatformOnly.FORGE) private int skyLight = 0;
    @PlatformOnly(PlatformOnly.FORGE) private int color = 0xFFFFFFFF;
    @PlatformOnly(PlatformOnly.FORGE) private boolean ambientOcclusion = true;

    @ApiStatus.Internal
    ElementBuilder(ModelBuilder parent)
    {
        this.parent = parent;
    }

    private void validateVec(float x, float y, float z)
    {
        Validate.isTrue(!(x < -16F) && !(x > 32F), "X out of range, must be within [-16, 32]. Found: %.2f", x);
        Validate.isTrue(!(y < -16F) && !(y > 32F), "Y out of range, must be within [-16, 32]. Found: %.2f", x);
        Validate.isTrue(!(z < -16F) && !(z > 32F), "Z out of range, must be within [-16, 32]. Found: %.2f", x);
    }

    public ElementBuilder from(Vector3fc from)
    {
        return from(from.x(), from.y(), from.z());
    }

    public ElementBuilder from(float x, float y, float z)
    {
        validateVec(x, y, z);
        from.set(x, y, z);
        return this;
    }

    public ElementBuilder to(Vector3fc to)
    {
        return to(to.x(), to.y(), to.z());
    }

    public ElementBuilder to(float x, float y, float z)
    {
        validateVec(x, y, z);
        to.set(x, y, z);
        return this;
    }

    public ElementBuilder shade(boolean shade)
    {
        this.shade = shade;
        return this;
    }

    public RotationBuilder rotation()
    {
        if(rotation == null)
            rotation = new RotationBuilder();
        return rotation;
    }

    public FaceBuilder face(Direction face)
    {
        return faceBuilders.computeIfAbsent(face, $ -> new FaceBuilder());
    }

    public ElementBuilder eachFace(BiConsumer<Direction, FaceBuilder> consumer)
    {
        faceBuilders.forEach(consumer);
        return this;
    }

    public ElementBuilder allFace(BiConsumer<Direction, FaceBuilder> consumer)
    {
        Stream.of(Direction.values()).forEach(face -> consumer.accept(face, face(face)));
        return this;
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public ElementBuilder emissivity(int blockLight, int skyLight)
    {
        this.blockLight = blockLight;
        this.skyLight = skyLight;
        return this;
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public ElementBuilder color(int color)
    {
        this.color = color;
        return this;
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public ElementBuilder ambientOcclusion(boolean ambientOcclusion)
    {
        this.ambientOcclusion = ambientOcclusion;
        return this;
    }

    public ModelBuilder end()
    {
        return parent;
    }

    @ApiStatus.Internal
    BlockElement toVanilla()
    {
        var partFaces = ImmutableMap.<Direction, BlockElementFace>builder();
        faceBuilders.forEach((face, builder) -> partFaces.put(face, builder.toVanilla()));

        return new BlockElement(
                new Vector3f(from),
                new Vector3f(to),
                partFaces.build(),
                rotation == null ? null : rotation.toVanilla(),
                shade
        );
    }

    @ApiStatus.Internal
    JsonObject toJson(boolean serializePlatformOnly)
    {
        var json = new JsonObject();

        if(!from.equals(0F, 0F, 0F))
            JsonHelper.addJsonIfNotEmpty(json, "from", JsonHelper.toJson(from));
        if(!to.equals(16F, 16F, 16F))
            JsonHelper.addJsonIfNotEmpty(json, "to", JsonHelper.toJson(to));
        if(rotation != null)
            JsonHelper.addJsonIfNotEmpty(json, "rotation", rotation.toJson());
        if(!shade)
            json.addProperty("shade", shade);

        if(!faceBuilders.isEmpty())
        {
            var element = toVanilla();
            var facesJson = new JsonObject();

            faceBuilders.forEach((face, value) -> JsonHelper.addJsonIfNotEmpty(
                    facesJson,
                    face.getSerializedName(),
                    value.toJson(face, element, serializePlatformOnly)
            ));

            JsonHelper.addJsonIfNotEmpty(json, "faces", facesJson);
        }

        if(serializePlatformOnly)
            JsonHelper.addJsonIfNotEmpty(json, "forge_data", serializeForgeData(color, blockLight, skyLight, ambientOcclusion));

        return json;
    }

    private static JsonObject serializeForgeData(int color, int blockLight, int skyLight, boolean ambientOcclusion)
    {
        var json = new JsonObject();

        if(color != 0xFFFFFFFF)
            json.addProperty("color", color);
        if(blockLight != 0)
            json.addProperty("block_light", blockLight);
        if(skyLight != 0)
            json.addProperty("sky_light", skyLight);
        if(!ambientOcclusion)
            json.addProperty("ambient_occlusion", ambientOcclusion);

        return json;
    }

    public final class RotationBuilder
    {
        private final Vector3f origin = new Vector3f(0F);
        private Direction.Axis axis = Direction.Axis.X;
        private float angle = 0F;
        private boolean rescale = false;

        @ApiStatus.Internal
        RotationBuilder() { }

        public RotationBuilder origin(Vector3fc origin)
        {
            return origin(origin.x(), origin.y(), origin.z());
        }

        public RotationBuilder origin(float x, float y, float z)
        {
            origin.set(x, y, z);
            return this;
        }

        public RotationBuilder axis(Direction.Axis axis)
        {
            this.axis = axis;
            return this;
        }

        public RotationBuilder angle(float angle)
        {
            Validate.isTrue(angle == 0F || Math.abs(angle) == 22.5F || Math.abs(angle) == 45F, "Invalid rotation %.2f found, only -45/-22.5/0/22.5/45 allowed", angle);
            this.angle = angle;
            return this;
        }

        public RotationBuilder rescale(boolean rescale)
        {
            this.rescale = rescale;
            return this;
        }

        public ElementBuilder end()
        {
            return ElementBuilder.this;
        }

        @ApiStatus.Internal
        BlockElementRotation toVanilla()
        {
            return new BlockElementRotation(new Vector3f(origin), axis, angle, rescale);
        }

        @ApiStatus.Internal
        JsonObject toJson()
        {
            var json = new JsonObject();

            if(!origin.equals(0F, 0F, 0F))
                JsonHelper.addJsonIfNotEmpty(json, "origin", JsonHelper.toJson(origin));
            if(axis != Direction.Axis.X)
                json.addProperty("axis", axis.getSerializedName());
            if(angle != 0F)
                json.addProperty("angle", angle);
            if(rescale)
                json.addProperty("rescale", rescale);

            return json;
        }
    }

    public final class FaceBuilder
    {
        private final Vector4f uv = new Vector4f(0F);
        @Nullable private String texture = null;
        @Nullable private Direction cullFace = null;
        private int rotation = 0;
        private int tintIndex = -1;
        @PlatformOnly(PlatformOnly.FORGE) private int blockLight = 0;
        @PlatformOnly(PlatformOnly.FORGE) private int skyLight = 0;
        @PlatformOnly(PlatformOnly.FORGE) private int color = 0xFFFFFFFF;
        @PlatformOnly(PlatformOnly.FORGE) private boolean ambientOcclusion = true;

        @ApiStatus.Internal
        FaceBuilder() { }

        public FaceBuilder uv(Vector4fc uv)
        {
            return uv(uv.x(), uv.y(), uv.z(), uv.w());
        }

        public FaceBuilder uv(Vector2fc min, Vector2fc max)
        {
            return uv(min.x(), min.y(), max.x(), max.y());
        }

        public FaceBuilder uv(float minX, float minY, float maxX, float maxY)
        {
            uv.set(minX, minY, maxX, maxY);
            return this;
        }

        public FaceBuilder texture(String texture)
        {
            this.texture = texture;
            return this;
        }

        public FaceBuilder cullFace(Direction cullFace)
        {
            this.cullFace = cullFace;
            return this;
        }

        public FaceBuilder rotation(int rotation)
        {
            Validate.isTrue(rotation >= 0 && rotation % 90 == 0 && rotation / 90 <= 3, "Invalid rotation specified %d, only 0/90/180/270 allowed", rotation);
            this.rotation = rotation;
            return this;
        }

        public FaceBuilder tintIndex(int tintIndex)
        {
            this.tintIndex = tintIndex;
            return this;
        }

        @PlatformOnly(PlatformOnly.FORGE)
        public FaceBuilder emissivity(int blockLight, int skyLight)
        {
            this.blockLight = blockLight;
            this.skyLight = skyLight;
            return this;
        }

        @PlatformOnly(PlatformOnly.FORGE)
        public FaceBuilder color(int color)
        {
            this.color = color;
            return this;
        }

        @PlatformOnly(PlatformOnly.FORGE)
        public FaceBuilder ambientOcclusion(boolean ambientOcclusion)
        {
            this.ambientOcclusion = ambientOcclusion;
            return this;
        }

        public ElementBuilder end()
        {
            return ElementBuilder.this;
        }

        @ApiStatus.Internal
        BlockElementFace toVanilla()
        {
            return new BlockElementFace(
                    cullFace,
                    tintIndex,
                    texture == null ? "#missingno" : texture,
                    new BlockFaceUV(
                            new float[] { uv.x, uv.y, uv.z, uv.w },
                            rotation
                    )
            );
        }

        @ApiStatus.Internal
        JsonObject toJson(Direction face, BlockElement element, boolean serializePlatformOnly)
        {
            var vanilla = toVanilla();
            var json = new JsonObject();

            JsonHelper.addTexture(json, "texture", texture);

            if(!Arrays.equals(vanilla.uv.uvs, element.uvsByFace(face)))
                JsonHelper.addJsonIfNotEmpty(json, "uv", JsonHelper.toJson(uv));
            if(cullFace != null)
                json.addProperty("cullface", cullFace.getSerializedName());
            if(rotation != 0)
                json.addProperty("rotation", rotation);
            if(tintIndex != -1)
                json.addProperty("tintindex", tintIndex);
            if(serializePlatformOnly)
                JsonHelper.addJsonIfNotEmpty(json, "forge_data", serializeForgeData(color, blockLight, skyLight, ambientOcclusion));

            return json;
        }
    }
}
