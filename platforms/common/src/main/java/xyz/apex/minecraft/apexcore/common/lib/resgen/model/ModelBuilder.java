package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import xyz.apex.minecraft.apexcore.common.lib.PlatformOnly;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ModelBuilder extends ModelFile
{
    @Nullable private ModelFile parent = null;
    @Nullable private BlockModel.GuiLight guiLight;
    private final Map<String, String> textures = Maps.newLinkedHashMap();
    private final Map<ItemDisplayContext, TransformBuilder> transformsMap = Maps.newEnumMap(ItemDisplayContext.class);
    private final List<OverrideBuilder> overrides = Lists.newLinkedList();
    private final List<ElementBuilder> elements = Lists.newLinkedList();
    private boolean ambientOcclusion = true;
    @Nullable @PlatformOnly(PlatformOnly.FORGE) private ResourceLocation renderType = null;

    @ApiStatus.Internal
    ModelBuilder(ResourceLocation location)
    {
        super(location);
    }

    public ModelBuilder parent(ModelFile parent)
    {
        this.parent = parent;
        return this;
    }

    public ModelBuilder texture(String textureSlot, String texture)
    {
        if(textureSlot.isBlank() || texture.isBlank())
            return this;

        textures.put(textureSlot, texture);
        return this;
    }

    public ModelBuilder texture(String textureSlot, ResourceLocation texture)
    {
        return texture(textureSlot, texture.toString());
    }

    public ModelBuilder guiLight(BlockModel.GuiLight guiLight)
    {
        this.guiLight = guiLight;
        return this;
    }

    public ModelBuilder ambientOcclusion(boolean ambientOcclusion)
    {
        this.ambientOcclusion = ambientOcclusion;
        return this;
    }

    public TransformBuilder transform(ItemDisplayContext displayContext)
    {
        return transformsMap.computeIfAbsent(displayContext, $ -> new TransformBuilder(this));
    }

    public OverrideBuilder override()
    {
        var override = new OverrideBuilder(this);
        overrides.add(override);
        return override;
    }

    public OverrideBuilder override(int index)
    {
        return overrides.get(index);
    }

    public ElementBuilder element()
    {
        var element = new ElementBuilder(this);
        elements.add(element);
        return element;
    }

    public ElementBuilder element(int index)
    {
        return elements.get(index);
    }

    @PlatformOnly(PlatformOnly.FORGE)
    public ModelBuilder renderType(ResourceLocation renderType)
    {
        this.renderType = renderType;
        return this;
    }

    private ItemTransform getTransform(ItemDisplayContext displayContext)
    {
        var transform = transformsMap.get(displayContext);
        return transform == null ? ItemTransform.NO_TRANSFORM : transform.toVanilla();
    }

    @ApiStatus.Internal
    List<BlockElement> toVanillaElements()
    {
        return elements.stream().map(ElementBuilder::toVanilla).toList();
    }

    @ApiStatus.Internal
    Map<String, Either<Material, String>> toVanillaTextureMap()
    {
        var textureMap = ImmutableMap.<String, Either<Material, String>>builder();

        textures.forEach((textureSlot, texturePath) -> {
            Either<Material, String> texture;

            if(textureSlot.charAt(0) == '#')
                texture = Either.right(textureSlot.substring(1));
            else
                texture = Either.left(new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(texturePath)));

            textureMap.put(textureSlot, texture);
        });

        return textureMap.build();
    }

    @ApiStatus.Internal
    ItemTransforms toVanillaItemTransforms()
    {
        return new ItemTransforms(
                getTransform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND),
                getTransform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND),
                getTransform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND),
                getTransform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND),
                getTransform(ItemDisplayContext.HEAD),
                getTransform(ItemDisplayContext.GUI),
                getTransform(ItemDisplayContext.GROUND),
                getTransform(ItemDisplayContext.FIXED)
        );
    }

    @ApiStatus.Internal
    List<ItemOverride> toVanillaItemOverrides()
    {
        return overrides.stream().map(OverrideBuilder::toVanilla).toList();
    }

    @ApiStatus.Internal
    BlockModel toVanilla()
    {
        return new BlockModel(
                parent == null ? null : parent.location,
                toVanillaElements(),
                toVanillaTextureMap(),
                ambientOcclusion,
                guiLight,
                toVanillaItemTransforms(),
                toVanillaItemOverrides()
        );
    }

    @ApiStatus.Internal
    @MustBeInvokedByOverriders
    JsonObject toJson(boolean serializePlatformOnly)
    {
        var json = new JsonObject();

        if(parent != null)
            json.addProperty("parent", parent.location.toString());
        if(!ambientOcclusion)
            json.addProperty("ambientocclusion", ambientOcclusion);
        // TODO: We should maybe AT the 'name' field to be accessible, and use that instead
        if(guiLight != null)
            json.addProperty("gui_light", guiLight.name().toLowerCase(Locale.ROOT));
        if(serializePlatformOnly && renderType != null)
            json.addProperty("render_type", renderType.toString());

        if(!transformsMap.isEmpty())
        {
            var transformsJson = new JsonObject();

            for(var entry : transformsMap.entrySet())
            {
                addJsonIfNotEmpty(
                        transformsJson,
                        entry.getKey().getSerializedName(),
                        entry.getValue().toJson(serializePlatformOnly)
                );
            }

            addJsonIfNotEmpty(json, "display", transformsJson);
        }

        if(!textures.isEmpty())
        {
            var texturesJson = new JsonObject();
            textures.forEach((key, value) -> addTexture(texturesJson, key, value));
            addJsonIfNotEmpty(json, "textures", texturesJson);
        }

        if(!elements.isEmpty())
        {
            var elementsJson = new JsonArray();
            elements.forEach(element -> addJsonIfNotEmpty(elementsJson, element.toJson(serializePlatformOnly)));
            addJsonIfNotEmpty(json, "elements", elementsJson);
        }

        if(!overrides.isEmpty())
        {
            var overridesJson = new JsonArray();
            overrides.forEach(override -> ModelBuilder.addJsonIfNotEmpty(overridesJson, override.toJson()));
            addJsonIfNotEmpty(json, "overrides", overridesJson);
        }

        return json;
    }

    public static JsonArray toJson(Vector3fc vec)
    {
        var json = new JsonArray();
        json.add(vec.x());
        json.add(vec.y());
        json.add(vec.z());
        return json;
    }

    public static JsonArray toJson(Vector4fc vec)
    {
        var json = new JsonArray();
        json.add(vec.x());
        json.add(vec.y());
        json.add(vec.z());
        json.add(vec.w());
        return json;
    }

    public static void addTexture(JsonObject json, String key, @Nullable String texture)
    {
        if(texture == null || texture.isBlank())
            return;

        if(texture.charAt(0) == '#')
            json.addProperty(key, texture);
        else
            json.addProperty(key, new ResourceLocation(texture).toString());

    }

    public static void addJsonIfNotEmpty(JsonObject json, String key, JsonElement element)
    {
        if(!isEmpty(element, false))
            json.add(key, element);
    }

    public static void addJsonIfNotEmpty(JsonArray json, JsonElement element)
    {
        if(!isEmpty(element, false))
            json.add(element);
    }

    private static boolean isEmpty(JsonElement element, boolean recurse)
    {
        if(element.isJsonNull())
            return true;
        else if(element.isJsonArray())
        {
            var array = element.getAsJsonArray();

            if(array.isEmpty())
                return true;
            if(!recurse)
                return false;

            for(var subElement : array)
            {
                if(!isEmpty(subElement, recurse))
                    return false;
            }

            return true;
        }
        else if(element.isJsonObject())
        {
            var obj = element.getAsJsonObject();

            if(obj.size() == 0)
                return true;
            if(!recurse)
                return false;

            for(var key : obj.keySet())
            {
                if(!isEmpty(obj.get(key), recurse))
                    return false;
            }

            return true;
        }
        else if(element.isJsonPrimitive())
        {
            var prim = element.getAsJsonPrimitive();

            if(prim.isString())
                return prim.getAsString().isBlank();

            return false;
        }
        else
            return false;
    }
}
