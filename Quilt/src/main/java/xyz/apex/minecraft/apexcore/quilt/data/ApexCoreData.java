package xyz.apex.minecraft.apexcore.quilt.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import xyz.apex.minecraft.apexcore.shared.ApexCore;

public final class ApexCoreData implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        var pack = generator.createPack();
        pack.addProvider(BlockTagGenerator::new);
    }

    @Override
    public String getEffectiveModId()
    {
        return ApexCore.ID;
    }
}
