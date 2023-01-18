package xyz.apex.forge.apexcore.registrate.builder;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;

import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.factory.ItemFactory;
import xyz.apex.forge.apexcore.registrate.entry.ItemEntry;
import xyz.apex.forge.apexcore.registrate.holder.ItemHolder;
import xyz.apex.forge.commonality.Mods;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.tterrag.registrate.providers.ProviderType.*;

@SuppressWarnings({ "unused", "deprecation" })
public final class ItemBuilder<
		OWNER extends CoreRegistrate<OWNER> & ItemHolder<OWNER>,
		ITEM extends Item,
		PARENT
> extends AbstractBuilder<OWNER, Item, ITEM, PARENT, ItemBuilder<OWNER, ITEM, PARENT>, ItemEntry<ITEM>>
{
	private final ItemFactory<ITEM> itemFactory;
	private NonNullSupplier<Item.Properties> initialProperties = Item.Properties::new;
	private NonNullFunction<Item.Properties, Item.Properties> propertiesModifier = NonNullUnaryOperator.identity();
	private NonNullSupplier<Supplier<ItemColor>> colorHandler = () -> () -> null;
	private Map<NonNullSupplier<? extends CreativeModeTab>, Consumer<CreativeModeTabModifier>> creativeModeTabs = Maps.newHashMap();

	public ItemBuilder(OWNER owner, PARENT parent, String name, BuilderCallback callback, ItemFactory<ITEM> itemFactory)
	{
		super(owner, parent, name, callback, ForgeRegistries.Keys.ITEMS, ItemEntry::new, ItemEntry::cast);

		this.itemFactory = itemFactory;

		onRegister(item -> {
			DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> OneTimeEventReceiver.addModListener(RegisterColorHandlersEvent.Item.class, event -> {
				var colorHandler = this.colorHandler.get().get();
				if(colorHandler != null) event.register(colorHandler, item);
			}));

			creativeModeTabs.forEach(owner::modifyCreativeModeTab);
			creativeModeTabs.clear(); // this registration should only fire once, to doubly ensure this, clear the map
		});
	}

	public ItemBuilder<OWNER, ITEM, PARENT> properties(NonNullUnaryOperator<Item.Properties> propertiesModifier)
	{
		this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
		return this;
	}

	public ItemBuilder<OWNER, ITEM, PARENT> initialProperties(NonNullSupplier<Item.Properties> initialProperties)
	{
		this.initialProperties = initialProperties;
		return this;
	}

	public ItemBuilder<OWNER, ITEM, PARENT> tab(NonNullSupplier<? extends CreativeModeTab> tab, Consumer<CreativeModeTabModifier> modifier)
	{
		creativeModeTabs.put(tab, modifier);
		return this;
	}

	public ItemBuilder<OWNER, ITEM, PARENT> tab(NonNullSupplier<? extends CreativeModeTab> tab)
	{
		return tab(tab, modifier -> modifier.accept(asSupplier()));
	}

	public ItemBuilder<OWNER, ITEM, PARENT> removeTab(NonNullSupplier<? extends CreativeModeTab> tab)
	{
		creativeModeTabs.remove(tab);
		return this;
	}

	public ItemBuilder<OWNER, ITEM, PARENT> color(NonNullSupplier<Supplier<ItemColor>> colorHandler)
	{
		this.colorHandler = colorHandler;
		return this;
	}

	public ItemBuilder<OWNER, ITEM, PARENT> defaultModel()
	{
		return model((ctx, provider) -> provider.generated(ctx));
	}

	public ItemBuilder<OWNER, ITEM, PARENT> model(NonNullBiConsumer<DataGenContext<Item, ITEM>, RegistrateItemModelProvider> consumer)
	{
		return setData(ITEM_MODEL, consumer);
	}

	public ItemBuilder<OWNER, ITEM, PARENT> defaultLang()
	{
		return lang(Item::getDescriptionId);
	}

	public ItemBuilder<OWNER, ITEM, PARENT> lang(String name)
	{
		return lang(Item::getDescriptionId, name);
	}

	public ItemBuilder<OWNER, ITEM, PARENT> recipe(NonNullBiConsumer<DataGenContext<Item, ITEM>, RegistrateRecipeProvider> consumer)
	{
		return setData(RECIPE, consumer);
	}

	@SafeVarargs
	public final ItemBuilder<OWNER, ITEM, PARENT> tag(TagKey<Item>... tags)
	{
		return tag(ITEM_TAGS, tags);
	}

	@SafeVarargs
	public final ItemBuilder<OWNER, ITEM, PARENT> removeTag(TagKey<Item>... tags)
	{
		return removeTag(ITEM_TAGS, tags);
	}

	public ItemBuilder<OWNER, ITEM, PARENT> food(FoodProperties foodProperties)
	{
		return properties(properties -> properties.food(foodProperties));
	}

	public ItemBuilder<OWNER, ITEM, PARENT> stacksTo(int stackSize)
	{
		return properties(properties -> properties.stacksTo(stackSize));
	}

	public ItemBuilder<OWNER, ITEM, PARENT> defaultDurability(int durability)
	{
		return properties(properties -> properties.defaultDurability(durability));
	}

	public ItemBuilder<OWNER, ITEM, PARENT> durability(int durability)
	{
		return properties(properties -> properties.durability(durability));
	}

	@Deprecated
	public ItemBuilder<OWNER, ITEM, PARENT> craftRemainder(Item craftingRemainingItem)
	{
		return properties(properties -> properties.craftRemainder(craftingRemainingItem));
	}

	public ItemBuilder<OWNER, ITEM, PARENT> craftRemainder(NonNullSupplier<? extends Item> craftingRemainingItem)
	{
		return properties(properties -> properties.craftRemainder(craftingRemainingItem.get()));
	}

	public ItemBuilder<OWNER, ITEM, PARENT> rarity(Rarity rarity)
	{
		return properties(properties -> properties.rarity(rarity));
	}

	public ItemBuilder<OWNER, ITEM, PARENT> fireResistant()
	{
		return properties(Item.Properties::fireResistant);
	}

	public ItemBuilder<OWNER, ITEM, PARENT> setNoRepair()
	{
		return properties(Item.Properties::setNoRepair);
	}

	@Override
	protected ITEM createEntry()
	{
		var properties = propertiesModifier.apply(initialProperties.get());
		return itemFactory.create(properties);
	}

	public static <OWNER extends CoreRegistrate<OWNER> & ItemHolder<OWNER>, ITEM extends Item, PARENT> ItemBuilder<OWNER, ITEM, PARENT> applyDefaults(ItemBuilder<OWNER, ITEM, PARENT> builder)
	{
		return builder
				.defaultModel()
				.defaultLang()
		;
	}

	public static <OWNER extends CoreRegistrate<OWNER> & ItemHolder<OWNER>, ITEM extends Item, PARENT> ItemBuilder<OWNER, ITEM, PARENT> applyBlockItemDefaults(ItemBuilder<OWNER, ITEM, PARENT> builder)
	{
		return builder
				.transform(ItemBuilder::applyDefaults)
				.clearData(LANG)
				.model((ctx, provider) -> {
					if(builder.parent instanceof BlockBuilder<?, ?, ?> blockBuilder)
					{
						builder.owner
								.getDataProvider(BLOCKSTATE)
								.flatMap(prov -> prov.getExistingVariantBuilder(blockBuilder.getEntry()))
								.map(bldr -> bldr.getModels().get(bldr.partialState()))
								.map(BlockStateProvider.ConfiguredModelList::toJSON)
								.filter(JsonElement::isJsonObject)
								.map(JsonElement::getAsJsonObject)
								.map(json -> json.get("model"))
								.map(JsonElement::getAsString)
								.ifPresentOrElse(str -> provider.withExistingParent(ctx.getName(), str), () -> provider.blockItem(builder.asSupplier()));
					}
					else
						provider.blockItem(builder.asSupplier());
				})
		;
	}

	public static <OWNER extends CoreRegistrate<OWNER> & ItemHolder<OWNER>, ITEM extends ForgeSpawnEggItem, PARENT> ItemBuilder<OWNER, ITEM, PARENT> applySpawnEggItemDefaults(ItemBuilder<OWNER, ITEM, PARENT> builder)
	{
		return builder
				.transform(ItemBuilder::applyDefaults)
				.tab(() -> CreativeModeTabs.SPAWN_EGGS)
				.model((ctx, provider) -> provider.withExistingParent(ctx.getName(), new ResourceLocation(Mods.MINECRAFT, "item/template_spawn_egg")))
		;
	}
}
