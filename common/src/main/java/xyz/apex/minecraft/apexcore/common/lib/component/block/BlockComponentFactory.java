package xyz.apex.minecraft.apexcore.common.lib.component.block;

import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentFactory;

/**
 * Factory used for all block components.
 *
 * @param <T> Block component type.
 */
@FunctionalInterface
public interface BlockComponentFactory<T extends BlockComponent> extends ComponentFactory<Block, BlockComponentHolder, T>
{
}
