package xyz.apex.minecraft.testmod.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;

import xyz.apex.minecraft.testmod.shared.TestMod;

public final class TestModData implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        TestModFabric.INSTANCE.initializeDataGen();
    }

    @Override
    public String getEffectiveModId()
    {
        return TestMod.ID;
    }
}
