package xyz.apex.minecraft.apexcore.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;

import net.minecraft.core.HolderLookup;

import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.util.ApexTags;
import xyz.apex.minecraft.apexcore.fabric.platform.FabricModPlatform;

public final class ApexCoreFabric extends FabricModPlatform implements ApexCore, DataGeneratorEntrypoint
{
    public static final ApexCoreFabric INSTANCE = new ApexCoreFabric();

    private ApexCoreFabric()
    {
        super(ID, null);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        generator.createPack().addProvider((output, registriesFuture) -> new FabricTagProvider.BlockTagProvider(output, registriesFuture) {
            @Override
            protected void addTags(HolderLookup.Provider arg)
            {
                tag(ApexTags.Blocks.IMMOVABLE).addOptionalTag(ConventionalBlockTags.MOVEMENT_RESTRICTED.location());
            }
        });
    }
}
