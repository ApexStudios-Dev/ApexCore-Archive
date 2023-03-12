package xyz.apex.minecraft.apexcore.common.component.components;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.ComponentBlock;
import xyz.apex.minecraft.apexcore.common.component.ComponentType;
import xyz.apex.minecraft.apexcore.common.component.ComponentTypes;
import xyz.apex.minecraft.apexcore.common.component.SimpleComponent;

import java.util.function.Consumer;

public final class BedComponent extends SimpleComponent
{
    public static final ComponentType<BedComponent> COMPONENT_TYPE = ComponentType
            .builder(new ResourceLocation(ApexCore.ID, "bed"), BedComponent.class)
                .requires(ComponentTypes.HORIZONTAL_FACING)
            .register();

    public static final EnumProperty<BedPart> PART = BedBlock.PART;
    public static final BooleanProperty OCCUPIED = BedBlock.OCCUPIED;
    public static final DirectionProperty FACING = HorizontalFacingComponent.FACING;

    @ApiStatus.Internal // public cause reflection
    public BedComponent(ComponentBlock block)
    {
        super(block);
    }

    @Override
    public BlockState registerDefaultBlockState(BlockState blockState)
    {
        return blockState.setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false);
    }

    @Override
    public void createBlockStateDefinition(Consumer<Property<?>> consumer)
    {
        consumer.accept(PART);
        consumer.accept(OCCUPIED);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(level.isClientSide) return InteractionResult.CONSUME;

        var bedPos = pos;
        var bedBlockState = blockState;
        var facing = blockState.getValue(FACING);
        var bed = block.toBlock();

        if(blockState.getValue(PART) != BedPart.HEAD)
        {
            bedPos = pos.relative(facing);
            bedBlockState = level.getBlockState(bedPos);
            if(!bedBlockState.is(bed)) return InteractionResult.FAIL;
        }

        if(!BedBlock.canSetSpawn(level))
        {
            var multiBlock = getComponent(ComponentTypes.MULTI_BLOCK);

            if(multiBlock == null)
            {
                level.removeBlock(bedPos, false);
                level.removeBlock(pos, false);
            }
            else
            {
                var multiBlockType = multiBlock.getMultiBlockType();
                var originPos = multiBlockType.getOriginPos(bedBlockState, bedPos);

                for(var localSpace : multiBlockType.getLocalPositions())
                {
                    var worldSpace = multiBlockType.getWorldSpaceFromLocalSpace(bedBlockState, originPos, localSpace);
                    level.removeBlock(worldSpace, false);
                }
            }

            var center = bedPos.getCenter();
            level.explode(null, DamageSource.badRespawnPointExplosion(center), null, center, 5F, true, Level.ExplosionInteraction.BLOCK);
        }
        else if(blockState.getValue(OCCUPIED))
        {
            // TODO: should we change this translation to be block specified
            if(!kickVillagerOutOfBed(level, bedPos)) player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
        }
        else
        {
            player.startSleepInBed(bedPos).ifLeft(problem -> {
                var message = problem.getMessage();
                if(message != null) player.displayClientMessage(message, true);
            });
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos)
    {
        var part = blockState.getValue(PART);

        if(direction == BedBlock.getNeighbourDirection(part, blockState.getValue(FACING)))
        {
            if(neighborState.is(block.toBlock()) && neighborState.getValue(PART) != part) return blockState.setValue(OCCUPIED, neighborState.getValue(OCCUPIED));
            return Blocks.AIR.defaultBlockState();
        }

        return blockState;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player)
    {
        if(hasComponent(ComponentTypes.MULTI_BLOCK)) return;

        var part = blockState.getValue(PART);

        if(part == BedPart.FOOT)
        {
            var bedPos = pos.relative(BedBlock.getNeighbourDirection(part, blockState.getValue(FACING)));
            var bedBlockState = level.getBlockState(bedPos);

            if(bedBlockState.is(block.toBlock()) && bedBlockState.getValue(PART) == BedPart.HEAD)
            {
                level.setBlock(bedPos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, bedPos, Block.getId(bedBlockState));
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx, BlockState blockState)
    {
        var facing = getRequiredComponent(ComponentTypes.HORIZONTAL_FACING).getFacingDirection(ctx);
        var pos = ctx.getClickedPos();
        var bedPos = pos.relative(facing);
        var level = ctx.getLevel();
        if(level.getBlockState(bedPos).canBeReplaced(ctx) && level.getWorldBorder().isWithinBounds(bedPos)) return blockState;
        return null;
    }

    private boolean kickVillagerOutOfBed(Level level, BlockPos pos)
    {
        var villagers = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
        if(villagers.isEmpty()) return false;

        var villager = villagers.get(0);
        villager.stopSleeping();
        return true;
    }
}
