package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.resgen.JsonHelper;
import xyz.apex.minecraft.apexcore.common.lib.resgen.model.ModelBuilder;

import java.util.List;

public final class Variant
{
    private Rotation xRot = Rotation.R0;
    private Rotation yRot = Rotation.R0;
    @Nullable private ResourceLocation modelPath = null;
    private boolean uvlock = false;
    private int weight = 0;

    private Variant() { }

    public Variant copy(Variant other)
    {
        xRot = other.xRot;
        yRot = other.yRot;
        modelPath = other.modelPath;
        uvlock = other.uvlock;
        weight = other.weight;
        return this;
    }

    public Variant merge(Variant other)
    {
        if(xRot == Rotation.R0 && other.xRot != Rotation.R0)
            xRot = other.xRot;
        if(yRot == Rotation.R0 && other.yRot != Rotation.R0)
            yRot = other.yRot;
        if(modelPath == null && other.modelPath != null)
            modelPath = other.modelPath;
        if(!uvlock && other.uvlock)
            uvlock = other.uvlock;
        if(weight == 0 && other.weight > 0)
            weight = other.weight;

        return this;
    }

    public Variant xRot(Rotation xRot)
    {
        this.xRot = xRot;
        return this;
    }

    public Variant yRot(Rotation yRot)
    {
        this.yRot = yRot;
        return this;
    }

    public Variant rotation(Rotation xRot, Rotation yRot)
    {
        return xRot(xRot).yRot(yRot);
    }

    public Variant xRot(int xRot)
    {
        return xRot(Rotation.valueOf(xRot));
    }

    public Variant yRot(int yRot)
    {
        return yRot(Rotation.valueOf(yRot));
    }

    public Variant rotation(int xRot, int yRot)
    {
        return xRot(xRot).yRot(yRot);
    }

    public Variant model(ResourceLocation modelPath)
    {
        this.modelPath = modelPath;
        return this;
    }

    public Variant model(String modelPath)
    {
        return model(new ResourceLocation(modelPath));
    }

    public Variant model(ModelBuilder model)
    {
        return model(model.modelPath());
    }

    public Variant uvlock(boolean uvlock)
    {
        this.uvlock = uvlock;
        return this;
    }

    public Variant uvlock()
    {
        return uvlock(true);
    }

    public Variant weight(int weight)
    {
        this.weight = weight;
        return this;
    }

    private JsonElement toJson()
    {
        var json = new JsonObject();

        if(xRot != Rotation.R0)
            json.addProperty("x", xRot.rotation);
        if(yRot != Rotation.R0)
            json.addProperty("y", yRot.rotation);
        if(modelPath != null)
            json.addProperty("model", modelPath.toString());
        if(uvlock)
            json.addProperty("uvlock", uvlock);
        if(weight > 0)
            json.addProperty("weight", weight);

        return json;
    }

    public static Variant variant()
    {
        return new Variant();
    }

    public static Variant copyOf(Variant other)
    {
        return variant().copy(other);
    }

    public static Variant merge(Variant left, Variant right)
    {
        return copyOf(left).merge(right);
    }

    public static JsonElement toJson(List<Variant> variants)
    {
        if(variants.size() == 1)
            return variants.get(0).toJson();

        var json = new JsonArray();
        variants.stream().map(Variant::toJson).forEach(variant -> JsonHelper.addJsonIfNotEmpty(json, variant));
        return json;
    }

    public enum Rotation
    {
        R0(0),
        R90(90),
        R180(180),
        R270(270);

        private final int rotation;

        Rotation(int rotation)
        {
            this.rotation = rotation;
        }

        public static Rotation valueOf(int rotation)
        {
            return switch(Mth.positiveModulo(rotation, 360)) {
                default -> R0;
                case 90 -> R90;
                case 180 -> R180;
                case 270 -> R270;
            };
        }
    }
}
