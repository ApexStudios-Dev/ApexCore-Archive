package xyz.apex.minecraft.testmod.forge.data;

import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import xyz.apex.minecraft.apexcore.shared.util.Tags;
import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllItems;
import xyz.apex.minecraft.testmod.shared.init.AllTags;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public final class ItemTagGenerator extends ItemTagsProvider
{
    ItemTagGenerator(GatherDataEvent event, PackOutput output, BlockTagGenerator blockTags)
    {
        super(output, event.getLookupProvider(), blockTags, TestMod.ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider)
    {
        leadArmorTools();
        leadIngot();
        leadNugget();
        rawLeadOre();
        copyBlockTags();
    }

    private void leadArmorTools()
    {
        tag(Tags.Items.Forge.TOOLS_SWORDS, AllItems.LEAD_SWORD);
        tag(Tags.Items.Forge.TOOLS_PICKAXES, AllItems.LEAD_PICKAXE);
        tag(Tags.Items.Forge.TOOLS_AXES, AllItems.LEAD_AXE);
        tag(Tags.Items.Forge.TOOLS_SHOVELS, AllItems.LEAD_SHOVEL);
        tag(Tags.Items.Forge.TOOLS_HOES, AllItems.LEAD_HOE);
        tag(Tags.Items.Forge.ARMORS_HELMETS, AllItems.LEAD_HELMET);
        tag(Tags.Items.Forge.ARMORS_CHESTPLATES, AllItems.LEAD_CHESTPLATE);
        tag(Tags.Items.Forge.ARMORS_LEGGINGS, AllItems.LEAD_LEGGINGS);
        tag(Tags.Items.Forge.ARMORS_BOOTS, AllItems.LEAD_BOOTS);
        tag(Tags.Items.Forge.ARMORS, AllItems.LEAD_HORSE_ARMOR);

        tag(Tags.Items.Fabric.SWORDS, AllItems.LEAD_SWORD);
        tag(Tags.Items.Fabric.PICKAXES, AllItems.LEAD_PICKAXE);
        tag(Tags.Items.Fabric.AXES, AllItems.LEAD_AXE);
        tag(Tags.Items.Fabric.SHOVELS, AllItems.LEAD_SHOVEL);
        tag(Tags.Items.Fabric.HOES, AllItems.LEAD_HOE);
    }

    private void leadIngot()
    {
        tag(Tags.Items.Forge.INGOTS, AllTags.Items.Forge.INGOTS_LEAD);
        tag(AllTags.Items.Forge.INGOTS_LEAD, AllItems.LEAD_INGOT);
        tag(AllTags.Items.Fabric.LEAD_INGOT, AllItems.LEAD_INGOT);
        tag(AllTags.Items.Fabric.LEAD_INGOTS, AllItems.LEAD_INGOT);
    }

    private void leadNugget()
    {
        tag(Tags.Items.Forge.NUGGETS, AllTags.Items.Forge.NUGGETS_LEAD);
        tag(AllTags.Items.Forge.NUGGETS_LEAD, AllItems.LEAD_NUGGET);
        tag(AllTags.Items.Fabric.LEAD_NUGGET, AllItems.LEAD_NUGGET);
        tag(AllTags.Items.Fabric.LEAD_NUGGETS, AllItems.LEAD_NUGGET);
    }

    private void rawLeadOre()
    {
        tag(AllTags.Items.Fabric.RAW_LEAD_ORE, AllItems.RAW_LEAD);
        tag(AllTags.Items.Fabric.RAW_LEAD_ORES, AllItems.RAW_LEAD);
    }

    private void copyBlockTags()
    {
        copy(Tags.Blocks.Forge.ORES, Tags.Items.Forge.ORES);
        copy(Tags.Blocks.Forge.STORAGE_BLOCKS, Tags.Items.Forge.STORAGE_BLOCKS);
        copy(Tags.Blocks.Fabric.ORES, Tags.Items.Fabric.ORES);

        copy(AllTags.Blocks.Forge.ORES_LEAD, AllTags.Items.Forge.ORES_LEAD);
        copy(AllTags.Blocks.Forge.STORAGE_BLOCKS_LEAD, AllTags.Items.Forge.STORAGE_BLOCKS_LEAD);

        copy(AllTags.Blocks.Fabric.LEAD_ORE, AllTags.Items.Fabric.LEAD_ORE);
        copy(AllTags.Blocks.Fabric.LEAD_ORES, AllTags.Items.Fabric.LEAD_ORES);
        copy(AllTags.Blocks.Fabric.LEAD_BLOCK, AllTags.Items.Fabric.LEAD_BLOCK);
        copy(AllTags.Blocks.Fabric.LEAD_BLOCKS, AllTags.Items.Fabric.LEAD_BLOCKS);
        copy(AllTags.Blocks.Fabric.RAW_LEAD_BLOCK, AllTags.Items.Fabric.RAW_LEAD_BLOCK);
        copy(AllTags.Blocks.Fabric.RAW_LEAD_BLOCKS, AllTags.Items.Fabric.RAW_LEAD_BLOCKS);
    }

    private void tag(TagKey<Item> tag, @Nullable Object... values)
    {
        if(values == null || values.length == 0) return;
        var builder = tag(tag);
        Arrays.stream(values).filter(Objects::nonNull).forEach(value -> tag(builder, value));
    }

    private void tag(IntrinsicTagAppender<Item> builder, Object obj)
    {
        if(obj instanceof TagKey<?> valueTag)
        {
            if(valueTag.isFor(ForgeRegistries.Keys.ITEMS)) builder.addOptionalTag(valueTag.location());
        }
        else if(obj instanceof Item item)
        {
            var registryName = ForgeRegistries.ITEMS.getKey(item);
            if(registryName != null) builder.addOptional(registryName);
        }
        else if(obj instanceof ItemLike itemLike) tag(builder, itemLike.asItem()); // should wrap back around to Item instanceof branch
        else if(obj instanceof Supplier<?> supplier) tag(builder, supplier.get()); // should wrap back around to Item instanceof branch
        else LogManager.getLogger().error("Unknown ObjectType: {}", obj.getClass().getCanonicalName());
    }
}
