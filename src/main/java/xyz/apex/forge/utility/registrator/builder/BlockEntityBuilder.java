package xyz.apex.forge.utility.registrator.builder;

import com.google.common.collect.Sets;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.Util;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.tags.Tag;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.entry.BlockEntityEntry;
import xyz.apex.forge.utility.registrator.factory.BlockEntityFactory;
import xyz.apex.java.utility.nullness.NonnullFunction;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

@SuppressWarnings({ "deprecation", "ConstantConditions", "unused" })
public final class BlockEntityBuilder<OWNER extends AbstractRegistrator<OWNER>, BLOCK_ENTITY extends BlockEntity, PARENT> extends RegistratorBuilder<OWNER, BlockEntityType<?>, BlockEntityType<BLOCK_ENTITY>, PARENT, BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT>, BlockEntityEntry<BLOCK_ENTITY>>
{
	private final BlockEntityFactory<BLOCK_ENTITY> blockEntityFactory;
	private final Set<NonnullSupplier<? extends Block>> validBlocks = Sets.newHashSet();
	@Nullable private NonnullSupplier<NonnullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super BLOCK_ENTITY>>> renderer = null;

	public BlockEntityBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback, BlockEntityFactory<BLOCK_ENTITY> blockEntityFactory)
	{
		super(owner, parent, registryName, callback, BlockEntityType.class, BlockEntityEntry::new, BlockEntityEntry::cast);

		this.blockEntityFactory = blockEntityFactory;
		onRegister(this::onRegister);
	}

	private void onRegister(BlockEntityType<BLOCK_ENTITY> blockEntityType)
	{
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> OneTimeEventReceiver.addModListener(FMLClientSetupEvent.class, event -> event.enqueueWork(() -> registerRenderer(blockEntityType))));
	}

	@OnlyIn(Dist.CLIENT)
	private void registerRenderer(BlockEntityType<BLOCK_ENTITY> blockEntityType)
	{
		if(renderer != null)
			BlockEntityRenderers.register(blockEntityType, renderer.get()::apply);
	}

	@Override
	protected @NonnullType BlockEntityType<BLOCK_ENTITY> createEntry()
	{
		var supplier = toSupplier();
		var validBlocks = this.validBlocks.stream().map(NonnullSupplier::get).toArray(Block[]::new);
		var builder = BlockEntityType.Builder.of((pos, blockState) -> blockEntityFactory.create(supplier.get(), pos, blockState), validBlocks);
		return builder.build(Util.fetchChoiceType(References.BLOCK_ENTITY, getRegistryNameFull()));

	}

	public BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> validBlock(NonnullSupplier<? extends Block> block)
	{
		validBlocks.add(block);
		return this;
	}

	@SafeVarargs
	public final BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> validBlocks(NonnullSupplier<? extends Block>... blocks)
	{
		Collections.addAll(validBlocks, blocks);
		return this;
	}

	public BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> renderer(NonnullSupplier<NonnullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super BLOCK_ENTITY>>> renderer)
	{
		this.renderer = renderer;
		return this;
	}

	@SafeVarargs
	public final BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> tag(Tag.Named<BlockEntityType<?>>... tags)
	{
		return tag(AbstractRegistrator.BLOCK_ENTITY_TAGS_PROVIDER, tags);
	}

	@SafeVarargs
	public final BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> removeTag(Tag.Named<BlockEntityType<?>>... tags)
	{
		return removeTag(AbstractRegistrator.BLOCK_ENTITY_TAGS_PROVIDER, tags);
	}
}
