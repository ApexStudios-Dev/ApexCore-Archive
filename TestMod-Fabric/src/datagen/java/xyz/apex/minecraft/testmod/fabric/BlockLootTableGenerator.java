package xyz.apex.minecraft.testmod.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import xyz.apex.minecraft.apexcore.fabric.data.BlockLootTableProvider;
import xyz.apex.minecraft.testmod.shared.TestMod;

public final class BlockLootTableGenerator extends BlockLootTableProvider
{
    BlockLootTableGenerator(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generate()
    {
        dropSelf(TestMod.TEST_BLOCK.get());
    }
}
