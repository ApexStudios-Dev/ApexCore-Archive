package xyz.apex.forge.utility.registrator.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.RegistryObject;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("unused")
public final class BlockEntityEntry<BLOCK_ENTITY extends BlockEntity> extends RegistryEntry<BlockEntityType<BLOCK_ENTITY>> implements NonnullSupplier<BlockEntityType<BLOCK_ENTITY>>
{
	public BlockEntityEntry(AbstractRegistrator<?> registrator, RegistryObject<BlockEntityType<BLOCK_ENTITY>> delegate)
	{
		super(registrator, delegate);
	}

	public boolean isInBlockEntityTypeTag(Tag<BlockEntityType<?>> tag)
	{
		return asBlockEntityType().isIn(tag);
	}

	public boolean isBlockEntityType(BlockEntityType<?> blockEntityType)
	{
		return asBlockEntityType() == blockEntityType;
	}

	@Nullable
	public BLOCK_ENTITY createBlockEntity(BlockPos pos, BlockState blockState)
	{
		return asBlockEntityType().create(pos, blockState);
	}

	@Nullable
	public BLOCK_ENTITY getBlockEntity(BlockGetter level, BlockPos pos)
	{
		return asBlockEntityType().getBlockEntity(level, pos);
	}

	public Optional<BLOCK_ENTITY> getBlockEntityOptional(BlockGetter level, BlockPos pos)
	{
		return Optional.ofNullable(getBlockEntity(level, pos));
	}

	public boolean isValidBlockState(BlockState blockState)
	{
		return asBlockEntityType().isValid(blockState);
	}

	public boolean isValidBlock(Block block)
	{
		return asBlockEntityType().validBlocks.contains(block);
	}

	public BlockEntityType<BLOCK_ENTITY> asBlockEntityType()
	{
		return get();
	}

	public static <BLOCK_ENTITY extends BlockEntity> BlockEntityEntry<BLOCK_ENTITY> cast(RegistryEntry<BlockEntityType<BLOCK_ENTITY>> registryEntry)
	{
		return cast(BlockEntityEntry.class, registryEntry);
	}

	public static <BLOCK_ENTITY extends BlockEntity> BlockEntityEntry<BLOCK_ENTITY> cast(com.tterrag.registrate.util.entry.RegistryEntry<BlockEntityType<BLOCK_ENTITY>> registryEntry)
	{
		return cast(BlockEntityEntry.class, registryEntry);
	}
}
