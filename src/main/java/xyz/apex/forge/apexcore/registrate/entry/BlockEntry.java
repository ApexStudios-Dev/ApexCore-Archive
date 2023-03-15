package xyz.apex.forge.apexcore.registrate.entry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import xyz.apex.forge.apexcore.lib.util.RegistryHelper;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;

public final class BlockEntry<BLOCK extends Block> extends ItemLikeEntry<BLOCK>
{
	public BlockEntry(CoreRegistrate<?> owner, RegistryObject<BLOCK> delegate)
	{
		super(owner, delegate);
	}

	public BlockState defaultBlockState()
	{
		return get().defaultBlockState();
	}

	public boolean isIn(BlockState blockState)
	{
		return is(blockState.getBlock());
	}

	public boolean is(@Nullable Block block)
	{
		return get() == block;
	}

	public boolean hasBlockTag(TagKey<Block> tag)
	{
		return RegistryHelper.hasTag(ForgeRegistries.BLOCKS, tag, get());
	}

	public static <BLOCK extends Block> BlockEntry<BLOCK> cast(RegistryEntry<BLOCK> registryEntry)
	{
		return cast(BlockEntry.class, registryEntry);
	}
}
