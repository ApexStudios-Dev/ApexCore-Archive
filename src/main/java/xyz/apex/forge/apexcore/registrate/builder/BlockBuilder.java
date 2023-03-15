package xyz.apex.forge.apexcore.registrate.builder;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.factory.BlockEntityFactory;
import xyz.apex.forge.apexcore.registrate.builder.factory.BlockFactory;
import xyz.apex.forge.apexcore.registrate.builder.factory.BlockItemFactory;
import xyz.apex.forge.apexcore.registrate.entry.BlockEntry;
import xyz.apex.forge.apexcore.registrate.holder.BlockEntityHolder;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.tterrag.registrate.providers.ProviderType.*;

@SuppressWarnings({ "unused", "deprecation" })
public final class BlockBuilder<
		OWNER extends CoreRegistrate<OWNER> & BlockEntityHolder<OWNER>,
		BLOCK extends Block,
		PARENT
> extends AbstractBuilder<OWNER, Block, BLOCK, PARENT, BlockBuilder<OWNER, BLOCK, PARENT>, BlockEntry<BLOCK>>
{
	private final BlockFactory<BLOCK> blockFactory;
	private final List<Supplier<Supplier<RenderType>>> renderTypes = Lists.newArrayListWithExpectedSize(1);
	private NonNullSupplier<BlockBehaviour.Properties> initialProperties = () -> BlockBehaviour.Properties.copy(Blocks.STONE);
	private NonNullFunction<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesModifier = NonNullUnaryOperator.identity();
	private NonNullSupplier<Supplier<BlockColor>> colorHandler = () -> () -> null;

	public BlockBuilder(OWNER owner, PARENT parent, String name, BuilderCallback callback, BlockFactory<BLOCK> blockFactory)
	{
		super(owner, parent, name, callback, ForgeRegistries.Keys.BLOCKS, BlockEntry::new, BlockEntry::cast);

		this.blockFactory = blockFactory;

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> onRegister(block -> {
			OneTimeEventReceiver.addModListener(RegisterColorHandlersEvent.Block.class, event -> {
				var colorHandler = this.colorHandler.get().get();

				if(colorHandler != null)
					event.register(colorHandler, block);
			});

			OneTimeEventReceiver.addModListener(FMLClientSetupEvent.class, event -> {
				var size = renderTypes.size();

				if(size == 1)
				{
					var renderType = renderTypes.get(0).get().get();
					ItemBlockRenderTypes.setRenderLayer(block, renderType);
				}
				else if(size > 1)
				{
					var renderTypes = this.renderTypes
							.stream()
							.map(Supplier::get)
							.map(Supplier::get)
							.collect(ImmutableSet.toImmutableSet())
					;

					ItemBlockRenderTypes.setRenderLayer(block, renderTypes::contains);
				}
			});
		}));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> properties(NonNullUnaryOperator<BlockBehaviour.Properties> propertiesModifier)
	{
		this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
		return this;
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> initialProperties(Material material)
	{
		initialProperties = () -> BlockBehaviour.Properties.of(material);
		return this;
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> initialProperties(Material material, DyeColor dyeColor)
	{
		initialProperties = () -> BlockBehaviour.Properties.of(material, dyeColor);
		return this;
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> initialProperties(Material material, MaterialColor materialColor)
	{
		initialProperties = () -> BlockBehaviour.Properties.of(material, materialColor);
		return this;
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> initialProperties(Material material, Function<BlockState, MaterialColor> materialColorFunction)
	{
		initialProperties = () -> BlockBehaviour.Properties.of(material, materialColorFunction);
		return this;
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> initialProperties(NonNullSupplier<? extends BlockBehaviour> block)
	{
		initialProperties = () -> BlockBehaviour.Properties.copy(block.get());
		return this;
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> renderType(Supplier<Supplier<RenderType>> renderType)
	{
		renderTypes.add(renderType);
		return this;
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> simpleItem()
	{
		return item().build();
	}

	public <ITEM extends Item> ItemBuilder<OWNER, ITEM, BlockBuilder<OWNER, BLOCK, PARENT>> item(BlockItemFactory<BLOCK, ITEM> blockItemFactory)
	{
		return owner.item(self, name, properties -> blockItemFactory.create(getEntry(), properties))
		            .transform(ItemBuilder::applyBlockItemDefaults)
		;
	}

	public ItemBuilder<OWNER, Item, BlockBuilder<OWNER, BLOCK, PARENT>> item()
	{
		return item(BlockItemFactory.blockItem());
	}

	public <BLOCK_ENTITY extends BlockEntity> BlockBuilder<OWNER, BLOCK, PARENT> simpleBlockEntity(BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return blockEntity(factory).build();
	}

	public <BLOCK_ENTITY extends BlockEntity> BlockEntityBuilder<OWNER, BLOCK_ENTITY, BlockBuilder<OWNER, BLOCK, PARENT>> blockEntity(BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return owner.blockEntity(this, name, factory).validBlock(asSupplier());
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> color(NonNullSupplier<Supplier<BlockColor>> colorHandler)
	{
		this.colorHandler = colorHandler;
		return this;
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> defaultBlockState()
	{
		return blockState((ctx, provider) -> provider.simpleBlock(ctx.get()));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> blockState(NonNullBiConsumer<DataGenContext<Block, BLOCK>, RegistrateBlockstateProvider> consumer)
	{
		return setData(BLOCKSTATE, consumer);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> defaultLang()
	{
		return lang(Block::getDescriptionId);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> lang(String name)
	{
		return lang(Block::getDescriptionId, name);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> defaultLoot()
	{
		return loot(RegistrateBlockLootTables::dropSelf);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> loot(NonNullBiConsumer<RegistrateBlockLootTables, BLOCK> consumer)
	{
		return setData(LOOT, (ctx, provider) -> provider.addLootAction(RegistrateLootTableProvider.LootType.BLOCK, lootTables -> {
			if(!ctx.get().getLootTable().equals(BuiltInLootTables.EMPTY))
				consumer.accept(lootTables, ctx.get());
		}));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> recipe(NonNullBiConsumer<DataGenContext<Block, BLOCK>, RegistrateRecipeProvider> consumer)
	{
		return setData(RECIPE, consumer);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> noCollission()
	{
		return properties(BlockBehaviour.Properties::noCollission);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> noOcclusion()
	{
		return properties(BlockBehaviour.Properties::noOcclusion);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> friction(float friction)
	{
		return properties(properties -> properties.friction(friction));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> speedFactor(float speedFactor)
	{
		return properties(properties -> properties.speedFactor(speedFactor));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> jumpFactor(float jumpFactor)
	{
		return properties(properties -> properties.jumpFactor(jumpFactor));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> sound(SoundType soundType)
	{
		return properties(properties -> properties.sound(soundType));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> lightLevel(ToIntFunction<BlockState> lightEmission)
	{
		return properties(properties -> properties.lightLevel(lightEmission));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> strength(float destroyTime, float explosionResistance)
	{
		return properties(properties -> properties.strength(destroyTime, explosionResistance));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> instabreak()
	{
		return properties(BlockBehaviour.Properties::instabreak);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> strength(float strength)
	{
		return properties(properties -> properties.strength(strength));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> randomTicks()
	{
		return properties(BlockBehaviour.Properties::randomTicks);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> dynamicShape()
	{
		return properties(BlockBehaviour.Properties::dynamicShape);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> noLootTable()
	{
		return properties(BlockBehaviour.Properties::noLootTable);
	}

	@Deprecated
	public BlockBuilder<OWNER, BLOCK, PARENT> dropsLike(Block block)
	{
		return properties(properties -> properties.dropsLike(block));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> lootFrom(java.util.function.Supplier<? extends Block> block)
	{
		return properties(properties -> properties.lootFrom(block));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> air()
	{
		return properties(BlockBehaviour.Properties::air);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate)
	{
		return properties(properties -> properties.isValidSpawn(predicate));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> isRedstoneConductor(BlockBehaviour.StatePredicate predicate)
	{
		return properties(properties -> properties.isRedstoneConductor(predicate));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> isSuffocating(BlockBehaviour.StatePredicate predicate)
	{
		return properties(properties -> properties.isSuffocating(predicate));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> isViewBlocking(BlockBehaviour.StatePredicate predicate)
	{
		return properties(properties -> properties.isViewBlocking(predicate));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> hasPostProcess(BlockBehaviour.StatePredicate predicate)
	{
		return properties(properties -> properties.hasPostProcess(predicate));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> emissiveRendering(BlockBehaviour.StatePredicate predicate)
	{
		return properties(properties -> properties.emissiveRendering(predicate));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> requiresCorrectToolForDrops()
	{
		return properties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> color(MaterialColor materialColor)
	{
		return properties(properties -> properties.color(materialColor));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> destroyTime(float destroyTime)
	{
		return properties(properties -> properties.destroyTime(destroyTime));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> explosionResistance(float explosionResistance)
	{
		return properties(properties -> properties.explosionResistance(explosionResistance));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> offsetType(BlockBehaviour.OffsetType offsetType)
	{
		return properties(properties -> properties.offsetType(offsetType));
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> noParticlesOnBreak()
	{
		return properties(BlockBehaviour.Properties::noParticlesOnBreak);
	}

	public BlockBuilder<OWNER, BLOCK, PARENT> requiredFeatures(FeatureFlag... flags)
	{
		return properties(properties -> properties.requiredFeatures(flags));
	}

	@SafeVarargs
	public final BlockBuilder<OWNER, BLOCK, PARENT> tag(TagKey<Block>... tags)
	{
		return tag(BLOCK_TAGS, tags);
	}

	@SafeVarargs
	public final BlockBuilder<OWNER, BLOCK, PARENT> removeTag(TagKey<Block>... tags)
	{
		return removeTag(BLOCK_TAGS, tags);
	}

	@Override
	protected BLOCK createEntry()
	{
		var properties = propertiesModifier.apply(initialProperties.get());
		return blockFactory.create(properties);
	}

	public static <OWNER extends CoreRegistrate<OWNER> & BlockEntityHolder<OWNER>, BLOCK extends Block, PARENT> BlockBuilder<OWNER, BLOCK, PARENT> applyDefaults(BlockBuilder<OWNER, BLOCK, PARENT> builder)
	{
		return builder
				.defaultBlockState()
				.defaultLoot()
				.defaultLang()
		;
	}
}
