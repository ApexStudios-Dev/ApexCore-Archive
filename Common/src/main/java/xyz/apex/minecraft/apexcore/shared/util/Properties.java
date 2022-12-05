package xyz.apex.minecraft.apexcore.shared.util;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import xyz.apex.minecraft.apexcore.shared.hooks.ItemHooks;

import java.util.function.Supplier;

public interface Properties
{
    // region: Blocks
    Supplier<BlockBehaviour.Properties> BLOCK_STONE = () -> BlockBehaviour.Properties.copy(Blocks.STONE);
    Supplier<BlockBehaviour.Properties> BLOCK_LOGS = () -> BlockBehaviour.Properties.copy(Blocks.OAK_LOG);
    Supplier<BlockBehaviour.Properties> BLOCK_PLANKS = () -> BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS);
    Supplier<BlockBehaviour.Properties> BLOCK_GLASS = () -> BlockBehaviour.Properties.copy(Blocks.GLASS);
    Supplier<BlockBehaviour.Properties> BLOCK_WOOL = () -> BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL);
    Supplier<BlockBehaviour.Properties> BLOCK_CARPET = () -> BlockBehaviour.Properties.copy(Blocks.WHITE_CARPET);
    Supplier<BlockBehaviour.Properties> BLOCK_DIRT = () -> BlockBehaviour.Properties.copy(Blocks.DIRT);

    Supplier<BlockBehaviour.Properties> BLOCK_ORE = () -> BlockBehaviour.Properties.copy(Blocks.IRON_ORE);
    Supplier<BlockBehaviour.Properties> BLOCK_STORAGE = () -> BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK);
    Supplier<BlockBehaviour.Properties> BLOCK_RAW_ORE = () -> BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK);
    Supplier<BlockBehaviour.Properties> BLOCK_DEEPSLATE_ORE = () -> BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE);
    // endregion

    // region: Items
    Supplier<Item.Properties> ITEM_GENERIC = () -> ItemHooks.copyProperties(Items.STONE);
    Supplier<Item.Properties> ITEM_BUCKET = () -> ItemHooks.copyProperties(Items.BUCKET);
    Supplier<Item.Properties> ITEM_GLASS_BOTTLE = () -> ItemHooks.copyProperties(Items.GLASS_BOTTLE);
    Supplier<Item.Properties> ITEM_TOOL = () -> new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS);
    Supplier<Item.Properties> ITEM_COMBAT = () -> new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT);
    // endregion
}
