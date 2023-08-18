package xyz.apex.minecraft.apexcore.common.lib.multiblock;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BaseBlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;

import java.util.Objects;
import java.util.function.BiFunction;

public final class MultiBlockComponent extends BaseBlockComponent
{
    public static final BlockComponentType<MultiBlockComponent> COMPONENT_TYPE = BlockComponentType.register(ApexCore.ID, "multi_block", MultiBlockComponent::new);

    @Nullable private MultiBlockType multiBlockType;

    private MultiBlockComponent(BlockComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    public void setMultiBlockType(MultiBlockType multiBlockType)
    {
        Validate.isTrue(!isRegistered(), "Cannot register MultiBlockType outside of Component registration");
        this.multiBlockType = multiBlockType;
    }

    public MultiBlockType getMultiBlockType()
    {
        return Objects.requireNonNull(multiBlockType);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        var multiBlockType = getMultiBlockType();

        if(multiBlockType.renderAtOriginOnly())
            return getIndex(multiBlockType, blockState) != 0 ? RenderShape.INVISIBLE : null;

        return null;
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState defaultBlockState)
    {
        return setIndex(getMultiBlockType(), defaultBlockState, 0);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(getMultiBlockType().getProperty());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext context)
    {
        var level = context.getLevel();
        var root = context.getClickedPos();
        var placer = context.getPlayer();

        var multiBlockType = getMultiBlockType();

        for(var i = 0; i < multiBlockType.size(); i++)
        {
            var newBlockState = setIndex(multiBlockType, placementBlockState, i);
            var worldPosition = worldPosition(multiBlockType, root, newBlockState);

            if(!canPlaceAt(level, worldPosition, newBlockState, placer))
                return null;
        }

        return placementBlockState;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        var multiBlockType = getMultiBlockType();

        if(getIndex(multiBlockType, blockState) != 0)
            return;

        var blockType = getGameObject();

        for(var i = 0; i < multiBlockType.size(); i++)
        {
            var newBlockState = setIndex(multiBlockType, blockState, i);
            var worldPosition = worldPosition(multiBlockType, pos, newBlockState);

            if(worldPosition.equals(pos))
                continue;

            if(!level.getBlockState(worldPosition).is(blockType))
                level.destroyBlock(worldPosition, true, placer);

            var soundType = newBlockState.getSoundType();
            level.setBlockAndUpdate(worldPosition, newBlockState);

            if(placer instanceof ServerPlayer sPlayer)
                CriteriaTriggers.PLACED_BLOCK.trigger(sPlayer, worldPosition, stack);

            // level.playSound(placer, worldPosition, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
            level.gameEvent(GameEvent.BLOCK_PLACE, worldPosition, GameEvent.Context.of(placer, newBlockState));
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player)
    {
        var multiBlockType = getMultiBlockType();
        var blockType = getGameObject();
        var root = rootPosition(multiBlockType, pos, blockState);

        for(var i = 0; i < multiBlockType.size(); i++)
        {
            var worldPosition = worldPosition(multiBlockType, root, setIndex(multiBlockType, blockState, i));

            if(worldPosition.equals(pos))
                continue;

            if(level.getBlockState(worldPosition).is(blockType))
                destroyBlock(level, worldPosition, player);
        }

        if(!root.equals(pos) && level.getBlockState(root).is(blockType))
            destroyBlock(level, root, player);
    }

    private void destroyBlock(Level level, BlockPos pos, @Nullable LivingEntity destroyer)
    {
        var blockState = level.getBlockState(pos);

        if(level.removeBlock(pos, false))
        {
            level.addDestroyBlockEffect(pos, blockState);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(destroyer, blockState));
        }
    }

    public static int getIndex(MultiBlockType multiBlockType, BlockState blockState)
    {
        return blockState.getValue(multiBlockType.getProperty());
    }

    public static BlockState setIndex(MultiBlockType multiBlockType, BlockState blockState, int index)
    {
        return blockState.setValue(multiBlockType.getProperty(), index);
    }

    public static BlockPos rootPosition(MultiBlockType multiBlockType, BlockPos worldPosition, BlockState blockState)
    {
        var localPositions = multiBlockType.getLocalPositions();
        var index = getIndex(multiBlockType, blockState);
        var localPosition = correctedLocalPosition(localPositions.get(index), blockState);
        return worldPosition.subtract(localPosition);
    }

    public static BlockPos worldPosition(MultiBlockType multiBlockType, BlockPos root, BlockState blockState)
    {
        var localPositions = multiBlockType.getLocalPositions();
        var index = getIndex(multiBlockType, blockState);
        var localPosition = correctedLocalPosition(localPositions.get(index), blockState);
        return root.offset(localPosition);
    }

    public static BlockPos correctedLocalPosition(BlockPos localPosition, BlockState blockState)
    {
        return blockState.getOptionalValue(HorizontalDirectionalBlock.FACING).map(facing -> rotateLocalPosition(localPosition, facing)).orElse(localPosition);
    }

    public static BlockPos rotateLocalPosition(BlockPos localPosition, Direction facing)
    {
        return localPosition.rotate(switch(facing.getOpposite()) {
            default -> Rotation.NONE;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case EAST -> Rotation.CLOCKWISE_90;
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
        });
    }

    @UnknownNullability("Nullable when 'mapper' returns null")
    public static <T> T asRoot(BlockGetter level, MultiBlockType multiBlockType, BlockPos worldPosition, BlockState blockState, BiFunction<BlockPos, BlockState, T> mapper)
    {
        var rootPos = rootPosition(multiBlockType, worldPosition, blockState);
        var rootBlockState = level.getBlockState(rootPos);
        return mapper.apply(rootPos, rootBlockState);
    }

    @UnknownNullability("Nullable when 'mapper' returns null")
    public static <T> T asRoot(BlockGetter level, BlockPos worldPosition, BlockState blockState, BiFunction<BlockPos, BlockState, T> mapper)
    {
        if(!(blockState.getBlock() instanceof BlockComponentHolder holder))
            return mapper.apply(worldPosition, blockState);

        var multiBlockComponent = holder.getComponent(COMPONENT_TYPE);

        if(multiBlockComponent == null)
            return mapper.apply(worldPosition, blockState);

        return asRoot(level, multiBlockComponent.getMultiBlockType(), worldPosition, blockState, mapper);
    }

    // TODO: Move somewhere more common
    public static boolean canPlaceAt(Level level, BlockPos placePos, @Nullable BlockState placeBlockState, @Nullable LivingEntity placer)
    {
        if(level.isOutsideBuildHeight(placePos))
            return false;
        if(!level.getWorldBorder().isWithinBounds(placePos))
            return false;
        if(!level.getBlockState(placePos).canBeReplaced())
            return false;
        if(placeBlockState == null)
            return true;
        if(!placeBlockState.getBlock().isEnabled(level.enabledFeatures()))
            return false;
        if(!placeBlockState.canSurvive(level, placePos))
            return false;

        var collisionContext = placer == null ? CollisionContext.empty() : CollisionContext.of(placer);

        if(!level.isUnobstructed(placeBlockState, placePos, collisionContext))
            return false;

        return placeBlockState.canSurvive(level, placePos);
    }

    public static boolean canPlaceAt(BlockPlaceContext context, @Nullable BlockPos posOverride, @Nullable BlockState placeBlockState)
    {
        var level = context.getLevel();
        var placePos = posOverride == null ? context.getClickedPos() : posOverride;
        var placer = context.getPlayer();

        if(level.isOutsideBuildHeight(placePos))
            return false;
        if(!level.getWorldBorder().isWithinBounds(placePos))
            return false;
        if(!level.getBlockState(placePos).canBeReplaced(context))
            return false;
        if(placeBlockState == null)
            return true;
        if(!placeBlockState.getBlock().isEnabled(level.enabledFeatures()))
            return false;
        if(!placeBlockState.canSurvive(level, placePos))
            return false;

        var collisionContext = placer == null ? CollisionContext.empty() : CollisionContext.of(placer);

        if(!level.isUnobstructed(placeBlockState, placePos, collisionContext))
            return false;

        return placeBlockState.canSurvive(level, placePos);
    }
}
