package xyz.apex.minecraft.testmod.shared;

import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

public interface TestMod
{
    String ID = "testmod";

    static void bootstrap()
    {
        AllBlocks.bootstrap();
        AllItems.bootstrap();
    }
}
