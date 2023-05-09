package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import xyz.apex.minecraft.apexcore.common.lib.component.BaseComponent;

/**
 * Base implementation for block entity components.
 */
public class BlockEntityComponent extends BaseComponent<BlockEntity, BlockEntityComponentHolder>
{
    protected BlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        super(componentHolder);
    }
}
