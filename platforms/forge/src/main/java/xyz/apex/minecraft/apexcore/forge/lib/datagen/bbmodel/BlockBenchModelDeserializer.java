package xyz.apex.minecraft.apexcore.forge.lib.datagen.bbmodel;

import com.google.gson.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Math;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * BlockBench Model Deserializer.
 * <p>
 * Deserializes BBModels into Forges Model builder system.
 */
@ApiStatus.Internal
public final class BlockBenchModelDeserializer
{
    /**
     * False to not validate that referenced models exist.
     * <p>
     * Essentially swaps out any {@link ModelFile.ExistingModelFile} for {@link ModelFile.UncheckedModelFile}.
     * <p>
     * Set in constructor of your {@link BlockBenchModelConverter}.
     */
    public static boolean VALIDATE_REF_MODELS_EXIST = true;

    /**
     * False to not parse textures during deserialization.
     * <p>
     * Forge validates texture existence when one is set,
     * this can be troublesome when you want validation enabled
     * but do not currently have textures.
     * <p>
     * Setting this to false will tell the deserializer to not parse textures, meaning
     * generated BBModels will not have any textures associated with them,
     * best to call {@link ModelBuilder#texture(String, ResourceLocation)} while processing your models,
     * to apply textures to them.
     * <p>
     * Set in constructor of your {@link BlockBenchModelConverter}.
     */
    public static boolean PARSE_TEXTURES = true;

    private static final String EMISSIVITY_ELEMENT_TAG = "forge-emissivity";
    private static final char EMISSIVITY_EQ_TAG = '=';
    private static final int EMISSIVITY_INVALID = -1;
    private static final int EMISSIVITY_MIN = 0;
    private static final int EMISSIVITY_MAX = 15;
    private static final int EMISSIVITY_DEFAULT = EMISSIVITY_MAX;

