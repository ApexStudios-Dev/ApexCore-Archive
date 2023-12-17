package dev.apexstudios.apexcore.common.generator.model;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface ModelFile
{
    ModelFile MISSING = of("builtin/missing");

    ModelFile ITEM_GENERATED = of("item/generated");
    ModelFile ITEM_HANDHELD = of("item/handheld");
    ModelFile ITEM_HANDHELD_RED = of("item/handheld_red");
    ModelFile ITEM_TEMPLATE_SPAWN_EGG = of("item/template_spawn_egg");

    ModelFile BLOCK_CUBE = of("block/cube");
    ModelFile BLOCK_CUBE_ALL = of("block/cube_all");

    ResourceLocation path();

    static ModelFile of(ResourceLocation path)
    {
        return () -> path;
    }

    static ModelFile of(String ownerId, String path)
    {
        return of(new ResourceLocation(ownerId, path));
    }

    static ModelFile of(String path)
    {
        return of(new ResourceLocation(path));
    }
}
