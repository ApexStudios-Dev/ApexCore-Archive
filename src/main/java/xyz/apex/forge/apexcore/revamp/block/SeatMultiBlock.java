package xyz.apex.forge.apexcore.revamp.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

import xyz.apex.java.utility.nullness.NonnullConsumer;

public abstract class SeatMultiBlock extends BaseMultiBlock implements ISeatMultiBlock
{
	public SeatMultiBlock(Properties properties)
	{
		super(properties);

		registerDefaultState(defaultBlockState().setValue(OCCUPIED, false));
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		return useSeatBlock(blockState, level, pos, player);
	}

	@Override
	protected void registerProperties(NonnullConsumer<Property<?>> consumer)
	{
		super.registerProperties(consumer);
		consumer.accept(OCCUPIED);
	}
}
