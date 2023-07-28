package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.resgen.model.ModelFile;

public final class StateModelBuilder
{
    @Nullable private ModelFile model = null;
    private int rotX = 0;
    private int rotY = 0;
    private boolean uvLock = false;
    private int weight = 1;

    public StateModelBuilder() { }

    public StateModelBuilder model(String modelPath)
    {
        return model(new ModelFile(modelPath));
    }

    public StateModelBuilder model(ResourceLocation modelPath)
    {
        return model(new ModelFile(modelPath));
    }

    public StateModelBuilder model(ModelFile model)
    {
        this.model = model;
        return this;
    }

    public StateModelBuilder rotX(int rotX)
    {
        Validate.isTrue(rotX <= 270 && rotX % 3 == 0);
        this.rotX = rotX;
        return this;
    }

    public StateModelBuilder rotY(int rotY)
    {
        Validate.isTrue(rotY <= 270 && rotY % 3 == 0);
        this.rotY = rotY;
        return this;
    }

    public StateModelBuilder rotation(int rotX, int rotY)
    {
        return rotX(rotX).rotY(rotY);
    }

    public StateModelBuilder uvLock(boolean uvLock)
    {
        this.uvLock = uvLock;
        return this;
    }

    public StateModelBuilder uvLock()
    {
        return uvLock(true);
    }

    public StateModelBuilder weight(int weight)
    {
        Validate.isTrue(weight >= 1);
        this.weight = weight;
        return this;
    }

    @ApiStatus.Internal
    JsonObject toJson()
    {
        Validate.notNull(model);

        var json = new JsonObject();
        json.addProperty("model", model.getModelPath().toString());

        if(rotX != 0)
            json.addProperty("x", rotX);
        if(rotY != 0)
            json.addProperty("y", rotY);
        if(uvLock)
            json.addProperty("uvlock", uvLock);
        if(weight > 1)
            json.addProperty("weight", weight);

        return json;
    }
}
