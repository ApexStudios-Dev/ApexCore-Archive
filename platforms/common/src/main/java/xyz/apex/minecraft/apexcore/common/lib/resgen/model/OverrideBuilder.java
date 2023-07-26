package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public final class OverrideBuilder
{
    private final ModelBuilder parent;
    @Nullable private ModelFile model = null;
    private final Object2FloatMap<ResourceLocation> properties = new Object2FloatOpenHashMap<>();

    @ApiStatus.Internal
    OverrideBuilder(ModelBuilder parent)
    {
        this.parent = parent;
    }

    public OverrideBuilder model(ModelFile model)
    {
        this.model = model;
        return this;
    }

    public OverrideBuilder property(ResourceLocation propertyName, float propertyValue)
    {
        properties.put(propertyName, propertyValue);
        return this;
    }

    public ModelBuilder end()
    {
        return parent;
    }

    @ApiStatus.Internal
    ItemOverride toVanilla()
    {
        Validate.notNull(model);

        return new ItemOverride(
                model.location,
                properties.object2FloatEntrySet()
                          .stream()
                          .map(entry -> new ItemOverride.Predicate(entry.getKey(), entry.getFloatValue()))
                          .toList()
        );
    }

    @ApiStatus.Internal
    JsonObject toJson()
    {
        Validate.notNull(model);

        var json = new JsonObject();

        json.addProperty("model", model.location.toString());

        if(!properties.isEmpty())
        {
            var propertiesJson = new JsonObject();
            properties.forEach((propertyName, propertyValue) -> propertiesJson.addProperty(propertyName.toString(), propertyValue));
            ModelBuilder.addJsonIfNotEmpty(json, "predicate", propertiesJson);
        }

        return json;
    }
}
