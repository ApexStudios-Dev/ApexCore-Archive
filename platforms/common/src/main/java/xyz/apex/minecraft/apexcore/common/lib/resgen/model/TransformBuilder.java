package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.ItemTransform;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import xyz.apex.minecraft.apexcore.common.lib.PlatformOnly;
import xyz.apex.minecraft.apexcore.common.lib.resgen.JsonHelper;

public final class TransformBuilder
{
    private final ModelBuilder parent;
    private final Vector3f rotation = new Vector3f(0F);
    private final Vector3f rightRotation = new Vector3f(0F);
    private final Vector3f translation = new Vector3f(0F);
    private final Vector3f scale = new Vector3f(1F);

    @ApiStatus.Internal
    TransformBuilder(ModelBuilder parent)
    {
        this.parent = parent;
    }

    public TransformBuilder rotation(Vector3fc rotation)
    {
        return rotation(rotation.x(), rotation.y(), rotation.z());
    }

    public TransformBuilder rotation(float x, float y, float z)
    {
        rotation.set(x, y, z);
        return this;
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public TransformBuilder leftRotation(Vector3fc leftRotation)
    {
        return rotation(leftRotation);
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public TransformBuilder leftRotation(float x, float y, float z)
    {
        return rotation(x, y, z);
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public TransformBuilder rightRotation(Vector3fc rightRotation)
    {
        return rightRotation(rightRotation.x(), rightRotation.y(), rightRotation.z());
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public TransformBuilder rightRotation(float x, float y, float z)
    {
        rightRotation.set(x, y, z);
        return this;
    }

    public TransformBuilder translation(Vector3fc translation)
    {
        return translation(translation.x(), translation.y(), translation.z());
    }

    public TransformBuilder translation(float x, float y, float z)
    {
        translation.set(x, y, z);
        return this;
    }

    public TransformBuilder scale(Vector3fc scale)
    {
        return scale(scale.x(), scale.y(), scale.z());
    }

    public TransformBuilder scale(float x, float y, float z)
    {
        scale.set(x, y, z);
        return this;
    }

    public TransformBuilder scale(float scale)
    {
        return scale(scale, scale, scale);
    }

    public ModelBuilder end()
    {
        return parent;
    }

    @ApiStatus.Internal
    ItemTransform toVanilla()
    {
        return new ItemTransform(rotation, translation, scale);
    }

    @ApiStatus.Internal
    JsonObject toJson(boolean serializePlatformOnly)
    {
        var json = new JsonObject();

        if(!rotation.equals(0F, 0F, 0F))
            JsonHelper.addJsonIfNotEmpty(json, "rotation", JsonHelper.toJson(rotation));
        if(!translation.equals(0F, 0F, 0F))
            JsonHelper.addJsonIfNotEmpty(json, "translation", JsonHelper.toJson(translation));
        if(!scale.equals(1F, 1F, 1F))
            JsonHelper.addJsonIfNotEmpty(json, "scale", JsonHelper.toJson(scale));
        if(serializePlatformOnly && !rightRotation.equals(0F, 0F, 0F))
            JsonHelper.addJsonIfNotEmpty(json, "right_rotation", JsonHelper.toJson(rightRotation));

        return json;
    }
}
