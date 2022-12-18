package xyz.apex.minecraft.apexcore.shared.data.providers.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.client.renderer.block.model.ItemTransforms;

import java.util.Locale;
import java.util.Map;

public final class DisplaysBuilder<T extends ModelBuilder<T>> extends ModelBuilder.Builder<DisplaysBuilder<T>, T>
{
    private final Map<ItemTransforms.TransformType, DisplayBuilder<T>> builders = Maps.newHashMap();

    @ApiStatus.Internal
    DisplaysBuilder(T parent)
    {
        super(parent);
    }

    public DisplayBuilder<T> display(ItemTransforms.TransformType transformType)
    {
        Preconditions.checkNotNull(transformType, "TransformType must not be null");
        return builders.computeIfAbsent(transformType, $ -> new DisplayBuilder<>(this));
    }

    @ApiStatus.Internal
    @Nullable
    @Override
    protected JsonElement toJson()
    {
        if(builders.isEmpty()) return null;
        var root = new JsonObject();
        builders.forEach((key, value) -> ModelBuilder.addJson(root, key.name().toLowerCase(Locale.ROOT), value.toJson()));
        return root;
    }

    public static final class DisplayBuilder<T extends ModelBuilder<T>> extends ModelBuilder.Builder<DisplayBuilder<T>, DisplaysBuilder<T>>
    {
        private final Vector3f leftRotation = new Vector3f(0F);
        private final Vector3f rightRotation = new Vector3f(0F);
        private final Vector3f translation = new Vector3f(0F);
        private final Vector3f scale = new Vector3f(1F);

        @ApiStatus.Internal
        private DisplayBuilder(DisplaysBuilder<T> parent)
        {
            super(parent);
        }

        // region: Rotations
        // region: Left
        public DisplayBuilder<T> leftRotation(Vector3f leftRotation)
        {
            Preconditions.checkNotNull(leftRotation, "Vector3F must not be null");
            this.leftRotation.set(leftRotation);
            return this;
        }

        public DisplayBuilder<T> leftRotation(float x, float y, float z)
        {
            leftRotation.set(x, y, z);
            return this;
        }

        public DisplayBuilder<T> rotation(Vector3f leftRotation)
        {
            Preconditions.checkNotNull(leftRotation, "Vector3F must not be null");
            return leftRotation(leftRotation);
        }

        public DisplayBuilder<T> rotation(float x, float y, float z)
        {
            return leftRotation(x, y, z);
        }
        // endregion

        // region: Right
        public DisplayBuilder<T> rightRotation(Vector3f rightRotation)
        {
            Preconditions.checkNotNull(rightRotation, "Vector3F must not be null");
            this.rightRotation.set(rightRotation);
            return this;
        }

        public DisplayBuilder<T> rightRotation(float x, float y, float z)
        {
            rightRotation.set(x, y, z);
            return this;
        }
        // endregion
        // endregion

        // region: Translation
        public DisplayBuilder<T> translation(Vector3f translation)
        {
            Preconditions.checkNotNull(translation, "Vector3F must not be null");
            this.translation.set(translation);
            return this;
        }

        public DisplayBuilder<T> translation(float x, float y, float z)
        {
            translation.set(x, y, z);
            return this;
        }
        // endregion

        // region: Scale
        public DisplayBuilder<T> scale(Vector3f scale)
        {
            Preconditions.checkNotNull(scale, "Vector3F must not be null");
            this.scale.set(scale);
            return this;
        }

        public DisplayBuilder<T> scale(float x, float y, float z)
        {
            scale.set(x, y, z);
            return this;
        }

        public DisplayBuilder<T> scale(float scale)
        {
            return scale(scale, scale, scale);
        }
        // endregion

        public ModelBuilder<T> build()
        {
            return end().end();
        }

        @ApiStatus.Internal
        @Nullable
        @Override
        protected JsonElement toJson()
        {
            var isDefaultLeftRotation = leftRotation.equals(0F, 0F, 0F);
            var isDefaultTranslation = translation.equals(0F, 0F, 0F);
            var isDefaultScale = scale.equals(1F, 1F, 1F);

            if(isDefaultLeftRotation && isDefaultTranslation && isDefaultScale) return null;

            var root = new JsonObject();
            var hasRightRotation = !rightRotation.equals(0F, 0F, 0F);

            if(!isDefaultTranslation) ModelBuilder.addJson(root, "translation", ModelBuilder.serializeVector(translation));
            if(!isDefaultScale) ModelBuilder.addJson(root, "scale", ModelBuilder.serializeVector(scale));
            if(hasRightRotation) ModelBuilder.addJson(root, "right_rotation", ModelBuilder.serializeVector(rightRotation));

            if(!isDefaultLeftRotation)
            {
                var serialized = ModelBuilder.serializeVector(leftRotation);
                ModelBuilder.addJson(root, "rotation", serialized);
                if(hasRightRotation) ModelBuilder.addJson(root, "left_rotation", serialized);
            }

            return root;
        }
    }
}
