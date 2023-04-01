package xyz.apex.minecraft.apexcore.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import xyz.apex.minecraft.apexcore.common.ApexCore;

import java.util.concurrent.CompletableFuture;

public final class BlockTagsGenerator extends FabricTagProvider.BlockTagProvider
{
    BlockTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture)
    {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg)
    {
        tag(ApexCore.BlockTags.IMMOVABLE);
    }
}
