package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentFactory;

/**
 * Factory used for all block entity components.
 *
 * @param <T> Block entity component type.
 */
@FunctionalInterface
public interface BlockEntityComponentFactory<T extends BlockEntityComponent> extends ComponentFactory<BlockEntity, BlockEntityComponentHolder, T>
{
}
