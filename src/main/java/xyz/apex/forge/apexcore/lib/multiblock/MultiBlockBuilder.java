package xyz.apex.forge.apexcore.lib.multiblock;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.DistExecutor;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.builder.BlockEntityBuilder;
import xyz.apex.forge.utility.registrator.builder.ItemBuilder;
import xyz.apex.forge.utility.registrator.builder.RegistratorBuilder;
import xyz.apex.forge.utility.registrator.entry.BlockEntry;
import xyz.apex.forge.utility.registrator.factory.BlockEntityFactory;
import xyz.apex.forge.utility.registrator.factory.item.BlockItemFactory;
import xyz.apex.java.utility.nullness.NonnullBiConsumer;
import xyz.apex.java.utility.nullness.NonnullFunction;
import xyz.apex.java.utility.nullness.NonnullSupplier;
import xyz.apex.java.utility.nullness.NonnullUnaryOperator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.ToIntFunction;

// Copy of BlockBuilder but modified for MultiBlock
public class MultiBlockBuilder<OWNER extends AbstractRegistrator<OWNER>, BLOCK extends MultiBlock, PARENT> extends RegistratorBuilder<OWNER, Block, BLOCK, PARENT, MultiBlockBuilder<OWNER, BLOCK, PARENT>, BlockEntry<BLOCK>>
{
	private final MultiBlockFactory<BLOCK> blockFactory;
	private NonnullSupplier<BlockBehaviour.Properties> initialProperties;
	private NonnullUnaryOperator<BlockBehaviour.Properties> propertiesModifier = NonnullUnaryOperator.identity();
	private final List<NonnullSupplier<NonnullSupplier<RenderType>>> renderTypes = Lists.newArrayList();
	@Nullable private NonnullSupplier<NonnullSupplier<BlockColor>> colorHandler = null;
	@Nullable private ItemBuilder<OWNER, ?, MultiBlockBuilder<OWNER, BLOCK, PARENT>> itemBuilder = null;
	@Nullable private BlockEntityBuilder<OWNER, ?, MultiBlockBuilder<OWNER, BLOCK, PARENT>> blockEntityBuilder = null;
	private final MultiBlockPattern pattern;

