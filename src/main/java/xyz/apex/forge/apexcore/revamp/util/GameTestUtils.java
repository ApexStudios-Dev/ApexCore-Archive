package xyz.apex.forge.apexcore.revamp.util;

import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.VisibleForTesting;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestAssertPosException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import xyz.apex.java.utility.nullness.NonnullSupplier;

import java.util.function.BooleanSupplier;

@VisibleForTesting
@TestOnly
public final class GameTestUtils
{
	public static void assertTrue(GameTestHelper test, BlockPos pos, BooleanSupplier supplier, NonnullSupplier<String> errorMessage)
	{
		if(!supplier.getAsBoolean())
			throw new GameTestAssertPosException(errorMessage.get(), test.absolutePos(pos), pos, test.getTick());
	}

	public static void assertBlockStateHasProperty(GameTestHelper test, BlockPos pos, BlockState blockState, Property<?> property)
	{
		assertTrue(test, pos, () -> blockState.hasProperty(property), () -> "Provided BlockState['%s'] does have required Property['%s']".formatted(blockState, property.getName()));
	}
}
