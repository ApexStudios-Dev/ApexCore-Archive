package xyz.apex.minecraft.apexcore.shared.data.providers.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Registry;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ModelBuilder<T extends ModelBuilder<T>>
{
    protected final ResourceLocation modelPath;
    protected final String modelFolder;

    private final Map<TextureSlot, ResourceLocation> textures = Maps.newHashMap();
    private final Map<String, Builder<?, T>> children = Maps.newHashMap();
    @Nullable private ResourceLocation parent;
    @Nullable private BlockModel.GuiLight guiLight;
    @SuppressWarnings("unchecked") protected final T self = (T) this;

    @ApiStatus.Internal
    public ModelBuilder(String modelFolder, ResourceLocation modelPath)
    {
        this.modelFolder = StringUtils.appendIfMissing(modelFolder, "/");
        this.modelPath = modelPath;
    }

    @ApiStatus.Internal
    public ModelBuilder(ResourceKey<? extends Registry<?>> registryType, ResourceLocation modelPath)
    {
        this(registryType.location().getPath(), modelPath);
    }

    @ApiStatus.Internal
    public ModelBuilder(ModelProvider<?> modelProvider, ResourceLocation modelPath)
    {
        this(modelProvider.folder, modelPath);
    }

    public final ResourceLocation getModelPath()
    {
        return ModelProvider.extendWith(modelPath, modelFolder);
    }

    public String getModelFolder()
    {
        return modelFolder;
    }

    @SuppressWarnings("unchecked")
    protected final <R extends Builder<R, T>> R child(String name, Function<T, R> factory)
    {
        return (R) children.computeIfAbsent(name, $ -> factory.apply(self));
    }

    // region: Textures
    public final T texture(TextureSlot slot, ResourceLocation texture)
    {
        Preconditions.checkNotNull(slot, "TextureSlot must not be null");
        Preconditions.checkNotNull(texture, "ResourceLocation must not be null");
        textures.put(slot, ModelProvider.extendWith(texture, modelFolder));
        return self;
    }

    public final ResourceLocation getTexture(TextureSlot slot)
    {
        return textures.get(slot);
    }
    // endregion

    // region: Parent
    public final T parent(String modId, String path)
    {
        Preconditions.checkNotNull(modId, "String modId must not be null");
        Preconditions.checkNotNull(path, "String path must not be null");
        return parent(new ResourceLocation(modId, path));
    }

    public final T parent(String path)
    {
        Preconditions.checkNotNull(path, "String must not be null");
        return parent(new ResourceLocation(path));
    }

    public final T parent(@Nullable ResourceLocation parent)
    {
        this.parent = parent;
        return self;
    }

    public final T parent(@Nullable ModelBuilder<?> parent)
    {
        return parent(parent == null ? null : parent.getModelPath());
    }

    public final Optional<ResourceLocation> getParent()
    {
        return Optional.ofNullable(parent);
    }
    // endregion

    // region: GuiLight
    public final T guiLight(@Nullable BlockModel.GuiLight guiLight)
    {
        this.guiLight = guiLight;
        return self;
    }

    public final Optional<BlockModel.GuiLight> getGuiLight()
    {
        return Optional.ofNullable(guiLight);
    }
    // endregion

    // region: Display
    public final DisplaysBuilder<T> displays()
    {
        return child("display", DisplaysBuilder::new);
    }

    public final DisplaysBuilder.DisplayBuilder<T> display(ItemTransforms.TransformType transformType)
    {
        Preconditions.checkNotNull(transformType, "TransformType must not be null");
        return displays().display(transformType);
    }
    // endregion

    @ApiStatus.Internal
    protected JsonObject toJson()
    {
        var root = new JsonObject();

        if(parent != null) root.addProperty("parent", parent.toString());
        if(guiLight != null) root.addProperty("gui_light", guiLight.name().toLowerCase(Locale.ROOT));

        children.forEach((name, child) -> addJson(root, name, child.toJson()));

        if(!textures.isEmpty())
        {
            var texturesJson = new JsonObject();
            textures.forEach((key, value) -> texturesJson.addProperty(key.getId(), value.toString()));
            addJson(root, "textures", texturesJson);
        }

        /*
        {
            "parent": "[<mod_id>:]<mod_type>/<path>",
            "gui_light": "{GuiLight}",
            "display": {
                ...
                "{TransformType}": {
                    "rotation": [ x, y, z ],
                    "translation": [ x, y, z ],
                    "scale": [ x, y, z ]
                }
                ...
            },
            "textures": {
                "<key>": "[<mod_id>:]<atlas_name>/<path>"
            },
            "elements": [
                ...
                {
                    "from": [ x, y, z ],
                    "to": [ x, y, z ],
                    "shade": {true|false},
                    "rotation": {
                        "origin": [ x, y, z ],
                        "axis": "{Direction.Axis}",
                        "angle": {-45|-22.5|0|22.5|45}
                    },
                    "faces": {
                        ...
                        "{Direction}": {
                            "uv": [ uMin, vMin, uMax, vMax ],
                            "texture": "{(#<ref>)}",
                            "texture": "{[<mod_id>:]<atlas_name>/<path>}",
                            "cullface": "{Direction}",
                            "rotation": {0|90|180|270},
                            "tintIndex": {-1|>=0}
                        }
                        ...
                    }
                }
                ...
            ],
            "overrides": [
                ...
                {
                    "predicate": {
                        "[<mod_id>:]<name>": {float_value}
                    },
                    "model": "[<mod_id>:]<mod_type>/<path>"
                }
                ...
            ]
        }
         */

        return root;
    }

    static void addJson(JsonObject root, String key, @Nullable JsonElement json)
    {
        if(isEmpty(json)) return;
        root.add(key, json);
    }

    static void addJson(JsonArray root, @Nullable JsonElement json)
    {
        if(isEmpty(json)) return;
        root.add(json);
    }

    static boolean isEmpty(@Nullable JsonElement json)
    {
        if(json == null || json.isJsonNull()) return true;
        else if(json.isJsonArray()) return json.getAsJsonArray().isEmpty();
        else if(json.isJsonObject()) return json.getAsJsonObject().size() == 0;
        return false;
    }

    @Nullable
    static JsonElement serializeVector(@Nullable Vector3f vector)
    {
        if(vector == null) return null;

        var json = new JsonArray();
        json.add(vector.x());
        json.add(vector.y());
        json.add(vector.z());
        return json;
    }

    public static abstract class Builder<B extends Builder<B, P>, P>
    {
        protected final P parent;
        @SuppressWarnings("unchecked") protected final B self = (B) this;

        @ApiStatus.Internal
        protected Builder(P parent)
        {
            this.parent = parent;
        }

        public final P end()
        {
            return parent;
        }

        @ApiStatus.Internal @Nullable protected abstract JsonElement toJson();
    }
}
