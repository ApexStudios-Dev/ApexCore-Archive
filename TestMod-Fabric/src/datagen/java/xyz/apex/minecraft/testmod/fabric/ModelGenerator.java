package xyz.apex.minecraft.testmod.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

import xyz.apex.minecraft.testmod.shared.TestMod;

public final class ModelGenerator extends FabricModelProvider
{
    ModelGenerator(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators)
    {
        generators.createTrivialCube(TestMod.TEST_BLOCK.get());
        generators.createTrivialCube(TestMod.TEST_MULTI_BLOCK.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators)
    {
        generators.generateFlatItem(TestMod.TEST_ITEM.get(), ModelTemplates.FLAT_ITEM);
    }
}
