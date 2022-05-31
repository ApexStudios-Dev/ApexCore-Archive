package xyz.apex.forge.utility.registrator;

import com.google.common.annotations.Beta;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.*;
import com.tterrag.registrate.util.entry.RegistryEntry;
import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.Util;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.*;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.versions.forge.ForgeVersion;

import xyz.apex.forge.utility.registrator.builder.*;
import xyz.apex.forge.utility.registrator.entry.PaintingEntry;
import xyz.apex.forge.utility.registrator.entry.RecipeSerializerEntry;
import xyz.apex.forge.utility.registrator.factory.*;
import xyz.apex.forge.utility.registrator.factory.item.*;
import xyz.apex.forge.utility.registrator.helper.ForgeSpawnEggItem;
import xyz.apex.forge.utility.registrator.provider.BlockListReporter;
import xyz.apex.forge.utility.registrator.provider.RegistrateLangExtProvider;
import xyz.apex.forge.utility.registrator.provider.RegistrateParticleProvider;
import xyz.apex.forge.utility.registrator.provider.RegistrateSoundProvider;
import xyz.apex.java.utility.Lazy;
import xyz.apex.java.utility.nullness.NonnullConsumer;
import xyz.apex.java.utility.nullness.NonnullFunction;
import xyz.apex.java.utility.nullness.NonnullSupplier;
import xyz.apex.java.utility.nullness.NonnullUnaryOperator;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings({ "DeprecatedIsStillUsed", "unchecked", "UnusedReturnValue", "UnstableApiUsage", "unused", "deprecation", "CommentedOutCode" })
public abstract class AbstractRegistrator<REGISTRATOR extends AbstractRegistrator<REGISTRATOR>>
{
	public static final String REGISTRATOR_ID = "registrator";
	public static final String FORGE_ID = ForgeVersion.MOD_ID;
	public static final String MINECRAFT_ID = "minecraft";

	// region: Reflected Constants
	// public static final EntityPredicate.Builder ENTITY_ON_FIRE = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build());
	public static final EntityPredicate.Builder ENTITY_ON_FIRE = EntityLoot.ENTITY_ON_FIRE;

	// public static final StaticTagHelper<Block> BLOCK_TAG_REGISTRY = ObfuscationReflectionHelper.getPrivateValue(BlockTags.class, null, "field_199899_c");
	// public static final StaticTagHelper<Item> ITEM_TAG_REGISTRY = ObfuscationReflectionHelper.getPrivateValue(ItemTags.class, null, "field_199906_c");
	// public static final StaticTagHelper<EntityType<?>> ENTITY_TYPE_TAG_REGISTRY = ObfuscationReflectionHelper.getPrivateValue(EntityTypeTags.class, null, "field_219766_c");
	// public static final StaticTagHelper<Fluid> FLUID_TAG_REGISTRY = ObfuscationReflectionHelper.getPrivateValue(FluidTags.class, null, "field_206961_c");

	public static final StaticTagHelper<Block> BLOCK_TAG_REGISTRY = BlockTags.HELPER;
	public static final StaticTagHelper<Item> ITEM_TAG_REGISTRY = ItemTags.HELPER;
	public static final StaticTagHelper<EntityType<?>> ENTITY_TYPE_TAG_REGISTRY = EntityTypeTags.HELPER;
	public static final StaticTagHelper<Fluid> FLUID_TAG_REGISTRY = FluidTags.HELPER;
	// endregion

	// region: ProviderTypes
	public static final ProviderType<RegistrateLangExtProvider> LANG_EXT_PROVIDER = ProviderType.register(REGISTRATOR_ID + ":lang_ext", (owner, event) -> new RegistrateLangExtProvider(owner, event.getGenerator()));
	public static final ProviderType<RegistrateSoundProvider> SOUNDS_PROVIDER = ProviderType.register(REGISTRATOR_ID + ":sounds", (owner, event) -> new RegistrateSoundProvider(owner, event.getGenerator(), event.getExistingFileHelper()));
	public static final ProviderType<RegistrateParticleProvider> PARTICLE_PROVIDER = ProviderType.register(REGISTRATOR_ID + ":particles", (owner, event) -> new RegistrateParticleProvider(owner, event.getGenerator(), event.getExistingFileHelper()));

	// region: Tags
	public static final ProviderType<RegistrateTagsProvider<Potion>> POTION_TYPE_TAGS_PROVIDER = ProviderType.register(REGISTRATOR_ID + ":tags/potion_type", type -> (owner, event) -> new RegistrateTagsProvider<>(owner, type, "potion_types", event.getGenerator(), Registry.POTION, event.getExistingFileHelper()));
	public static final ProviderType<RegistrateTagsProvider<Enchantment>> ENCHANTMENT_TAGS_PROVIDER = ProviderType.register(REGISTRATOR_ID + ":tags/enchantment", type -> (owner, event) -> new RegistrateTagsProvider<>(owner, type, "enchantments", event.getGenerator(), Registry.ENCHANTMENT, event.getExistingFileHelper()));
	public static final ProviderType<RegistrateTagsProvider<BlockEntityType<?>>> BLOCK_ENTITY_TAGS_PROVIDER = ProviderType.register(REGISTRATOR_ID + ":tags/block_entity_type", type -> (owner, event) -> new RegistrateTagsProvider<>(owner, type, "tile_entity_types", event.getGenerator(), Registry.BLOCK_ENTITY_TYPE, event.getExistingFileHelper()));
	// endregion
	// endregion

	@Deprecated public final Backend backend;
	protected final REGISTRATOR self = (REGISTRATOR) this;
	protected final IEventBus modBus;

	protected AbstractRegistrator(String modId)
	{
		modBus = FMLJavaModLoadingContext.get().getModEventBus();
		backend = new Backend(modId);
	}

	public final String getModId()
	{
		return backend.getModid();
	}

	public final REGISTRATOR self()
	{
		return self;
	}

	public final IEventBus getModBus()
	{
		return modBus;
	}

	public final ResourceLocation id(String path)
	{
		return new ResourceLocation(getModId(), path);
	}

	public final String idString(String path)
	{
		return getModId() + ':' + path;
	}

