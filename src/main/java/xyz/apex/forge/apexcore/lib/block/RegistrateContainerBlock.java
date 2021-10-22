package xyz.apex.forge.apexcore.lib.block;

import com.tterrag.registrate.AbstractRegistrate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class RegistrateContainerBlock<T extends TileEntity> extends RegistrateBlockEntityBlock<T>
{
	public RegistrateContainerBlock(Properties properties, AbstractRegistrate<?> registrate)
	{
		super(properties, registrate);
	}

	@Override
	public ActionResultType use(BlockState blockState, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
	{
		ItemStack stack = player.getItemInHand(hand);

		if(player instanceof ServerPlayerEntity)
		{
			if(!openContainerScreen(blockState, level, pos, (ServerPlayerEntity) player, hand, stack, stack.getHoverName()))
				return ActionResultType.PASS;
		}

		return ActionResultType.sidedSuccess(level.isClientSide);
	}

	protected boolean openContainerScreen(BlockState blockState, World level, BlockPos pos, ServerPlayerEntity player, Hand hand, ItemStack stack, ITextComponent titleComponent)
	{
		NetworkHooks.openGui(player, getMenuProvider(blockState, level, pos));
		return true;
	}
}
