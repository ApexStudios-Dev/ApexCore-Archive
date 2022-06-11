package xyz.apex.forge.apexcore.lib.multiblock;

import net.minecraft.world.level.block.state.BlockBehaviour;

// Replaced with revamped code
@Deprecated(since = "4.3.5", forRemoval = true)
@FunctionalInterface
public interface MultiBlockFactory<BLOCK extends MultiBlock>
{
	MultiBlockFactory<MultiBlock> DEFAULT = MultiBlock::new;

	BLOCK create(BlockBehaviour.Properties properties, MultiBlockPattern pattern);
}
