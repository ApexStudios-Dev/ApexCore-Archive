package xyz.apex.minecraft.apexcore.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class ApexCoreFabricData implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator)
    {
        var pack = dataGenerator.createPack();

        pack.addProvider(BlockTagsGenerator::new);
    }
}
