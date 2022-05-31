package xyz.apex.forge.utility.registrator.factory;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

@FunctionalInterface
public interface BlockFactory<BLOCK extends Block>
{
	BlockFactory<Block> DEFAULT = Block::new;

	BLOCK create(BlockBehaviour.Properties properties);
}
