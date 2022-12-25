package xyz.apex.minecraft.apexcore.shared.multiblock;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import xyz.apex.minecraft.apexcore.shared.mixin.accessors.BlockAccessor;
import xyz.apex.minecraft.apexcore.shared.mixin.invokers.BlockInvoker;

public class SimpleMultiBlock extends Block implements MultiBlock
{
    protected final MultiBlockType multiBlockType;

    public SimpleMultiBlock(MultiBlockType multiBlockType, Properties properties)
    {
        super(properties);

        this.multiBlockType = multiBlockType;
        replaceBlockStateContainer(this); // must be after we set the MultiBlockType field
        registerDefaultState(multiBlockType.registerDefaultBlockState(defaultBlockState()));
    }

    @Override
    public boolean isSameBlockTypeForMultiBlock(BlockState blockState)
    {
        return blockState.is(this);
    }

    @Override
    public final MultiBlockType getMultiBlockType()
    {
        return multiBlockType;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        return multiBlockType.getStateForPlacement(this, defaultBlockState(), ctx);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        return multiBlockType.canSurvive(this, level, pos, blockState);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
    {
        multiBlockType.onPlace(this, blockState, level, pos, oldBlockState);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockstate, boolean isMoving)
    {
        multiBlockType.onRemove(this, blockState, level, pos, newBlockstate);
        super.onRemove(blockState, level, pos, newBlockstate, isMoving);
    }

    @SuppressWarnings("ConstantValue")
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        // null on first call, as it's set in constructor and this method is called from super
        // none-null on second call, as that's fired in our constructor and replaces the vanilla state definition
        if(multiBlockType != null) multiBlockType.registerBlockProperty(builder::add);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return multiBlockType.isOrigin(blockState) ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    // TODO: Find better place for this, right now only used by SimpleMultiBlock but can be used by any block type
    public static <T extends Block> void replaceBlockStateContainer(T block)
    {
        var builder = new StateDefinition.Builder<Block, BlockState>(block);
        ((BlockInvoker) block).ApexCore$createBlockStateDefinition(builder);
        var stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        ((BlockAccessor) block).ApexCore$setStateDefinition(stateDefinition);
        ((BlockInvoker) block).ApexCore$registerDefaultState(stateDefinition.any());
    }

    public static class WithHorizontalFacing extends HorizontalDirectionalBlock implements MultiBlock
    {
        protected final MultiBlockType multiBlockType;

        public WithHorizontalFacing(MultiBlockType multiBlockType, Properties properties)
        {
            super(properties);

            this.multiBlockType = multiBlockType;
            replaceBlockStateContainer(this);
            registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        }

        @Override
        public boolean isSameBlockTypeForMultiBlock(BlockState blockState)
        {
            return blockState.is(this);
        }

        @Override
        public final MultiBlockType getMultiBlockType()
        {
            return multiBlockType;
        }

        @Nullable
        @Override
        public BlockState getStateForPlacement(BlockPlaceContext ctx)
        {
            var defaultBlockState = defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
            return multiBlockType.getStateForPlacement(this, defaultBlockState, ctx);
        }

        @Override
        public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
        {
            return multiBlockType.canSurvive(this, level, pos, blockState);
        }

        @Override
        public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
        {
            multiBlockType.onPlace(this, blockState, level, pos, oldBlockState);
        }

        @Override
        public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockstate, boolean isMoving)
        {
            multiBlockType.onRemove(this, blockState, level, pos, newBlockstate);
            super.onRemove(blockState, level, pos, newBlockstate, isMoving);
        }

        @SuppressWarnings("ConstantValue")
        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
        {
            builder.add(FACING);
            // null on first call, as it's set in constructor and this method is called from super
            // none-null on second call, as that's fired in our constructor and replaces the vanilla state definition
            if(multiBlockType != null) multiBlockType.registerBlockProperty(builder::add);
        }

        @Override
        public RenderShape getRenderShape(BlockState blockState)
        {
            return multiBlockType.isOrigin(blockState) ? RenderShape.MODEL : RenderShape.INVISIBLE;
        }
    }
}
