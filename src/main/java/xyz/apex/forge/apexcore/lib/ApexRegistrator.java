package xyz.apex.forge.apexcore.lib;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;

import xyz.apex.forge.apexcore.lib.multiblock.MultiBlock;
import xyz.apex.forge.apexcore.lib.multiblock.MultiBlockBuilder;
import xyz.apex.forge.apexcore.lib.multiblock.MultiBlockFactory;
import xyz.apex.forge.apexcore.lib.multiblock.MultiBlockPattern;
import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullFunction;
import xyz.apex.java.utility.nullness.NonnullSupplier;

public class ApexRegistrator<REGISTRATOR extends ApexRegistrator<REGISTRATOR>> extends AbstractRegistrator<REGISTRATOR>
{
	protected ApexRegistrator(String modId)
	{
		super(modId);
	}

	// region: MultiBlock
	protected final <MULTI_BLOCK extends MultiBlock, PARENT> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, PARENT> multiBlockEntry(String registryName, PARENT parent, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, NonnullSupplier<AbstractBlock.Properties> initialProperties, MultiBlockPattern pattern)
	{
		return entry(registryName, callback -> new MultiBlockBuilder<>(self, parent, registryName, callback, multiBlockFactory, initialProperties, pattern));
	}

	public final <MULTI_BLOCK extends MultiBlock, PARENT> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, PARENT> multiBlock(String registryName, PARENT parent, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, parent, Material.STONE, multiBlockFactory, pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock, PARENT> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, PARENT> multiBlock(String registryName, PARENT parent, Material material, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlockEntry(registryName, parent, multiBlockFactory, () -> AbstractBlock.Properties.of(material), pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock, PARENT> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, PARENT> multiBlock(String registryName, PARENT parent, Material material, DyeColor materialColor, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlockEntry(registryName, parent, multiBlockFactory, () -> AbstractBlock.Properties.of(material, materialColor), pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock, PARENT> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, PARENT> multiBlock(String registryName, PARENT parent, Material material, MaterialColor materialColor, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlockEntry(registryName, parent, multiBlockFactory, () -> AbstractBlock.Properties.of(material, materialColor), pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock, PARENT> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, PARENT> multiBlock(String registryName, PARENT parent, Material material, NonnullFunction<BlockState, MaterialColor> materialColorFactory, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlockEntry(registryName, parent, multiBlockFactory, () -> AbstractBlock.Properties.of(material, materialColorFactory), pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock, PARENT> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, PARENT> multiBlock(String registryName, PARENT parent, NonnullSupplier<AbstractBlock> blockProperties, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlockEntry(registryName, parent, multiBlockFactory, () -> AbstractBlock.Properties.copy(blockProperties.get()), pattern);
	}

	public final <PARENT> MultiBlockBuilder<REGISTRATOR, MultiBlock, PARENT> multiBlock(String registryName, PARENT parent, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, parent, MultiBlockFactory.DEFAULT, pattern);
	}

	public final <PARENT> MultiBlockBuilder<REGISTRATOR, MultiBlock, PARENT> multiBlock(String registryName, PARENT parent, Material material, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, parent, material, MultiBlockFactory.DEFAULT, pattern);
	}

	public final <PARENT> MultiBlockBuilder<REGISTRATOR, MultiBlock, PARENT> multiBlock(String registryName, PARENT parent, Material material, DyeColor materialColor, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, parent, material, materialColor, MultiBlockFactory.DEFAULT, pattern);
	}

	public final <PARENT> MultiBlockBuilder<REGISTRATOR, MultiBlock, PARENT> multiBlock(String registryName, PARENT parent, Material material, MaterialColor materialColor, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, parent, material, materialColor, MultiBlockFactory.DEFAULT, pattern);
	}

	public final <PARENT> MultiBlockBuilder<REGISTRATOR, MultiBlock, PARENT> multiBlock(String registryName, PARENT parent, Material material, NonnullFunction<BlockState, MaterialColor> materialColorFactory, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, parent, material, materialColorFactory, MultiBlockFactory.DEFAULT, pattern);
	}

	public final <PARENT> MultiBlockBuilder<REGISTRATOR, MultiBlock, PARENT> multiBlock(String registryName, PARENT parent, NonnullSupplier<AbstractBlock> blockProperties, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, parent, blockProperties, MultiBlockFactory.DEFAULT, pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, REGISTRATOR> multiBlock(String registryName, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, multiBlockFactory, pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, REGISTRATOR> multiBlock(String registryName, Material material, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, material, multiBlockFactory, pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, REGISTRATOR> multiBlock(String registryName, Material material, DyeColor materialColor, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, material, materialColor, multiBlockFactory, pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, REGISTRATOR> multiBlock(String registryName, Material material, MaterialColor materialColor, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, material, materialColor, multiBlockFactory, pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, REGISTRATOR> multiBlock(String registryName, Material material, NonnullFunction<BlockState, MaterialColor> materialColorFactory, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, material, materialColorFactory, multiBlockFactory, pattern);
	}

	public final <MULTI_BLOCK extends MultiBlock> MultiBlockBuilder<REGISTRATOR, MULTI_BLOCK, REGISTRATOR> multiBlock(String registryName, NonnullSupplier<AbstractBlock> blockProperties, MultiBlockFactory<MULTI_BLOCK> multiBlockFactory, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, blockProperties, multiBlockFactory, pattern);
	}

	public final MultiBlockBuilder<REGISTRATOR, MultiBlock, REGISTRATOR> multiBlock(String registryName, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, MultiBlockFactory.DEFAULT, pattern);
	}

	public final MultiBlockBuilder<REGISTRATOR, MultiBlock, REGISTRATOR> multiBlock(String registryName, Material material, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, material, MultiBlockFactory.DEFAULT, pattern);
	}

	public final MultiBlockBuilder<REGISTRATOR, MultiBlock, REGISTRATOR> multiBlock(String registryName, Material material, DyeColor materialColor, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, material, materialColor, MultiBlockFactory.DEFAULT, pattern);
	}

	public final MultiBlockBuilder<REGISTRATOR, MultiBlock, REGISTRATOR> multiBlock(String registryName, Material material, MaterialColor materialColor, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, material, materialColor, MultiBlockFactory.DEFAULT, pattern);
	}

	public final MultiBlockBuilder<REGISTRATOR, MultiBlock, REGISTRATOR> multiBlock(String registryName, Material material, NonnullFunction<BlockState, MaterialColor> materialColorFactory, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, material, materialColorFactory, MultiBlockFactory.DEFAULT, pattern);
	}

	public final MultiBlockBuilder<REGISTRATOR, MultiBlock, REGISTRATOR> multiBlock(String registryName, NonnullSupplier<AbstractBlock> blockProperties, MultiBlockPattern pattern)
	{
		return multiBlock(registryName, self, blockProperties, MultiBlockFactory.DEFAULT, pattern);
	}
	// endregion
}
