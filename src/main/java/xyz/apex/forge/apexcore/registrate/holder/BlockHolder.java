package xyz.apex.forge.apexcore.registrate.holder;

import net.minecraft.world.level.block.Block;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.BlockBuilder;
import xyz.apex.forge.apexcore.registrate.builder.factory.BlockFactory;

@SuppressWarnings("unchecked")
public interface BlockHolder<OWNER extends CoreRegistrate<OWNER> & BlockEntityHolder<OWNER>> extends ItemHolder<OWNER>
{
	private OWNER self()
	{
		return (OWNER) this;
	}

	default BlockBuilder<OWNER, Block, OWNER> block()
	{
		return block(self(), BlockFactory.DEFAULT);
	}

	default BlockBuilder<OWNER, Block, OWNER> block(String name)
	{
		return block(self(), name, BlockFactory.DEFAULT);
	}

	default <PARENT> BlockBuilder<OWNER, Block, PARENT> block(PARENT parent)
	{
		return block(parent, self().currentName(), BlockFactory.DEFAULT);
	}

	default <PARENT> BlockBuilder<OWNER, Block, PARENT> block(PARENT parent, String name)
	{
		return block(parent, name, BlockFactory.DEFAULT);
	}

	default <BLOCK extends Block> BlockBuilder<OWNER, BLOCK, OWNER> block(BlockFactory<BLOCK> factory)
	{
		return block(self(), factory);
	}

	default <BLOCK extends Block> BlockBuilder<OWNER, BLOCK, OWNER> block(String name, BlockFactory<BLOCK> factory)
	{
		return block(self(), name, factory);
	}

	default <BLOCK extends Block, PARENT> BlockBuilder<OWNER, BLOCK, PARENT> block(PARENT parent, BlockFactory<BLOCK> factory)
	{
		return block(parent, self().currentName(), factory);
	}

	default <BLOCK extends Block, PARENT> BlockBuilder<OWNER, BLOCK, PARENT> block(PARENT parent, String name, BlockFactory<BLOCK> factory)
	{
		return self().entry(name, callback -> new BlockBuilder<>(self(), parent, name, callback, factory)
				.transform(BlockBuilder::applyDefaults)
		);
	}
}
