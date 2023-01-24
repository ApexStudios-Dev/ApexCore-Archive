package xyz.apex.minecraft.apexcore.common.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
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
    Supplier<BlockBehaviour.Properties> BLOCK_TORCH = () -> BlockBehaviour.Properties.copy(Blocks.TORCH);

    Supplier<BlockBehaviour.Properties> BLOCK_ORE = () -> BlockBehaviour.Properties.copy(Blocks.IRON_ORE);
    Supplier<BlockBehaviour.Properties> BLOCK_STORAGE = () -> BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK);
    Supplier<BlockBehaviour.Properties> BLOCK_RAW_ORE = () -> BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK);
    Supplier<BlockBehaviour.Properties> BLOCK_DEEPSLATE_ORE = () -> BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE);
    // endregion

    // region: Items
    Supplier<Item.Properties> ITEM_GENERIC = Item.Properties::new;
    Supplier<Item.Properties> ITEM_TOOL = () -> new Item.Properties().stacksTo(1);
    Supplier<Item.Properties> ITEM_COMBAT = () -> new Item.Properties().stacksTo(1);
    // endregion
}