	public MultiBlockBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback, MultiBlockFactory<BLOCK> blockFactory, NonnullSupplier<BlockBehaviour.Properties> initialProperties, MultiBlockPattern pattern)
	{
		super(owner, parent, registryName, callback, Block.class, BlockEntry::new, BlockEntry::cast);

		this.pattern = pattern;
		this.blockFactory = blockFactory;
		this.initialProperties = initialProperties;
		onRegister(this::onRegister);

		// apply normal Registrate defaults
		defaultBlockState().defaultLoot().defaultLang();
	}

	private void onRegister(BLOCK block)
	{
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			OneTimeEventReceiver.addModListener(ColorHandlerEvent.Block.class, event -> registerBlockColor(event, block));
			registerRenderTypes(block);
		});
	}

	@OnlyIn(Dist.CLIENT)
	private void registerBlockColor(ColorHandlerEvent.Block event, BLOCK block)
	{
		if(colorHandler != null)
			event.getBlockColors().register(colorHandler.get().get(), block);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerRenderTypes(BLOCK block)
	{
		if(renderTypes.size() == 1)
		{
			RenderType renderType = renderTypes.get(0).get().get();
			ItemBlockRenderTypes.setRenderLayer(block, renderType);
		}
		else if(renderTypes.size() > 1)
		{
			ImmutableSet<RenderType> renderTypes = this.renderTypes.stream().map(NonnullSupplier::get).map(NonnullSupplier::get).collect(ImmutableSet.toImmutableSet());
			ItemBlockRenderTypes.setRenderLayer(block, renderTypes::contains);
		}
	}

	@Override
	protected @NonnullType BLOCK createEntry()
	{
		if(itemBuilder != null)
			copyMappingsTo(itemBuilder);
		if(blockEntityBuilder != null)
			copyMappingsTo(blockEntityBuilder);

		var properties = initialProperties.get();
		properties = propertiesModifier.apply(properties);
		return blockFactory.create(properties, pattern);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> properties(NonnullUnaryOperator<BlockBehaviour.Properties> propertiesModifier)
	{
		this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
		return this;
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> initialProperties(Material material)
	{
		this.initialProperties = () -> BlockBehaviour.Properties.of(material);
		return this;
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> initialProperties(Material material, DyeColor materialColor)
	{
		this.initialProperties = () -> BlockBehaviour.Properties.of(material, materialColor);
		return this;
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> initialProperties(Material material, MaterialColor materialColor)
	{
		this.initialProperties = () -> BlockBehaviour.Properties.of(material, materialColor);
		return this;
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> initialProperties(Material material, NonnullFunction<BlockState, MaterialColor> materialColorFactory)
	{
		this.initialProperties = () -> BlockBehaviour.Properties.of(material, materialColorFactory);
		return this;
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> initialProperties(NonnullSupplier<? extends Block> block)
	{
		this.initialProperties = () -> BlockBehaviour.Properties.copy(block.get());
		return this;
	}

	// region: Properties Wrappers
	public MultiBlockBuilder<OWNER, BLOCK, PARENT> noCollission()
	{
		return properties(BlockBehaviour.Properties::noCollission);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> noOcclusion()
	{
		return properties(BlockBehaviour.Properties::noOcclusion);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> friction(float friction)
	{
		return properties(properties -> properties.friction(friction));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> speedFactor(float speedFactor)
	{
		return properties(properties -> properties.speedFactor(speedFactor));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> jumpFactor(float jumpFactor)
	{
		return properties(properties -> properties.jumpFactor(jumpFactor));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> sound(SoundType soundType)
	{
		return properties(properties -> properties.sound(soundType));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> lightLevel(ToIntFunction<BlockState> lightLevelFunction)
	{
		return properties(properties -> properties.lightLevel(lightLevelFunction));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> strength(float destroyTime, float explosionResistance)
	{
		return properties(properties -> properties.strength(destroyTime, explosionResistance));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> instabreak()
	{
		return properties(BlockBehaviour.Properties::instabreak);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> strength(float strength)
	{
		return properties(properties -> properties.strength(strength));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> randomTicks()
	{
		return properties(BlockBehaviour.Properties::randomTicks);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> dynamicShape()
	{
		return properties(BlockBehaviour.Properties::dynamicShape);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> noDrops()
	{
		return properties(BlockBehaviour.Properties::noDrops);
	}

	@Deprecated
	public MultiBlockBuilder<OWNER, BLOCK, PARENT> dropsLike(Block block)
	{
		return properties(properties -> properties.dropsLike(block));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> dropsLike(NonnullSupplier<? extends Block> block)
	{
		return properties(properties -> properties.lootFrom(block));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> air()
	{
		return properties(BlockBehaviour.Properties::air);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> extendedPositionPredicate)
	{
		return properties(properties -> properties.isValidSpawn(extendedPositionPredicate));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> isRedstoneConductor(BlockBehaviour.StatePredicate positionPredicate)
	{
		return properties(properties -> properties.isRedstoneConductor(positionPredicate));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> isSuffocating(BlockBehaviour.StatePredicate positionPredicate)
	{
		return properties(properties -> properties.isSuffocating(positionPredicate));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> isViewBlocking(BlockBehaviour.StatePredicate positionPredicate)
	{
		return properties(properties -> properties.isViewBlocking(positionPredicate));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> hasPostProcess(BlockBehaviour.StatePredicate positionPredicate)
	{
		return properties(properties -> properties.hasPostProcess(positionPredicate));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> emissiveRendering(BlockBehaviour.StatePredicate positionPredicate)
	{
		return properties(properties -> properties.emissiveRendering(positionPredicate));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> requiresCorrectToolForDrops()
	{
		return properties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
	}
	// endregion

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> addRenderType(NonnullSupplier<NonnullSupplier<RenderType>> renderType)
	{
		renderTypes.add(renderType);
		return this;
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> simpleItem()
	{
		return simpleItem(BlockItemFactory.forBlock());
	}

	public <ITEM extends BlockItem> MultiBlockBuilder<OWNER, BLOCK, PARENT> simpleItem(BlockItemFactory<BLOCK, ITEM> blockItemFactory)
	{
		return item(blockItemFactory).build();
	}

	public ItemBuilder<OWNER, BlockItem, MultiBlockBuilder<OWNER, BLOCK, PARENT>> item()
	{
		return item(BlockItemFactory.forBlock());
	}

	public <ITEM extends BlockItem> ItemBuilder<OWNER, ITEM, MultiBlockBuilder<OWNER, BLOCK, PARENT>> item(BlockItemFactory<BLOCK, ITEM> blockItemFactory)
	{
		// lazy builders, same instance if called multiple times
		if(itemBuilder == null)
			itemBuilder = owner.blockItem(getName(), this, toSupplier(), blockItemFactory);

		return (ItemBuilder<OWNER, ITEM, MultiBlockBuilder<OWNER, BLOCK, PARENT>>) itemBuilder;
	}

	public <BLOCK_ENTITY extends BlockEntity> MultiBlockBuilder<OWNER, BLOCK, PARENT> simpleBlockEntity(BlockEntityFactory<BLOCK_ENTITY> blockEntityFactory)
	{
		return blockEntity(blockEntityFactory).build();
	}

	public <BLOCK_ENTITY extends BlockEntity> BlockEntityBuilder<OWNER, BLOCK_ENTITY, MultiBlockBuilder<OWNER, BLOCK, PARENT>> blockEntity(BlockEntityFactory<BLOCK_ENTITY> blockEntityFactory)
	{
		// lazy builders, same instance if called multiple times
		if(blockEntityBuilder == null)
			blockEntityBuilder = owner.blockEntity(getName(), this, blockEntityFactory).validBlock(() -> asSupplier().get());

		return (BlockEntityBuilder<OWNER, BLOCK_ENTITY, MultiBlockBuilder<OWNER, BLOCK, PARENT>>) blockEntityBuilder;
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> color(NonnullSupplier<NonnullSupplier<BlockColor>> colorHandler)
	{
		this.colorHandler = colorHandler;
		return this;
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> defaultBlockState()
	{
		return blockState((ctx, provider) -> provider.simpleBlock(ctx.getEntry()));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> blockState(NonnullBiConsumer<DataGenContext<Block, BLOCK>, RegistrateBlockstateProvider> consumer)
	{
		return setDataGenerator(ProviderType.BLOCKSTATE, consumer);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> defaultLang()
	{
		return lang(Block::getDescriptionId);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> lang(String name)
	{
		return lang(Block::getDescriptionId, name);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> defaultLoot()
	{
		return loot(RegistrateBlockLootTables::dropSelf);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> loot(NonnullBiConsumer<RegistrateBlockLootTables, BLOCK> consumer)
	{
		return setDataGenerator(ProviderType.LOOT, (ctx, provider) -> provider.addLootAction(RegistrateLootTableProvider.LootType.BLOCK, lootTables -> consumer.accept(lootTables, ctx.getEntry())));
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> recipe(NonnullBiConsumer<DataGenContext<Block, BLOCK>, RegistrateRecipeProvider> consumer)
	{
		return setDataGenerator(ProviderType.RECIPE, consumer);
	}

	@SafeVarargs
	public final MultiBlockBuilder<OWNER, BLOCK, PARENT> tag(Tag.Named<Block>... tags)
	{
		return tag(ProviderType.BLOCK_TAGS, tags);
	}

	@SafeVarargs
	public final MultiBlockBuilder<OWNER, BLOCK, PARENT> removeTag(Tag.Named<Block>... tags)
	{
		return removeTag(ProviderType.BLOCK_TAGS, tags);
	}

	public MultiBlockBuilder<OWNER, BLOCK, PARENT> lang(String languageKey, String localizedValue)
	{
		return lang(languageKey, Block::getDescriptionId, localizedValue);
	}
}
