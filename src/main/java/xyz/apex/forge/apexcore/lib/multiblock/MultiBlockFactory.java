package xyz.apex.forge.apexcore.lib.multiblock;

import net.minecraft.world.level.block.state.BlockBehaviour;

@FunctionalInterface
public interface MultiBlockFactory<BLOCK extends MultiBlock>
{
	MultiBlockFactory<MultiBlock> DEFAULT = MultiBlock::new;

	BLOCK create(BlockBehaviour.Properties properties, MultiBlockPattern pattern);
}
