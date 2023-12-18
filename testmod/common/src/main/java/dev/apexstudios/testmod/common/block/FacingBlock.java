package dev.apexstudios.testmod.common.block;

import com.mojang.serialization.MapCodec;
import dev.apexstudios.apexcore.common.util.VoxelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public final class FacingBlock extends HorizontalDirectionalBlock
{
    private static final MapCodec<FacingBlock> CODEC = simpleCodec(FacingBlock::new);
    private static final Map<Direction, VoxelShape> SHAPES = VoxelHelper.rotateHorizontal(Direction.NORTH, Shapes.or(
            box(.5D, 9D, 2D, 2.5D, 14D, 13D),
            box(13.5D, 9D, 2D, 15.5D, 14D, 13D),
            box(0D, 14D, 0D, 16D, 16D, 16D),
            box(13D, 6D, 13D, 16D, 14D, 16D),
            box(0D, 6D, 13D, 3D, 14D, 16D)
    ));

    public FacingBlock(Properties properties)
    {
        super(properties);

        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec()
    {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPES.get(blockState.getValue(FACING));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(!level.isClientSide)
        {
            var oldFacing = blockState.getValue(FACING);
            var newBlockState = blockState.cycle(FACING);
            var newFacing = newBlockState.getValue(FACING);
            level.setBlock(pos, newBlockState, 18);
            player.displayClientMessage(Component.literal("Cycled FacingBlock: %s -> %s".formatted(oldFacing.getName(), newFacing.getName())), true);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
