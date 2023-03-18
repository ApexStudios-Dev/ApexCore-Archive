package xyz.apex.minecraft.apexcore.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.ComponentBlock;
import xyz.apex.minecraft.apexcore.common.component.ComponentType;
import xyz.apex.minecraft.apexcore.common.component.SimpleComponent;

import java.util.function.Consumer;

public final class MultiBlockComponent extends SimpleComponent
{
    public static final ComponentType<MultiBlockComponent> COMPONENT_TYPE = ComponentType.register(
            new ResourceLocation(ApexCore.ID, "multiblock"),
            MultiBlockComponent.class,
            MultiBlockPattern.class
    );

    private final MultiBlockType multiBlockType;

    @ApiStatus.Internal // public cause reflection
    public MultiBlockComponent(ComponentBlock block, MultiBlockPattern multiBlockPattern)
    {
        super(block);

        multiBlockType = multiBlockPattern.finalizeFor(block);
    }

    public MultiBlockType getMultiBlockType()
    {
        return multiBlockType;
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState blockState)
    {
        return multiBlockType.registerDefaultBlockState(blockState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx, BlockState blockState)
    {
        return multiBlockType.getStateForPlacement(blockState, ctx);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
    {
        return multiBlockType.canSurvive(level, pos, blockState);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
    {
        multiBlockType.onPlace(blockState, level, pos, oldBlockState);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        multiBlockType.onRemove(blockState, level, pos, newBlockState);
    }

    @Override
    public void createBlockStateDefinition(Consumer<Property<?>> consumer)
    {
        multiBlockType.registerBlockProperty(consumer);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return multiBlockType.isOrigin(blockState) ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }
}
