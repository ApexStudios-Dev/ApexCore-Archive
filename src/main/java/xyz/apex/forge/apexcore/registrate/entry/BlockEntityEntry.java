package xyz.apex.forge.apexcore.registrate.entry;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

import java.util.Optional;

public final class BlockEntityEntry<BLOCK_ENTITY extends BlockEntity> extends RegistryEntry<BlockEntityType<BLOCK_ENTITY>>
{
	public BlockEntityEntry(CoreRegistrate<?> owner, RegistryObject<BlockEntityType<BLOCK_ENTITY>> delegate)
	{
		super(owner, delegate);
	}

	@Nullable
	public BLOCK_ENTITY create(BlockPos pos, BlockState blockState)
	{
		return get().create(pos, blockState);
	}

	public boolean is(@Nullable BlockEntity blockEntity)
	{
		return blockEntity != null && blockEntity.getType() == get();
	}

	public Optional<BLOCK_ENTITY> get(BlockGetter level, BlockPos pos)
	{
		return Optional.ofNullable(getNullable(level, pos));
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public BLOCK_ENTITY getNullable(BlockGetter level, BlockPos pos)
	{
		var blockEntity = level.getBlockEntity(pos);
		return is(blockEntity) ? (BLOCK_ENTITY) blockEntity : null;
	}

	public static <BLOCK_ENTITY extends BlockEntity> BlockEntityEntry<BLOCK_ENTITY> cast(com.tterrag.registrate.util.entry.RegistryEntry<BlockEntityType<BLOCK_ENTITY>> registryEntry)
	{
		return cast(BlockEntityEntry.class, registryEntry);
	}
}