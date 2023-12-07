package dev.apexstudios.apexcore.fabric.loader;

import dev.apexstudios.apexcore.common.generator.ResourceGeneration;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public interface FabricResourceGeneration
{
    static void generate(FabricDataGenerator generator)
    {
        var ownerId = generator.getModId();
        var pack = generator.createPack();
        var validationEnabled = generator.isStrictValidationEnabled();
        ResourceGeneration.generate(ownerId, validationEnabled, consumer -> pack.addProvider(consumer::apply));
    }
}
