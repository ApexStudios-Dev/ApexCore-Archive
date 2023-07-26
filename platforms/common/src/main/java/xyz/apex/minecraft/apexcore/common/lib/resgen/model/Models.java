package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface Models
{
    ModelFile BLOCK_CUBE = new ModelFile("block/cube");
    ModelFile BLOCK_CUBE_ALL = new ModelFile("block/cube_all");
    ModelFile BLOCK_CUBE_BOTTOM_TOP = new ModelFile("block/cube_bottom_top");
    ModelFile BLOCK_CUBE_TOP = new ModelFile("block/cube_top");
    ModelFile ITEM_GENERATED = new ModelFile("item/generated");
    ModelFile ITEM_HAND_HELD = new ModelFile("item/cube_top");

    private ModelProvider provider()
    {
        return (ModelProvider) this;
    }

    ModelBuilder getBuilder(ResourceLocation modelPath);

    default ModelBuilder withParent(ResourceLocation modelPath, ModelFile parent)
    {
        return getBuilder(modelPath).parent(parent);
    }

    default ModelBuilder withParent(ResourceLocation modelPath, ResourceLocation parent)
    {
        return getBuilder(modelPath).parent(parent);
    }

    default ModelBuilder withParent(ResourceLocation modelPath, String parent)
    {
        return getBuilder(modelPath).parent(parent);
    }

    default ModelBuilder cube(ResourceLocation modelPath, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west)
    {
        return withParent(modelPath, BLOCK_CUBE)
                .texture("down", down)
                .texture("up", up)
                .texture("north", north)
                .texture("south", south)
                .texture("east", east)
                .texture("west", west);
    }

    default ModelBuilder cubeAll(ResourceLocation modelPath, ResourceLocation texture)
    {
        return withParent(modelPath, BLOCK_CUBE_ALL).texture("all", texture);
    }

    default ModelBuilder cubeBottomTop(ResourceLocation modelPath, ResourceLocation top, ResourceLocation bottom, ResourceLocation side)
    {
        return withParent(modelPath, BLOCK_CUBE_BOTTOM_TOP)
                .texture("top", top)
                .texture("bottom", bottom)
                .texture("side", side);
    }

    default ModelBuilder cubeTop(ResourceLocation modelPath, ResourceLocation top, ResourceLocation side)
    {
        return withParent(modelPath, BLOCK_CUBE_TOP)
                .texture("top", top)
                .texture("side", side);
    }

    default ModelBuilder generated(ResourceLocation modelPath, ResourceLocation texture)
    {
        return withParent(modelPath, ITEM_GENERATED)
                .texture("layer0", texture);
    }

    default ModelBuilder handHeld(ResourceLocation modelPath, ResourceLocation texture)
    {
        return withParent(modelPath, ITEM_HAND_HELD)
                .texture("layer0", texture);
    }

    default ModelFile existingModel(ResourceLocation modelPath)
    {
        return new ModelFile(modelPath);
    }
}
