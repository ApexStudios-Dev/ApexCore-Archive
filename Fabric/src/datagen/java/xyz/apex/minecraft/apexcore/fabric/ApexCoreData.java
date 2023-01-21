package xyz.apex.minecraft.apexcore.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import xyz.apex.minecraft.apexcore.shared.ApexCore;

public final class ApexCoreData implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
    }

    @Override
    public String getEffectiveModId()
    {
        return ApexCore.ID;
    }
}
