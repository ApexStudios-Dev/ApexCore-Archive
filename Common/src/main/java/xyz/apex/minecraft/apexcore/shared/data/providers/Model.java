package xyz.apex.minecraft.apexcore.shared.data.providers;

import com.mojang.math.Transformation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface Model<T extends Model.ModelBuilder<T>> extends DataProvider
{
    ResourceLocation extendWithFolder(ResourceLocation location);

    ResourceLocation removeFolder(ResourceLocation location);

    String extendWithFolder(String location);

    String removeFolder(String location);

    ResourceLocation modLocation(String location);

    default ResourceLocation mcLocation(String location)
    {
        return new ResourceLocation(location);
    }

    default T modelBuilder(String modelPath)
    {
        return modelBuilder(new ResourceLocation(modelPath));
    }

    default T modelBuilder(String namespace, String modelPath)
    {
        return modelBuilder(new ResourceLocation(namespace, modelPath));
    }

    T modelBuilder(ResourceLocation modelPath);

    default T modelBuilder(ModelFile model)
    {
        return modelBuilder(model.getLocation());
    }

    default T withExistingParent(String modelPath, String parentPath)
    {
        return withExistingParent(new ResourceLocation(modelPath), parentPath);
    }

    default T withExistingParent(String modelPath, String parentNamespace, String parentPath)
    {
        return withExistingParent(new ResourceLocation(modelPath), new ResourceLocation(parentNamespace, parentPath));
    }

    default T withExistingParent(String modelPath, ResourceLocation parentPath)
    {
        return withExistingParent(new ResourceLocation(modelPath), parentPath);
    }

    default T withExistingParent(String modelPath, ModelFile parent)
    {
        parent.assertExistence();
        return withExistingParent(new ResourceLocation(modelPath), parent.getLocation());
    }

    default T withExistingParent(ResourceLocation modelPath, String parentPath)
    {
        return withExistingParent(modelPath, new ResourceLocation(parentPath));
    }

    default T withExistingParent(ResourceLocation modelPath, String parentNamespace, String parentPath)
    {
        return withExistingParent(modelPath, new ResourceLocation(parentNamespace, parentPath));
    }

    default T withExistingParent(ResourceLocation modelPath, ResourceLocation parentPath)
    {
        return modelBuilder(modelPath).parent(parentPath);
    }

    default T withExistingParent(ResourceLocation modelPath, ModelFile parent)
    {
        parent.assertExistence();
        return withExistingParent(modelPath, parent.getLocation());
    }

    ModelFile existingModel(ResourceLocation modelPath);

    default ModelFile existingModel(String namespace, String modelPath)
    {
        return existingModel(new ResourceLocation(namespace, modelPath));
    }

    default ModelFile existingModel(String modelPath)
    {
        return existingModel(new ResourceLocation(modelPath));
    }

    static ResourceLocation extendWith(ResourceLocation location, CharSequence prefix, CharSequence... prefixes)
    {
        var path = location.getPath();

        if(StringUtils.startsWith(path, prefix)) return location;
        if(StringUtils.startsWithAny(path, prefixes)) return location;

        path = StringUtils.prependIfMissing(path, prefix, prefixes);
        return new ResourceLocation(location.getNamespace(), path);
    }

    static String extendWith(String path, CharSequence prefix, CharSequence... prefixes)
    {
        if(path.contains(":"))
        {
            var index = path.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);
            var namespace = ResourceLocation.DEFAULT_NAMESPACE;
            path = path.substring(index + 1);
            if(index >= 1) namespace = path.substring(0, index);
            path = StringUtils.prependIfMissing(path, prefix, prefixes);
            return "%s:%s".formatted(namespace, path);
        }

        return StringUtils.prependIfMissing(path, prefix, prefixes);
    }

    static ResourceLocation remove(ResourceLocation location, String prefix)
    {
        var path = location.getPath();
        if(StringUtils.startsWith(path, prefix)) return new ResourceLocation(location.getNamespace(), StringUtils.remove(path, prefix));
        return location;
    }

    static String remove(String path, String prefix)
    {
        if(path.contains(":"))
        {
            var index = path.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);
            var namespace = ResourceLocation.DEFAULT_NAMESPACE;
            path = path.substring(index + 1);
            if(index >= 1) namespace = path.substring(0, index);
            if(StringUtils.startsWith(path, prefix)) path = StringUtils.remove(path, prefix);
            return "%s:%s".formatted(namespace, path);
        }

        if(StringUtils.startsWith(path, prefix)) return StringUtils.remove(path, prefix);
        return path;
    }

    @ApiStatus.NonExtendable
    interface ItemModel<T extends ItemModel.ItemModelBuilder<T>> extends Model<T>
    {
        default T basicItem(Item item)
        {
            return basicItem(BuiltInRegistries.ITEM.getKey(item));
        }

        default T basicItem(ItemLike item)
        {
            return basicItem(item.asItem());
        }

        default T basicItem(Supplier<? extends ItemLike> item)
        {
            return basicItem(item.get());
        }

        default T basicItem(ResourceLocation registryName)
        {
            var modelName = extendWithFolder(registryName);
            return withExistingParent(modelName, mcLocation("item/generated")).texture(TextureSlot.LAYER0, modelName);
        }

        default T blockItem(Item item)
        {
            return blockItem(BuiltInRegistries.ITEM.getKey(item));
        }

        default T blockItem(ItemLike item)
        {
            return blockItem(item.asItem());
        }

        default T blockItem(Supplier<? extends ItemLike> item)
        {
            return blockItem(item.get());
        }

        default T blockItem(ResourceLocation registryName)
        {
            var modelName = extendWithFolder(registryName);
            return withExistingParent(modelName, extendWith(registryName, "block/"));
        }

        @ApiStatus.NonExtendable
        interface ItemModelBuilder<T extends ItemModelBuilder<T>> extends ModelBuilder<T>
        {
            OverrideBuilder<T> override();

            OverrideBuilder<T> override(int index);
        }

        @ApiStatus.NonExtendable
        interface OverrideBuilder<T extends ItemModelBuilder<T>>
        {
            default OverrideBuilder<T> model(ModelFile modelFile)
            {
                modelFile.assertExistence();
                return model(modelFile.getLocation());
            }

            OverrideBuilder<T> model(ResourceLocation modelPath);

            default OverrideBuilder<T> model(String namespace, String modelPath)
            {
                return model(new ResourceLocation(namespace, modelPath));
            }

            default OverrideBuilder<T> model(String modelPath)
            {
                return model(new ResourceLocation(modelPath));
            }

            OverrideBuilder<T> predicate(ResourceLocation key, float value);

            default OverrideBuilder<T> predicate(String namespace, String key, float value)
            {
                return predicate(new ResourceLocation(namespace, key), value);
            }

            default OverrideBuilder<T> predicate(String key, float value)
            {
                return predicate(new ResourceLocation(key), value);
            }

            T end();
        }
    }

    @ApiStatus.NonExtendable
    interface BlockModel<T extends BlockModel.BlockModelBuilder<T>> extends Model<T>
    {
        default T cubeAll(Block block)
        {
            return cubeAll(BuiltInRegistries.BLOCK.getKey(block));
        }

        default T cubeAll(Supplier<? extends Block> block)
        {
            return cubeAll(block.get());
        }

        default T cubeAll(ResourceLocation registryName)
        {
            var modelName = extendWithFolder(registryName);
            return withExistingParent(modelName, mcLocation("block/cube_all"))
                    .texture(TextureSlot.ALL, modelName)
                    .texture(TextureSlot.PARTICLE, modelName)
            ;
        }

        @ApiStatus.NonExtendable
        interface BlockModelBuilder<T extends BlockModelBuilder<T>> extends ModelBuilder<T>
        {
            RootTransformBuilder<T> rootTransform();
        }

        @ApiStatus.NonExtendable
        interface RootTransformBuilder<T extends BlockModelBuilder<T>>
        {
            default RootTransformBuilder<T> translation(Vector3f translation)
            {
                return translation(translation.x(), translation.y(), translation.z());
            }

            RootTransformBuilder<T> translation(float x, float y, float z);

            RootTransformBuilder<T> rotation(Quaternionf rotation);

            default RootTransformBuilder<T> rotation(Vector3f rotation)
            {
                return rotation(rotation.x(), rotation.y(), rotation.z(), true);
            }

            RootTransformBuilder<T> rotation(float x, float y, float z, boolean isDegrees);

            default RootTransformBuilder<T> leftRotation(Quaternionf rotation)
            {
                return rotation(rotation);
            }

            default RootTransformBuilder<T> leftRotation(Vector3f rotation)
            {
                return leftRotation(rotation.x(), rotation.y(), rotation.z(), true);
            }

            default RootTransformBuilder<T> leftRotation(float x, float y, float z, boolean isDegrees)
            {
                return rotation(x, y, z, isDegrees);
            }

            RootTransformBuilder<T> rightRotation(Quaternionf rotation);

            default RootTransformBuilder<T> rightRotation(Vector3f rotation)
            {
                return rightRotation(rotation.x(), rotation.y(), rotation.z(), true);
            }

            RootTransformBuilder<T> rightRotation(float x, float y, float z, boolean isDegrees);

            default RootTransformBuilder<T> postRotation(Quaternionf rotation)
            {
                return rightRotation(rotation);
            }

            default RootTransformBuilder<T> postRotation(Vector3f rotation)
            {
                return postRotation(rotation.x(), rotation.y(), rotation.z(), true);
            }

            default RootTransformBuilder<T> postRotation(float x, float y, float z, boolean isDegrees)
            {
                return rightRotation(x, y, z, isDegrees);
            }

            default RootTransformBuilder<T> scale(float scale)
            {
                return scale(scale, scale, scale);
            }

            RootTransformBuilder<T> scale(float x, float y, float z);

            default RootTransformBuilder<T> scale(Vector3f scale)
            {
                return scale(scale.x(), scale.y(), scale.z());
            }

            default RootTransformBuilder<T> transform(Transformation transformation)
            {
                return translation(transformation.getTranslation()).leftRotation(transformation.getLeftRotation()).rightRotation(transformation.getRightRotation()).scale(transformation.getScale());
            }

            RootTransformBuilder<T> origin(Vector3f origin);

            RootTransformBuilder<T> origin(TransformOrigin origin);

            T end();
        }

        enum TransformOrigin implements StringRepresentable
        {
            CENTER(new Vector3f(.5F, .5F, .5F), "center"),
            CORNER(new Vector3f(), "corner"),
            OPPOSING_CORNER(new Vector3f(1F, 1F, 1F), "opposing-corner");

            private final Vector3f vec;
            private final String name;

            TransformOrigin(Vector3f vec, String name)
            {
                this.vec = vec;
                this.name = name;
            }

            public Vector3f getVector()
            {
                return vec;
            }

            @Override
            public String getSerializedName()
            {
                return name;
            }
        }
    }

    @ApiStatus.NonExtendable
    interface ModelBuilder<T extends ModelBuilder<T>> extends ModelFile
    {
        default T parent(ModelFile parent)
        {
            parent.assertExistence();
            return parent(parent.getLocation());
        }

        T parent(ResourceLocation modelPath);

        default T parent(String namespace, String modelPath)
        {
            return parent(new ResourceLocation(namespace, modelPath));
        }

        default T parent(String modelPath)
        {
            return parent(new ResourceLocation(modelPath));
        }

        default T texture(String key, ResourceLocation texture)
        {
            return texture(key, texture.toString());
        }

        default T texture(String key, String namespace, String texture)
        {
            return texture(key, "%s:%s".formatted(namespace, texture));
        }

        T texture(String key, String texture);

        default T texture(TextureSlot key, ResourceLocation texture)
        {
            return texture(key.getId(), texture.toString());
        }

        default T texture(TextureSlot key, String namespace, String texture)
        {
            return texture(key.getId(), "%s:%s".formatted(namespace, texture));
        }

        default T texture(TextureSlot key, String texture)
        {
            return texture(key.getId(), texture);
        }

        default T texture(TextureSlot key, TextureSlot texture)
        {
            return texture(key.getId(), texture.toString());
        }

        T renderType(ResourceLocation renderType);

        default T renderType(String namespace, String renderType)
        {
            return renderType(new ResourceLocation(namespace, renderType));
        }

        default T renderType(String renderType)
        {
            return renderType(new ResourceLocation(renderType));
        }

        TransformsBuilder<T> transforms();

        T ao(boolean ao);

        default T ao()
        {
            return ao(true);
        }

        default T noAO()
        {
            return ao(false);
        }

        T guiLight(net.minecraft.client.renderer.block.model.BlockModel.GuiLight guiLight);

        default T frontLight()
        {
            return guiLight(net.minecraft.client.renderer.block.model.BlockModel.GuiLight.FRONT);
        }

        default T sideLight()
        {
            return guiLight(net.minecraft.client.renderer.block.model.BlockModel.GuiLight.SIDE);
        }

        ElementBuilder<T> element();

        ElementBuilder<T> element(int index);
    }

    @ApiStatus.NonExtendable
    interface ElementBuilder<T extends ModelBuilder<T>>
    {
        default ElementBuilder<T> from(Vector3f from)
        {
            return from(from.x(), from.y(), from.z());
        }

        ElementBuilder<T> from(float x, float y, float z);

        default ElementBuilder<T> to(Vector3f to)
        {
            return from(to.x(), to.y(), to.z());
        }

        ElementBuilder<T> to(float x, float y, float z);

        FaceBuilder<T> face(Direction face);

        RotationBuilder<T> rotation();

        ElementBuilder<T> shade(boolean shade);

        default ElementBuilder<T> shade()
        {
            return shade(true);
        }

        default ElementBuilder<T> noShade()
        {
            return shade(false);
        }

        default ElementBuilder<T> allFaces(BiConsumer<Direction, FaceBuilder<T>> action)
        {
            Arrays.stream(Direction.values()).forEach(face -> action.accept(face, face(face)));
            return this;
        }

        ElementBuilder<T> faces(BiConsumer<Direction, FaceBuilder<T>> action);

        default ElementBuilder<T> textureAll(ResourceLocation texture)
        {
            return textureAll(texture.toString());
        }

        default ElementBuilder<T> textureAll(String namespace, String texture)
        {
            return textureAll("%s:%s".formatted(namespace, texture));
        }

        default ElementBuilder<T> textureAll(String texture)
        {
            return allFaces((face, builder) -> builder.texture(texture));
        }

        default ElementBuilder<T> textureAll(TextureSlot textureSlot)
        {
            return textureAll(textureSlot.toString());
        }

        default ElementBuilder<T> texture(ResourceLocation texture)
        {
            return texture(texture.toString());
        }

        default ElementBuilder<T> texture(String namespace, String texture)
        {
            return texture("%s:%s".formatted(namespace, texture));
        }

        default ElementBuilder<T> texture(String texture)
        {
            return faces((face, builder) -> builder.texture(texture));
        }

        default ElementBuilder<T> texture(TextureSlot textureSlot)
        {
            return texture(textureSlot.toString());
        }

        default ElementBuilder<T> cube(ResourceLocation texture)
        {
            return cube(texture.toString());
        }

        default ElementBuilder<T> cube(String namespace, String texture)
        {
            return cube("%s:%s".formatted(namespace, texture));
        }

        default ElementBuilder<T> cube(String texture)
        {
            return allFaces((face, builder) -> builder.texture(texture).cullFace(face));
        }

        default ElementBuilder<T> cube(TextureSlot textureSlot)
        {
            return cube(textureSlot.toString());
        }

        T end();
    }

    @ApiStatus.NonExtendable
    interface FaceBuilder<T extends ModelBuilder<T>>
    {
        FaceBuilder<T> cullFace(@Nullable Direction face);

        FaceBuilder<T> tintIndex(int tintIndex);

        default FaceBuilder<T> texture(ResourceLocation texture)
        {
            return texture(texture.toString());
        }

        default FaceBuilder<T> texture(String namespace, String texture)
        {
            return texture("%s:%s".formatted(namespace, texture));
        }

        FaceBuilder<T> texture(String texture);

        default FaceBuilder<T> texture(TextureSlot textureSlot)
        {
            return texture(textureSlot.toString());
        }

        FaceBuilder<T> uvs(float minU, float minV, float maxU, float maxV);

        FaceBuilder<T> rotation(FaceRotation rotation);

        FaceBuilder<T> emissivity(int emissivity);

        default FaceBuilder<T> emissive()
        {
            return emissivity(15);
        }

        FaceBuilder<T> ao(boolean ao);

        default FaceBuilder<T> ao()
        {
            return ao(true);
        }

        default FaceBuilder<T> noAO()
        {
            return ao(false);
        }

        ElementBuilder<T> end();
    }

    @ApiStatus.NonExtendable
    interface RotationBuilder<T extends ModelBuilder<T>>
    {
        default RotationBuilder<T> origin(Vector3f origin)
        {
            return origin(origin.x(), origin.y(), origin.z());
        }

        RotationBuilder<T> origin(float x, float y, float z);

        RotationBuilder<T> axis(Direction.Axis axis);

        RotationBuilder<T> angle(float angle);

        RotationBuilder<T> rescale(boolean rescale);

        default RotationBuilder<T> rescale()
        {
            return rescale(true);
        }

        default RotationBuilder<T> noRescale()
        {
            return rescale(false);
        }

        ElementBuilder<T> end();
    }

    @ApiStatus.NonExtendable
    interface TransformsBuilder<T extends ModelBuilder<T>>
    {
        TransformVecBuilder<T> transform(ItemTransforms.TransformType transformType);

        default TransformVecBuilder<T> thirdPersonLeftHand()
        {
            return transform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
        }

        default TransformVecBuilder<T> thirdPersonRightHand()
        {
            return transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        }

        default TransformVecBuilder<T> firstPersonLeftHand()
        {
            return transform(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND);
        }

        default TransformVecBuilder<T> firstPersonRightHand()
        {
            return transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
        }

        default TransformVecBuilder<T> head()
        {
            return transform(ItemTransforms.TransformType.HEAD);
        }

        default TransformVecBuilder<T> gui()
        {
            return transform(ItemTransforms.TransformType.GUI);
        }

        default TransformVecBuilder<T> ground()
        {
            return transform(ItemTransforms.TransformType.GROUND);
        }

        default TransformVecBuilder<T> fixed()
        {
            return transform(ItemTransforms.TransformType.FIXED);
        }

        T end();
    }

    @ApiStatus.NonExtendable
    interface TransformVecBuilder<T extends ModelBuilder<T>>
    {
        default TransformVecBuilder<T> rotation(Vector3f rotation)
        {
            return rotation(rotation.x(), rotation.y(), rotation.z());
        }

        TransformVecBuilder<T> rotation(float x, float y, float z);

        default TransformVecBuilder<T> leftRotation(Vector3f leftRotation)
        {
            return leftRotation(leftRotation.x(), leftRotation.y(), leftRotation.z());
        }

        default TransformVecBuilder<T> leftRotation(float x, float y, float z)
        {
            return rotation(x, y, z);
        }

        default TransformVecBuilder<T> translation(Vector3f translation)
        {
            return translation(translation.x(), translation.y(), translation.z());
        }

        TransformVecBuilder<T> translation(float x, float y, float z);

        default TransformVecBuilder<T> scale(Vector3f scale)
        {
            return scale(scale.x(), scale.y(), scale.z());
        }

        TransformVecBuilder<T> scale(float x, float y, float z);

        default TransformVecBuilder<T> scale(float scale)
        {
            return scale(scale, scale, scale);
        }

        default TransformVecBuilder<T> rightRotation(Vector3f rightRotation)
        {
            return rightRotation(rightRotation.x(), rightRotation.y(), rightRotation.z());
        }

        TransformVecBuilder<T> rightRotation(float x, float y, float z);

        TransformsBuilder<T> end();
    }

    @ApiStatus.NonExtendable
    interface ModelFile
    {
        ResourceLocation getLocation();

        ResourceLocation getUncheckedLocation();

        void assertExistence();
    }

    enum FaceRotation
    {
        ZERO(0),
        CLOCKWISE_90(90),
        UPSIDE_DOWN(180),
        COUNTERCLOCKWISE_90(270);

        public final int rotation;

        FaceRotation(int rotation)
        {
            this.rotation = rotation;
        }
    }
}
