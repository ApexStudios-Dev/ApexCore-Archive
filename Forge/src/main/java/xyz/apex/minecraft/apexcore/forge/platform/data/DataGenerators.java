package xyz.apex.minecraft.apexcore.forge.platform.data;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.ApiStatus;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.util.Lazy;

import java.util.Set;

public final class DataGenerators
{
    private static final Set<String> DATA_MODS = Sets.newHashSet();

    @ApiStatus.Internal
    public static void register(String modId)
    {
        if(!ModLoadingContext.get().getActiveNamespace().equals(modId)) return;
        if(DATA_MODS.contains(modId)) return;
        DATA_MODS.add(modId);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerators::onGatherData);
    }

    private static void onGatherData(GatherDataEvent event)
    {
        var modId = event.getModContainer().getModId();
        if(!DATA_MODS.contains(modId)) return;
        GamePlatform.getLogger().info("Registering mod '{}' DataGenerators...", modId);

        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var lookupProvider = Lazy.of(event::getLookupProvider);
        var output = Lazy.of(generator::getPackOutput);

        var client = event.includeClient();
        var server = event.includeServer();

        // Client
        var blockModels = new ModelGenerator.BlockModelGenerator(output, lookupProvider, modId, existingFileHelper);

        generator.addProvider(client, new EnglishGenerator(output, lookupProvider, modId, existingFileHelper));
        // generator.addProvider(client, new BlockStateProvider(output, modId, blockModels));
        generator.addProvider(client, blockModels);
        generator.addProvider(client, new ModelGenerator.ItemModelGenerator(output, lookupProvider, modId, existingFileHelper));

        // Server
        generator.addProvider(server, new RecipeGenerator(output, lookupProvider, modId, existingFileHelper));
        var blockTags = generator.addProvider(server, new TagGenerator.BlockTagGenerator(output, lookupProvider, modId, existingFileHelper));
        generator.addProvider(server, new TagGenerator.ItemTagGenerator(output, lookupProvider, modId, existingFileHelper, blockTags));
    }
}
