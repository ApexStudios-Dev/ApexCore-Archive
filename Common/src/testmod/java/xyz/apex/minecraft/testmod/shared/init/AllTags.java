package xyz.apex.minecraft.testmod.shared.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.util.Tags;
import xyz.apex.minecraft.testmod.shared.TestMod;

public interface AllTags
{
    interface Blocks
    {
        interface Forge
        {
            TagKey<Block> ORES_LEAD = Tags.Blocks.forge("ores/lead");
            TagKey<Block> STORAGE_BLOCKS_LEAD = Tags.Blocks.forge("storage_blocks/lead");

            private static void bootstrap() {}
        }

        interface Fabric
        {
            TagKey<Block> LEAD_ORE = Tags.Blocks.fabric("lead_ore");
            TagKey<Block> LEAD_ORES = Tags.Blocks.fabric("lead_ores");
            TagKey<Block> LEAD_BLOCK = Tags.Blocks.fabric("lead_block");
            TagKey<Block> LEAD_BLOCKS = Tags.Blocks.fabric("lead_blocks");
            TagKey<Block> RAW_LEAD_BLOCK = Tags.Blocks.fabric("raw_lead_block");
            TagKey<Block> RAW_LEAD_BLOCKS = Tags.Blocks.fabric("raw_lead_blocks");

            private static void bootstrap() {}
        }

        private static TagKey<Block> tag(String name)
        {
            return AllTags.tag(Registries.BLOCK,  name);
        }

        private static void bootstrap()
        {
            Forge.bootstrap();
            Fabric.bootstrap();
        }
    }

    interface Items
    {
        interface Forge
        {
            TagKey<Item> ORES_LEAD = Tags.Items.forge("ores/lead");
            TagKey<Item> STORAGE_BLOCKS_LEAD = Tags.Items.forge("storage_blocks/lead");
            TagKey<Item> INGOTS_LEAD = Tags.Items.forge("ingots/lead");
            TagKey<Item> NUGGETS_LEAD = Tags.Items.forge("nuggets/lead");

            private static void bootstrap() {}
        }

        interface Fabric
        {
            TagKey<Item> LEAD_ORE = Tags.Items.fabric("lead_ore");
            TagKey<Item> LEAD_ORES = Tags.Items.fabric("lead_ores");
            TagKey<Item> LEAD_BLOCK = Tags.Items.fabric("lead_block");
            TagKey<Item> LEAD_BLOCKS = Tags.Items.fabric("lead_blocks");
            TagKey<Item> RAW_LEAD_BLOCK = Tags.Items.fabric("raw_lead_block");
            TagKey<Item> RAW_LEAD_BLOCKS = Tags.Items.fabric("raw_lead_blocks");
            TagKey<Item> LEAD_INGOT = Tags.Items.fabric("lead_ingot");
            TagKey<Item> LEAD_INGOTS = Tags.Items.fabric("lead_ingots");
            TagKey<Item> LEAD_NUGGET = Tags.Items.fabric("lead_nugget");
            TagKey<Item> LEAD_NUGGETS = Tags.Items.fabric("lead_nuggets");
            TagKey<Item> RAW_LEAD_ORE = Tags.Items.fabric("raw_lead_ore");
            TagKey<Item> RAW_LEAD_ORES = Tags.Items.fabric("raw_lead_ores");

            private static void bootstrap() {}
        }

        private static TagKey<Item> tag(String name)
        {
            return AllTags.tag(Registries.ITEM,  name);
        }

        private static void bootstrap()
        {
            Forge.bootstrap();
            Fabric.bootstrap();
        }
    }

    private static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String name)
    {
        return Tags.tag(registryType, TestMod.ID, name);
    }

    static void bootstrap()
    {
        Blocks.bootstrap();
        Items.bootstrap();
    }
}
