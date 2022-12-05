package xyz.apex.minecraft.testmod.forge.data;

import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.versions.forge.ForgeVersion;

import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

public final class ItemTagGenerator extends ItemTagsProvider
{
    public static final TagKey<Item> LEAD_ORE = ItemTags.create(BlockTagGenerator.LEAD_ORE.location());
    public static final TagKey<Item> LEAD_BLOCK = ItemTags.create(BlockTagGenerator.LEAD_BLOCK.location());
    public static final TagKey<Item> LEAD_INGOT = ItemTags.create(new ResourceLocation(ForgeVersion.MOD_ID, "ingots/lead"));
    public static final TagKey<Item> LEAD_NUGGET = ItemTags.create(new ResourceLocation(ForgeVersion.MOD_ID, "nuggets/lead"));

    public ItemTagGenerator(GatherDataEvent event, BlockTagGenerator blockTags)
    {
        super(event.getGenerator(), blockTags, TestMod.ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags()
    {
        var leadOre = AllBlocks.LEAD_ORE.asItem();
        var deepslateLeadOre = AllBlocks.DEEPSLATE_LEAD_ORE.asItem();
        var leadBlock = AllBlocks.LEAD_BLOCK.asItem();
        var rawLeadBlock = AllBlocks.RAW_LEAD_BLOCK.asItem();
        var leadIngot = AllItems.LEAD_INGOT.get();
        var leadNugget = AllItems.LEAD_NUGGET.get();
        var rawLead = AllItems.RAW_LEAD.get();

        tag(Tags.Items.ORES).addTag(LEAD_ORE);// forge:ores + forge:ores/lead
        tag(LEAD_ORE).add(leadOre, deepslateLeadOre);
        tag(ItemTags.create(new ResourceLocation("c", "lead_ore"))).add(leadOre, deepslateLeadOre);
        tag(ItemTags.create(new ResourceLocation("c", "lead_ores"))).add(leadOre, deepslateLeadOre);
        tag(Tags.Items.STORAGE_BLOCKS).addTag(LEAD_BLOCK); // forge:storage_blocks + forge:storage_blocks/lead
        tag(LEAD_BLOCK).add(leadBlock, rawLeadBlock);
        tag(ItemTags.create(new ResourceLocation("c", "lead_block"))).add(leadBlock);
        tag(ItemTags.create(new ResourceLocation("c", "lead_blocks"))).add(leadBlock);
        tag(ItemTags.create(new ResourceLocation("c", "raw_lead_block"))).add(rawLeadBlock);
        tag(ItemTags.create(new ResourceLocation("c", "raw_lead_blocks"))).add(rawLeadBlock);
        tag(Tags.Items.TOOLS_SWORDS).add(AllItems.LEAD_SWORD.get()); // forge:tools/swords
        tag(Tags.Items.TOOLS_PICKAXES).add(AllItems.LEAD_PICKAXE.get()); // forge:tools/pickaxes
        tag(Tags.Items.TOOLS_AXES).add(AllItems.LEAD_AXE.get()); // forge:tools/axes
        tag(Tags.Items.TOOLS_SHOVELS).add(AllItems.LEAD_SHOVEL.get()); // forge:tools/shovels
        tag(Tags.Items.TOOLS_HOES).add(AllItems.LEAD_HOE.get()); // forge:tools/hoes
        tag(Tags.Items.ARMORS_HELMETS).add(AllItems.LEAD_HELMET.get()); // forge:armors/helmets
        tag(Tags.Items.ARMORS_CHESTPLATES).add(AllItems.LEAD_CHESTPLATE.get());// forge:armors/chestplates
        tag(Tags.Items.ARMORS_LEGGINGS).add(AllItems.LEAD_LEGGINGS.get());// forge:armors/leggings
        tag(Tags.Items.ARMORS_BOOTS).add(AllItems.LEAD_BOOTS.get());// forge:armors/boots
        tag(Tags.Items.ARMORS).add(AllItems.LEAD_HORSE_ARMOR.get());// forge:armors
        tag(Tags.Items.INGOTS).addTag(LEAD_INGOT);// forge:ingots + forge:ingots/lead
        tag(LEAD_INGOT).add(leadIngot);
        tag(ItemTags.create(new ResourceLocation("c", "lead_ingot"))).add(leadIngot);
        tag(ItemTags.create(new ResourceLocation("c", "lead_ingots"))).add(leadIngot);
        tag(Tags.Items.NUGGETS).addTag(LEAD_NUGGET);// forge:nuggets + forge:nuggets/lead
        tag(LEAD_NUGGET).add(leadNugget);
        tag(ItemTags.create(new ResourceLocation("c", "lead_nugget"))).add(leadNugget);
        tag(ItemTags.create(new ResourceLocation("c", "lead_nuggets"))).add(leadNugget);
        tag(ItemTags.create(new ResourceLocation("c", "raw_lead_ore"))).add(rawLead);
        tag(ItemTags.create(new ResourceLocation("c", "raw_lead_ores"))).add(rawLead);
    }
}
