package xyz.apex.forge.apexcore.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

@Deprecated(forRemoval = true)
public abstract class ContainerBlock<T extends BlockEntity> extends BlockEntityBlock<T>
{
	public ContainerBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		ItemStack stack = player.getItemInHand(hand);

		if(player instanceof ServerPlayer serverPlayer)
		{
			if(!openContainerScreen(blockState, level, pos, serverPlayer, hand, stack, stack.getHoverName()))
				return InteractionResult.PASS;
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	protected boolean openContainerScreen(BlockState blockState, Level level, BlockPos pos, ServerPlayer player, InteractionHand hand, ItemStack stack, Component titleComponent)
	{
		NetworkHooks.openGui(player, getMenuProvider(blockState, level, pos));
		return true;
	}
}