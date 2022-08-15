package xyz.apex.forge.apexcore.registrate.holder;

import net.minecraft.world.level.block.entity.BlockEntity;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.BlockEntityBuilder;
import xyz.apex.forge.apexcore.registrate.builder.factory.BlockEntityFactory;

@SuppressWarnings("unchecked")
public interface BlockEntityHolder<OWNER extends CoreRegistrate<OWNER> & BlockEntityHolder<OWNER>> extends BlockHolder<OWNER>
{
	private OWNER self()
	{
		return (OWNER) this;
	}

	default <BLOCK_ENTITY extends BlockEntity> BlockEntityBuilder<OWNER, BLOCK_ENTITY, OWNER> blockEntity(BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return blockEntity(self(), factory);
	}

	default <BLOCK_ENTITY extends BlockEntity> BlockEntityBuilder<OWNER, BLOCK_ENTITY, OWNER> blockEntity(String name, BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return blockEntity(self(), name, factory);
	}

	default <BLOCK_ENTITY extends BlockEntity, PARENT> BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> blockEntity(PARENT parent, BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return blockEntity(parent, self().currentName(), factory);
	}

	default <BLOCK_ENTITY extends BlockEntity, PARENT> BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> blockEntity(PARENT parent, String name, BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return self().entry(name, callback -> new BlockEntityBuilder<>(self(), parent, name, callback, factory));
	}
}