package xyz.apex.forge.apexcore.core.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import xyz.apex.forge.apexcore.core.block.entity.PlayerPlushieBlockEntity;
import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.lib.block.VoxelShaper;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;
import xyz.apex.forge.apexcore.revamp.block.BaseBlock;
import xyz.apex.java.utility.nullness.NonnullConsumer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public final class PlayerPlushieBlock extends BaseBlock.WithBlockEntity<PlayerPlushieBlockEntity>
{
	public static final VoxelShape SHAPE = box(2D, 0D, 3D, 14D, 13D, 14D);
	public static final VoxelShaper SHAPER = VoxelShaper.forHorizontal(SHAPE, Direction.NORTH);

	public PlayerPlushieBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	protected void registerProperties(NonnullConsumer<Property<?>> consumer)
	{
		super.registerProperties(consumer);
		consumer.accept(FACING_4_WAY);
		consumer.accept(WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState blockState, IBlockReader level, BlockPos pos, ISelectionContext ctx)
	{
		Direction facing = getFacing(blockState);
		return SHAPER.get(facing);
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
}
