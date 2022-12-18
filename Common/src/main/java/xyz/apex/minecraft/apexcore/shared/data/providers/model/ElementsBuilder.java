package xyz.apex.minecraft.apexcore.shared.data.providers.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.core.Direction;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public final class ElementsBuilder<T extends ModelBuilder<T>> extends ModelBuilder.Builder<ElementsBuilder<T>, T>
{
    private final List<ElementBuilder<T>> elements = Lists.newArrayList();

    @ApiStatus.Internal
    ElementsBuilder(T parent)
    {
        super(parent);
    }

    // region: Elements
    public ElementBuilder<T> element()
    {
        var result = new ElementBuilder<>(this);
        elements.add(result);
        return result;
    }

    public ElementBuilder<T> element(int index)
    {
        return elements.get(index);
    }
    // endregion

    @ApiStatus.Internal
    @Nullable
    @Override
    protected JsonElement toJson()
    {
        if(elements.isEmpty()) return null;
        var root = new JsonArray();
        elements.stream().map(ElementBuilder::toJson).forEach(e -> ModelBuilder.addJson(root, e));
        return root;
    }

    public static final class ElementBuilder<T extends ModelBuilder<T>> extends ModelBuilder.Builder<ElementBuilder<T>, ElementsBuilder<T>>
    {
        private final Vector3f from = new Vector3f(0F);
        private final Vector3f to = new Vector3f(16F);
        private boolean shade = true;

        private final RotationBuilder<T> rotation = new RotationBuilder<>(this);
        private final Map<Direction, FaceBuilder<T>> faces = Maps.newEnumMap(Direction.class);

        @ApiStatus.Internal
        private ElementBuilder(ElementsBuilder<T> parent)
        {
            super(parent);
        }

        // region: From
        public ElementBuilder<T> from(Vector3f from)
        {
            this.from.set(from);
            return this;
        }

        public ElementBuilder<T> from(float x, float y, float z)
        {
            from.set(x, y, z);
            return this;
        }
        // endregion

        // region: To
        public ElementBuilder<T> to(Vector3f from)
        {
            this.to.set(from);
            return this;
        }

        public ElementBuilder<T> to(float x, float y, float z)
        {
            to.set(x, y, z);
            return this;
        }
        // endregion

        // region: Shade
        public ElementBuilder<T> shade(boolean shade)
        {
            this.shade = shade;
            return this;
        }

        public ElementBuilder<T> shade()
        {
            return shade(true);
        }

        public ElementBuilder<T> noShade()
        {
            return shade(false);
        }
        // endregion

        public RotationBuilder<T> rotation()
        {
            return rotation;
        }

        // region: Faces
        public FaceBuilder<T> face(Direction face)
        {
            return faces.computeIfAbsent(face, $ -> new FaceBuilder<>(this));
        }

        public ElementBuilder<T> faces(BiConsumer<Direction, FaceBuilder<T>> consumer)
        {
            faces.forEach(consumer);
            return this;
        }

        public ElementBuilder<T> allFaces(BiConsumer<Direction, FaceBuilder<T>> consumer)
        {
            Arrays.stream(Direction.values()).forEach(face -> consumer.accept(face, face(face)));
            return this;
        }
        // endregion

        public T build()
        {
            return end().end();
        }

        @ApiStatus.Internal
        @Nullable
        @Override
        protected JsonElement toJson()
        {
            var isDefaultFrom = from.equals(0F, 0F, 0F);
            var isDefaultTo = to.equals(16F, 16F, 16F);
            var isDefaultShade = shade;

            if(isDefaultFrom && isDefaultTo && isDefaultShade) return null;

            var root = new JsonObject();

            if(!isDefaultFrom) ModelBuilder.addJson(root, "from", ModelBuilder.serializeVector(from));
            if(!isDefaultTo) ModelBuilder.addJson(root, "to", ModelBuilder.serializeVector(to));
            if(!isDefaultShade) root.addProperty("shade", shade);

            ModelBuilder.addJson(root, "rotation", rotation.toJson());

            if(!faces.isEmpty())
            {
                var facesJson = new JsonObject();
                faces((face, builder) -> ModelBuilder.addJson(facesJson, face.getName(), builder.toJson()));
                ModelBuilder.addJson(root, "faces", facesJson);
            }

            return root;
        }
    }

    public static final class RotationBuilder<T extends ModelBuilder<T>> extends ModelBuilder.Builder<RotationBuilder<T>, ElementBuilder<T>>
    {
        private final Vector3f origin = new Vector3f(0F);
        private Direction.Axis axis = Direction.Axis.X;
        private float angle = 0F;

        @ApiStatus.Internal
        private RotationBuilder(ElementBuilder<T> parent)
        {
            super(parent);
        }

        // region: Origin
        public RotationBuilder<T> origin(Vector3f origin)
        {
            this.origin.set(origin);
            return this;
        }

        public RotationBuilder<T> origin(float x, float y, float z)
        {
            origin.set(x, y, z);
            return this;
        }
        // endregion

        public RotationBuilder<T> axis(Direction.Axis axis)
        {
            this.axis = axis;
            return this;
        }

        public RotationBuilder<T> angle(float angle)
        {
            Preconditions.checkArgument(angle == 0F || Mth.abs(angle) == 22.5F || Mth.abs(angle) == 45F, "Invalid angle %f, only -45/-22.5/0/22.5/45 is allowed", angle);
            this.angle = angle;
            return this;
        }

        @ApiStatus.Internal
        @Nullable
        @Override
        protected JsonElement toJson()
        {
            var isDefaultOrigin = origin.equals(0F, 0F, 0F);
            if(isDefaultOrigin && axis == Direction.Axis.X && angle == 0F) return null;

            var root = new JsonObject();

            if(!isDefaultOrigin) ModelBuilder.addJson(root, "origin", ModelBuilder.serializeVector(origin));
            if(axis != Direction.Axis.X) root.addProperty("axis", axis.getSerializedName());
            if(angle != 0F) root.addProperty("angle", angle);

            return root;
        }
    }

    public static final class FaceBuilder<T extends ModelBuilder<T>> extends ModelBuilder.Builder<FaceBuilder<T>, ElementBuilder<T>>
    {
        private final float[] uv = new float[] { 0F, 0F, 16F, 16F };
        @Nullable private TextureSlot textureSlot;
        @Nullable private Direction cullface;
        private Rotation rotation = Rotation.NONE;
        private int tintIndex = -1;

        @ApiStatus.Internal
        private FaceBuilder(ElementBuilder<T> parent)
        {
            super(parent);
        }

        public FaceBuilder<T> uv(float minU, float minV, float maxU, float maxV)
        {
            uv[0] = minU;
            uv[1] = minV;
            uv[2] = maxU;
            uv[3] = maxV;
            return this;
        }

        public FaceBuilder<T> texture(TextureSlot textureSlot)
        {
            this.textureSlot = textureSlot;
            return this;
        }

        public FaceBuilder<T> cullface(Direction cullface)
        {
            this.cullface = cullface;
            return this;
        }

        public FaceBuilder<T> rotation(Rotation rotation)
        {
            this.rotation = rotation;
            return this;
        }

        public FaceBuilder<T> tintIndex(int tintIndex)
        {
            Preconditions.checkArgument(tintIndex == -1 || tintIndex >= 0, "Invalid tintIndex: %d, Must be -1 or >= 0", tintIndex);
            this.tintIndex = tintIndex;
            return this;
        }

        @ApiStatus.Internal
        @Override
        protected JsonElement toJson()
        {
            Preconditions.checkNotNull(textureSlot, "Missing required TextureSlot property");

            var root = new JsonObject();

            var uvJson = new JsonArray();
            IntStream.range(0, uv.length).mapToObj(i -> uv[i]).forEach(uvJson::add);
            ModelBuilder.addJson(root, "uv", uvJson);

            root.addProperty("texture", textureSlot.getId());

            if(cullface != null) root.addProperty("cull_face", cullface.getSerializedName());
            if(rotation != Rotation.NONE) root.addProperty("rotation", rotation.rotate(0, 90));
            if(tintIndex != -1 && tintIndex >= 0) root.addProperty("tint_index", tintIndex);

            return root;
        }
    }
}
