package xyz.apex.forge.apexcore.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

// Does not support having BlockEntities or Containers
public class WallLightBlock extends BaseBlock
{
	public WallLightBlock(Properties properties)
	{
		super(properties);
	}

	// region: WallLight
	@Nullable
	@Override
	protected BlockState modifyPlacementState(BlockState placementBlockState, BlockPlaceContext ctx)
	{
		var blockState = super.modifyPlacementState(placementBlockState, ctx);

		if(blockState != null && supportsFacing(blockState))
		{
			var level = ctx.getLevel();
			var pos = ctx.getClickedPos();
			var nearestLookingDirections = ctx.getNearestLookingDirections();

			for(var direction : nearestLookingDirections)
			{
				if(direction.getAxis().isHorizontal())
				{
					var opposite = direction.getOpposite();
					var modifiedBlockState = setFacing(blockState, opposite);

					if(modifiedBlockState.canSurvive(level, pos))
						return modifiedBlockState;
				}
			}
		}

		return blockState;
	}

	@Override
	public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
	{
		if(supportsFacing(blockState))
		{
			var facing = getFacing(blockState);
			var oppositePos = pos.relative(facing.getOpposite());
			var oppositeBlockState = level.getBlockState(oppositePos);
			return oppositeBlockState.isFaceSturdy(level, oppositePos, facing);
		}

		return super.canSurvive(blockState, level, pos);
	}

	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource rng)
	{
		if(supportsWaterLogging(blockState) && isWaterLogged(blockState))
			return;

		var x = pos.getX() + .5D;
		var y = pos.getY() + .5D;
		var z = pos.getZ() + .5D;

		spawnLightParticles(level, pos, blockState, x, y, z, rng);
	}

	protected void spawnLightParticles(Level level, BlockPos pos, BlockState blockState, double pX, double pY, double pZ, RandomSource rng)
	{
		var x = pX;
		var y = pY;
		var z = pZ;

		if(supportsFacing(blockState))
		{
			var facing = getFacing(blockState).getOpposite();

			var hStep = .12D;
			var vStep = .24D;

			x = pX + (hStep * facing.getStepX());
			y = pY + .2D + vStep;
			z = pZ + (hStep * facing.getStepZ());
		}

		onLightParticle(level, pos, blockState, x, y, z, rng);
	}

	protected void onLightParticle(Level level, BlockPos pos, BlockState blockState, double pX, double pY, double pZ, RandomSource rng)
	{
		level.addParticle(ParticleTypes.SMOKE, pX, pY, pZ, 0D, 0D, 0D);
		level.addParticle(ParticleTypes.FLAME, pX, pY, pZ, 0D, 0D, 0D);
	}
	// endregion
}
