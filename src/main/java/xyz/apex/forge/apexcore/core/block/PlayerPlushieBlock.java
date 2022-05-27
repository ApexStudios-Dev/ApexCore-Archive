package xyz.apex.forge.apexcore.core.block;

import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import xyz.apex.forge.apexcore.core.block.entity.PlayerPlushieBlockEntity;
import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.lib.block.BlockEntityBlock;
import xyz.apex.forge.apexcore.lib.block.VoxelShaper;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public final class PlayerPlushieBlock extends BlockEntityBlock<PlayerPlushieBlockEntity> implements IWaterLoggable
{
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final VoxelShape SHAPE = box(2D, 0D, 3D, 14D, 13D, 14D);
	public static final VoxelShaper SHAPER = VoxelShaper.forHorizontal(SHAPE, Direction.NORTH);

	public PlayerPlushieBlock(Properties properties)
	{
		super(properties);

		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState blockState, IBlockReader level, BlockPos pos, ISelectionContext ctx)
	{
		Direction facing = blockState.getValue(FACING);
		return SHAPER.get(facing);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx)
	{
		BlockState blockState = defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		boolean waterLogged = fluidState.is(FluidTags.WATER);
		return blockState.setValue(WATERLOGGED, waterLogged);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState blockState, IBlockReader level, BlockPos pos)
	{
		return !blockState.getValue(WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState blockState)
	{
		return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
	}

	@Override
	public BlockState updateShape(BlockState blockState, Direction facing, BlockState facingBlockState, IWorld level, BlockPos pos, BlockPos facingPos)
	{
		if(blockState.getValue(WATERLOGGED))
			level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return super.updateShape(blockState, facing, facingBlockState, level, pos, facingPos);
	}

	@Override
	public boolean isPathfindable(BlockState blockState, IBlockReader level, BlockPos pos, PathType pathType)
	{
		return false;
	}

	@Override
	public void setPlacedBy(World level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
	{
		SupporterManager.SupporterInfo supporterInfo = PlayerPlushie.getSupporterInfo(stack);
		PlayerPlushieBlockEntity blockEntity = getBlockEntity(level, pos);

		if(blockEntity != null && supporterInfo != null)
			blockEntity.setSupporterInfo(supporterInfo);
	}

	@Override
	public void fillItemCategory(ItemGroup itemGroup, NonNullList<ItemStack> stacks)
	{
		stacks.addAll(PlayerPlushie.getPlushieItems());
	}

	@Override
	public BlockRenderType getRenderShape(BlockState blockState)
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	protected TileEntityType<PlayerPlushieBlockEntity> getBlockEntityType()
	{
		return PlayerPlushie.PLAYER_PLUSHIE_BLOCK_ENTITY.asBlockEntityType();
	}

	@Override
	public ItemStack getPickBlock(BlockState blockState, RayTraceResult result, IBlockReader level, BlockPos pos, PlayerEntity player)
	{
		PlayerPlushieBlockEntity blockEntity = getBlockEntity(level, pos);

		if(blockEntity != null)
		{
			SupporterManager.SupporterInfo supporterInfo = blockEntity.getSupporterInfo();

			if(supporterInfo != null)
				return PlayerPlushie.getPlushieItem(supporterInfo);
		}

		return super.getPickBlock(blockState, result, level, pos, player);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		SupporterManager.SupporterInfo info = PlayerPlushie.getSupporterInfo(stack);

		if(info != null)
		{
			String username = info.getUsername();

			if(level instanceof World)
			{
				World world = (World) level;
				PlayerEntity player = info.getPlayer(world::getPlayerByUUID);

				if(player != null)
					username = player.getScoreboardName();
			}

			String supportLevelName = info.getLevel().getSerializedName().toLowerCase(Locale.ROOT);
			String letter = supportLevelName.substring(0, 1).toUpperCase(Locale.ROOT);
			supportLevelName = letter + supportLevelName.substring(1);

			tooltip.add(new StringTextComponent(username)
					.withStyle(TextFormatting.GRAY, TextFormatting.ITALIC)
					.append(" (")
					.append(new StringTextComponent(supportLevelName).withStyle(TextFormatting.AQUA))
					.append(")")
			);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, WATERLOGGED);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public BlockState rotate(BlockState blockState, Rotation rotation)
	{
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, Mirror mirror)
	{
		return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
	}
}
