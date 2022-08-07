package xyz.apex.forge.apexcore.registrate.builder;

import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import xyz.apex.forge.apexcore.lib.util.RegistryHelper;
import xyz.apex.forge.apexcore.registrate.CoreRegistrate;
import xyz.apex.forge.apexcore.registrate.builder.factory.EntityFactory;
import xyz.apex.forge.apexcore.registrate.entry.EntityEntry;
import xyz.apex.forge.apexcore.registrate.holder.EntityHolder;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.tterrag.registrate.providers.ProviderType.ENTITY_TAGS;
import static com.tterrag.registrate.providers.ProviderType.LOOT;

@SuppressWarnings("unchecked")
public final class EntityBuilder<
		OWNER extends CoreRegistrate<OWNER> & EntityHolder<OWNER>,
		ENTITY extends Entity,
		PARENT
> extends AbstractBuilder<OWNER, EntityType<?>, EntityType<ENTITY>, PARENT, EntityBuilder<OWNER, ENTITY, PARENT>, EntityEntry<ENTITY>>
{
	public static final String SPAWN_EGG_SUFFIX = "_spawn_egg";

	private final EntityFactory<ENTITY> entityFactory;

	private MobCategory mobCategory = MobCategory.MISC;
	private NonNullUnaryOperator<EntityType.Builder<ENTITY>> builderModifier = NonNullUnaryOperator.identity();
	private Supplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super ENTITY>>> rendererFactory = () -> null;
	private Supplier<AttributeSupplier.Builder> attributesFactory = () -> null;
	@Nullable private SpawnPlacementData<ENTITY> spawnPlacementData = null;

	public EntityBuilder(OWNER owner, PARENT parent, String name, BuilderCallback callback, EntityFactory<ENTITY> entityFactory)
	{
		super(owner, parent, name, callback, ForgeRegistries.Keys.ENTITY_TYPES, EntityEntry::new, EntityEntry::cast);

		this.entityFactory = entityFactory;

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> onRegister(entityType -> {
			OneTimeEventReceiver.addModListener(EntityRenderersEvent.RegisterRenderers.class, event -> {
				var renderer = rendererFactory.get();

				if(renderer != null)
				{
					try
					{
						event.registerEntityRenderer(entityType, renderer::apply);
					}
					catch(Exception e)
					{
						throw new IllegalStateException("Failed to register renderer for Entity %s:%s".formatted(owner.modId, name), e);
					}
				}
			});

			OneTimeEventReceiver.addModListener(EntityAttributeCreationEvent.class, event -> {
				var attributeBuilder = attributesFactory.get();

				if(attributeBuilder != null)
					event.put((EntityType<? extends LivingEntity>) entityType, attributeBuilder.build());
			});

			OneTimeEventReceiver.addModListener(FMLCommonSetupEvent.class, event -> event.enqueueWork(() -> {
				if(spawnPlacementData != null)
					registerEntitySpawnPlacements(entityType, spawnPlacementData);
			}));
		}));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> properties(NonNullUnaryOperator<EntityType.Builder<ENTITY>> builderModifier)
	{
		this.builderModifier = this.builderModifier.andThen(builderModifier);
		return this;
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> renderer(Supplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super ENTITY>>> rendererFactory)
	{
		this.rendererFactory = rendererFactory;
		return this;
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> attributes(Supplier<AttributeSupplier.Builder> attributesFactory)
	{
		this.attributesFactory = attributesFactory;
		return this;
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> category(MobCategory mobCategory)
	{
		this.mobCategory = mobCategory;
		return this;
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> spawnPlacement(SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightMapType, SpawnPlacements.SpawnPredicate<ENTITY> spawnPredicate)
	{
		spawnPlacementData = new SpawnPlacementData<>(spawnPlacementType, heightMapType, spawnPredicate);
		return this;
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> spawnPlacement(SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightMapType)
	{
		return spawnPlacement(spawnPlacementType, heightMapType, (entityType, level, spawnType, pos, rng) -> get().isIn(entityType));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> defaultSpawnEgg(int backgroundColor, int highlightColor)
	{
		return spawnEgg(backgroundColor, highlightColor).build();
	}

	@SuppressWarnings({ "RedundantCast", "rawtypes" })
	public ItemBuilder<OWNER, ForgeSpawnEggItem, EntityBuilder<OWNER, ENTITY, PARENT>> spawnEgg(int backgroundColor, int highlightColor)
	{
		return owner
				.item(this, "%s_%s".formatted(name, SPAWN_EGG_SUFFIX), properties -> new ForgeSpawnEggItem((Supplier<? extends EntityType<? extends Mob>>) (Supplier) asSupplier(), backgroundColor, highlightColor, properties))
				.transform(ItemBuilder::applySpawnEggItemDefaults)
		;
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> defaultLang()
	{
		return lang(EntityType::getDescriptionId);
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> lang(String name)
	{
		return lang(EntityType::getDescriptionId, name);
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> loot(NonNullBiConsumer<RegistrateEntityLootTables, EntityType<ENTITY>> consumer)
	{
		return setData(LOOT, (ctx, provider) -> provider.addLootAction(RegistrateLootTableProvider.LootType.ENTITY, lootTables -> consumer.accept(lootTables, ctx.get())));
	}

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

	public EntityBuilder<OWNER, ENTITY, PARENT> immuneTo(Block... blocks)
	{
		return properties(properties -> properties.immuneTo(blocks));
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

	public EntityBuilder<OWNER, ENTITY, PARENT> setUpdateInterval(int interval)
	{
		return properties(properties -> properties.setUpdateInterval(interval));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> setTrackingRange(int range)
	{
		return properties(properties -> properties.setTrackingRange(range));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> setShouldReceiveVelocityUpdates(boolean value)
	{
		return properties(properties -> properties.setShouldReceiveVelocityUpdates(value));
	}

	public EntityBuilder<OWNER, ENTITY, PARENT> setCustomClientFactory(BiFunction<PlayMessages.SpawnEntity, Level, ENTITY> customClientFactory)
	{
		return properties(properties -> properties.setCustomClientFactory(customClientFactory));
	}

	@SafeVarargs
	public final EntityBuilder<OWNER, ENTITY, PARENT> tag(TagKey<EntityType<?>>... tags)
	{
		return tag(ENTITY_TAGS, tags);
	}

	@SafeVarargs
	public final EntityBuilder<OWNER, ENTITY, PARENT> removeTag(TagKey<EntityType<?>>... tags)
	{
		return removeTag(ENTITY_TAGS, tags);
	}

	@Override
	protected EntityType<ENTITY> createEntry()
	{
		return builderModifier.apply(EntityType.Builder.of(entityFactory, mobCategory)).build("%s:%s".formatted(owner.modId, name));
	}

	public static <ENTITY extends Entity> void registerEntitySpawnPlacements(EntityType<ENTITY> entityType, SpawnPlacementData<ENTITY> spawnPlacementData)
	{
		var existingData = SpawnPlacements.DATA_BY_TYPE.put(entityType, spawnPlacementData.toVanillaData());

		if(existingData != null)
			throw new IllegalStateException("Duplicate SpawnPlacement registration for type: %s".formatted(RegistryHelper.getRegistryName(ForgeRegistries.ENTITY_TYPES, entityType)));
	}

	private record SpawnPlacementData<ENTITY extends Entity>(SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightMapType, SpawnPlacements.SpawnPredicate<ENTITY> spawnPredicate)
	{
		public SpawnPlacements.Data toVanillaData()
		{
			return new SpawnPlacements.Data(heightMapType, spawnPlacementType, spawnPredicate);
		}
	}
}