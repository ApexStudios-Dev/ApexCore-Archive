package xyz.apex.forge.utility.registrator.builder;

import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.entry.EntityEntry;
import xyz.apex.forge.utility.registrator.factory.EntityFactory;
import xyz.apex.forge.utility.registrator.factory.item.SpawnEggItemFactory;
import xyz.apex.forge.utility.registrator.helper.ForgeSpawnEggItem;
import xyz.apex.java.utility.nullness.*;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Supplier;

@SuppressWarnings({ "deprecation", "unchecked", "unused", "UnusedReturnValue" })
public final class EntityBuilder<OWNER extends AbstractRegistrator<OWNER>, ENTITY extends Entity, PARENT> extends RegistratorBuilder<OWNER, EntityType<?>, EntityType<ENTITY>, PARENT, EntityBuilder<OWNER, ENTITY, PARENT>, EntityEntry<ENTITY>>
{
	public static final String SPAWN_EGG_SUFFIX = "_spawn_egg";

	private final MobCategory mobCategory;
	private final EntityFactory<ENTITY> entityFactory;
	private NonnullUnaryOperator<EntityType.Builder<ENTITY>> propertiesModifier = NonnullUnaryOperator.identity();
	@Nullable private NonnullSupplier<NonnullFunction<EntityRendererProvider.Context, EntityRenderer<? super ENTITY>>> renderer = null;
	@Nullable private NonnullSupplier<AttributeSupplier.Builder> attributes = null;
	@Nullable private SpawnPlacements.Type placementType = null;
	@Nullable private Heightmap.Types heightMapType = null;
	@Nullable private SpawnPlacements.SpawnPredicate<ENTITY> placementPredicate;
	@Nullable private ItemBuilder<OWNER, ?, EntityBuilder<OWNER, ENTITY, PARENT>> spawnEggBuilder = null;

