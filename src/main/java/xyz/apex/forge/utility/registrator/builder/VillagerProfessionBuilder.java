package xyz.apex.forge.utility.registrator.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tterrag.registrate.builders.BuilderCallback;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.entry.VillagerProfessionEntry;
import xyz.apex.java.utility.nullness.NonnullBiConsumer;
import xyz.apex.java.utility.nullness.NonnullSupplier;
import xyz.apex.java.utility.nullness.NonnullType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings({ "UnstableApiUsage", "unused" })
public final class VillagerProfessionBuilder<OWNER extends AbstractRegistrator<OWNER>, PARENT> extends RegistratorBuilder<OWNER, VillagerProfession, VillagerProfession, PARENT, VillagerProfessionBuilder<OWNER, PARENT>, VillagerProfessionEntry>
{
	private final Set<NonnullSupplier<? extends Item>> requestedItems = Sets.newHashSet();
	private final Set<NonnullSupplier<? extends Block>> secondaryPoi = Sets.newHashSet();
	private Supplier<SoundEvent> workSound = () -> null;
	private NonnullSupplier<PoiType> poiType = () -> PoiType.UNEMPLOYED;
	private NonnullBiConsumer<VillagerProfession, VillagerTradesRegistrar> villagerTradesConsumer = NonnullBiConsumer.noop();
	private NonnullBiConsumer<VillagerProfession, WandererTradesRegistrar> wandererTradesConsumer = NonnullBiConsumer.noop();
	@Nullable private PointOfInterestBuilder<OWNER, VillagerProfessionBuilder<OWNER, PARENT>> pointOfInterestBuilder = null;