	// region: Tags
	// region: Generic
	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> tag(StaticTagHelper<TYPE> tagRegistry, String tagNamespace, String tagPath)
	{
		if(AbstractRegistrate.isDevEnvironment())
			return tagOptional(tagRegistry, tagNamespace, tagPath);

		return tagRegistry.bind(tagNamespace + ':' + tagPath);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> vanillaTag(StaticTagHelper<TYPE> tagRegistry, String tagPath)
	{
		return tag(tagRegistry, MINECRAFT_ID, tagPath);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> forgeTag(StaticTagHelper<TYPE> tagRegistry, String tagPath)
	{
		return tag(tagRegistry, FORGE_ID, tagPath);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> registratorTag(StaticTagHelper<TYPE> tagRegistry, String tagPath)
	{
		return tag(tagRegistry, REGISTRATOR_ID, tagPath);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> moddedTag(StaticTagHelper<TYPE> tagRegistry, String tagPath)
	{
		return tag(tagRegistry, getModId(), tagPath);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> tagOptional(StaticTagHelper<TYPE> tagRegistry, String tagNamespace, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return tagRegistry.createOptional(new ResourceLocation(tagNamespace, tagPath), tagDefaults);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> tagOptional(StaticTagHelper<TYPE> tagRegistry, String tagNamespace, String tagPath)
	{
		return tagOptional(tagRegistry, tagNamespace, tagPath, null);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> vanillaTagOptional(StaticTagHelper<TYPE> tagRegistry, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return tagOptional(tagRegistry, MINECRAFT_ID, tagPath, tagDefaults);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> vanillaTagOptional(StaticTagHelper<TYPE> tagRegistry, String tagPath)
	{
		return vanillaTagOptional(tagRegistry, tagPath, null);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> forgeTagOptional(StaticTagHelper<TYPE> tagRegistry, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return tagOptional(tagRegistry, FORGE_ID, tagPath, tagDefaults);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> forgeTagOptional(StaticTagHelper<TYPE> tagRegistry, String tagPath)
	{
		return forgeTagOptional(tagRegistry, tagPath, null);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> registratorTagOptional(StaticTagHelper<TYPE> tagRegistry, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return tagOptional(tagRegistry, REGISTRATOR_ID, tagPath, tagDefaults);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> registratorTagOptional(StaticTagHelper<TYPE> tagRegistry, String tagPath)
	{
		return registratorTagOptional(tagRegistry, tagPath, null);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> moddedTagOptional(StaticTagHelper<TYPE> tagRegistry, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return tagOptional(tagRegistry, getModId(), tagPath, tagDefaults);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> moddedTagOptional(StaticTagHelper<TYPE> tagRegistry, String tagPath)
	{
		return moddedTagOptional(tagRegistry, tagPath, null);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> tag(IForgeRegistry<TYPE> tagRegistry, String tagNamespace, String tagPath)
	{
		if(AbstractRegistrate.isDevEnvironment())
			return tagOptional(tagRegistry, tagNamespace, tagPath);

		return ForgeTagHandler.makeWrapperTag(tagRegistry, new ResourceLocation(tagNamespace, tagPath));
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> vanillaTag(IForgeRegistry<TYPE> tagRegistry, String tagPath)
	{
		return tag(tagRegistry, MINECRAFT_ID, tagPath);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> forgeTag(IForgeRegistry<TYPE> tagRegistry, String tagPath)
	{
		return tag(tagRegistry, FORGE_ID, tagPath);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> registratorTag(IForgeRegistry<TYPE> tagRegistry, String tagPath)
	{
		return tag(tagRegistry, REGISTRATOR_ID, tagPath);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tag.Named<TYPE> moddedTag(IForgeRegistry<TYPE> tagRegistry, String tagPath)
	{
		return tag(tagRegistry, getModId(), tagPath);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> tagOptional(IForgeRegistry<TYPE> tagRegistry, String tagNamespace, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return ForgeTagHandler.createOptionalTag(tagRegistry, new ResourceLocation(tagNamespace, tagPath));
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> tagOptional(IForgeRegistry<TYPE> tagRegistry, String tagNamespace, String tagPath)
	{
		return tagOptional(tagRegistry, tagNamespace, tagPath, null);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> vanillaTagOptional(IForgeRegistry<TYPE> tagRegistry, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return tagOptional(tagRegistry, MINECRAFT_ID, tagPath, tagDefaults);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> vanillaTagOptional(IForgeRegistry<TYPE> tagRegistry, String tagPath)
	{
		return vanillaTagOptional(tagRegistry, tagPath, null);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> forgeTagOptional(IForgeRegistry<TYPE> tagRegistry, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return tagOptional(tagRegistry, FORGE_ID, tagPath, tagDefaults);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> forgeTagOptional(IForgeRegistry<TYPE> tagRegistry, String tagPath)
	{
		return forgeTagOptional(tagRegistry, tagPath, null);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> registratorTagOptional(IForgeRegistry<TYPE> tagRegistry, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return tagOptional(tagRegistry, REGISTRATOR_ID, tagPath, tagDefaults);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> registratorTagOptional(IForgeRegistry<TYPE> tagRegistry, String tagPath)
	{
		return registratorTagOptional(tagRegistry, tagPath, null);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> moddedTagOptional(IForgeRegistry<TYPE> tagRegistry, String tagPath, @Nullable Set<Supplier<TYPE>> tagDefaults)
	{
		return tagOptional(tagRegistry, getModId(), tagPath, tagDefaults);
	}

	public final <TYPE extends IForgeRegistryEntry<TYPE>> Tags.IOptionalNamedTag<TYPE> moddedTagOptional(IForgeRegistry<TYPE> tagRegistry, String tagPath)
	{
		return moddedTagOptional(tagRegistry, tagPath, null);
	}
	// endregion

	// region: Block
	public final Tag.Named<Block> blockTag(String tagNamespace, String tagPath)
	{
		return tag(BLOCK_TAG_REGISTRY, tagNamespace, tagPath);
	}

	public final Tag.Named<Block> vanillaBlockTag(String tagPath)
	{
		return blockTag(MINECRAFT_ID, tagPath);
	}

	public final Tag.Named<Block> forgeBlockTag(String tagPath)
	{
		return blockTag(FORGE_ID, tagPath);
	}

	public final Tag.Named<Block> registratorBlockTag(String tagPath)
	{
		return blockTag(REGISTRATOR_ID, tagPath);
	}

	public final Tag.Named<Block> moddedBlockTag(String tagPath)
	{
		return blockTag(getModId(), tagPath);
	}

	public final Tags.IOptionalNamedTag<Block> blockTagOptional(String tagNamespace, String tagPath, @Nullable Set<Supplier<Block>> tagDefaults)
	{
		return tagOptional(BLOCK_TAG_REGISTRY, tagNamespace, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Block> blockTagOptional(String tagNamespace, String tagPath)
	{
		return blockTagOptional(tagNamespace, tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Block> vanillaBlockTagOptional(String tagPath, @Nullable Set<Supplier<Block>> tagDefaults)
	{
		return blockTagOptional(MINECRAFT_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Block> vanillaBlockTagOptional(String tagPath)
	{
		return vanillaBlockTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Block> forgeBlockTagOptional(String tagPath, @Nullable Set<Supplier<Block>> tagDefaults)
	{
		return blockTagOptional(FORGE_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Block> forgeBlockTagOptional(String tagPath)
	{
		return forgeBlockTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Block> registratorBlockTagOptional(String tagPath, @Nullable Set<Supplier<Block>> tagDefaults)
	{
		return blockTagOptional(REGISTRATOR_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Block> registratorBlockTagOptional(String tagPath)
	{
		return registratorBlockTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Block> moddedBlockTagOptional(String tagPath, @Nullable Set<Supplier<Block>> tagDefaults)
	{
		return blockTagOptional(getModId(), tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Block> moddedBlockTagOptional(String tagPath)
	{
		return moddedBlockTagOptional(tagPath, null);
	}
	// endregion

	// region: Item
	public final Tag.Named<Item> itemTag(String tagNamespace, String tagPath)
	{
		return tag(ITEM_TAG_REGISTRY, tagNamespace, tagPath);
	}

	public final Tag.Named<Item> vanillaItemTag(String tagPath)
	{
		return itemTag(MINECRAFT_ID, tagPath);
	}

	public final Tag.Named<Item> forgeItemTag(String tagPath)
	{
		return itemTag(FORGE_ID, tagPath);
	}

	public final Tag.Named<Item> registratorItemTag(String tagPath)
	{
		return itemTag(REGISTRATOR_ID, tagPath);
	}

	public final Tag.Named<Item> moddedItemTag(String tagPath)
	{
		return itemTag(getModId(), tagPath);
	}

	public final Tags.IOptionalNamedTag<Item> itemTagOptional(String tagNamespace, String tagPath, @Nullable Set<Supplier<Item>> tagDefaults)
	{
		return tagOptional(ITEM_TAG_REGISTRY, tagNamespace, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Item> itemTagOptional(String tagNamespace, String tagPath)
	{
		return itemTagOptional(tagNamespace, tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Item> vanillaItemTagOptional(String tagPath, @Nullable Set<Supplier<Item>> tagDefaults)
	{
		return itemTagOptional(MINECRAFT_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Item> vanillaItemTagOptional(String tagPath)
	{
		return vanillaItemTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Item> forgeItemTagOptional(String tagPath, @Nullable Set<Supplier<Item>> tagDefaults)
	{
		return itemTagOptional(FORGE_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Item> forgeItemTagOptional(String tagPath)
	{
		return forgeItemTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Item> registratorItemTagOptional(String tagPath, @Nullable Set<Supplier<Item>> tagDefaults)
	{
		return itemTagOptional(REGISTRATOR_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Item> registratorItemTagOptional(String tagPath)
	{
		return registratorItemTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Item> moddedItemTagOptional(String tagPath, @Nullable Set<Supplier<Item>> tagDefaults)
	{
		return itemTagOptional(getModId(), tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Item> moddedItemTagOptional(String tagPath)
	{
		return moddedItemTagOptional(tagPath, null);
	}
	// endregion

	// region: EntityType
	public final Tag.Named<EntityType<?>> entityTypeTag(String tagNamespace, String tagPath)
	{
		return tag(ENTITY_TYPE_TAG_REGISTRY, tagNamespace, tagPath);
	}

	public final Tag.Named<EntityType<?>> vanillaEntityTypeTag(String tagPath)
	{
		return entityTypeTag(MINECRAFT_ID, tagPath);
	}

	public final Tag.Named<EntityType<?>> forgeEntityTypeTag(String tagPath)
	{
		return entityTypeTag(FORGE_ID, tagPath);
	}

	public final Tag.Named<EntityType<?>> registratorEntityTypeTag(String tagPath)
	{
		return entityTypeTag(REGISTRATOR_ID, tagPath);
	}

	public final Tag.Named<EntityType<?>> moddedEntityTypeTag(String tagPath)
	{
		return entityTypeTag(getModId(), tagPath);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> entityTypeTagOptional(String tagNamespace, String tagPath, @Nullable Set<Supplier<EntityType<?>>> tagDefaults)
	{
		return tagOptional(ENTITY_TYPE_TAG_REGISTRY, tagNamespace, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> entityTypeTagOptional(String tagNamespace, String tagPath)
	{
		return entityTypeTagOptional(tagNamespace, tagPath, null);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> vanillaEntityTypeTagOptional(String tagPath, @Nullable Set<Supplier<EntityType<?>>> tagDefaults)
	{
		return entityTypeTagOptional(MINECRAFT_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> vanillaEntityTypeTagOptional(String tagPath)
	{
		return vanillaEntityTypeTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> forgeEntityTypeTagOptional(String tagPath, @Nullable Set<Supplier<EntityType<?>>> tagDefaults)
	{
		return entityTypeTagOptional(FORGE_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> forgeEntityTypeTagOptional(String tagPath)
	{
		return forgeEntityTypeTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> registratorEntityTypeTagOptional(String tagPath, @Nullable Set<Supplier<EntityType<?>>> tagDefaults)
	{
		return entityTypeTagOptional(REGISTRATOR_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> registratorEntityTypeTagOptional(String tagPath)
	{
		return registratorEntityTypeTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> moddedEntityTypeTagOptional(String tagPath, @Nullable Set<Supplier<EntityType<?>>> tagDefaults)
	{
		return entityTypeTagOptional(getModId(), tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<EntityType<?>> moddedEntityTypeTagOptional(String tagPath)
	{
		return moddedEntityTypeTagOptional(tagPath, null);
	}
	// endregion

	// region: Fluid
	public final Tag.Named<Fluid> fluidTag(String tagNamespace, String tagPath)
	{
		return tag(FLUID_TAG_REGISTRY, tagNamespace, tagPath);
	}

	public final Tag.Named<Fluid> vanillaFluidTag(String tagPath)
	{
		return fluidTag(MINECRAFT_ID, tagPath);
	}

	public final Tag.Named<Fluid> forgeFluidTag(String tagPath)
	{
		return fluidTag(FORGE_ID, tagPath);
	}

	public final Tag.Named<Fluid> registratorFluidTag(String tagPath)
	{
		return fluidTag(REGISTRATOR_ID, tagPath);
	}

	public final Tag.Named<Fluid> moddedFluidTag(String tagPath)
	{
		return fluidTag(getModId(), tagPath);
	}

	public final Tags.IOptionalNamedTag<Fluid> fluidTagOptional(String tagNamespace, String tagPath, @Nullable Set<Supplier<Fluid>> tagDefaults)
	{
		return tagOptional(FLUID_TAG_REGISTRY, tagNamespace, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Fluid> fluidTagOptional(String tagNamespace, String tagPath)
	{
		return fluidTagOptional(tagNamespace, tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Fluid> vanillaFluidTagOptional(String tagPath, @Nullable Set<Supplier<Fluid>> tagDefaults)
	{
		return fluidTagOptional(MINECRAFT_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Fluid> vanillaFluidTagOptional(String tagPath)
	{
		return vanillaFluidTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Fluid> forgeFluidTagOptional(String tagPath, @Nullable Set<Supplier<Fluid>> tagDefaults)
	{
		return fluidTagOptional(FORGE_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Fluid> forgeFluidTagOptional(String tagPath)
	{
		return forgeFluidTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Fluid> registratorFluidTagOptional(String tagPath, @Nullable Set<Supplier<Fluid>> tagDefaults)
	{
		return fluidTagOptional(REGISTRATOR_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Fluid> registratorFluidTagOptional(String tagPath)
	{
		return registratorFluidTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<Fluid> moddedFluidTagOptional(String tagPath, @Nullable Set<Supplier<Fluid>> tagDefaults)
	{
		return fluidTagOptional(getModId(), tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<Fluid> moddedFluidTagOptional(String tagPath)
	{
		return moddedFluidTagOptional(tagPath, null);
	}
	// endregion

	// region: BlockEntityType
	public final Tag.Named<BlockEntityType<?>> blockEntityTypeTag(String tagNamespace, String tagPath)
	{
		return tag(ForgeRegistries.BLOCK_ENTITIES, tagNamespace, tagPath);
	}

	public final Tag.Named<BlockEntityType<?>> vanillaBlockEntityTypeTag(String tagPath)
	{
		return blockEntityTypeTag(MINECRAFT_ID, tagPath);
	}

	public final Tag.Named<BlockEntityType<?>> forgeBlockEntityTypeTag(String tagPath)
	{
		return blockEntityTypeTag(FORGE_ID, tagPath);
	}

	public final Tag.Named<BlockEntityType<?>> registratorBlockEntityTypeTag(String tagPath)
	{
		return blockEntityTypeTag(REGISTRATOR_ID, tagPath);
	}

	public final Tag.Named<BlockEntityType<?>> moddedBlockEntityTypeTag(String tagPath)
	{
		return blockEntityTypeTag(getModId(), tagPath);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> blockEntityTypeTagOptional(String tagNamespace, String tagPath, @Nullable Set<Supplier<BlockEntityType<?>>> tagDefaults)
	{
		return tagOptional(ForgeRegistries.BLOCK_ENTITIES, tagNamespace, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> blockEntityTypeTagOptional(String tagNamespace, String tagPath)
	{
		return blockEntityTypeTagOptional(tagNamespace, tagPath, null);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> vanillaBlockEntityTypeTagOptional(String tagPath, @Nullable Set<Supplier<BlockEntityType<?>>> tagDefaults)
	{
		return blockEntityTypeTagOptional(MINECRAFT_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> vanillaBlockEntityTypeTagOptional(String tagPath)
	{
		return vanillaBlockEntityTypeTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> forgeBlockEntityTypeTagOptional(String tagPath, @Nullable Set<Supplier<BlockEntityType<?>>> tagDefaults)
	{
		return blockEntityTypeTagOptional(FORGE_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> forgeBlockEntityTypeTagOptional(String tagPath)
	{
		return forgeBlockEntityTypeTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> registratorBlockEntityTypeTagOptional(String tagPath, @Nullable Set<Supplier<BlockEntityType<?>>> tagDefaults)
	{
		return blockEntityTypeTagOptional(REGISTRATOR_ID, tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> registratorBlockEntityTypeTagOptional(String tagPath)
	{
		return registratorBlockEntityTypeTagOptional(tagPath, null);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> moddedBlockEntityTypeTagOptional(String tagPath, @Nullable Set<Supplier<BlockEntityType<?>>> tagDefaults)
	{
		return blockEntityTypeTagOptional(getModId(), tagPath, tagDefaults);
	}

	public final Tags.IOptionalNamedTag<BlockEntityType<?>> moddedBlockEntityTypeTagOptional(String tagPath)
	{
		return moddedBlockEntityTypeTagOptional(tagPath, null);
	}
	// endregion
	// endregion

	// region: Block
	protected final <BLOCK extends Block, PARENT> BlockBuilder<REGISTRATOR, BLOCK, PARENT> blockEntry(String registryName, PARENT parent, BlockFactory<BLOCK> blockFactory, NonnullSupplier<BlockBehaviour.Properties> initialProperties)
	{
		return entry(registryName, callback -> new BlockBuilder<>(self, parent, registryName, callback, blockFactory, initialProperties));
	}

	public final <BLOCK extends Block, PARENT> BlockBuilder<REGISTRATOR, BLOCK, PARENT> block(String registryName, PARENT parent, BlockFactory<BLOCK> blockFactory)
	{
		return block(registryName, parent, Material.STONE, blockFactory);
	}

	public final <BLOCK extends Block, PARENT> BlockBuilder<REGISTRATOR, BLOCK, PARENT> block(String registryName, PARENT parent, Material material, BlockFactory<BLOCK> blockFactory)
	{
		return blockEntry(registryName, parent, blockFactory, () -> BlockBehaviour.Properties.of(material));
	}

	public final <BLOCK extends Block, PARENT> BlockBuilder<REGISTRATOR, BLOCK, PARENT> block(String registryName, PARENT parent, Material material, DyeColor materialColor, BlockFactory<BLOCK> blockFactory)
	{
		return blockEntry(registryName, parent, blockFactory, () -> BlockBehaviour.Properties.of(material, materialColor));
	}

	public final <BLOCK extends Block, PARENT> BlockBuilder<REGISTRATOR, BLOCK, PARENT> block(String registryName, PARENT parent, Material material, MaterialColor materialColor, BlockFactory<BLOCK> blockFactory)
	{
		return blockEntry(registryName, parent, blockFactory, () -> BlockBehaviour.Properties.of(material, materialColor));
	}

	public final <BLOCK extends Block, PARENT> BlockBuilder<REGISTRATOR, BLOCK, PARENT> block(String registryName, PARENT parent, Material material, NonnullFunction<BlockState, MaterialColor> materialColorFactory, BlockFactory<BLOCK> blockFactory)
	{
		return blockEntry(registryName, parent, blockFactory, () -> BlockBehaviour.Properties.of(material, materialColorFactory));
	}

	public final <BLOCK extends Block, PARENT> BlockBuilder<REGISTRATOR, BLOCK, PARENT> block(String registryName, PARENT parent, NonnullSupplier<BlockBehaviour> blockProperties, BlockFactory<BLOCK> blockFactory)
	{
		return blockEntry(registryName, parent, blockFactory, () -> BlockBehaviour.Properties.copy(blockProperties.get()));
	}

	public final <PARENT> BlockBuilder<REGISTRATOR, Block, PARENT> block(String registryName, PARENT parent)
	{
		return block(registryName, parent, BlockFactory.DEFAULT);
	}

	public final <PARENT> BlockBuilder<REGISTRATOR, Block, PARENT> block(String registryName, PARENT parent, Material material)
	{
		return block(registryName, parent, material, BlockFactory.DEFAULT);
	}

	public final <PARENT> BlockBuilder<REGISTRATOR, Block, PARENT> block(String registryName, PARENT parent, Material material, DyeColor materialColor)
	{
		return block(registryName, parent, material, materialColor, BlockFactory.DEFAULT);
	}

	public final <PARENT> BlockBuilder<REGISTRATOR, Block, PARENT> block(String registryName, PARENT parent, Material material, MaterialColor materialColor)
	{
		return block(registryName, parent, material, materialColor, BlockFactory.DEFAULT);
	}

	public final <PARENT> BlockBuilder<REGISTRATOR, Block, PARENT> block(String registryName, PARENT parent, Material material, NonnullFunction<BlockState, MaterialColor> materialColorFactory)
	{
		return block(registryName, parent, material, materialColorFactory, BlockFactory.DEFAULT);
	}

	public final <PARENT> BlockBuilder<REGISTRATOR, Block, PARENT> block(String registryName, PARENT parent, NonnullSupplier<BlockBehaviour> blockProperties)
	{
		return block(registryName, parent, blockProperties, BlockFactory.DEFAULT);
	}

	public final <BLOCK extends Block> BlockBuilder<REGISTRATOR, BLOCK, REGISTRATOR> block(String registryName, BlockFactory<BLOCK> blockFactory)
	{
		return block(registryName, self, blockFactory);
	}

	public final <BLOCK extends Block> BlockBuilder<REGISTRATOR, BLOCK, REGISTRATOR> block(String registryName, Material material, BlockFactory<BLOCK> blockFactory)
	{
		return block(registryName, self, material, blockFactory);
	}

	public final <BLOCK extends Block> BlockBuilder<REGISTRATOR, BLOCK, REGISTRATOR> block(String registryName, Material material, DyeColor materialColor, BlockFactory<BLOCK> blockFactory)
	{
		return block(registryName, self, material, materialColor, blockFactory);
	}

	public final <BLOCK extends Block> BlockBuilder<REGISTRATOR, BLOCK, REGISTRATOR> block(String registryName, Material material, MaterialColor materialColor, BlockFactory<BLOCK> blockFactory)
	{
		return block(registryName, self, material, materialColor, blockFactory);
	}

	public final <BLOCK extends Block> BlockBuilder<REGISTRATOR, BLOCK, REGISTRATOR> block(String registryName, Material material, NonnullFunction<BlockState, MaterialColor> materialColorFactory, BlockFactory<BLOCK> blockFactory)
	{
		return block(registryName, self, material, materialColorFactory, blockFactory);
	}

	public final <BLOCK extends Block> BlockBuilder<REGISTRATOR, BLOCK, REGISTRATOR> block(String registryName, NonnullSupplier<BlockBehaviour> blockProperties, BlockFactory<BLOCK> blockFactory)
	{
		return block(registryName, self, blockProperties, blockFactory);
	}

	public final BlockBuilder<REGISTRATOR, Block, REGISTRATOR> block(String registryName)
	{
		return block(registryName, self, BlockFactory.DEFAULT);
	}

	public final BlockBuilder<REGISTRATOR, Block, REGISTRATOR> block(String registryName, Material material)
	{
		return block(registryName, self, material, BlockFactory.DEFAULT);
	}

	public final BlockBuilder<REGISTRATOR, Block, REGISTRATOR> block(String registryName, Material material, DyeColor materialColor)
	{
		return block(registryName, self, material, materialColor, BlockFactory.DEFAULT);
	}

	public final BlockBuilder<REGISTRATOR, Block, REGISTRATOR> block(String registryName, Material material, MaterialColor materialColor)
	{
		return block(registryName, self, material, materialColor, BlockFactory.DEFAULT);
	}

	public final BlockBuilder<REGISTRATOR, Block, REGISTRATOR> block(String registryName, Material material, NonnullFunction<BlockState, MaterialColor> materialColorFactory)
	{
		return block(registryName, self, material, materialColorFactory, BlockFactory.DEFAULT);
	}

	public final BlockBuilder<REGISTRATOR, Block, REGISTRATOR> block(String registryName, NonnullSupplier<BlockBehaviour> blockProperties)
	{
		return block(registryName, self, blockProperties, BlockFactory.DEFAULT);
	}
	// endregion

	// region: Item
	// region: Simple
	public final <ITEM extends Item, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> item(String registryName, PARENT parent, ItemFactory<ITEM> itemFactory)
	{
		return entry(registryName, callback -> new ItemBuilder<>(self, parent, registryName, callback, itemFactory));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, Item, PARENT> item(String registryName, PARENT parent)
	{
		return item(registryName, parent, ItemFactory.DEFAULT);
	}

	public final <ITEM extends Item> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> item(String registryName, ItemFactory<ITEM> itemFactory)
	{
		return item(registryName, self, itemFactory);
	}

	public final ItemBuilder<REGISTRATOR, Item, REGISTRATOR> item(String registryName)
	{
		return item(registryName, self, ItemFactory.DEFAULT);
	}
	// endregion

	// region: BlockItem
	public final <BLOCK extends Block, ITEM extends BlockItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> blockItem(String registryName, PARENT parent, NonnullSupplier<BLOCK> block, BlockItemFactory<BLOCK, ITEM> blockItemFactory)
	{
		return item(registryName, parent, properties -> blockItemFactory.create(block.get(), properties))
				// apply registrate default block item properties
				// block items should not register translations
				// they use the same translation key as the block
				.clearDataGenerator(ProviderType.LANG)
				.clearDataGenerator(AbstractRegistrator.LANG_EXT_PROVIDER)
				.model((ctx, provider) -> blockItemModel(ctx, provider, block))
		;
	}

	public final <BLOCK extends Block, PARENT> ItemBuilder<REGISTRATOR, BlockItem, PARENT> blockItem(String registryName, PARENT parent, NonnullSupplier<BLOCK> block)
	{
		return blockItem(registryName, parent, block, BlockItemFactory.forBlock());
	}

	public final <BLOCK extends Block, ITEM extends BlockItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> blockItem(String registryName, NonnullSupplier<BLOCK> block, BlockItemFactory<BLOCK, ITEM> blockItemFactory)
	{
		return blockItem(registryName, self, block, blockItemFactory);
	}

	public final <BLOCK extends Block> ItemBuilder<REGISTRATOR, BlockItem, REGISTRATOR> blockItem(String registryName, NonnullSupplier<BLOCK> block)
	{
		return blockItem(registryName, self, block, BlockItemFactory.forBlock());
	}

	private <BLOCK extends Block, ITEM extends BlockItem> void blockItemModel(DataGenContext<Item, ITEM> ctx, RegistrateItemModelProvider provider, NonnullSupplier<BLOCK> block)
	{
		var model = getDataProvider(ProviderType.BLOCKSTATE)
				.flatMap(p -> p.getExistingVariantBuilder(block.get()))
				.map(builder -> builder.getModels().get(builder.partialState()))
				.map(BlockStateProvider.ConfiguredModelList::toJSON)
				.filter(JsonElement::isJsonObject)
				.map(JsonElement::getAsJsonObject)
				.map(json -> json.get("model"))
				.map(JsonElement::getAsString);

		if(model.isPresent())
			provider.withExistingParent(ctx.getName(), model.get());
		else
			provider.blockItem(block::get);
	}
	// endregion

	// region: BannerPatternItem
	public final <ITEM extends BannerPatternItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> bannerPatternItem(String registryName, PARENT parent, BannerPattern bannerPattern, BannerPatternItemFactory<ITEM> bannerPatternItemFactory)
	{
		return item(registryName, parent, properties -> bannerPatternItemFactory.create(bannerPattern, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, BannerPatternItem, PARENT> bannerPatternItem(String registryName, PARENT parent, BannerPattern bannerPattern)
	{
		return bannerPatternItem(registryName, parent, bannerPattern, BannerPatternItemFactory.DEFAULT);
	}

	public final <ITEM extends BannerPatternItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> bannerPatternItem(String registryName, BannerPattern bannerPattern, BannerPatternItemFactory<ITEM> bannerPatternItemFactory)
	{
		return bannerPatternItem(registryName, self, bannerPattern, bannerPatternItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, BannerPatternItem, REGISTRATOR> bannerPatternItem(String registryName, BannerPattern bannerPattern)
	{
		return bannerPatternItem(registryName, self, bannerPattern, BannerPatternItemFactory.DEFAULT);
	}
	// endregion

	// region: RecordItem
	public final <ITEM extends RecordItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> musicDiscItem(String registryName, PARENT parent, int comparatorValue, NonnullSupplier<SoundEvent> soundEvent, RecordItemFactory<ITEM> recordItemFactory)
	{
		return item(registryName, parent, properties -> recordItemFactory.create(comparatorValue, soundEvent, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, RecordItem, PARENT> musicDiscItem(String registryName, PARENT parent, int comparatorValue, NonnullSupplier<SoundEvent> soundEvent)
	{
		return musicDiscItem(registryName, parent, comparatorValue, soundEvent, RecordItemFactory.DEFAULT);
	}

	public final <ITEM extends RecordItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> musicDiscItem(String registryName, int comparatorValue, NonnullSupplier<SoundEvent> soundEvent, RecordItemFactory<ITEM> recordItemFactory)
	{
		return musicDiscItem(registryName, self, comparatorValue, soundEvent, recordItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, RecordItem, REGISTRATOR> musicDiscItem(String registryName, int comparatorValue, NonnullSupplier<SoundEvent> soundEvent)
	{
		return musicDiscItem(registryName, self, comparatorValue, soundEvent, RecordItemFactory.DEFAULT);
	}
	// endregion

	// region: TieredItem
	public final <ITEM extends TieredItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> tieredItem(String registryName, PARENT parent, Tier itemTier, TieredItemFactory<ITEM> tieredItemFactory)
	{
		return item(registryName, parent, properties -> tieredItemFactory.create(itemTier, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, TieredItem, PARENT> tieredItem(String registryName, PARENT parent, Tier itemTier)
	{
		return tieredItem(registryName, parent, itemTier, TieredItemFactory.DEFAULT);
	}

	public final <ITEM extends TieredItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> tieredItem(String registryName, Tier itemTier, TieredItemFactory<ITEM> tieredItemFactory)
	{
		return tieredItem(registryName, self, itemTier, tieredItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, TieredItem, REGISTRATOR> tieredItem(String registryName, Tier itemTier)
	{
		return tieredItem(registryName, self, itemTier, TieredItemFactory.DEFAULT);
	}

	// region: SwordItem
	public final <ITEM extends SwordItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> swordItem(String registryName, PARENT parent, Tier itemTier, int attackDamage, float attackSpeed, SwordItemFactory<ITEM> swordItemFactory)
	{
		return item(registryName, parent, properties -> swordItemFactory.create(itemTier, attackDamage, attackSpeed, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, SwordItem, PARENT> swordItem(String registryName, PARENT parent, Tier itemTier, int attackDamage, float attackSpeed)
	{
		return swordItem(registryName, parent, itemTier, attackDamage, attackSpeed, SwordItemFactory.DEFAULT);
	}

	public final <ITEM extends SwordItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> swordItem(String registryName, Tier itemTier, int attackDamage, float attackSpeed, SwordItemFactory<ITEM> swordItemFactory)
	{
		return swordItem(registryName, self, itemTier, attackDamage, attackSpeed, swordItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, SwordItem, REGISTRATOR> swordItem(String registryName, Tier itemTier, int attackDamage, float attackSpeed)
	{
		return swordItem(registryName, self, itemTier, attackDamage, attackSpeed, SwordItemFactory.DEFAULT);
	}
	// endregion

	// region: DiggerItem
	public final <ITEM extends DiggerItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> diggerItem(String registryName, PARENT parent, Tier itemTier, float attackDamage, float attackSpeed, Tag<Block> diggables, DiggerItemFactory<ITEM> diggerItemFactory)
	{
		return item(registryName, parent, properties -> diggerItemFactory.create(attackDamage, attackSpeed, itemTier, diggables, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, DiggerItem, PARENT> diggerItem(String registryName, PARENT parent, Tier itemTier, float attackDamage, float attackSpeed, Tag<Block> diggables)
	{
		return diggerItem(registryName, parent, itemTier, attackDamage, attackSpeed, diggables, DiggerItemFactory.DEFAULT);
	}

	public final <ITEM extends DiggerItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> diggerItem(String registryName, Tier itemTier, float attackDamage, float attackSpeed, Tag<Block> diggables, DiggerItemFactory<ITEM> diggerItemFactory)
	{
		return diggerItem(registryName, self, itemTier, attackDamage, attackSpeed, diggables, diggerItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, DiggerItem, REGISTRATOR> diggerItem(String registryName, Tier itemTier, float attackDamage, float attackSpeed, Tag<Block> diggables)
	{
		return diggerItem(registryName, self, itemTier, attackDamage, attackSpeed, diggables, DiggerItemFactory.DEFAULT);
	}

	// region: AxeItem
	public final <ITEM extends AxeItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> axeItem(String registryName, PARENT parent, Tier itemTier, float attackDamage, float attackSpeed, AxeItemFactory<ITEM> axeItemFactory)
	{
		return item(registryName, parent, properties -> axeItemFactory.create(itemTier, attackDamage, attackSpeed, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, AxeItem, PARENT> axeItem(String registryName, PARENT parent, Tier itemTier, float attackDamage, float attackSpeed)
	{
		return axeItem(registryName, parent, itemTier, attackDamage, attackSpeed, AxeItemFactory.DEFAULT);
	}

	public final <ITEM extends AxeItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> axeItem(String registryName, Tier itemTier, float attackDamage, float attackSpeed, AxeItemFactory<ITEM> axeItemFactory)
	{
		return axeItem(registryName, self, itemTier, attackDamage, attackSpeed, axeItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, AxeItem, REGISTRATOR> axeItem(String registryName, Tier itemTier, float attackDamage, float attackSpeed)
	{
		return axeItem(registryName, self, itemTier, attackDamage, attackSpeed, AxeItemFactory.DEFAULT);
	}
	// endregion

	// region: HoeItem
	public final <ITEM extends HoeItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> hoeItem(String registryName, PARENT parent, Tier itemTier, int attackDamage, float attackSpeed, HoeItemFactory<ITEM> hoeItemFactory)
	{
		return item(registryName, parent, properties -> hoeItemFactory.create(itemTier, attackDamage, attackSpeed, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, HoeItem, PARENT> hoeItem(String registryName, PARENT parent, Tier itemTier, int attackDamage, float attackSpeed)
	{
		return hoeItem(registryName, parent, itemTier, attackDamage, attackSpeed, HoeItemFactory.DEFAULT);
	}

	public final <ITEM extends HoeItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> hoeItem(String registryName, Tier itemTier, int attackDamage, float attackSpeed, HoeItemFactory<ITEM> hoeItemFactory)
	{
		return hoeItem(registryName, self, itemTier, attackDamage, attackSpeed, hoeItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, HoeItem, REGISTRATOR> hoeItem(String registryName, Tier itemTier, int attackDamage, float attackSpeed)
	{
		return hoeItem(registryName, self, itemTier, attackDamage, attackSpeed, HoeItemFactory.DEFAULT);
	}
	// endregion

	// region: PickaxeItem
	public final <ITEM extends PickaxeItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> pickaxeItem(String registryName, PARENT parent, Tier itemTier, int attackDamage, float attackSpeed, PickaxeItemFactory<ITEM> pickaxeItemFactory)
	{
		return item(registryName, parent, properties -> pickaxeItemFactory.create(itemTier, attackDamage, attackSpeed, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, PickaxeItem, PARENT> pickaxeItem(String registryName, PARENT parent, Tier itemTier, int attackDamage, float attackSpeed)
	{
		return pickaxeItem(registryName, parent, itemTier, attackDamage, attackSpeed, PickaxeItemFactory.DEFAULT);
	}

	public final <ITEM extends PickaxeItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> pickaxeItem(String registryName, Tier itemTier, int attackDamage, float attackSpeed, PickaxeItemFactory<ITEM> pickaxeItemFactory)
	{
		return pickaxeItem(registryName, self, itemTier, attackDamage, attackSpeed, pickaxeItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, PickaxeItem, REGISTRATOR> pickaxeItem(String registryName, Tier itemTier, int attackDamage, float attackSpeed)
	{
		return pickaxeItem(registryName, self, itemTier, attackDamage, attackSpeed, PickaxeItemFactory.DEFAULT);
	}
	// endregion

	// region: ShovelItem
	public final <ITEM extends ShovelItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> shovelItem(String registryName, PARENT parent, Tier itemTier, float attackDamage, float attackSpeed, ShovelItemFactory<ITEM> shovelItemFactory)
	{
		return item(registryName, parent, properties -> shovelItemFactory.create(itemTier, attackDamage, attackSpeed, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, ShovelItem, PARENT> shovelItem(String registryName, PARENT parent, Tier itemTier, float attackDamage, float attackSpeed)
	{
		return shovelItem(registryName, parent, itemTier, attackDamage, attackSpeed, ShovelItemFactory.DEFAULT);
	}

	public final <ITEM extends ShovelItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> shovelItem(String registryName, Tier itemTier, float attackDamage, float attackSpeed, ShovelItemFactory<ITEM> shovelItemFactory)
	{
		return shovelItem(registryName, self, itemTier, attackDamage, attackSpeed, shovelItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, ShovelItem, REGISTRATOR> shovelItem(String registryName, Tier itemTier, float attackDamage, float attackSpeed)
	{
		return shovelItem(registryName, self, itemTier, attackDamage, attackSpeed, ShovelItemFactory.DEFAULT);
	}
	// endregion
	// endregion
	// endregion

	// region: HorseArmorItem
	public final <ITEM extends HorseArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> horseArmorItem(String registryName, PARENT parent, int protection, String textureName, HorseArmorItemFactory<ITEM> horseArmorItemFactory)
	{
		return item(registryName, parent, properties -> horseArmorItemFactory.create(protection, buildHorseArmorTexture(textureName), properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, HorseArmorItem, PARENT> horseArmorItem(String registryName, PARENT parent, int protection, String textureName)
	{
		return horseArmorItem(registryName, parent, protection, textureName, HorseArmorItemFactory.DEFAULT);
	}

	public final <ITEM extends HorseArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> horseArmorItem(String registryName, int protection, String textureName, HorseArmorItemFactory<ITEM> horseArmorItemFactory)
	{
		return horseArmorItem(registryName, self, protection, textureName, horseArmorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, HorseArmorItem, REGISTRATOR> horseArmorItem(String registryName, int protection, String textureName)
	{
		return horseArmorItem(registryName, self, protection, textureName, HorseArmorItemFactory.DEFAULT);
	}

	private ResourceLocation buildHorseArmorTexture(String textureName)
	{
		return id("textures/entity/horse/armor/horse_armor_" + textureName + ".png");
	}

	// region: DyeableHorseArmorItem
	public final <ITEM extends DyeableHorseArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> dyeableHorseArmorItem(String registryName, PARENT parent, int protection, String textureName, HorseArmorItemFactory.DyeableFactory<ITEM> horseArmorItemFactory)
	{
		return item(registryName, parent, properties -> horseArmorItemFactory.create(protection, buildHorseArmorTexture(textureName), properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, DyeableHorseArmorItem, PARENT> dyeableHorseArmorItem(String registryName, PARENT parent, int protection, String textureName)
	{
		return dyeableHorseArmorItem(registryName, parent, protection, textureName, HorseArmorItemFactory.DyeableFactory.DYEABLE_DEFAULT);
	}

	public final <ITEM extends DyeableHorseArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> dyeableHorseArmorItem(String registryName, int protection, String textureName, HorseArmorItemFactory.DyeableFactory<ITEM> horseArmorItemFactory)
	{
		return dyeableHorseArmorItem(registryName, self, protection, textureName, horseArmorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, DyeableHorseArmorItem, REGISTRATOR> dyeableHorseArmorItem(String registryName, int protection, String textureName)
	{
		return dyeableHorseArmorItem(registryName, self, protection, textureName, HorseArmorItemFactory.DyeableFactory.DYEABLE_DEFAULT);
	}
	// endregion
	// endregion

	// region: ArmorItem
	public final <ITEM extends ArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> armor(String registryName, PARENT parent, ArmorMaterial armorMaterial, EquipmentSlot slotType, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return item(registryName, parent, properties -> armorItemFactory.create(armorMaterial, slotType, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, ArmorItem, PARENT> armor(String registryName, PARENT parent, ArmorMaterial armorMaterial, EquipmentSlot slotType)
	{
		return armor(registryName, parent, armorMaterial, slotType, ArmorItemFactory.DEFAULT);
	}

	public final <ITEM extends ArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> armor(String registryName, ArmorMaterial armorMaterial, EquipmentSlot slotType, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return armor(registryName, self, armorMaterial, slotType, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, ArmorItem, REGISTRATOR> armor(String registryName, ArmorMaterial armorMaterial, EquipmentSlot slotType)
	{
		return armor(registryName, self, armorMaterial, slotType, ArmorItemFactory.DEFAULT);
	}

	// region: Helmet
	public final <ITEM extends ArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> helmetArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return armor(registryName, parent, armorMaterial, EquipmentSlot.HEAD, armorItemFactory);
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, ArmorItem, PARENT> helmetArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial)
	{
		return helmetArmorItem(registryName, parent, armorMaterial, ArmorItemFactory.DEFAULT);
	}

	public final <ITEM extends ArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> helmetArmorItem(String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return helmetArmorItem(registryName, self, armorMaterial, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, ArmorItem, REGISTRATOR> helmetArmorItem(String registryName, ArmorMaterial armorMaterial)
	{
		return helmetArmorItem(registryName, self, armorMaterial, ArmorItemFactory.DEFAULT);
	}
	// endregion

	// region: Chestplate
	public final <ITEM extends ArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> chestplateArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return armor(registryName, parent, armorMaterial, EquipmentSlot.CHEST, armorItemFactory);
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, ArmorItem, PARENT> chestplateArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial)
	{
		return chestplateArmorItem(registryName, parent, armorMaterial, ArmorItemFactory.DEFAULT);
	}

	public final <ITEM extends ArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> chestplateArmorItem(String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return chestplateArmorItem(registryName, self, armorMaterial, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, ArmorItem, REGISTRATOR> chestplateArmorItem(String registryName, ArmorMaterial armorMaterial)
	{
		return chestplateArmorItem(registryName, self, armorMaterial, ArmorItemFactory.DEFAULT);
	}
	// endregion

	// region: Leggings
	public final <ITEM extends ArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> leggingsArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return armor(registryName, parent, armorMaterial, EquipmentSlot.LEGS, armorItemFactory);
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, ArmorItem, PARENT> leggingsArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial)
	{
		return leggingsArmorItem(registryName, parent, armorMaterial, ArmorItemFactory.DEFAULT);
	}

	public final <ITEM extends ArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> leggingsArmorItem(String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return leggingsArmorItem(registryName, self, armorMaterial, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, ArmorItem, REGISTRATOR> leggingsArmorItem(String registryName, ArmorMaterial armorMaterial)
	{
		return leggingsArmorItem(registryName, self, armorMaterial, ArmorItemFactory.DEFAULT);
	}
	// endregion

	// region: Boots
	public final <ITEM extends ArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> bootsArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return armor(registryName, parent, armorMaterial, EquipmentSlot.FEET, armorItemFactory);
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, ArmorItem, PARENT> bootsArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial)
	{
		return bootsArmorItem(registryName, parent, armorMaterial, ArmorItemFactory.DEFAULT);
	}

	public final <ITEM extends ArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> bootsArmorItem(String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<ITEM> armorItemFactory)
	{
		return bootsArmorItem(registryName, self, armorMaterial, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, ArmorItem, REGISTRATOR> bootsArmorItem(String registryName, ArmorMaterial armorMaterial)
	{
		return bootsArmorItem(registryName, self, armorMaterial, ArmorItemFactory.DEFAULT);
	}
	// endregion

	// region: DyeableArmorItem
	public final <ITEM extends DyeableArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> dyeableArmor(String registryName, PARENT parent, ArmorMaterial armorMaterial, EquipmentSlot slotType, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return item(registryName, parent, properties -> armorItemFactory.create(armorMaterial, slotType, properties));
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, DyeableArmorItem, PARENT> dyeableArmor(String registryName, PARENT parent, ArmorMaterial armorMaterial, EquipmentSlot slotType)
	{
		return armor(registryName, parent, armorMaterial, slotType, ArmorItemFactory.DYEABLE_DEFAULT);
	}

	public final <ITEM extends DyeableArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> dyeableArmor(String registryName, ArmorMaterial armorMaterial, EquipmentSlot slotType, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return dyeableArmor(registryName, self, armorMaterial, slotType, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, DyeableArmorItem, REGISTRATOR> dyeableArmor(String registryName, ArmorMaterial armorMaterial, EquipmentSlot slotType)
	{
		return armor(registryName, self, armorMaterial, slotType, ArmorItemFactory.DYEABLE_DEFAULT);
	}

	// region: Helmet
	public final <ITEM extends DyeableArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> dyeableHelmetArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return dyeableArmor(registryName, parent, armorMaterial, EquipmentSlot.HEAD, armorItemFactory);
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, DyeableArmorItem, PARENT> dyeableHelmetArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial)
	{
		return dyeableHelmetArmorItem(registryName, parent, armorMaterial, ArmorItemFactory.DYEABLE_DEFAULT);
	}

	public final <ITEM extends DyeableArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> dyeableHelmetArmorItem(String registryName, ArmorMaterial armorMaterial, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return dyeableHelmetArmorItem(registryName, self, armorMaterial, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, DyeableArmorItem, REGISTRATOR> dyeableHelmetArmorItem(String registryName, ArmorMaterial armorMaterial)
	{
		return dyeableHelmetArmorItem(registryName, self, armorMaterial, ArmorItemFactory.DYEABLE_DEFAULT);
	}
	// endregion

	// region: Chestplate
	public final <ITEM extends DyeableArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> dyeableChestplateArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return dyeableArmor(registryName, parent, armorMaterial, EquipmentSlot.CHEST, armorItemFactory);
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, DyeableArmorItem, PARENT> dyeableChestplateArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial)
	{
		return dyeableChestplateArmorItem(registryName, parent, armorMaterial, ArmorItemFactory.DYEABLE_DEFAULT);
	}

	public final <ITEM extends DyeableArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> dyeableChestplateArmorItem(String registryName, ArmorMaterial armorMaterial, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return dyeableChestplateArmorItem(registryName, self, armorMaterial, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, DyeableArmorItem, REGISTRATOR> dyeableChestplateArmorItem(String registryName, ArmorMaterial armorMaterial)
	{
		return dyeableChestplateArmorItem(registryName, self, armorMaterial, ArmorItemFactory.DYEABLE_DEFAULT);
	}
	// endregion

	// region: Leggings
	public final <ITEM extends DyeableArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> dyeableLeggingsArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return dyeableArmor(registryName, parent, armorMaterial, EquipmentSlot.LEGS, armorItemFactory);
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, DyeableArmorItem, PARENT> dyeableLeggingsArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial)
	{
		return dyeableLeggingsArmorItem(registryName, parent, armorMaterial, ArmorItemFactory.DYEABLE_DEFAULT);
	}

	public final <ITEM extends DyeableArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> dyeableLeggingsArmorItem(String registryName, ArmorMaterial armorMaterial, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return dyeableLeggingsArmorItem(registryName, self, armorMaterial, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, DyeableArmorItem, REGISTRATOR> dyeableLeggingsArmorItem(String registryName, ArmorMaterial armorMaterial)
	{
		return dyeableLeggingsArmorItem(registryName, self, armorMaterial, ArmorItemFactory.DYEABLE_DEFAULT);
	}
	// endregion

	// region: Boots
	public final <ITEM extends DyeableArmorItem, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> dyeableBootsArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return dyeableArmor(registryName, parent, armorMaterial, EquipmentSlot.FEET, armorItemFactory);
	}

	public final <PARENT> ItemBuilder<REGISTRATOR, DyeableArmorItem, PARENT> dyeableBootsArmorItem(String registryName, PARENT parent, ArmorMaterial armorMaterial)
	{
		return dyeableBootsArmorItem(registryName, parent, armorMaterial, ArmorItemFactory.DYEABLE_DEFAULT);
	}

	public final <ITEM extends DyeableArmorItem> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> dyeableBootsArmorItem(String registryName, ArmorMaterial armorMaterial, ArmorItemFactory.DyeableFactory<ITEM> armorItemFactory)
	{
		return dyeableBootsArmorItem(registryName, self, armorMaterial, armorItemFactory);
	}

	public final ItemBuilder<REGISTRATOR, DyeableArmorItem, REGISTRATOR> dyeableBootsArmorItem(String registryName, ArmorMaterial armorMaterial)
	{
		return dyeableBootsArmorItem(registryName, self, armorMaterial, ArmorItemFactory.DYEABLE_DEFAULT);
	}
	// endregion
	// endregion
	// endregion

	// region: SpawnEggItem
	public final <ENTITY extends Entity, ITEM extends ForgeSpawnEggItem<ENTITY>, PARENT> ItemBuilder<REGISTRATOR, ITEM, PARENT> spawnEggItem(String registryName, PARENT parent, NonnullSupplier<EntityType<ENTITY>> entityType, int backgroundColor, int highlightColor, SpawnEggItemFactory<ENTITY, ITEM> spawnEggItemFactory)
	{
		return item(registryName, parent, properties -> spawnEggItemFactory.create(entityType, backgroundColor, highlightColor, properties))
				.model((ctx, provider) -> provider.withExistingParent(ctx.getName(), new ResourceLocation("item/template_spawn_egg")))
				.color(() -> () -> ForgeSpawnEggItem::getSpawnEggColor);
	}

	public final <ENTITY extends Entity, PARENT> ItemBuilder<REGISTRATOR, ForgeSpawnEggItem<ENTITY>, PARENT> spawnEggItem(String registryName, PARENT parent, NonnullSupplier<EntityType<ENTITY>> entityType, int backgroundColor, int highlightColor)
	{
		return spawnEggItem(registryName, parent, entityType, backgroundColor, highlightColor, SpawnEggItemFactory.forEntity());
	}

	public final <ENTITY extends Entity, ITEM extends ForgeSpawnEggItem<ENTITY>> ItemBuilder<REGISTRATOR, ITEM, REGISTRATOR> spawnEggItem(String registryName, NonnullSupplier<EntityType<ENTITY>> entityType, int backgroundColor, int highlightColor, SpawnEggItemFactory<ENTITY, ITEM> spawnEggItemFactory)
	{
		return spawnEggItem(registryName, self, entityType, backgroundColor, highlightColor, spawnEggItemFactory);
	}

	public final <ENTITY extends Entity> ItemBuilder<REGISTRATOR, ForgeSpawnEggItem<ENTITY>, REGISTRATOR> spawnEggItem(String registryName, NonnullSupplier<EntityType<ENTITY>> entityType, int backgroundColor, int highlightColor)
	{
		return spawnEggItem(registryName, self, entityType, backgroundColor, highlightColor, SpawnEggItemFactory.forEntity());
	}
	// endregion
	// endregion

	// region: BlockEntityType
	public final <BLOCK_ENTITY extends BlockEntity, PARENT> BlockEntityBuilder<REGISTRATOR, BLOCK_ENTITY, PARENT> blockEntity(String registryName, PARENT parent, BlockEntityFactory<BLOCK_ENTITY> blockEntityFactory)
	{
		return entry(registryName, callback -> new BlockEntityBuilder<>(self, parent, registryName, callback, blockEntityFactory));
	}

	public final <BLOCK_ENTITY extends BlockEntity> BlockEntityBuilder<REGISTRATOR, BLOCK_ENTITY, REGISTRATOR> blockEntity(String registryName, BlockEntityFactory<BLOCK_ENTITY> blockEntityFactory)
	{
		return blockEntity(registryName, self, blockEntityFactory);
	}
	// endregion

	// region: MenuType
	public final <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>, PARENT> MenuBuilder<REGISTRATOR, MENU, SCREEN, PARENT> container(String registryName, PARENT parent, MenuFactory<MENU> menuFactory, @Nullable NonnullSupplier<MenuFactory.ScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return entry(registryName, callback -> new MenuBuilder<>(self, parent, registryName, callback, menuFactory, screenFactory));
	}

	public final <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> MenuBuilder<REGISTRATOR, MENU, SCREEN, REGISTRATOR> container(String registryName, MenuFactory<MENU> menuFactory, @Nullable NonnullSupplier<MenuFactory.ScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return container(registryName, self, menuFactory, screenFactory);
	}

	public final <MENU extends AbstractContainerMenu, PARENT> MenuBuilder<REGISTRATOR, MENU, ?, PARENT> container(String registryName, PARENT parent, MenuFactory<MENU> menuFactory)
	{
		return container(registryName, parent, menuFactory, null);
	}

	public final <MENU extends AbstractContainerMenu> MenuBuilder<REGISTRATOR, MENU, ?, REGISTRATOR> container(String registryName, MenuFactory<MENU> menuFactory)
	{
		return container(registryName, self, menuFactory);
	}
	// endregion

	// region: Sound
	public final <PARENT> SoundBuilder<REGISTRATOR, PARENT> sound(String registryName, PARENT parent)
	{
		return entry(registryName, callback -> new SoundBuilder<>(self, parent, registryName, callback));
	}

	public final SoundBuilder<REGISTRATOR, REGISTRATOR> sound(String registryName)
	{
		return sound(registryName, self);
	}
	// endregion

	// region: Entity
	public final <ENTITY extends Entity, PARENT> EntityBuilder<REGISTRATOR, ENTITY, PARENT> entity(String registryName, PARENT parent, MobCategory mobCategory, EntityFactory<ENTITY> entityFactory)
	{
		return entry(registryName, callback -> new EntityBuilder<>(self, parent, registryName, callback, mobCategory, entityFactory));
	}

	public final <ENTITY extends Entity> EntityBuilder<REGISTRATOR, ENTITY, REGISTRATOR> entity(String registryName, MobCategory mobCategory, EntityFactory<ENTITY> entityFactory)
	{
		return entity(registryName, self, mobCategory, entityFactory);
	}
	// endregion

	// region: PaintingType
	public final <PARENT> PaintingBuilder<REGISTRATOR, PARENT> painting(String registryName, PARENT parent)
	{
		return entry(registryName, callback -> new PaintingBuilder<>(self, parent, registryName, callback));
	}

	public final PaintingBuilder<REGISTRATOR, REGISTRATOR> painting(String registryName)
	{
		return painting(registryName, self);
	}

	public final <PARENT> PaintingEntry painting(String registryName, PARENT parent, int width, int height)
	{
		return painting(registryName, parent).size(width, height).register();
	}

	public final PaintingEntry painting(String registryName, int width, int height)
	{
		return painting(registryName, self, width, height);
	}
	// endregion

	// region: PointOfInterestType
	public final <PARENT> PointOfInterestBuilder<REGISTRATOR, PARENT> pointOfInterest(String registryName, PARENT parent)
	{
		return entry(registryName, callback -> new PointOfInterestBuilder<>(self, parent, registryName, callback));
	}

	public final PointOfInterestBuilder<REGISTRATOR, REGISTRATOR> pointOfInterest(String registryName)
	{
		return pointOfInterest(registryName, self);
	}
	// endregion

	// region: VillagerProfession
	public final <PARENT> VillagerProfessionBuilder<REGISTRATOR, PARENT> villagerProfession(String registryName, PARENT parent)
	{
		return entry(registryName, callback -> new VillagerProfessionBuilder<>(self, parent, registryName, callback));
	}

	public final VillagerProfessionBuilder<REGISTRATOR, REGISTRATOR> villagerProfession(String registryName)
	{
		return villagerProfession(registryName, self);
	}
	// endregion

	// region: RecipeSerializer
	public final <RECIPE_TYPE extends RecipeSerializer<RECIPE>, RECIPE extends Recipe<INVENTORY>, INVENTORY extends Container, PARENT> RecipeSerializerEntry<RECIPE_TYPE, RECIPE> recipeSerializer(String registryName, PARENT parent, RecipeSerializerFactory<RECIPE_TYPE, RECIPE, INVENTORY> recipeSerializerFactory)
	{
		return entry(registryName, callback -> new RecipeSerializerBuilder<>(self, parent, registryName, callback, recipeSerializerFactory)).register();
	}

	public final <RECIPE_TYPE extends RecipeSerializer<RECIPE>, RECIPE extends Recipe<INVENTORY>, INVENTORY extends Container> RecipeSerializerEntry<RECIPE_TYPE, RECIPE> recipeSerializer(String registryName, RecipeSerializerFactory<RECIPE_TYPE, RECIPE, INVENTORY> recipeSerializerFactory)
	{
		return recipeSerializer(registryName, self, recipeSerializerFactory);
	}
	// endregion

	// region: Enchantment
	public final <ENCHANTMENT extends Enchantment, PARENT> EnchantmentBuilder<REGISTRATOR, ENCHANTMENT, PARENT> enchantment(String registryName, PARENT parent, EnchantmentCategory enchantmentCategory, EnchantmentFactory<ENCHANTMENT> enchantmentFactory)
	{
		return entry(registryName, callback -> new EnchantmentBuilder<>(self, parent, registryName, callback, enchantmentCategory, enchantmentFactory));
	}

	public final <ENCHANTMENT extends Enchantment> EnchantmentBuilder<REGISTRATOR, ENCHANTMENT, REGISTRATOR> enchantment(String registryName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<ENCHANTMENT> enchantmentFactory)
	{
		return enchantment(registryName, self, enchantmentCategory, enchantmentFactory);
	}

	public final <PARENT> EnchantmentBuilder<REGISTRATOR, Enchantment, PARENT> enchantment(String registryName, PARENT parent, EnchantmentCategory enchantmentCategory)
	{
		return enchantment(registryName, parent, enchantmentCategory, EnchantmentFactory.DEFAULT);
	}

	public final EnchantmentBuilder<REGISTRATOR, Enchantment, REGISTRATOR> enchantment(String registryName, EnchantmentCategory enchantmentCategory)
	{
		return enchantment(registryName, self, enchantmentCategory, EnchantmentFactory.DEFAULT);
	}
	// endregion

	// region: ParticleType
	public final <PARTICLE extends ParticleOptions, PARENT> ParticleBuilder<REGISTRATOR, PARTICLE, PARENT> particle(String registryName, PARENT parent, ParticleFactory<PARTICLE> particleFactory)
	{
		return entry(registryName, callback -> new ParticleBuilder<>(self, parent, registryName, callback, particleFactory));
	}

	public final <PARTICLE extends ParticleOptions> ParticleBuilder<REGISTRATOR, PARTICLE, REGISTRATOR> particle(String registryName, ParticleFactory<PARTICLE> particleFactory)
	{
		return particle(registryName, self, particleFactory);
	}
	// endregion

	// region: Registrate Wrappers
	public final <BASE extends IForgeRegistryEntry<BASE>, TYPE extends BASE> RegistryEntry<TYPE> get(String registryName, Class<? super BASE> registryType)
	{
		return backend.<BASE, TYPE>get(registryName, registryType);
	}

	@Beta
	public final <BASE extends IForgeRegistryEntry<BASE>, TYPE extends BASE> RegistryEntry<TYPE> getOptional(String registryName, Class<? super BASE> registryType)
	{
		return backend.<BASE, TYPE>getOptional(registryName, registryType);
	}

	public final <BASE extends IForgeRegistryEntry<BASE>> Collection<RegistryEntry<BASE>> getAll(Class<? super BASE> registryType)
	{
		return backend.<BASE>getAll(registryType);
	}

	public final <BASE extends IForgeRegistryEntry<BASE>> REGISTRATOR addRegisterCallback(Class<? super BASE> registryType, Runnable callback)
	{
		backend.<BASE>addRegisterCallback(registryType, callback);
		return self;
	}

	public final <BASE extends IForgeRegistryEntry<BASE>> boolean isRegistered(Class<? super BASE> registryType)
	{
		return backend.<BASE>isRegistered(registryType);
	}

	public final <PROVIDER extends RegistrateProvider> Optional<PROVIDER> getDataProvider(ProviderType<PROVIDER> providerType)
	{
		return backend.getDataProvider(providerType);
	}

	public final <PROVIDER extends RegistrateProvider> REGISTRATOR addDataGenerator(ProviderType<? extends PROVIDER> providerType, NonnullConsumer<? super PROVIDER> consumer)
	{
		backend.addDataGenerator(providerType, consumer::accept);
		return self;
	}

	public final TranslatableComponent addLang(String translationKey, String localizedValue)
	{
		return backend.addLang(translationKey, localizedValue);
	}

	public final TranslatableComponent addLang(String type, ResourceLocation id, String localizedValue)
	{
		return backend.addLang(type, id, localizedValue);
	}

	public final TranslatableComponent addLang(String type, ResourceLocation id, String suffix, String localizedValue)
	{
		return backend.addLang(type, id, suffix, localizedValue);
	}

	public final TranslatableComponent addRawLang(String translationKey, String localizedValue)
	{
		return backend.addLang(translationKey, localizedValue);
	}

	public final REGISTRATOR skipErrors(boolean skipErrors)
	{
		backend.skipErrors(skipErrors);
		return self;
	}

	public final REGISTRATOR skipErrors()
	{
		return skipErrors(true);
	}

	public final REGISTRATOR itemGroup(NonnullSupplier<? extends CreativeModeTab> itemGroup)
	{
		backend.itemGroup(itemGroup::get);
		return self;
	}

	public final REGISTRATOR itemGroup(NonnullSupplier<? extends CreativeModeTab> itemGroup, String localizedName)
	{
		backend.itemGroup(itemGroup::get, localizedName);
		return self;
	}

	public final REGISTRATOR transform(NonnullUnaryOperator<REGISTRATOR> transformer)
	{
		return transformer.apply(self);
	}

	public final <BASE extends IForgeRegistryEntry<BASE>, TYPE extends BASE, PARENT, BUILDER extends RegistratorBuilder<REGISTRATOR, BASE, TYPE, PARENT, BUILDER, ENTRY>, ENTRY extends xyz.apex.forge.utility.registrator.entry.RegistryEntry<TYPE>> BUILDER transform(NonnullFunction<REGISTRATOR, BUILDER> transformer)
	{
		return transformer.apply(self);
	}

	public final <BASE extends IForgeRegistryEntry<BASE>, TYPE extends BASE, PARENT, BACKEND_BUILDER extends Builder<BASE, TYPE, PARENT, BACKEND_BUILDER>>  BACKEND_BUILDER entry(String registryName, NonnullFunction<BuilderCallback, BACKEND_BUILDER> backendBuilderFactory)
	{
		return backend.entry(registryName, backendBuilderFactory::apply);
	}

	@Beta
	public final <BASE extends IForgeRegistryEntry<BASE>>  Supplier<IForgeRegistry<BASE>> makeRegistry(String registryName, Class<? super BASE> registryType, NonnullSupplier<RegistryBuilder<BASE>> registryBuilder)
	{
		return backend.makeRegistry(registryName, registryType, registryBuilder);
	}
	// endregion

	// region: Translation
	public final TranslatableComponent addLang(String languageKey, String translationKey, String localizedValue)
	{
		var prefixedKey = getModId() + '.' + translationKey;
		addDataGenerator(LANG_EXT_PROVIDER, provider -> provider.add(languageKey, prefixedKey, localizedValue));
		return new TranslatableComponent(prefixedKey);
	}

	public final TranslatableComponent addLang(String languageKey, String type, ResourceLocation id, String localizedValue)
	{
		return addRawLang(languageKey, Util.makeDescriptionId(type, id), localizedValue);
	}

	public final TranslatableComponent addLang(String languageKey, String type, ResourceLocation id, String suffix, String localizedValue)
	{
		return addRawLang(languageKey, Util.makeDescriptionId(type, id) + '.' + suffix, localizedValue);
	}

	public final TranslatableComponent addRawLang(String languageKey, String translationKey, String localizedValue)
	{
		if(backend.isDataGenerationEnabled())
			backend.extraLang.get().add(Triple.of(languageKey, translationKey, localizedValue));

		return new TranslatableComponent(translationKey);
	}
	// endregion

	public final REGISTRATOR addSoundGenerator(NonnullConsumer<RegistrateSoundProvider> consumer)
	{
		return addDataGenerator(SOUNDS_PROVIDER, consumer);
	}

	public static <REGISTRATOR extends AbstractRegistrator<REGISTRATOR>> Lazy<REGISTRATOR> create(NonnullSupplier<REGISTRATOR> registratorFactory)
	{
		return Lazy.of(registratorFactory, true);
	}

	public static <REGISTRATOR extends AbstractRegistrator<REGISTRATOR>> Lazy<REGISTRATOR> create(String modId, NonnullFunction<String, REGISTRATOR> registratorFactory)
	{
		return create(() -> registratorFactory.apply(modId));
	}

	public static Lazy<Registrator> createBasic(String modId)
	{
		return Registrator.create(modId);
	}

	public final class Backend extends AbstractRegistrate<Backend>
	{
		private final Lazy<List<Triple<String, String, String>>> extraLang = Lazy.of(this::getExtraLang, true);
		private final Lazy<Boolean> doDataGen = Lazy.of(this::shouldDoDataGeneration, true);

		private Backend(String modId)
		{
			super(modId);

			registerEventListeners(modBus);
		}

		@Override
		protected void onData(GatherDataEvent event)
		{
			super.onData(event);

			DataGenerator generator = event.getGenerator();

			if(event.includeReports())
				generator.addProvider(new BlockListReporter(AbstractRegistrator.this, generator));
		}

		public boolean isDataGenerationEnabled()
		{
			return doDataGen.get();
		}

		private List<Triple<String, String, String>> getExtraLang()
		{
			var translations = Lists.<Triple<String, String, String>>newArrayList();
			addDataGenerator(LANG_EXT_PROVIDER, provider -> translations.forEach(t -> provider.add(t.getLeft(), t.getMiddle(), t.getRight())));
			return translations;
		}

		private boolean shouldDoDataGeneration()
		{
			try
			{
				Boolean flag = ObfuscationReflectionHelper.getPrivateValue(AbstractRegistrate.class, backend, "doDatagen");
				return flag != null && flag;
			}
			catch(Exception e)
			{
				return false;
			}
		}
	}
}
