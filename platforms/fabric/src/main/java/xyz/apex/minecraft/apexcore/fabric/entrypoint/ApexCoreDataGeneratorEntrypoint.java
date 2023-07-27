package xyz.apex.minecraft.apexcore.fabric.entrypoint;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ApexDataProvider;

@ApiStatus.Internal
public final class ApexCoreDataGeneratorEntrypoint implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        ApexDataProvider.register(ApexCore.ID, func -> generator.createPack().addProvider(func::apply));
    }
}
