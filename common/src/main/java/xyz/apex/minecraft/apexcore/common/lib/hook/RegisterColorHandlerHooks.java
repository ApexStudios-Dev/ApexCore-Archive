package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

import java.util.function.Supplier;

/**
 * Hooks for registering block and item color handlers.
 */
@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.NonExtendable
public interface RegisterColorHandlerHooks
{
    /**
     * Registers a new color handler for the given block.
     *
     * @param block      Block to register color handler for.
     * @param blockColor Color handler to be registered.
     */
    void registerBlockColor(Supplier<? extends Block> block, Supplier<Supplier<BlockColor>> blockColor);

    /**
     * Registers a new color handler for the given item.
     *
     * @param item      Item to register color handler for.
     * @param itemColor Color handler to be registered.
     */
    void registerItemColor(Supplier<ItemLike> item, Supplier<Supplier<ItemColor>> itemColor);
}
