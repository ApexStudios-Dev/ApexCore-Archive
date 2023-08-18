package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface Models
{
    ResourceLocation BUILTIN_ENTITY = new ResourceLocation("builtin/entity");
    ResourceLocation BLOCK_CARPET = new ResourceLocation("block/carpet");
    ResourceLocation BLOCK_CUBE = new ResourceLocation("block/cube");
    ResourceLocation BLOCK_CUBE_ALL = new ResourceLocation("block/cube_all");
    ResourceLocation BLOCK_ORIENTABLE = new ResourceLocation("block/orientable");
    ResourceLocation BLOCK_ORIENTABLE_WITH_BOTTOM = new ResourceLocation("block/orientable_with_bottom");
    ResourceLocation ITEM_GENERATED = new ResourceLocation("item/generated");
    ResourceLocation ITEM_TEMPLATE_SPAWN_EGG = new ResourceLocation("item/template_spawn_egg");
    ResourceLocation ITEM_HAND_HELD = new ResourceLocation("item/handheld");
    ResourceLocation ITEM_HAND_HELD_ROD = new ResourceLocation("item/handheld_rod");

    private ModelProvider provider()
    {
        return (ModelProvider) this;
    }

    ModelBuilder getBuilder(ResourceLocation modelPath);

    default ModelBuilder getBuilder(Block block)
    {
        return getBuilder(getModelPath(block));
    }

    default ModelBuilder getBuilder(Item item)
    {
        return getBuilder(getModelPath(item));
    }

    default ResourceLocation registryName(Block block)
    {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    default ResourceLocation registryName(Item item)
    {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    default ResourceLocation getTexturePath(Block block, String suffix)
    {
        return TextureMapping.getBlockTexture(block, suffix);
    }

    default ResourceLocation getTexturePath(Block block)
    {
        return TextureMapping.getBlockTexture(block);
    }

    default ResourceLocation getTexturePath(Item item, String suffix)
    {
        return TextureMapping.getItemTexture(item, suffix);
    }

    default ResourceLocation getTexturePath(Item item)
    {
        return TextureMapping.getItemTexture(item);
    }

    default ResourceLocation getModelPath(Block block, String suffix)
    {
        return ModelLocationUtils.getModelLocation(block, suffix);
    }

    default ResourceLocation getModelPath(Block block)
    {
        return ModelLocationUtils.getModelLocation(block);
    }

    default ResourceLocation getModelPath(Item item, String suffix)
    {
        return ModelLocationUtils.getModelLocation(item, suffix);
    }

    default ResourceLocation getModelPath(Item item)
    {
        return ModelLocationUtils.getModelLocation(item);
    }

    default ModelBuilder withParent(ResourceLocation modelPath, ResourceLocation parent)
    {
        return getBuilder(modelPath).parent(parent);
    }

    default ModelBuilder withParent(ResourceLocation modelPath, String parent)
    {
        return withParent(modelPath, new ResourceLocation(parent));
    }

    default ModelBuilder withParent(ResourceLocation modelPath, ModelBuilder parent)
    {
        return withParent(modelPath, parent.modelPath());
    }

    default ModelBuilder withParent(Block block, ResourceLocation parent)
    {
        return withParent(getModelPath(block), parent);
    }

    default ModelBuilder withParent(Block block, String parent)
    {
        return withParent(getModelPath(block), new ResourceLocation(parent));
    }

    default ModelBuilder withParent(Block block, ModelBuilder model)
    {
        return withParent(getModelPath(block), model.modelPath());
    }

    default ModelBuilder withParent(Item item, ResourceLocation parent)
    {
        return withParent(getModelPath(item), parent);
    }

    default ModelBuilder withParent(Item item, String parent)
    {
        return withParent(getModelPath(item), new ResourceLocation(parent));
    }

    default ModelBuilder withParent(Item item, ModelBuilder model)
    {
        return withParent(getModelPath(item), model.modelPath());
    }

    default ModelBuilder entity(ResourceLocation modelPath)
    {
        return withParent(modelPath, BUILTIN_ENTITY);
    }

    default ModelBuilder entity(Block block)
    {
        return entity(getModelPath(block));
    }

    default ModelBuilder entity(Item item)
    {
        return entity(getModelPath(item));
    }

    default ModelBuilder spawnEgg(ResourceLocation modelPath)
    {
        return withParent(modelPath, ITEM_TEMPLATE_SPAWN_EGG);
    }

    default ModelBuilder spawnEgg(Block block)
    {
        return spawnEgg(getModelPath(block));
    }

    default ModelBuilder spawnEgg(Item item)
    {
        return spawnEgg(getModelPath(item));
    }

    default ModelBuilder blockItem(Block block)
    {
        return blockItem(block.asItem(), block);
    }

    default ModelBuilder blockItem(Item item, Block parent)
    {
        return withParent(item, getModelPath(parent));
    }

    default ModelBuilder blockItem(Item item)
    {
        return withParent(item, registryName(item).withPrefix("block/"));
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

    default ModelBuilder cube(ResourceLocation modelPath, String down, String up, String north, String south, String east, String west)
    {
        return cube(modelPath, new ResourceLocation(down), new ResourceLocation(up), new ResourceLocation(north), new ResourceLocation(south), new ResourceLocation(east), new ResourceLocation(west));
    }

    default ModelBuilder cube(Block block, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west)
    {
        return cube(getModelPath(block), down, up, north, south, east, west);
    }

    default ModelBuilder cube(Block block, String down, String up, String north, String south, String east, String west)
    {
        return cube(getModelPath(block), new ResourceLocation(down), new ResourceLocation(up), new ResourceLocation(north), new ResourceLocation(south), new ResourceLocation(east), new ResourceLocation(west));
    }

    default ModelBuilder cube(Item item, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west)
    {
        return cube(getModelPath(item), down, up, north, south, east, west);
    }

    default ModelBuilder cube(Item item, String down, String up, String north, String south, String east, String west)
    {
        return cube(getModelPath(item), new ResourceLocation(down), new ResourceLocation(up), new ResourceLocation(north), new ResourceLocation(south), new ResourceLocation(east), new ResourceLocation(west));
    }

    default ModelBuilder cubeAll(ResourceLocation modelPath, ResourceLocation texture)
    {
        return withParent(modelPath, BLOCK_CUBE_ALL).texture("all", texture);
    }

    default ModelBuilder cubeAll(ResourceLocation modelPath, String texture)
    {
        return cubeAll(modelPath, new ResourceLocation(texture));
    }

    default ModelBuilder cubeAll(Block block, ResourceLocation texture)
    {
        return cubeAll(getModelPath(block), texture);
    }

    default ModelBuilder cubeAll(Block block, String texture)
    {
        return cubeAll(getModelPath(block), new ResourceLocation(texture));
    }

    default ModelBuilder cubeAll(Item item, ResourceLocation texture)
    {
        return cubeAll(getModelPath(item), texture);
    }

    default ModelBuilder cubeAll(Item item, String texture)
    {
        return cubeAll(getModelPath(item), new ResourceLocation(texture));
    }

    default ModelBuilder carpet(ResourceLocation modelPath, ResourceLocation texture)
    {
        return withParent(modelPath, BLOCK_CARPET).texture("wool", texture);
    }

    default ModelBuilder carpet(ResourceLocation modelPath, String texture)
    {
        return carpet(modelPath, new ResourceLocation(texture));
    }

    default ModelBuilder carpet(Block block, ResourceLocation texture)
    {
        return carpet(getModelPath(block), texture);
    }

    default ModelBuilder carpet(Block block, String texture)
    {
        return carpet(getModelPath(block), new ResourceLocation(texture));
    }

    default ModelBuilder carpet(Item item, ResourceLocation texture)
    {
        return carpet(getModelPath(item), texture);
    }

    default ModelBuilder carpet(Item item, String texture)
    {
        return carpet(getModelPath(item), new ResourceLocation(texture));
    }

    default ModelBuilder orientable(ResourceLocation modelPath, ResourceLocation front, ResourceLocation side, ResourceLocation top)
    {
        return withParent(modelPath, BLOCK_ORIENTABLE)
                .texture("front", front)
                .texture("side", side)
                .texture("top", top);
    }

    default ModelBuilder orientable(ResourceLocation modelPath, String front, String side, String top)
    {
        return orientable(modelPath, new ResourceLocation(front), new ResourceLocation(side), new ResourceLocation(top));
    }

    default ModelBuilder orientable(Block block, ResourceLocation front, ResourceLocation side, ResourceLocation top)
    {
        return orientable(getModelPath(block), front, side, top);
    }

    default ModelBuilder orientable(Block block, String front, String side, String top)
    {
        return orientable(getModelPath(block), new ResourceLocation(front), new ResourceLocation(side), new ResourceLocation(top));
    }

    default ModelBuilder orientable(Item item, ResourceLocation front, ResourceLocation side, ResourceLocation top)
    {
        return orientable(getModelPath(item), front, side, top);
    }

    default ModelBuilder orientable(Item item, String front, String side, String top)
    {
        return orientable(getModelPath(item), new ResourceLocation(front), new ResourceLocation(side), new ResourceLocation(top));
    }

    default ModelBuilder orientableWithBottom(ResourceLocation modelPath, ResourceLocation front, ResourceLocation side, ResourceLocation top, ResourceLocation bottom)
    {
        return withParent(modelPath, BLOCK_ORIENTABLE_WITH_BOTTOM)
                .texture("front", front)
                .texture("side", side)
                .texture("top", top)
                .texture("bottom", bottom);
    }

    default ModelBuilder orientableWithBottom(ResourceLocation modelPath, String front, String side, String top, String bottom)
    {
        return orientableWithBottom(modelPath, new ResourceLocation(front), new ResourceLocation(side), new ResourceLocation(top), new ResourceLocation(bottom));
    }

    default ModelBuilder orientableWithBottom(Block block, ResourceLocation front, ResourceLocation side, ResourceLocation top, ResourceLocation bottom)
    {
        return orientableWithBottom(getModelPath(block), front, side, top, bottom);
    }

    default ModelBuilder orientableWithBottom(Block block, String front, String side, String top, String bottom)
    {
        return orientableWithBottom(getModelPath(block), new ResourceLocation(front), new ResourceLocation(side), new ResourceLocation(top), new ResourceLocation(bottom));
    }

    default ModelBuilder orientableWithBottom(Item item, ResourceLocation front, ResourceLocation side, ResourceLocation top, ResourceLocation bottom)
    {
        return orientableWithBottom(getModelPath(item), front, side, top, bottom);
    }

    default ModelBuilder orientableWithBottom(Item item, String front, String side, String top, String bottom)
    {
        return orientableWithBottom(getModelPath(item), new ResourceLocation(front), new ResourceLocation(side), new ResourceLocation(top), new ResourceLocation(bottom));
    }

    default ModelBuilder generated(ResourceLocation modelPath, ResourceLocation texture)
    {
        return withParent(modelPath, ITEM_GENERATED).texture("layer0", texture);
    }

    default ModelBuilder generated(ResourceLocation modelPath, String texture)
    {
        return generated(modelPath, new ResourceLocation(texture));
    }

    default ModelBuilder generated(Block block, ResourceLocation texture)
    {
        return generated(getModelPath(block), texture);
    }

    default ModelBuilder generated(Block block, String texture)
    {
        return generated(getModelPath(block), new ResourceLocation(texture));
    }

    default ModelBuilder generated(Item item, ResourceLocation texture)
    {
        return generated(getModelPath(item), texture);
    }

    default ModelBuilder generated(Item item, String texture)
    {
        return generated(getModelPath(item), new ResourceLocation(texture));
    }

    default ModelBuilder generated(Block block)
    {
        return generated(getModelPath(block), getTexturePath(block));
    }

    default ModelBuilder generated(Item item)
    {
        return generated(getModelPath(item), getTexturePath(item));
    }

    default ModelBuilder handHeld(ResourceLocation modelPath, ResourceLocation texture)
    {
        return withParent(modelPath, ITEM_HAND_HELD).texture("layer0", texture);
    }

    default ModelBuilder handHeld(ResourceLocation modelPath, String texture)
    {
        return handHeld(modelPath, new ResourceLocation(texture));
    }

    default ModelBuilder handHeld(Block block, ResourceLocation texture)
    {
        return handHeld(getModelPath(block), texture);
    }

    default ModelBuilder handHeld(Block block, String texture)
    {
        return handHeld(getModelPath(block), new ResourceLocation(texture));
    }

    default ModelBuilder handHeld(Item item, ResourceLocation texture)
    {
        return handHeld(getModelPath(item), texture);
    }

    default ModelBuilder handHeld(Item item, String texture)
    {
        return handHeld(getModelPath(item), new ResourceLocation(texture));
    }

    default ModelBuilder handHeld(Block block)
    {
        return handHeld(getModelPath(block), getTexturePath(block));
    }

    default ModelBuilder handHeld(Item item)
    {
        return handHeld(getModelPath(item), getTexturePath(item));
    }

    default ModelBuilder handHeldRod(ResourceLocation modelPath, ResourceLocation texture)
    {
        return withParent(modelPath, ITEM_HAND_HELD_ROD).texture("layer0", texture);
    }

    default ModelBuilder handHeldRod(ResourceLocation modelPath, String texture)
    {
        return handHeldRod(modelPath, new ResourceLocation(texture));
    }

    default ModelBuilder handHeldRod(Block block, ResourceLocation texture)
    {
        return handHeldRod(getModelPath(block), texture);
    }

    default ModelBuilder handHeldRod(Block block, String texture)
    {
        return handHeldRod(getModelPath(block), new ResourceLocation(texture));
    }

    default ModelBuilder handHeldRod(Item item, ResourceLocation texture)
    {
        return handHeldRod(getModelPath(item), texture);
    }

    default ModelBuilder handHeldRod(Item item, String texture)
    {
        return handHeldRod(getModelPath(item), new ResourceLocation(texture));
    }

    default ModelBuilder handHeldRod(Block block)
    {
        return handHeldRod(getModelPath(block), getTexturePath(block));
    }

    default ModelBuilder handHeldRod(Item item)
    {
        return handHeldRod(getModelPath(item), getTexturePath(item));
    }
}
