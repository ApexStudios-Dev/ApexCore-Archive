package xyz.apex.minecraft.testmod.shared;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;
import xyz.apex.minecraft.testmod.shared.init.AllTags;

public interface TestMod
{
    String ID = "testmod";

    static void bootstrap()
    {
        AllTags.bootstrap();
        AllBlocks.bootstrap();
        AllItems.bootstrap();

        GamePlatform.events().register(TestMod.ID);
    }
}
