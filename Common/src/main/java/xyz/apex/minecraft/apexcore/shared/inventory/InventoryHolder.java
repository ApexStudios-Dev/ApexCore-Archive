package xyz.apex.minecraft.apexcore.shared.inventory;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.multiblock.MultiBlock;

public interface InventoryHolder
{
    Inventory getInventory();

    // Use this method rather than looking up yourself
    // its not as simple as just BlockEntity instanceof check
    // since MultiBlock types only create block entities at the origin point
    // we need to delegate those blocks off to the origin point
    // since no block entity will exist at those locations to instance check against
    @Nullable
    static Inventory lookupInventory(@Nullable BlockGetter level, BlockPos pos, BlockState blockState)
    {
        if(level == null) return null;

        if(blockState.getBlock() instanceof MultiBlock multiBlock)
        {
            if(!multiBlock.isSameBlockTypeForMultiBlock(blockState)) return null;

            var multiBlockType = multiBlock.getMultiBlockType();

            if(!multiBlockType.isOrigin(blockState))
            {
                var originPos = multiBlockType.getOriginPos(multiBlock, blockState, pos);
                var originBlockState = level.getBlockState(originPos);
                return lookupInventory(level, originPos, originBlockState);
            }
        }

        var blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof InventoryHolder holder ? holder.getInventory() : null;
    }
}