	public VillagerProfessionBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback)
	{
		super(owner, parent, registryName, callback, VillagerProfession.class, VillagerProfessionEntry::new, VillagerProfessionEntry::cast);

		onRegister(this::onRegister);
	}

	private void onRegister(VillagerProfession profession)
	{
		var villagerTrades = new VillagerTradesRegistrar(profession);
		var wandererTrades = new WandererTradesRegistrar(profession);

		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, false, VillagerTradesEvent.class, event -> villagerTrades.onEvent(event, villagerTradesConsumer));
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, false, WandererTradesEvent.class, event -> wandererTrades.onEvent(event, wandererTradesConsumer));
	}

	@Override
	protected @NonnullType VillagerProfession createEntry()
	{
		if(pointOfInterestBuilder != null)
			copyMappingsTo(pointOfInterestBuilder);

		var registryName = getRegistryNameFull();
		var pointOfInterestType = this.poiType.get();
		ImmutableSet<Item> requestedItems = this.requestedItems.stream().map(NonnullSupplier::get).collect(ImmutableSet.toImmutableSet());
		ImmutableSet<Block> secondaryPoi = this.secondaryPoi.stream().map(NonnullSupplier::get).collect(ImmutableSet.toImmutableSet());
		var workSound = this.workSound.get();
		return new VillagerProfession(registryName, pointOfInterestType, requestedItems, secondaryPoi, workSound);
	}

	// region: POI
	public VillagerProfessionBuilder<OWNER, PARENT> pointOfInterestType(NonnullSupplier<PoiType> poiType)
	{
		this.poiType = poiType;
		return this;
	}

	public PointOfInterestBuilder<OWNER, VillagerProfessionBuilder<OWNER, PARENT>> poi()
	{
		if(pointOfInterestBuilder == null)
		{
			pointOfInterestBuilder = owner.pointOfInterest(getName(), this);
			poiType = () -> pointOfInterestBuilder.asSupplier().get();
		}

		return pointOfInterestBuilder;
	}

	// region: Secondary POI
	public VillagerProfessionBuilder<OWNER, PARENT> secondaryPoi(NonnullSupplier<? extends Block> secondaryPoi)
	{
		this.secondaryPoi.add(secondaryPoi);
		return this;
	}

	@SafeVarargs
	public final VillagerProfessionBuilder<OWNER, PARENT> secondaryPois(NonnullSupplier<? extends Block>... secondaryPois)
	{
		Collections.addAll(secondaryPoi, secondaryPois);
		return this;
	}
	// endregion
	// endregion

	// region: Requested Items
	public VillagerProfessionBuilder<OWNER, PARENT> requestItem(NonnullSupplier<? extends Item> item)
	{
		requestedItems.add(item);
		return this;
	}

	@SafeVarargs
	public final VillagerProfessionBuilder<OWNER, PARENT> requestItems(NonnullSupplier<? extends Item>... items)
	{
		Collections.addAll(requestedItems, items);
		return this;
	}
	// endregion

	public VillagerProfessionBuilder<OWNER, PARENT> villagerTrades(NonnullBiConsumer<VillagerProfession, VillagerTradesRegistrar> villagerTradesConsumer)
	{
		this.villagerTradesConsumer = villagerTradesConsumer;
		return this;
	}

	public VillagerProfessionBuilder<OWNER, PARENT> wandererTrades(NonnullBiConsumer<VillagerProfession, WandererTradesRegistrar> wandererTradesConsumer)
	{
		this.wandererTradesConsumer = wandererTradesConsumer;
		return this;
	}

	public VillagerProfessionBuilder<OWNER, PARENT> workSound(NonnullSupplier<SoundEvent> workSound)
	{
		this.workSound = workSound;
		return this;
	}

	public SoundBuilder<OWNER, VillagerProfessionBuilder<OWNER, PARENT>> workSound(String soundName)
	{
		var sound = owner.sound(soundName, this);
		workSound = sound.asSupplier();
		return sound;
	}

	private String getTranslationKey()
	{
		// <villager_translation_key>.<mod_id>.<profession_registry_name>
		return EntityType.VILLAGER.getDescriptionId() + '.' + owner.getModId() + '.' + getName();
	}

	public VillagerProfessionBuilder<OWNER, PARENT> defaultLang()
	{
		return lang(profession -> getTranslationKey());
	}

	public VillagerProfessionBuilder<OWNER, PARENT> lang(String name)
	{
		return lang(profession -> getTranslationKey(), name);
	}

	public VillagerProfessionBuilder<OWNER, PARENT> lang(String languageKey, String localizedValue)
	{
		return lang(languageKey, profession -> getTranslationKey(), localizedValue);
	}

	public enum TradeLevel
	{
		ONE, TWO, THREE, FOUR, FIVE;

		public static final TradeLevel[] LEVELS = values();

		public int getLevel()
		{
			return ordinal() + 1;
		}

		public static TradeLevel from(int level)
		{
			return level <= 5 && level >= 0 ? LEVELS[level] : ONE;
		}
	}

	public static final class VillagerTradesRegistrar
	{
		private final Map<TradeLevel, Set<VillagerTrades.ItemListing>> tradeMap = Maps.newEnumMap(TradeLevel.class);
		private final VillagerProfession profession;

		private VillagerTradesRegistrar(VillagerProfession profession)
		{
			this.profession = profession;
		}

		void onEvent(VillagerTradesEvent event, NonnullBiConsumer<VillagerProfession, VillagerTradesRegistrar> consumer)
		{
			if(event.getType() == profession)
			{
				consumer.accept(profession, this);

				if(!tradeMap.isEmpty())
				{
					var trades = event.getTrades();
					tradeMap.forEach((key, value) -> trades.put(key.getLevel(), ImmutableList.copyOf(value)));
					tradeMap.clear();
				}
			}
		}

		// region: Generic
		public VillagerTradesRegistrar register(TradeLevel level, VillagerTrades.ItemListing trade)
		{
			tradeMap.computeIfAbsent(level, $ -> Sets.newHashSet()).add(trade);
			return this;
		}

		public VillagerTradesRegistrar register(TradeLevel level, VillagerTrades.ItemListing... trades)
		{
			var tradeSet = tradeMap.computeIfAbsent(level, $ -> Sets.newHashSet());
			Collections.addAll(tradeSet, trades);
			return this;
		}

		public VillagerTradesRegistrar register(TradeLevel level, Collection<VillagerTrades.ItemListing> trades)
		{
			tradeMap.computeIfAbsent(level, $ -> Sets.newHashSet()).addAll(trades);
			return this;
		}
		// endregion

		// region: Levels
		// region: Level One
		public VillagerTradesRegistrar one(VillagerTrades.ItemListing trade)
		{
			return register(TradeLevel.ONE, trade);
		}

		public VillagerTradesRegistrar one(VillagerTrades.ItemListing... trades)
		{
			return register(TradeLevel.ONE, trades);
		}

		public VillagerTradesRegistrar one(Collection<VillagerTrades.ItemListing> trades)
		{
			return register(TradeLevel.ONE, trades);
		}
		// endregion

		// region: Level Two
		public VillagerTradesRegistrar two(VillagerTrades.ItemListing trade)
		{
			return register(TradeLevel.TWO, trade);
		}

		public VillagerTradesRegistrar two(VillagerTrades.ItemListing... trades)
		{
			return register(TradeLevel.TWO, trades);
		}

		public VillagerTradesRegistrar two(Collection<VillagerTrades.ItemListing> trades)
		{
			return register(TradeLevel.TWO, trades);
		}
		// endregion

		// region: Level Three
		public VillagerTradesRegistrar three(VillagerTrades.ItemListing trade)
		{
			return register(TradeLevel.THREE, trade);
		}

		public VillagerTradesRegistrar three(VillagerTrades.ItemListing... trades)
		{
			return register(TradeLevel.THREE, trades);
		}

		public VillagerTradesRegistrar three(Collection<VillagerTrades.ItemListing> trades)
		{
			return register(TradeLevel.THREE, trades);
		}
		// endregion

		// region: Level Four
		public VillagerTradesRegistrar four(VillagerTrades.ItemListing trade)
		{
			return register(TradeLevel.FOUR, trade);
		}

		public VillagerTradesRegistrar four(VillagerTrades.ItemListing... trades)
		{
			return register(TradeLevel.FOUR, trades);
		}

		public VillagerTradesRegistrar four(Collection<VillagerTrades.ItemListing> trades)
		{
			return register(TradeLevel.FOUR, trades);
		}
		// endregion

		// region: Level Five
		public VillagerTradesRegistrar five(VillagerTrades.ItemListing trade)
		{
			return register(TradeLevel.FIVE, trade);
		}

		public VillagerTradesRegistrar five(VillagerTrades.ItemListing... trades)
		{
			return register(TradeLevel.FIVE, trades);
		}

		public VillagerTradesRegistrar five(Collection<VillagerTrades.ItemListing> trades)
		{
			return register(TradeLevel.FIVE, trades);
		}
		// endregion
		// endregion
	}

	public static final class WandererTradesRegistrar
	{
		private final Set<VillagerTrades.ItemListing> genericTrades = Sets.newHashSet();
		private final Set<VillagerTrades.ItemListing> rareTrades = Sets.newHashSet();
		private final VillagerProfession profession;

		private WandererTradesRegistrar(VillagerProfession profession)
		{
			this.profession = profession;
		}

		void onEvent(WandererTradesEvent event, NonnullBiConsumer<VillagerProfession, WandererTradesRegistrar> consumer)
		{
			consumer.accept(profession, this);

			if(!genericTrades.isEmpty())
			{
				event.getGenericTrades().addAll(genericTrades);
				genericTrades.clear();
			}

			if(!rareTrades.isEmpty())
			{
				event.getRareTrades().addAll(rareTrades);
				rareTrades.clear();
			}
		}

		// region: Trades
		public WandererTradesRegistrar register(boolean rare, VillagerTrades.ItemListing trade)
		{
			(rare ? rareTrades : genericTrades).add(trade);
			return this;
		}

		public WandererTradesRegistrar register(boolean rare, VillagerTrades.ItemListing... trades)
		{
			Collections.addAll(rare ? rareTrades : genericTrades, trades);
			return this;
		}

		public WandererTradesRegistrar register(boolean rare, Collection<VillagerTrades.ItemListing> trades)
		{
			(rare ? rareTrades : genericTrades).addAll(trades);
			return this;
		}

		// region: Generic Trades
		public WandererTradesRegistrar generic(VillagerTrades.ItemListing trade)
		{
			return register(false, trade);
		}

		public WandererTradesRegistrar generic(VillagerTrades.ItemListing... trades)
		{
			return register(false, trades);
		}

		public WandererTradesRegistrar generic(Collection<VillagerTrades.ItemListing> trades)
		{
			return register(false, trades);
		}
		// endregion

		// region: Rare Trades
		public WandererTradesRegistrar rare(VillagerTrades.ItemListing trade)
		{
			return register(true, trade);
		}

		public WandererTradesRegistrar rare(VillagerTrades.ItemListing... trades)
		{
			return register(true, trades);
		}

		public WandererTradesRegistrar rare(Collection<VillagerTrades.ItemListing> trades)
		{
			return register(true, trades);
		}
		// endregion
		// endregion
	}
}
