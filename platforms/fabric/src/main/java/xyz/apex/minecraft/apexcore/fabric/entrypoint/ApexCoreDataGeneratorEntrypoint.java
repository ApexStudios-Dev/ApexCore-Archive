package xyz.apex.minecraft.apexcore.fabric.entrypoint;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCoreTests;

@ApiStatus.Internal
public final class ApexCoreDataGeneratorEntrypoint implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        var pack = generator.createPack();
        ApexCoreTests.registerTestResourceGen((packType, dataProviderFunction) -> pack.addProvider(dataProviderFunction::apply));
    }
}
