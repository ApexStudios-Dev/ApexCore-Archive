package xyz.apex.forge.apexcore.core.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import xyz.apex.forge.apexcore.core.block.entity.PlayerPlushieBlockEntity;
import xyz.apex.forge.apexcore.core.init.PlayerPlushie;
import xyz.apex.forge.apexcore.lib.block.VoxelShaper;
import xyz.apex.forge.apexcore.revamp.block.BaseBlock;
import xyz.apex.java.utility.nullness.NonnullConsumer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public final class PlayerPlushieBlock extends BaseBlock.WithBlockEntity<PlayerPlushieBlockEntity> implements SimpleWaterloggedBlock
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
	public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext ctx)
	{
		var facing = getFacing(blockState);
		return SHAPER.get(facing);
	}

	@Override
	public boolean isPathfindable(BlockState blockState, BlockGetter level, BlockPos pos, PathComputationType pathType)
	{
		return false;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
	{
		var supporterInfo = PlayerPlushie.getSupporterInfo(stack);
		var blockEntity = getBlockEntity(level, pos);

		if(blockEntity != null && supporterInfo != null)
			blockEntity.setSupporterInfo(supporterInfo);
	}

	@Override
	public void fillItemCategory(CreativeModeTab itemGroup, NonNullList<ItemStack> stacks)
	{
		stacks.addAll(PlayerPlushie.getPlushieItems());
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState)
	{
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	protected BlockEntityType<PlayerPlushieBlockEntity> getBlockEntityType()
	{
		return PlayerPlushie.PLAYER_PLUSHIE_BLOCK_ENTITY.asBlockEntityType();
	}

	@Override
	public ItemStack getCloneItemStack(BlockState blockState, HitResult result, BlockGetter level, BlockPos pos, Player player)
	{
		var blockEntity = getBlockEntity(level, pos);

		if(blockEntity != null)
		{
			var supporterInfo = blockEntity.getSupporterInfo();

			if(supporterInfo != null)
				return PlayerPlushie.getPlushieItem(supporterInfo);
		}

		return super.getCloneItemStack(blockState, result, level, pos, player);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
	{
		var info = PlayerPlushie.getSupporterInfo(stack);

		if(info != null)
		{
			var username = info.getUsername();

			if(level instanceof Level world)
			{
				var player = info.getPlayer(world::getPlayerByUUID);

				if(player != null)
					username = player.getScoreboardName();
			}

			var supportLevelName = info.getLevel().getSerializedName().toLowerCase(Locale.ROOT);
			var letter = supportLevelName.substring(0, 1).toUpperCase(Locale.ROOT);
			supportLevelName = letter + supportLevelName.substring(1);

			tooltip.add(new TextComponent(username)
					.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)
					.append(" (")
					.append(new TextComponent(supportLevelName).withStyle(ChatFormatting.AQUA))
					.append(")")
			);
		}
	}
}
