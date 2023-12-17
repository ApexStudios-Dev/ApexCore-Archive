package dev.apexstudios.apexcore.common.generator.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import com.google.errorprone.annotations.ForOverride;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import dev.apexstudios.apexcore.common.util.JsonHelper;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.covers1624.quack.util.SneakyUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ModelBuilder<B extends ModelBuilder<B, T>, T> implements ModelFile
{
    public static final String MISSING_TEXTURE = "#missingno";

    protected final ModelGenerator<T, B> generator;
    private final ResourceLocation path;

    private OptionalLike<ModelFile> parent = OptionalLike.empty();
    private final Map<String, String> textures = Maps.newHashMap();
    private final Multimap<String, Consumer<DisplayBuilder>> displays = HashMultimap.create();
    private final Int2ObjectMap<List<Consumer<ElementBuilder>>> elements = new Int2ObjectOpenHashMap<>();

    protected ModelBuilder(ModelGenerator<T, B> generator, ResourceLocation path)
    {
        this.generator = generator;
        this.path = path;
    }

    // region: Parent
    @Nullable
    public final ModelFile parent()
    {
        return parent.getRaw();
    }

    public final B parent(@Nullable ModelFile parent)
    {
        this.parent = OptionalLike.of(parent);
        return self();
    }

    public final B parent(ResourceLocation parent)
    {
        return parent(ModelFile.of(parent));
    }

    public final B parent(String ownerId, String parentPath)
    {
        return parent(new ResourceLocation(ownerId, parentPath));
    }

    public final B parent(String parentPath)
    {
        return parent(new ResourceLocation(parentPath));
    }

    public final B parent(T element)
    {
        return parent(generator.withModelDir(element));
    }

    public final B parent(Holder<T> holder)
    {
        var unwrapped = holder.unwrap();
        unwrapped.ifLeft(this::parent);
        unwrapped.ifRight(this::parent);
        return self();
    }

    public final B parent(DeferredHolder<T, ? super T> holder)
    {
        return parent(holder.registryKey());
    }

    public final B parent(ResourceKey<T> registryKey)
    {
        return parent(generator.withModelDir(registryKey.location()));
    }
    // endregion

    // region: Textures
    public final String texture(String textureSlot)
    {
        return textures.getOrDefault(textureSlot, MISSING_TEXTURE);
    }

    public final B texture(String textureSlot, ResourceLocation texturePath)
    {
        return texture(textureSlot, texturePath.toString());
    }

    public final B texture(String textureSlot, String textureOwnerId, String texturePath)
    {
        return texture(textureSlot, "%s:%s".formatted(textureOwnerId, texturePath));
    }

    public final B texture(String textureSlot, String texturePath)
    {
        textures.put(textureSlot, texturePath);
        return self();
    }

    public final B texture(String textureSlot, T element)
    {
        return texture(textureSlot, generator.withModelDir(element));
    }

    public final B texture(String textureSlot, Holder<T> holder)
    {
        var unwrapped = holder.unwrap();
        unwrapped.ifLeft(registryKey -> texture(textureSlot, registryKey));
        unwrapped.ifRight(element -> texture(textureSlot, element));
        return self();
    }

    public final B texture(String textureSlot, DeferredHolder<T, ? super T> holder)
    {
        return texture(textureSlot, holder.registryKey());
    }

    public final B texture(String textureSlot, ResourceKey<T> registryKey)
    {
        return texture(textureSlot, generator.withModelDir(registryKey.location()));
    }

    public final String particle()
    {
        return texture(TextureSlot.PARTICLE.getId());
    }

    public final B particle(ResourceLocation texturePath)
    {
        return particle(texturePath.toString());
    }

    public final B particle(String textureOwnerId, String texturePath)
    {
        return particle("%s:%s".formatted(textureOwnerId, texturePath));
    }

    public final B particle(String texturePath)
    {
        return texture(TextureSlot.PARTICLE.getId(), texturePath);
    }

    public final B particle(T element)
    {
        return particle(generator.withModelDir(element));
    }

    public final B particle(Holder<T> holder)
    {
        var unwrapped = holder.unwrap();
        unwrapped.ifLeft(this::particle);
        unwrapped.ifRight(this::particle);
        return self();
    }

    public final B particle(DeferredHolder<T, ? super T> holder)
    {
        return particle(holder.registryKey());
    }

    public final B particle(ResourceKey<T> registryKey)
    {
        return particle(generator.withModelDir(registryKey.location()));
    }
    // endregion

    // region: Display
    public final B display(String displayContext, Consumer<DisplayBuilder> builder)
    {
        // neoforge allows custom contexts
        displays.put(displayContext, builder);
        return self();
    }

    public final B display(ItemDisplayContext displayContext, Consumer<DisplayBuilder> builder)
    {
        return display(displayContext.getSerializedName(), builder);
    }
    // endregion

    // region: Elements
    public final B element(Consumer<ElementBuilder> builder)
    {
        return element(elements.size(), builder);
    }

    public final B element(int elementIndex, Consumer<ElementBuilder> builder)
    {
        elements.computeIfAbsent(elementIndex, $ -> Lists.newArrayList()).add(builder);
        return self();
    }
    // endregion

    @Override
    public final ResourceLocation path()
    {
        return path;
    }

    protected final B self()
    {
        return (B) this;
    }

    @ForOverride
    @ApiStatus.Internal
    protected void serializeExtraData(JsonObject root)
    {
    }

    public final JsonObject toJson()
    {
        var root = new JsonObject();

        parent.map(ModelFile::path).map(ResourceLocation::toString).ifPresent(parentPath -> root.addProperty("parent", parentPath));

        var texturesJson = new JsonObject();
        textures.forEach(texturesJson::addProperty);
        JsonHelper.appendIfNoneNull(root, "textures", texturesJson);

        var displaysJson = new JsonObject();

        Set.copyOf(displays.keys()).forEach(displayContext -> {
            var builder = new DisplayBuilder();
            displays.removeAll(displayContext).forEach(consumer -> consumer.accept(builder));
            JsonHelper.appendIfNoneNull(displaysJson, displayContext, builder.toJson());
        });

        JsonHelper.appendIfNoneNull(root, "displays", displaysJson);

        var elementsJson = new JsonArray();

        elements.forEach((elementIndex, callbacks) -> {
            var builder = new ElementBuilder();
            callbacks.forEach(callback -> callback.accept(builder));
            JsonHelper.appendIfNoneNull(elementsJson, builder.toJson());
        });

        JsonHelper.appendIfNoneNull(root, "elements", elementsJson);

        serializeExtraData(root);

        return root;
    }

    public static final class DisplayBuilder
    {
        private final Vector3f rotation = new Vector3f();
        private final Vector3f rightRotation = new Vector3f(); // neoforge allows rotations for both sides
        private final Vector3f translation = new Vector3f();
        private final Vector3f scale = new Vector3f(1F);

        private DisplayBuilder()
        {
        }

        public DisplayBuilder rotation(Vector3fc rotation)
        {
            this.rotation.set(rotation);
            return this;
        }

        public DisplayBuilder rotation(float x, float y, float z)
        {
            rotation.set(x, y, z);
            return this;
        }

        public DisplayBuilder rotation(Direction.Axis axis, float value)
        {
            switch(axis)
            {
                case X -> rotation.x = value;
                case Y -> rotation.y = value;
                case Z -> rotation.z = value;
            }
            return this;
        }

        public DisplayBuilder rotation(boolean right, Vector3fc rotation)
        {
            return right ? rightRotation(rightRotation) : leftRotation(rotation);
        }

        public DisplayBuilder rotation(boolean right, float x, float y, float z)
        {
            return right ? rightRotation(x, y, z) : leftRotation(x, y, z);
        }

        public DisplayBuilder rotation(boolean right, Direction.Axis axis, float value)
        {
            return right ? rightRotation(axis, value) : leftRotation(axis, value);
        }

        public DisplayBuilder rotation(HumanoidArm arm, Vector3fc rotation)
        {
            return switch(arm)
            {
                case LEFT -> leftRotation(rotation);
                case RIGHT -> rightRotation(rotation);
            };
        }

        public DisplayBuilder rotation(HumanoidArm arm, float x, float y, float z)
        {
            return switch(arm)
            {
                case LEFT -> leftRotation(x, y, z);
                case RIGHT -> rightRotation(x, y, z);
            };
        }

        public DisplayBuilder rotation(HumanoidArm arm, Direction.Axis axis, float value)
        {
            return switch(arm)
            {
                case LEFT -> leftRotation(axis, value);
                case RIGHT -> rightRotation(axis, value);
            };
        }

        public DisplayBuilder leftRotation(Vector3fc leftRotation)
        {
            return rotation(leftRotation);
        }

        public DisplayBuilder leftRotation(float x, float y, float z)
        {
            return rotation(x, y, z);
        }

        public DisplayBuilder leftRotation(Direction.Axis axis, float value)
        {
            return rotation(axis, value);
        }

        public DisplayBuilder rightRotation(Vector3fc rightRotation)
        {
            this.rightRotation.set(rightRotation);
            return this;
        }

        public DisplayBuilder rightRotation(float x, float y, float z)
        {
            rightRotation.set(x, y, z);
            return this;
        }

        public DisplayBuilder rightRotation(Direction.Axis axis, float value)
        {
            switch(axis)
            {
                case X -> rightRotation.x = value;
                case Y -> rightRotation.y = value;
                case Z -> rightRotation.z = value;
            }
            return this;
        }

        public DisplayBuilder translation(Vector3fc translation)
        {
            this.translation.set(translation);
            return this;
        }

        public DisplayBuilder translation(float x, float y, float z)
        {
            translation.set(x, y, z);
            return this;
        }

        public DisplayBuilder translation(Direction.Axis axis, float value)
        {
            switch(axis)
            {
                case X -> translation.x = value;
                case Y -> translation.y = value;
                case Z -> translation.z = value;
            }
            return this;
        }

        public DisplayBuilder scale(Vector3fc scale)
        {
            this.scale.set(scale);
            return this;
        }

        public DisplayBuilder scale(float x, float y, float z)
        {
            scale.set(x, y, z);
            return this;
        }

        public DisplayBuilder scale(float scale)
        {
            return scale(scale, scale, scale);
        }

        public DisplayBuilder scale(Direction.Axis axis, float value)
        {
            switch(axis)
            {
                case X -> scale.x = value;
                case Y -> scale.y = value;
                case Z -> scale.z = value;
            }
            return this;
        }

        private JsonElement toJson()
        {
            var root = new JsonObject();

            JsonHelper.appendIfNotZero(root, "rotation", rotation);
            JsonHelper.appendIfNotZero(root, "right_rotation", rightRotation);
            JsonHelper.appendIfNotZero(root, "translation", translation);
            JsonHelper.appendIfNotZero(root, "scale", scale);

            if(!scale.equals(1F, 1F, 1F))
                root.add("scale", JsonHelper.toJson(scale));

            return JsonHelper.nullOrElement(root);
        }
    }

    public static final class ElementBuilder extends WithNeoForgeData<ElementBuilder>
    {
        private static final boolean DEFAULT_SHADE = true;

        private final Vector3f from = new Vector3f();
        private final Vector3f to = new Vector3f();
        private boolean shade = DEFAULT_SHADE;
        private Consumer<RotationBuilder> rotation = SneakyUtils.nullCons();
        private Multimap<Direction, Consumer<FaceBuilder>> faces = MultimapBuilder.enumKeys(Direction.class).arrayListValues().build();

        private ElementBuilder()
        {
        }

        public ElementBuilder from(Vector3fc from)
        {
            this.from.set(from);
            return this;
        }

        public ElementBuilder from(float x, float y, float z)
        {
            from.set(x, y, z);
            return this;
        }

        public ElementBuilder from(Direction.Axis axis, float value)
        {
            switch(axis)
            {
                case X -> from.x = value;
                case Y -> from.y = value;
                case Z -> from.z = value;
            }

            return this;
        }

        public ElementBuilder to(Vector3fc to)
        {
            this.to.set(to);
            return this;
        }

        public ElementBuilder to(float x, float y, float z)
        {
            to.set(x, y, z);
            return this;
        }

        public ElementBuilder to(Direction.Axis axis, float value)
        {
            switch(axis)
            {
                case X -> to.x = value;
                case Y -> to.y = value;
                case Z -> to.z = value;
            }

            return this;
        }

        public ElementBuilder shade(boolean shade)
        {
            this.shade = shade;
            return this;
        }

        public ElementBuilder shade()
        {
            return shade(true);
        }

        public ElementBuilder noShade()
        {
            return shade(false);
        }

        public ElementBuilder rotation(Consumer<RotationBuilder> rotation)
        {
            this.rotation = this.rotation.andThen(rotation);
            return this;
        }

        public ElementBuilder face(Direction face, Consumer<FaceBuilder> builder)
        {
            faces.put(face, builder);
            return this;
        }

        public ElementBuilder faces(BiConsumer<Direction, FaceBuilder> builder)
        {
            for(var face : Direction.values())
            {
                face(face, bldr -> builder.accept(face, bldr));
            }

            return this;
        }

        private JsonElement toJson()
        {
            var root = new JsonObject();

            JsonHelper.appendIfNotZero(root, "from", from);
            JsonHelper.appendIfNotZero(root, "to", to);

            if(shade != DEFAULT_SHADE)
                root.addProperty("shade", shade);

            var rotation = new RotationBuilder();
            this.rotation.accept(rotation);
            JsonHelper.appendIfNoneNull(root, "rotation", rotation.toJson());

            var facesJson = new JsonObject();

            Set.copyOf(faces.keys()).forEach(face -> {
                var builder = new FaceBuilder();
                faces.removeAll(face).forEach(consumer -> consumer.accept(builder));
                JsonHelper.appendIfNoneNull(facesJson, face.getSerializedName(), builder.toJson());
            });

            JsonHelper.appendIfNoneNull(root, "faces", facesJson);

            appendNeoForgeData(root);

            return JsonHelper.nullOrElement(root);
        }
    }

    public static final class RotationBuilder
    {
        private static final float DEFAULT_ANGLE = 0F;
        private static final boolean DEFAULT_RESCALE = false;

        private final Vector3f origin = new Vector3f();
        private OptionalLike<Direction.Axis> axis = OptionalLike.empty();
        private float angle = DEFAULT_ANGLE;
        private boolean rescale = DEFAULT_RESCALE;

        private RotationBuilder()
        {
        }

        public RotationBuilder origin(Vector3fc origin)
        {
            this.origin.set(origin);
            return this;
        }

        public RotationBuilder origin(float x, float y, float z)
        {
            origin.set(x, y, z);
            return this;
        }

        public RotationBuilder origin(Direction.Axis axis, float value)
        {
            switch(axis)
            {
                case X -> origin.x = value;
                case Y -> origin.y = value;
                case Z -> origin.z = value;
            }

            return this;
        }

        public RotationBuilder axis(@Nullable Direction.Axis axis)
        {
            this.axis = OptionalLike.of(axis);
            return this;
        }

        public RotationBuilder angle(float angle)
        {
            Preconditions.checkArgument(angle == -45F || angle == -22.5F || angle == 0F || angle == 22.5F || angle == 45F);
            this.angle = angle;
            return this;
        }

        public RotationBuilder rescale(boolean rescale)
        {
            this.rescale = rescale;
            return this;
        }

        public RotationBuilder rescale()
        {
            return rescale(true);
        }

        private JsonElement toJson()
        {
            var root = new JsonObject();

            JsonHelper.appendIfNotZero(root, "origin", origin);
            axis.map(Direction.Axis::getSerializedName).ifPresent(axis -> root.addProperty("axis", axis));

            if(angle != DEFAULT_ANGLE)
                root.addProperty("angle", angle);
            if(rescale != DEFAULT_RESCALE)
                root.addProperty("rescale", rescale);

            return JsonHelper.nullOrElement(root);
        }
    }

    public static final class FaceBuilder extends WithNeoForgeData<FaceBuilder>
    {
        private static final String DEFAULT_TEXURE = MISSING_TEXTURE;
        private static final int DEFAULT_ROTATION = 0;
        private static final int DEFAULT_TINT_INDEX = -1;

        private final Vector4f uv = new Vector4f();
        private String textureSlot = DEFAULT_TEXURE;
        private OptionalLike<Direction> cullFace = OptionalLike.empty();
        private int rotation = DEFAULT_ROTATION;
        private int tintIndex = DEFAULT_TINT_INDEX;

        public FaceBuilder uv(Vector4fc uv)
        {
            this.uv.set(uv);
            return this;
        }

        public FaceBuilder uv(float minU, float minV, float maxU, float maxV)
        {
            uv.set(minU, minV, maxU, maxV);
            return this;
        }

        public FaceBuilder texture(String textureSlot)
        {
            this.textureSlot = textureSlot;
            return this;
        }

        public FaceBuilder cullFace(@Nullable Direction cullFace)
        {
            this.cullFace = OptionalLike.of(cullFace);
            return this;
        }

        public FaceBuilder rotation(int rotation)
        {
            Preconditions.checkArgument(rotation == 0 || rotation == 90 || rotation == 180 || rotation == 270);
            this.rotation = rotation;
            return this;
        }

        public FaceBuilder tintIndex(int tintIndex)
        {
            this.tintIndex = tintIndex <= -1 ? DEFAULT_TINT_INDEX : tintIndex;
            return this;
        }

        private JsonElement toJson()
        {
            var root = new JsonObject();

            JsonHelper.appendIfNotZero(root, "uv", uv);
            root.addProperty("texture", StringUtils.prependIfMissing(textureSlot, "#"));
            cullFace.map(Direction::getSerializedName).ifPresent(c -> root.addProperty("cullface", c));

            if(rotation != DEFAULT_ROTATION)
                root.addProperty("rotation", rotation);
            if(tintIndex != DEFAULT_TINT_INDEX)
                root.addProperty("tintindex", tintIndex);

            appendNeoForgeData(root);

            return JsonHelper.nullOrElement(root);
        }
    }

    public static final class NeoForgeElementBuilder
    {
        private static final int DEFAULT_COLOR = 0xFFFFFFFF;
        private static final int DEFAULT_BLOCK_LIGHT = 0;
        private static final int DEFAULT_SKY_LIGHT = 0;
        private static final boolean DEFAULT_AMBIENT_OCCLUSION = true;

        private int color = DEFAULT_COLOR;
        private int blockLight = DEFAULT_BLOCK_LIGHT;
        private int skyLight = DEFAULT_SKY_LIGHT;
        private boolean ambientOcclusion = DEFAULT_AMBIENT_OCCLUSION;

        private NeoForgeElementBuilder()
        {
        }

        public NeoForgeElementBuilder color(int color)
        {
            this.color = color;
            return this;
        }

        public NeoForgeElementBuilder color(int a, int r, int g, int b)
        {
            return color(FastColor.ARGB32.color(a, r, g, b));
        }

        public NeoForgeElementBuilder color(int r, int g, int b)
        {
            return color(255, r, g, b);
        }

        public NeoForgeElementBuilder color(float a, float r, float g, float b)
        {
            return color(
                    (int) (a * 255F),
                    (int) (r * 255F),
                    (int) (g * 255F),
                    (int) (b * 255F)
            );
        }

        public NeoForgeElementBuilder color(float r, float g, float b)
        {
            return color(1F, r, g, b);
        }

        public NeoForgeElementBuilder blockLight(int blockLight)
        {
            this.blockLight = blockLight;
            return this;
        }

        public NeoForgeElementBuilder skyLight(int skyLight)
        {
            this.skyLight = skyLight;
            return this;
        }

        public NeoForgeElementBuilder light(int blockLight, int skyLight)
        {
            return blockLight(blockLight).skyLight(skyLight);
        }

        public NeoForgeElementBuilder ambientOcclusion(boolean ambientOcclusion)
        {
            this.ambientOcclusion = ambientOcclusion;
            return this;
        }

        private JsonElement toJson()
        {
            var root = new JsonObject();

            if(color != DEFAULT_COLOR)
                root.addProperty("color", color);
            if(blockLight != DEFAULT_BLOCK_LIGHT)
                root.addProperty("block_light", blockLight);
            if(skyLight != DEFAULT_SKY_LIGHT)
                root.addProperty("sky_light", skyLight);
            if(ambientOcclusion != DEFAULT_AMBIENT_OCCLUSION)
                root.addProperty("ambient_occlusion", ambientOcclusion);

            return JsonHelper.nullOrElement(root);
        }
    }

    private static class WithNeoForgeData<B extends WithNeoForgeData<B>>
    {
        private Consumer<NeoForgeElementBuilder> neoforgeData = SneakyUtils.nullCons();

        protected WithNeoForgeData()
        {
        }

        public B neoforgeData(Consumer<NeoForgeElementBuilder> neoforgeData)
        {
            this.neoforgeData = this.neoforgeData.andThen(neoforgeData);
            return (B) this;
        }

        protected final void appendNeoForgeData(JsonObject root)
        {
            var neoforgeBuilder = new NeoForgeElementBuilder();
            neoforgeData.accept(neoforgeBuilder);
            JsonHelper.appendIfNoneNull(root, "neoforge_data", neoforgeBuilder.toJson());
        }
    }
}

/*
    root
        parent: ModelFile
        display
            position
                rotation: float3
                translation: float3 [ -80 - 80 ]
                scale: float3 [ 0 - 4 ]
        textures
            particle
            {var}
        elements
            from: float3 [ -16 - 32 ]
            to: float3 [ -16 - 32 ]
            shade: boolean, default false
            rotation
                origin: float3
                axis: Axis
                angle: float [ -45 | -25.5 | 0 | 22.5 | 45 ]
                rescale: boolean, default: false
            faces
                face: Direction
                    uv: float4 [ 0 - 16 ]
                    texture: #{TextureVar}
                    cullface: Direction
                    rotation: [ 0 | 90 | 180 | 270 ], default 0
                    tintindex: int [ >= 0 | -1 {unset} ], default -1

            neoforge_data
                // neoforge allows special data to be written per element
                color: hex, default #FFFFFFFF
                block_light: int [ 0 - 15 ], default 0
                sky_light: int [ 0 - 15 ], default 0
                ambient_occlusion: boolean, default true
 */