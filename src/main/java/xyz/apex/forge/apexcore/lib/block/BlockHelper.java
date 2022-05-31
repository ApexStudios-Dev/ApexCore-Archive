package xyz.apex.forge.apexcore.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;
import java.util.function.ToIntFunction;

public final class BlockHelper
{
	public static ToIntFunction<BlockState> litBlockEmission(int lightEmission)
	{
		return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightEmission : 0;
	}

	public static Boolean never(BlockState blockState, BlockGetter level, BlockPos pos, EntityType<?> entityType)
	{
		return false;
	}

	public static Boolean always(BlockState blockState, BlockGetter level, BlockPos pos, EntityType<?> entityType)
	{
		return true;
	}

	public static Boolean ocelotOrParrot(BlockState blockState, BlockGetter level, BlockPos pos, EntityType<?> entityType)
	{
		return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
	}

	public static boolean always(BlockState blockState, BlockGetter level, BlockPos pos)
	{
		return true;
	}

	public static boolean never(BlockState blockState, BlockGetter level, BlockPos pos)
	{
		return false;
	}

	public static void playBreakSound(Level level, BlockPos pos, @Nullable Entity entity)
	{
		var blockState = level.getBlockState(pos);
		var soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static void playPlaceSound(Level level, BlockPos pos, @Nullable Entity entity)
	{
		var blockState = level.getBlockState(pos);
		var soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static void playStepSound(Level level, BlockPos pos, @Nullable Entity entity)
	{
		var blockState = level.getBlockState(pos);
		var soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getStepSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static void playFallSound(Level level, BlockPos pos, @Nullable Entity entity)
	{
		var blockState = level.getBlockState(pos);
		var soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getFallSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static void playHitSound(Level level, BlockPos pos, @Nullable Entity entity)
	{
		var blockState = level.getBlockState(pos);
		var soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getHitSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static BlockState copyBlockProperties(BlockState from, BlockState to)
	{
		for(var property : from.getProperties())
		{
			to = copyBlockProperty(from, to, property);
		}

		return to;
	}

	public static <T extends Comparable<T>> BlockState copyBlockProperty(BlockState from, BlockState to, Property<T> property)
	{
		if(from.hasProperty(property) && to.hasProperty(property))
			return to.setValue(property, from.getValue(property));
		return to;
	}
}
