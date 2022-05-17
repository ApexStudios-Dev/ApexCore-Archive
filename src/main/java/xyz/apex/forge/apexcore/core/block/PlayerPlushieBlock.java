package xyz.apex.forge.apexcore.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import xyz.apex.forge.apexcore.lib.block.VoxelShaper;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class PlayerPlushieBlock extends HorizontalBlock implements IWaterLoggable
{
	public static final EnumProperty<Player> PLAYER = EnumProperty.create("player", Player.class);
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final VoxelShape SHAPE = box(2D, 0D, 3D, 14D, 11D, 14D);
	public static final VoxelShaper SHAPER = VoxelShaper.forHorizontal(SHAPE, Direction.NORTH);

	public static final String NBT_PLAYER = "PlushiePlayer";
	public static final String NBT_PLAYER_INDEX = "PlushiePlayer.Index";

	public PlayerPlushieBlock(Properties properties)
	{
		super(properties);

		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(PLAYER, Player.APEX));
	}

	@Override
	public VoxelShape getShape(BlockState blockState, IBlockReader level, BlockPos pos, ISelectionContext ctx)
	{
		Direction facing = blockState.getValue(FACING);
		return SHAPER.get(facing);
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
	public ItemStack getPickBlock(BlockState blockState, RayTraceResult target, IBlockReader level, BlockPos pos, PlayerEntity player)
	{
		ItemStack stack = super.getPickBlock(blockState, target, level, pos, player);
		Player modelPlayer = blockState.getValue(PLAYER);

		CompoundNBT stackTag = stack.getOrCreateTag();
		stackTag.putString(NBT_PLAYER, modelPlayer.serializedName);
		stackTag.putInt(NBT_PLAYER_INDEX, modelPlayer.ordinal());

		return stack;
	}

	@Override
	public void setPlacedBy(World level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
	{
		CompoundNBT stackTag = stack.getTag();

		if(stackTag != null && stackTag.contains(NBT_PLAYER_INDEX, Constants.NBT.TAG_ANY_NUMERIC))
		{
			int playerIndex = stackTag.getInt(NBT_PLAYER_INDEX);

			for(Player player : PLAYER.getPossibleValues())
			{
				if(player.ordinal() == playerIndex)
				{
					level.setBlock(pos, blockState.setValue(PLAYER, player), 3);
					break;
				}
			}
		}
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, PLAYER, WATERLOGGED);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public void fillItemCategory(ItemGroup itemGroup, NonNullList<ItemStack> items)
	{
		for(Player player : PLAYER.getPossibleValues())
		{
			ItemStack stack = asItem().getDefaultInstance();
			CompoundNBT stackTag = stack.getOrCreateTag();
			stackTag.putString(NBT_PLAYER, player.serializedName);
			stackTag.putInt(NBT_PLAYER_INDEX, player.ordinal());
			items.add(stack);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		CompoundNBT stackTag = stack.getTag();

		if(stackTag != null && stackTag.contains(NBT_PLAYER_INDEX, Constants.NBT.TAG_ANY_NUMERIC))
		{
			int stackIndex = stackTag.getInt(NBT_PLAYER_INDEX);

			for(Player player : PLAYER.getPossibleValues())
			{
				if(player.ordinal() == stackIndex)
				{
					String username = player.getUsername();

					if(level instanceof World)
					{
						PlayerEntity entity = ((World) level).getPlayerByUUID(player.getPlayerId());

						if(entity != null)
							username = entity.getScoreboardName();
					}

					IFormattableTextComponent component = new StringTextComponent(username).withStyle(TextFormatting.GRAY, TextFormatting.ITALIC);

					SupporterManager.getSupporters().stream().filter(info -> info.isFor(player.getPlayerId())).findFirst().ifPresent(info -> {
						String name = info.getLevel().getSerializedName();
						String englishName = name.toLowerCase(Locale.ROOT);
						String letter = englishName.substring(0, 1).toUpperCase(Locale.ROOT);
						englishName = letter + englishName.substring(1);

						component.append(" (").append(new StringTextComponent(englishName).withStyle(TextFormatting.AQUA)).append(")");
					});

					tooltip.add(component);

					break;
				}
			}
		}
	}

	public enum Player implements IStringSerializable
	{
		APEX("apex", "ApexSPG", UUID.fromString("43fd393b-879d-45ec-b2d5-ce8c4688ab66")),
		FANTASY("fantasy", "FantasyGaming", UUID.fromString("598535bd-f330-4123-b4d0-c6e618390477")),
		RUDY("rudy", "RudySPG", UUID.fromString("16d60e4e-53ba-4288-8062-ca1ea074b501")),
		TOBI("tobi", "TroublesomeTobi", UUID.fromString("ae3f6ca6-b28c-479b-9c97-4be7df600041"))
		;

		private final String serializedName;
		private final String username;
		private final UUID playerId;

		Player(String serializedName, String username, UUID playerId)
		{
			this.serializedName = serializedName;
			this.username = username;
			this.playerId = playerId;
		}

		@Override
		public String getSerializedName()
		{
			return serializedName;
		}

		public String getUsername()
		{
			return username;
		}

		public UUID getPlayerId()
		{
			return playerId;
		}
	}
}
