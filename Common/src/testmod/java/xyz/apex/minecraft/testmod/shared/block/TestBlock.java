package xyz.apex.minecraft.testmod.shared.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import xyz.apex.minecraft.testmod.shared.init.AllBlocks;

public final class TestBlock extends BaseEntityBlock
{
    public TestBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        var blockEntity = AllBlocks.TEST_BLOCK_ENTITY.getBlockEntity(level, pos);
        if(blockEntity == null) return InteractionResult.PASS;
        blockEntity.click(player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return AllBlocks.TEST_BLOCK_ENTITY.create(blockPos, blockState);
    }
}
