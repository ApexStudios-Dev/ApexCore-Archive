package xyz.apex.forge.apexcore.lib.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.ToIntFunction;

public final class BlockHelper
{
	public static ToIntFunction<BlockState> litBlockEmission(int lightEmission)
	{
		return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightEmission : 0;
	}

	public static Boolean never(BlockState blockState, IBlockReader level, BlockPos pos, EntityType<?> entityType)
	{
		return false;
	}

	public static Boolean always(BlockState blockState, IBlockReader level, BlockPos pos, EntityType<?> entityType)
	{
		return true;
	}

	public static Boolean ocelotOrParrot(BlockState blockState, IBlockReader level, BlockPos pos, EntityType<?> entityType)
	{
		return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
	}

	public static boolean always(BlockState blockState, IBlockReader level, BlockPos pos)
	{
		return true;
	}

	public static boolean never(BlockState blockState, IBlockReader level, BlockPos pos)
	{
		return false;
	}

	public static void playBreakSound(World level, BlockPos pos, @Nullable Entity entity)
	{
		BlockState blockState = level.getBlockState(pos);
		SoundType soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getBreakSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static void playPlaceSound(World level, BlockPos pos, @Nullable Entity entity)
	{
		BlockState blockState = level.getBlockState(pos);
		SoundType soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static void playStepSound(World level, BlockPos pos, @Nullable Entity entity)
	{
		BlockState blockState = level.getBlockState(pos);
		SoundType soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getStepSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static void playFallSound(World level, BlockPos pos, @Nullable Entity entity)
	{
		BlockState blockState = level.getBlockState(pos);
		SoundType soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getFallSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static void playHitSound(World level, BlockPos pos, @Nullable Entity entity)
	{
		BlockState blockState = level.getBlockState(pos);
		SoundType soundType = blockState.getSoundType(level, pos, entity);
		level.playSound(null, pos, soundType.getHitSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * .8F);
	}

	public static BlockState copyBlockProperties(BlockState from, BlockState to)
	{
		for(Property<?> property : from.getProperties())
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
