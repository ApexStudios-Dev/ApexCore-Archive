package xyz.apex.minecraft.testmod.forge.data;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.data.event.GatherDataEvent;

import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.testmod.shared.TestMod;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

public final class ItemModelGenerator extends ItemModelProvider
{
    ItemModelGenerator(GatherDataEvent event, PackOutput output)
    {
        super(output, TestMod.ID, event.getExistingFileHelper());
    }

    @Override
    protected void registerModels()
    {
        blockItem(AllBlocks.LEAD_ORE);
        blockItem(AllBlocks.DEEPSLATE_LEAD_ORE);
        blockItem(AllBlocks.LEAD_BLOCK);
        blockItem(AllBlocks.RAW_LEAD_BLOCK);
        blockItem(AllBlocks.MULTI_BLOCK);
        blockItem(AllBlocks.TEST_BLOCK);

        handHeldItem(AllItems.LEAD_SWORD);
        handHeldItem(AllItems.LEAD_PICKAXE);
        handHeldItem(AllItems.LEAD_AXE);
        handHeldItem(AllItems.LEAD_SHOVEL);
        handHeldItem(AllItems.LEAD_HOE);

        basicItem(AllItems.LEAD_HORSE_ARMOR.get());
        basicItem(AllItems.LEAD_INGOT.get());
        basicItem(AllItems.LEAD_NUGGET.get());
        basicItem(AllItems.RAW_LEAD.get());

        basicItem(AllItems.LEAD_HELMET.get());
        basicItem(AllItems.LEAD_CHESTPLATE.get());
        basicItem(AllItems.LEAD_LEGGINGS.get());
        basicItem(AllItems.LEAD_BOOTS.get());
    }

    @CanIgnoreReturnValue
    private ItemModelBuilder blockItem(RegistryEntry<? extends Block> block)
    {
        var name = block.getRegistryName();
        return withExistingParent(name.toString(), new ResourceLocation(name.getNamespace(), "block/%s".formatted(name.getPath())));
    }

    @CanIgnoreReturnValue
    private ItemModelBuilder handHeldItem(RegistryEntry<? extends Item> item)
    {
        var name = item.getRegistryName();
        return getBuilder(name.toString())
                .parent(new ModelFile.UncheckedModelFile(new ResourceLocation("minecraft", "item/handheld")))
                .texture("layer0", new ResourceLocation(name.getNamespace(), "item/%s".formatted(name.getPath())))
        ;
    }
}
