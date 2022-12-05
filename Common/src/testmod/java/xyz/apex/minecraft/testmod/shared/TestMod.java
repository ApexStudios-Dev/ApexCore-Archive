package xyz.apex.minecraft.testmod.shared;

import xyz.apex.minecraft.apexcore.shared.registry.block.BlockRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.item.ItemRegistry;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

public interface TestMod
{
    String ID = "testmod";

    static void bootstrap()
    {
        Registries.bootstrap();

        AllBlocks.bootstrap();
        AllItems.bootstrap();
    }

    interface Registries
    {
        BlockRegistry BLOCKS  = BlockRegistry.create(ID);
        ItemRegistry ITEMS = ItemRegistry.create(ID);

        private static void bootstrap()
        {
        }
    }
}
