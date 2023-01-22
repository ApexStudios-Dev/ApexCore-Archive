package xyz.apex.minecraft.apexcore.quilt.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;

import net.minecraft.core.HolderLookup;

import xyz.apex.minecraft.apexcore.shared.util.ApexTags;

import java.util.concurrent.CompletableFuture;

final class BlockTagGenerator extends FabricTagProvider.BlockTagProvider
{
    BlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture)
    {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(ApexTags.Blocks.IMMOVABLE).addTag(ConventionalBlockTags.MOVEMENT_RESTRICTED);
    }
}
