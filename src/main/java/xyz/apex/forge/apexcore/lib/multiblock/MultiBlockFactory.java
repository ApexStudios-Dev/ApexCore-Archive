package xyz.apex.forge.apexcore.lib.multiblock;

import net.minecraft.block.AbstractBlock;

@FunctionalInterface
public interface MultiBlockFactory<BLOCK extends MultiBlock>
{
	MultiBlockFactory<MultiBlock> DEFAULT = MultiBlock::new;

	BLOCK create(AbstractBlock.Properties properties, MultiBlockPattern pattern);
}
