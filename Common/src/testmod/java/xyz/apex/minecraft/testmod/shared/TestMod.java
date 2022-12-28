package xyz.apex.minecraft.testmod.shared;

import net.minecraft.world.item.CreativeModeTabs;

import xyz.apex.minecraft.apexcore.shared.event.events.CreativeModeTabEvent;
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

        CreativeModeTabEvent.BUILD_CONTENTS.addListener(builder -> {
            var tab = builder.getTab();

            if(tab == CreativeModeTabs.BUILDING_BLOCKS) builder.accept(AllBlocks.LEAD_BLOCK);
            else if(tab == CreativeModeTabs.NATURAL_BLOCKS)
            {
                builder.accept(AllBlocks.LEAD_ORE);
                builder.accept(AllBlocks.DEEPSLATE_LEAD_ORE);
                builder.accept(AllBlocks.RAW_LEAD_BLOCK);
            }
            else if(tab == CreativeModeTabs.TOOLS_AND_UTILITIES)
            {
                builder.accept(AllItems.LEAD_SHOVEL);
                builder.accept(AllItems.LEAD_PICKAXE);
                builder.accept(AllItems.LEAD_AXE);
                builder.accept(AllItems.LEAD_HOE);
            }
            else if(tab == CreativeModeTabs.COMBAT)
            {
                builder.accept(AllItems.LEAD_AXE);
                builder.accept(AllItems.LEAD_SWORD);
                builder.accept(AllItems.LEAD_HELMET);
                builder.accept(AllItems.LEAD_CHESTPLATE);
                builder.accept(AllItems.LEAD_LEGGINGS);
                builder.accept(AllItems.LEAD_BOOTS);
                builder.accept(AllItems.LEAD_HORSE_ARMOR);
            }
            else if(tab == CreativeModeTabs.INGREDIENTS)
            {
                builder.accept(AllItems.RAW_LEAD);
                builder.accept(AllItems.LEAD_NUGGET);
                builder.accept(AllItems.LEAD_INGOT);
            }
            else if(tab == CreativeModeTabs.OP_BLOCKS)
            {
                builder.accept(AllBlocks.MULTI_BLOCK);
                builder.accept(AllBlocks.TEST_BLOCK);
            }
        });
    }
}
