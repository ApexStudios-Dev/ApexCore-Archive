package xyz.apex.minecraft.apexcore.quilt;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;

import net.minecraft.core.HolderLookup;

import xyz.apex.minecraft.apexcore.quilt.platform.QuiltModPlatform;
import xyz.apex.minecraft.apexcore.shared.ApexCore;
import xyz.apex.minecraft.apexcore.shared.util.ApexTags;

public final class ApexCoreQuilt extends QuiltModPlatform implements ApexCore, DataGeneratorEntrypoint
{
    public static final QuiltModPlatform INSTANCE = new ApexCoreQuilt();

    private ApexCoreQuilt()
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