    private static final Gson GSON = new GsonBuilder()
            // .registerTypeAdapter(BlockModel.class, new ExtendedBlockModelDeserializer())
            .registerTypeAdapter(BlockElement.class, new BlockElement.Deserializer())
            .registerTypeAdapter(BlockElementFace.class, new BlockElementFace.Deserializer())
            .registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer())
            .registerTypeAdapter(ItemTransform.class, new ItemTransform.Deserializer())
            .registerTypeAdapter(ItemTransforms.class, new ItemTransforms.Deserializer())
            .registerTypeAdapter(ItemOverride.class, new ItemOverride.Deserializer())
            // .registerTypeAdapter(Transformation.class, new TransformationHelper.Deserializer())
            .create();

    private static final String JSON_OVERRIDES = "overrides";
    private static final String JSON_AMBIENT_OCCLUSION = "ambientocclusion";
    private static final String JSON_PARENT = "parent";
    private static final String JSON_RENDER_TYPE = "render_type";
    private static final String JSON_GUI_TYPE = "gui_type";
    private static final String JSON_ELEMENTS = "elements";
    private static final String JSON_DISPLAY = "display";
    private static final String JSON_TEXTURES = "textures";
    private static final String JSON_NAME = "name";

    /**
     * Deserializes and parses BBModel into a BlockModelBuilder.
     *
     * @param modelPath          Output BlockModel path.
     * @param filePath           Input BBModel path.
     * @param existingFileHelper Forge ExistingFileHelper.
     * @return Deserialized BBModel as BlockModelBuilder.
     */
    public static BlockModelBuilder blockModelBuilder(ResourceLocation modelPath, Path filePath, ExistingFileHelper existingFileHelper)
    {
        var root = deserialize(filePath);
        return builder(root, modelPath, existingFileHelper, new BlockModelBuilder(modelPath, existingFileHelper));
    }

    /**
     * Deserializes and parses BBModel into a ItemModelBuilder.
     *
     * @param modelPath          Output ItemModel path.
     * @param filePath           Input BBModel path.
     * @param existingFileHelper Forge ExistingFileHelper.
     * @return Deserialized BBModel as ItemModelBuilder.
     */
    public static ItemModelBuilder itemModelBuilder(ResourceLocation modelPath, Path filePath, ExistingFileHelper existingFileHelper)
    {
        var root = deserialize(filePath);
        var builder = builder(root, modelPath, existingFileHelper, new ItemModelBuilder(modelPath, existingFileHelper));

        if(root.has(JSON_OVERRIDES))
        {
            for(var jsonElement : GsonHelper.getAsJsonArray(root, JSON_OVERRIDES))
            {
                var itemOverride = GSON.getAdapter(ItemOverride.class).fromJsonTree(jsonElement);

                itemOverride.getPredicates().forEach(predicate -> builder
                        .override()
                        .model(getModel(itemOverride.getModel(), existingFileHelper, VALIDATE_REF_MODELS_EXIST))
                        .predicate(predicate.getProperty(), predicate.getValue())
                .end());
            }
        }

        return builder;
    }

    /**
     * Deserializes and parses BBModel into a generic ModelBuilder.
     *
     * @param root               BBModel json data.
     * @param modelPath          Output Model path.
     * @param existingFileHelper Forge ExistingFileHelper.
     * @param builder            Output Model builder.
     * @param <T>                Model builder type.
     * @return Deserialized BBModel as BlockModelBuilder.
     */
    @SuppressWarnings("ConstantValue")
    public static <T extends ModelBuilder<T>> T builder(JsonObject root, ResourceLocation modelPath, ExistingFileHelper existingFileHelper, T builder)
    {
        ApexCore.LOGGER.info(BlockBenchModelConverter.MARKER, "Loading BlockBench Model '{}'", modelPath);
        builder.ao(GsonHelper.getAsBoolean(root, JSON_AMBIENT_OCCLUSION, true));

        if(root.has(JSON_PARENT))
        {
            var parentPath = new ResourceLocation(GsonHelper.getAsString(root, JSON_PARENT));
            builder.parent(getModel(parentPath, existingFileHelper, VALIDATE_REF_MODELS_EXIST));
        }

        if(root.has(JSON_RENDER_TYPE))
        {
            var renderType = new ResourceLocation(GsonHelper.getAsString(root, JSON_RENDER_TYPE));
            builder.renderType(renderType);
        }

        if(root.has(JSON_GUI_TYPE))
        {
            var guiLight = BlockModel.GuiLight.getByName(GsonHelper.getAsString(root, JSON_GUI_TYPE));
            builder.guiLight(guiLight);
        }

        if(root.has(JSON_ELEMENTS))
        {
            for(var jsonElement : GsonHelper.getAsJsonArray(root, JSON_ELEMENTS))
            {
                var element = GSON.getAdapter(BlockElement.class).fromJsonTree(jsonElement);

                var elementBuilder = builder.element()
                        .from(element.from.x(), element.from.y(), element.from.z())
                        .to(element.to.x(), element.to.y(), element.to.z())
                .shade(element.shade);

                if(element.rotation != null)
                    elementRotation(elementBuilder, element.rotation);

                var emissivity = parseEmissivityFromName(jsonElement);

                element.faces.forEach((face, elementFace) -> elementBuilder
                        .face(face)
                            .cullface(elementFace.cullForDirection)
                            .tintindex(elementFace.tintIndex)
                            .texture(elementFace.texture)
                            .uvs(elementFace.uv.uvs[0], elementFace.uv.uvs[1], elementFace.uv.uvs[2], elementFace.uv.uvs[3])
                            .rotation(getRotation(elementFace.uv.rotation))
                            .emissivity(emissivity != -1 ? emissivity : 0, emissivity != -1 ? emissivity : 0)
                        .end()
                );
            }
        }

        if(root.has(JSON_DISPLAY))
        {
            var itemTransforms = GSON.getAdapter(ItemTransforms.class).fromJsonTree(GsonHelper.getAsJsonObject(root, JSON_DISPLAY));
            var transformsBuilder = builder.transforms();

            transform(transformsBuilder, itemTransforms.thirdPersonLeftHand, ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
            transform(transformsBuilder, itemTransforms.thirdPersonRightHand, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
            transform(transformsBuilder, itemTransforms.firstPersonLeftHand, ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
            transform(transformsBuilder, itemTransforms.firstPersonRightHand, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
            transform(transformsBuilder, itemTransforms.head, ItemDisplayContext.HEAD);
            transform(transformsBuilder, itemTransforms.gui, ItemDisplayContext.GUI);
            transform(transformsBuilder, itemTransforms.ground, ItemDisplayContext.GROUND);
            transform(transformsBuilder, itemTransforms.fixed, ItemDisplayContext.FIXED);

            itemTransforms.moddedTransforms.forEach((key, value) -> transform(transformsBuilder, value, key));
        }

        if(PARSE_TEXTURES && root.has(JSON_TEXTURES))
        {
            var texturesJson = GsonHelper.getAsJsonObject(root, JSON_TEXTURES);
            texturesJson.keySet().forEach(key -> builder.texture(key, GsonHelper.getAsString(texturesJson, key)));
        }

        // existingFileHelper.trackGenerated(modelPath, PackType.CLIENT_RESOURCES, ".json", "models");
        return builder;
    }

    private static JsonObject deserialize(Path filePath)
    {
        try
        {
            try(var reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8))
            {
                var json = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                Validate.notNull(json);
                return json;
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static <T extends ModelBuilder<T>> void elementRotation(ModelBuilder<T>.ElementBuilder elementBuilder, BlockElementRotation rotation)
    {
        // origin is multiplied by 0.0625 during deserialization
        // we divide by same value here to get back the raw values
        var rotationOrigin = rotation.origin();
        var oX = rotationOrigin.x / .0625F;
        var oY = rotationOrigin.y / .0625F;
        var oZ = rotationOrigin.z / .0625F;

        elementBuilder.rotation()
                      .angle(rotation.angle())
                      .axis(rotation.axis())
                      .origin(oX, oY, oZ)
                      .rescale(rotation.rescale())
      .end();
    }

    private static <T extends ModelBuilder<T>> void transform(ModelBuilder<T>.TransformsBuilder builder, ItemTransform transform, ItemDisplayContext displayContext)
    {
        // translation is multiplied by 0.0625 during deserialization
        // we divide by same value here to get back the raw values
        var tX = transform.translation.x() / .0625F;
        var tY = transform.translation.y() / .0625F;
        var tZ = transform.translation.z() / .0625F;

        builder.transform(displayContext)
               .translation(tX, tY, tZ)
               .scale(transform.scale.x(), transform.scale.y(), transform.scale.z())
               .leftRotation(transform.rotation.x(), transform.rotation.y(), transform.rotation.z())
               .rightRotation(transform.rightRotation.x(), transform.rightRotation.y(), transform.rightRotation.z())
       .end();
    }

    private static ModelBuilder.FaceRotation getRotation(int rotation)
    {
        return switch(rotation) {
            case 0 -> ModelBuilder.FaceRotation.ZERO;
            case 90 -> ModelBuilder.FaceRotation.CLOCKWISE_90;
            case 180 -> ModelBuilder.FaceRotation.UPSIDE_DOWN;
            case 270 -> ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90;
            default -> throw new JsonParseException("Unknown Block Face Rotation value: '%d'".formatted(rotation));
        };
    }

    private static ModelFile getModel(ResourceLocation modelPath, ExistingFileHelper existingFileHelper, boolean validateExists)
    {
        if(validateExists)
        {
            var model = new ModelFile.ExistingModelFile(modelPath, existingFileHelper);
            model.assertExistence();
            return model;
        }

        return new ModelFile.UncheckedModelFile(modelPath);
    }

    private static int parseEmissivityFromName(JsonElement element)
    {
        if(!element.isJsonObject())
            return EMISSIVITY_INVALID;

        var obj = element.getAsJsonObject();

        if(!obj.has(JSON_NAME))
            return EMISSIVITY_INVALID;

        var name = GsonHelper.getAsString(obj, JSON_NAME);
        var index = StringUtils.indexOfIgnoreCase(name, EMISSIVITY_ELEMENT_TAG);

        if(index <= EMISSIVITY_INVALID)
            return EMISSIVITY_INVALID;
        // anything above here ^^^ is invalid for obtaining emissivity value, return invalid value
        // check for custom value after the 'forge-emissivity' tag
        var eqIndex = index + EMISSIVITY_ELEMENT_TAG.length();

        // custom value specified
        if(name.charAt(eqIndex) == EMISSIVITY_EQ_TAG)
        {
            // extract out all numbers after the '='
            var num = new StringBuilder();

            for(var i = eqIndex + 1; i < name.length(); i++) // keep going to end of string
            {
                var c = name.charAt(i);

                if(!Character.isDigit(c))
                    break; // stop once we hit anything that is not a digit

                num.append(c);
            }

            try
            {
                // try parse the string into a integer
                return Math.clamp(Integer.parseInt(num.toString()), EMISSIVITY_MIN, EMISSIVITY_MAX);
            }
            catch(NumberFormatException ignored)
            {
            }
        }

        return EMISSIVITY_DEFAULT; // 'forge-emissivity' was defined but no '=<num>' was found, use full bright
    }
}
