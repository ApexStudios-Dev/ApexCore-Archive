package xyz.apex.minecraft.apexcore.forge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.apex.minecraft.apexcore.common.ApexCore;

import java.util.concurrent.CompletableFuture;

public final class BlockTagsGenerator extends BlockTagsProvider
{
    BlockTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, ApexCore.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider)
    {
        tag(ApexCore.BlockTags.IMMOVABLE);
    }
}