	public EntityBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback, MobCategory mobCategory, EntityFactory<ENTITY> entityFactory)
	{
		super(owner, parent, registryName, callback, EntityType.class, EntityEntry::new, EntityEntry::cast);

		this.mobCategory = mobCategory;
		this.entityFactory = entityFactory;
		onRegister(this::onRegister);

		// apply registrate defaults
		defaultLang();
	}

	private void onRegister(EntityType<ENTITY> entityType)
	{
		if(renderer != null)
		{
			DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> OneTimeEventReceiver.addModListener(EntityRenderersEvent.RegisterRenderers.class, event -> {
				try
				{
					event.registerEntityRenderer(entityType, renderer.get()::apply);
				}
				catch(Exception e)
				{
					throw new IllegalStateException("Failed to register renderer for Entity " + entityType.getRegistryName(), e);
				}
			}));
		}

		if(attributes != null)
			OneTimeEventReceiver.addModListener(EntityAttributeCreationEvent.class, event -> event.put((EntityType<? extends LivingEntity>) entityType, attributes.get().build()));

		if(placementType != null && heightMapType != null && placementPredicate != null)
			registerSpawnPlacement(entityType, placementType, heightMapType, placementPredicate);
	}

	@Override
	protected @NonnullType EntityType<ENTITY> createEntry()
	{
		if(spawnEggBuilder != null)
			copyMappingsTo(spawnEggBuilder);

		var builder = EntityType.Builder.of(entityFactory::create, mobCategory);
		builder = propertiesModifier.apply(builder);
		return builder.build(getRegistryNameFull());
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> properties(NonnullUnaryOperator<EntityType.Builder<ENTITY>> propertiesModifier)
	{
		this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
		return this;
	}

	// region: Properties Wrappers
	public EntityBuilder<OWNER, ENTITY, PARENT> sized(float width, float height)
	{
		return properties(properties -> properties.sized(width, height));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> noSummon()
	{
		return properties(EntityType.Builder::noSummon);
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> noSave()
	{
		return properties(EntityType.Builder::noSave);
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> fireImmune()
	{
		return properties(EntityType.Builder::fireImmune);
	}

	@Deprecated
	public EntityBuilder<OWNER, ENTITY, PARENT> immuneTo(Block... blocks)
	{
		return properties(properties -> properties.immuneTo(blocks));
	}

	@SafeVarargs
	public final EntityBuilder<OWNER, ENTITY, PARENT> immuneTo(NonnullSupplier<? extends Block>... blocks)
	{
		return properties(properties -> properties.immuneTo(Arrays.stream(blocks).map(Supplier::get).toArray(Block[]::new)));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> canSpawnFarFromPlayer()
	{
		return properties(EntityType.Builder::canSpawnFarFromPlayer);
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> clientTrackingRange(int clientTrackingRange)
	{
		return properties(properties -> properties.clientTrackingRange(clientTrackingRange));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> updateInterval(int updateInterval)
	{
		return properties(properties -> properties.updateInterval(updateInterval));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> setUpdateInterval(int updateInterval)
	{
		return properties(properties -> properties.setUpdateInterval(updateInterval));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> setTrackingRange(int trackingRange)
	{
		return properties(properties -> properties.setTrackingRange(trackingRange));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> setShouldReceiveVelocityUpdates(boolean shouldReceiveVelocityUpdates)
	{
		return properties(properties -> properties.setShouldReceiveVelocityUpdates(shouldReceiveVelocityUpdates));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> setCustomClientFactory(NonnullBiFunction<FMLPlayMessages.SpawnEntity, Level, ENTITY> customClientFactory)
	{
		return properties(properties -> properties.setCustomClientFactory(customClientFactory));
	}
	// endregion

	public EntityBuilder<OWNER, ENTITY, PARENT> renderer(NonnullSupplier<NonnullFunction<EntityRendererProvider.Context, EntityRenderer<? super ENTITY>>> renderer)
	{
		this.renderer = renderer;
		return this;
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> attributes(NonnullSupplier<AttributeSupplier.Builder> attributes)
	{
		this.attributes = attributes;
		return this;
	}

	public <ITEM extends ForgeSpawnEggItem<ENTITY>> EntityBuilder<OWNER, ENTITY, PARENT> simpleSpawnEgg(int backgroundColor, int highlightColor, SpawnEggItemFactory<ENTITY, ITEM> itemFactory)
	{
		return spawnEgg(backgroundColor, highlightColor, itemFactory).build();
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> simpleSpawnEgg(int backgroundColor, int highlightColor)
	{
		return simpleSpawnEgg(backgroundColor, highlightColor, SpawnEggItemFactory.forEntity());
	}

	public <ITEM extends ForgeSpawnEggItem<ENTITY>> ItemBuilder<OWNER, ITEM, EntityBuilder<OWNER, ENTITY, PARENT>> spawnEgg(int backgroundColor, int highlightColor, SpawnEggItemFactory<ENTITY, ITEM> itemFactory)
	{
		if(spawnEggBuilder == null)
			spawnEggBuilder = owner.spawnEggItem(getRegistryName() + SPAWN_EGG_SUFFIX, this, toSupplier(), backgroundColor, highlightColor, itemFactory);

		return (ItemBuilder<OWNER, ITEM, EntityBuilder<OWNER, ENTITY, PARENT>>) spawnEggBuilder;
	}

	public ItemBuilder<OWNER, ForgeSpawnEggItem<ENTITY>, EntityBuilder<OWNER, ENTITY, PARENT>> spawnEgg(int backgroundColor, int highlightColor)
	{
		return spawnEgg(backgroundColor, highlightColor, SpawnEggItemFactory.forEntity());
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> spawnPlacement(SpawnPlacements.Type placementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<ENTITY> placementPredicate)
	{
		this.placementType = placementType;
		this.heightMapType = heightmapType;
		this.placementPredicate = placementPredicate;
		return this;
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> defaultLang()
	{
		return lang(EntityType::getDescriptionId);
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> lang(String localizedValue)
	{
		return lang(EntityType::getDescriptionId, localizedValue);
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> lang(String languageKey, String localizedValue)
	{
		return lang(languageKey, EntityType::getDescriptionId, localizedValue);
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> loot(NonnullBiConsumer<RegistrateEntityLootTables, EntityType<ENTITY>> consumer)
	{
		return setDataGenerator(ProviderType.LOOT, (ctx, provider) -> provider.addLootAction(RegistrateLootTableProvider.LootType.ENTITY, lootTables -> consumer.accept(lootTables, ctx.getEntry())));
	}

	@SafeVarargs
	public final EntityBuilder<OWNER, ENTITY, PARENT> tag(Tag.Named<EntityType<?>>... tags)
	{
		return tag(ProviderType.ENTITY_TAGS, tags);
	}

	@SafeVarargs
	public final EntityBuilder<OWNER, ENTITY, PARENT> removeTag(Tag.Named<EntityType<?>>... tags)
	{
		return removeTags(ProviderType.ENTITY_TAGS, tags);
	}

	public static <ENTITY extends Entity> void registerSpawnPlacement(EntityType<ENTITY> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightMapType, SpawnPlacements.SpawnPredicate<ENTITY> placementPredicate)
	{
		var entitySpawnPlacementRegistryEntry = SpawnPlacements.DATA_BY_TYPE.put(entityType, new SpawnPlacements.Data(heightMapType, placementType, placementPredicate));

		if(entitySpawnPlacementRegistryEntry != null)
			throw new IllegalStateException("Duplicate registration for type " + Registry.ENTITY_TYPE.getKey(entityType));
	}
}
