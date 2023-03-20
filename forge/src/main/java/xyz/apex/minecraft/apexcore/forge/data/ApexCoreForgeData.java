package xyz.apex.minecraft.apexcore.forge.data;

import net.minecraftforge.data.event.GatherDataEvent;

public final class ApexCoreForgeData
{
    public static void onGatherData(GatherDataEvent event)
    {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();

        var includeClient = event.includeClient();
        var includeServer = event.includeServer();

        generator.addProvider(includeServer, new BlockTagsGenerator(packOutput, lookupProvider, existingFileHelper));
    }
}
