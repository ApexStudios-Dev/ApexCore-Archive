package xyz.apex.forge.apexcore.registrate.builder;

import com.google.common.collect.Sets;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.factory.BlockEntityFactory;
import xyz.apex.forge.apexcore.registrate.entry.BlockEntityEntry;
import xyz.apex.forge.apexcore.registrate.holder.BlockEntityHolder;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

public final class BlockEntityBuilder<
		OWNER extends CoreRegistrate<OWNER> & BlockEntityHolder<OWNER>,
		BLOCK_ENTITY extends BlockEntity,
		PARENT
> extends AbstractBuilder<OWNER, BlockEntityType<?>, BlockEntityType<BLOCK_ENTITY>, PARENT, BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT>, BlockEntityEntry<BLOCK_ENTITY>>
{
	private final BlockEntityFactory<BLOCK_ENTITY> blockEntityFactory;
	private final Set<NonNullSupplier<? extends Block>> validBlocks = Sets.newHashSet();
	private Supplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super BLOCK_ENTITY>>> rendererFactory = () -> null;

	public BlockEntityBuilder(OWNER owner, PARENT parent, String name, BuilderCallback callback, BlockEntityFactory<BLOCK_ENTITY> blockEntityFactory)
	{
		super(owner, parent, name, callback, ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, BlockEntityEntry::new, BlockEntityEntry::cast);

		this.blockEntityFactory = blockEntityFactory;

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> onRegister(blockEntityType -> OneTimeEventReceiver.addModListener(FMLClientSetupEvent.class, event -> event.enqueueWork(() -> {
			var renderer = rendererFactory.get();

			if(renderer != null)
				BlockEntityRenderers.register(blockEntityType, renderer::apply);
		}))));
	}

	public BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> validBlock(NonNullSupplier<? extends Block> block)
	{
		validBlocks.add(block);
		return this;
	}

	@SafeVarargs
	public final BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> validBlock(NonNullSupplier<? extends Block>... blocks)
	{
		Collections.addAll(validBlocks, blocks);
		return this;
	}

	public BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> renderer(Supplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super BLOCK_ENTITY>>> rendererFactory)
	{
		this.rendererFactory = rendererFactory;
		return this;
	}

	@Override
	protected BlockEntityType<BLOCK_ENTITY> createEntry()
	{
		return BlockEntityType.Builder.of((pos, blockState) -> blockEntityFactory.create(asSupplier().get(), pos, blockState), validBlocks.stream().map(Supplier::get).toArray(Block[]::new)).build(null);
	}
}
