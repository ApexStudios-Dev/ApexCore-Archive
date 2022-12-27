package xyz.apex.minecraft.testmod.forge.data;

import net.minecraft.data.DataProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import xyz.apex.minecraft.testmod.shared.TestMod;

@Mod.EventBusSubscriber(modid = TestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class TestModForgeData
{
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
        var generator = event.getGenerator();

        var client = event.includeClient();
        var server = event.includeServer();

        generator.addProvider(client, (DataProvider.Factory<BlockStateGenerator>) output -> new BlockStateGenerator(event, output));
        generator.addProvider(client, (DataProvider.Factory<ItemModelGenerator>) output -> new ItemModelGenerator(event, output));

        generator.addProvider(client, (DataProvider.Factory<LanguageGenerator>) LanguageGenerator::new);

        var blockTags = generator.addProvider(server, (DataProvider.Factory<BlockTagGenerator>) output -> new BlockTagGenerator(event, output));
        generator.addProvider(server, (DataProvider.Factory<ItemTagGenerator>) output -> new ItemTagGenerator(event, output, blockTags));

        generator.addProvider(server, (DataProvider.Factory<LootTableGenerator>) LootTableGenerator::new);
        generator.addProvider(server, (DataProvider.Factory<RecipeGenerator>) RecipeGenerator::new);
    }
}