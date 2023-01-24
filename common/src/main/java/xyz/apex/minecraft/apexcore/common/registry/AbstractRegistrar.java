package xyz.apex.minecraft.apexcore.common.registry;

import com.google.common.collect.*;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import xyz.apex.minecraft.apexcore.common.item.CustomHorseArmorItem;
import xyz.apex.minecraft.apexcore.common.multiblock.MultiBlock;
import xyz.apex.minecraft.apexcore.common.multiblock.MultiBlockFactory;
import xyz.apex.minecraft.apexcore.common.multiblock.MultiBlockType;
import xyz.apex.minecraft.apexcore.common.multiblock.SimpleMultiBlock;
import xyz.apex.minecraft.apexcore.common.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.common.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;
import xyz.apex.minecraft.apexcore.common.registry.builder.*;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;
import xyz.apex.minecraft.apexcore.common.registry.entry.RecipeEntry;
import xyz.apex.minecraft.apexcore.common.registry.entry.RegistryEntry;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@SuppressWarnings({ "UnusedReturnValue", "unchecked", "SuspiciousMethodCalls", "UnstableApiUsage", "rawtypes" })
public class AbstractRegistrar<S extends AbstractRegistrar<S>> implements PlatformHolder
{
    private final Table<ResourceKey<? extends Registry<?>>, String, Registration<?, ?>> registrations = HashBasedTable.create();
    private final Multimap<Pair<ResourceKey<? extends Registry<?>>, String>, Consumer<?>> registerCallbacks = HashMultimap.create();
    private final Multimap<ResourceKey<? extends Registry<?>>, Runnable> afterRegisterCallbacks = HashMultimap.create();
    private final Set<ResourceKey<? extends Registry<?>>> completedRegistrations = Sets.newHashSet();
    private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> deferredRegisters = Maps.newHashMap();
    private final String modId;
    @Nullable private ModPlatform mod;

    protected AbstractRegistrar(String modId)
    {
        this.modId = modId;
    }

    // region: Getters
    public final ModPlatform getMod()
    {
        return Objects.requireNonNull(mod);
    }

    @Override
    public final GamePlatform platform()
    {
        return mod == null ? GamePlatform.INSTANCE : mod;
    }

    public final String getModId()
    {
        return modId;
    }
    // endregion

    protected final S self()
    {
        return (S) this;
    }

    // region: Utility
    public final ResourceLocation registryName(String registrationName)
    {
        return new ResourceLocation(modId, registrationName);
    }

    public final S transform(UnaryOperator<S> transformer)
    {
        return transformer.apply(self());
    }

    public final <T, R extends T, O extends AbstractRegistrar<O>, P, B extends Builder<T, R, O, P, B>> B map(Function<S, B> mapper)
    {
        return mapper.apply(self());
    }
    // endregion

    // region: CreativeModeTab
    public final S creativeModeTab(String creativeModeTabName, Supplier<ItemStack> icon)
    {
        CreativeTabRegistry.create(registryName(creativeModeTabName), icon);
        return self();
    }

    public final S creativeModeTab(String creativeModeTabName, Consumer<CreativeModeTab.Builder> builder)
    {
        CreativeTabRegistry.create(registryName(creativeModeTabName), builder);
        return self();
    }
    // endregion

    // region: Builders
    // region: Simple
    public final <T, R extends T> RegistryEntry<R> simple(ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> entryFactory)
    {
        return simple(self(), registryType, registrationName, entryFactory);
    }

    public final <T, R extends T, P> RegistryEntry<R> simple(P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName, Supplier<R> entryFactory)
    {
        return new NoConfigBuilder<>(self(), parent, registryType, registrationName, entryFactory).register();
    }
    // endregion

    // region: Item
    // region: Generic
    public final <T extends Item, P> ItemBuilder<T, S, P> item(P parent, String itemName, ItemBuilder.Factory<T> factory)
    {
        return new ItemBuilder<>(self(), parent, itemName, factory);
    }

    public final <P> ItemBuilder<Item, S, P> item(P parent, String itemName)
    {
        return item(parent, itemName, Item::new);
    }

    public final <T extends Item> ItemBuilder<T, S, S> item(String itemName, ItemBuilder.Factory<T> factory)
    {
        return item(self(), itemName, factory);
    }

    public final ItemBuilder<Item, S, S> item(String itemName)
    {
        return item(self(), itemName, Item::new);
    }
    // endregion

    // region: Tiered
    // region: Sword
    public final <T extends SwordItem, P> ItemBuilder<T, S, P> swordItem(P parent, String itemName, Tier tier, int attackDamage, float attackSpeed, SwordItemFactory<T> factory)
    {
        return item(parent, itemName, factory.toItemFactory(tier, attackDamage, attackSpeed));
    }

    public final <P> ItemBuilder<SwordItem, S, P> swordItem(P parent, String itemName, Tier tier, int attackDamage, float attackSpeed)
    {
        return swordItem(parent, itemName, tier, attackDamage, attackSpeed, SwordItem::new);
    }

    public final <T extends SwordItem> ItemBuilder<T, S, S> swordItem(String itemName, Tier tier, int attackDamage, float attackSpeed, SwordItemFactory<T> factory)
    {
        return swordItem(self(), itemName, tier, attackDamage, attackSpeed, factory);
    }

    public final ItemBuilder<SwordItem, S, S> swordItem(String itemName, Tier tier, int attackDamage, float attackSpeed)
    {
        return swordItem(self(), itemName, tier, attackDamage, attackSpeed, SwordItem::new);
    }
    // endregion

    // region: Digger
    // region: Pickaxe
    public final <T extends PickaxeItem, P> ItemBuilder<T, S, P> pickaxeItem(P parent, String itemName, Tier tier, int attackDamage, float attackSpeed, PickaxeItemFactory<T> factory)
    {
        return item(parent, itemName, factory.toItemFactory(tier, attackDamage, attackSpeed));
    }

    public final <P> ItemBuilder<PickaxeItem, S, P> pickaxeItem(P parent, String itemName, Tier tier, int attackDamage, float attackSpeed)
    {
        return pickaxeItem(parent, itemName, tier, attackDamage, attackSpeed, PickaxeItem::new);
    }

    public final <T extends PickaxeItem> ItemBuilder<T, S, S> pickaxeItem(String itemName, Tier tier, int attackDamage, float attackSpeed, PickaxeItemFactory<T> factory)
    {
        return pickaxeItem(self(), itemName, tier, attackDamage, attackSpeed, factory);
    }

    public final ItemBuilder<PickaxeItem, S, S> pickaxeItem(String itemName, Tier tier, int attackDamage, float attackSpeed)
    {
        return pickaxeItem(self(), itemName, tier, attackDamage, attackSpeed, PickaxeItem::new);
    }
    // endregion

    // region: Axe
    public final <T extends AxeItem, P> ItemBuilder<T, S, P> axeItem(P parent, String itemName, Tier tier, float attackDamage, float attackSpeed, AxeItemFactory<T> factory)
    {
        return item(parent, itemName, factory.toItemFactory(tier, attackDamage, attackSpeed));
    }

    public final <P> ItemBuilder<AxeItem, S, P> axeItem(P parent, String itemName, Tier tier, float attackDamage, float attackSpeed)
    {
        return axeItem(parent, itemName, tier, attackDamage, attackSpeed, AxeItem::new);
    }

    public final <T extends AxeItem> ItemBuilder<T, S, S> axeItem(String itemName, Tier tier, float attackDamage, float attackSpeed, AxeItemFactory<T> factory)
    {
        return axeItem(self(), itemName, tier, attackDamage, attackSpeed, factory);
    }

    public final ItemBuilder<AxeItem, S, S> axeItem(String itemName, Tier tier, float attackDamage, float attackSpeed)
    {
        return axeItem(self(), itemName, tier, attackDamage, attackSpeed, AxeItem::new);
    }
    // endregion

    // region: Shovel
    public final <T extends ShovelItem, P> ItemBuilder<T, S, P> shovelItem(P parent, String itemName, Tier tier, int attackDamage, float attackSpeed, ShovelItemFactory<T> factory)
    {
        return item(parent, itemName, factory.toItemFactory(tier, attackDamage, attackSpeed));
    }

    public final <P> ItemBuilder<ShovelItem, S, P> shovelItem(P parent, String itemName, Tier tier, int attackDamage, float attackSpeed)
    {
        return shovelItem(parent, itemName, tier, attackDamage, attackSpeed, ShovelItem::new);
    }

    public final <T extends ShovelItem> ItemBuilder<T, S, S> shovelItem(String itemName, Tier tier, int attackDamage, float attackSpeed, ShovelItemFactory<T> factory)
    {
        return shovelItem(self(), itemName, tier, attackDamage, attackSpeed, factory);
    }

    public final ItemBuilder<ShovelItem, S, S> shovelItem(String itemName, Tier tier, int attackDamage, float attackSpeed)
    {
        return shovelItem(self(), itemName, tier, attackDamage, attackSpeed, ShovelItem::new);
    }
    // endregion

    // region: Hoe
    public final <T extends HoeItem, P> ItemBuilder<T, S, P> hoeItem(P parent, String itemName, Tier tier, int attackDamage, float attackSpeed, HoeItemFactory<T> factory)
    {
        return item(parent, itemName, factory.toItemFactory(tier, attackDamage, attackSpeed));
    }

    public final <P> ItemBuilder<HoeItem, S, P> hoeItem(P parent, String itemName, Tier tier, int attackDamage, float attackSpeed)
    {
        return hoeItem(parent, itemName, tier, attackDamage, attackSpeed, HoeItem::new);
    }

    public final <T extends HoeItem> ItemBuilder<T, S, S> hoeItem(String itemName, Tier tier, int attackDamage, float attackSpeed, HoeItemFactory<T> factory)
    {
        return hoeItem(self(), itemName, tier, attackDamage, attackSpeed, factory);
    }

    public final ItemBuilder<HoeItem, S, S> hoeItem(String itemName, Tier tier, int attackDamage, float attackSpeed)
    {
        return hoeItem(self(), itemName, tier, attackDamage, attackSpeed, HoeItem::new);
    }
    // endregion
    // endregion
    // endregion

    // region: Armor
    private <T extends ArmorItem, P> ItemBuilder<T, S, P> armorItem(P parent, String itemName, ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, ArmorItemFactory<T> factory)
    {
        return item(parent, itemName, factory.toItemFactory(armorMaterial, equipmentSlot));
    }

    // region: Helmet
    public final <T extends ArmorItem, P> ItemBuilder<T, S, P> helmetItem(P parent, String itemName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return armorItem(parent, itemName, armorMaterial, EquipmentSlot.HEAD, factory);
    }

    public final <P> ItemBuilder<ArmorItem, S, P> helmetItem(P parent, String itemName, ArmorMaterial armorMaterial)
    {
        return helmetItem(parent, itemName, armorMaterial, ArmorItem::new);
    }

    public final <T extends ArmorItem> ItemBuilder<T, S, S> helmetItem(String itemName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return helmetItem(self(), itemName, armorMaterial, factory);
    }

    public final ItemBuilder<ArmorItem, S, S> helmetItem(String itemName, ArmorMaterial armorMaterial)
    {
        return helmetItem(self(), itemName, armorMaterial, ArmorItem::new);
    }
    // endregion

    // region: Chestplate
    public final <T extends ArmorItem, P> ItemBuilder<T, S, P> chestplateItem(P parent, String itemName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return armorItem(parent, itemName, armorMaterial, EquipmentSlot.CHEST, factory);
    }

    public final <P> ItemBuilder<ArmorItem, S, P> chestplateItem(P parent, String itemName, ArmorMaterial armorMaterial)
    {
        return chestplateItem(parent, itemName, armorMaterial, ArmorItem::new);
    }

    public final <T extends ArmorItem> ItemBuilder<T, S, S> chestplateItem(String itemName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return chestplateItem(self(), itemName, armorMaterial, factory);
    }

    public final ItemBuilder<ArmorItem, S, S> chestplateItem(String itemName, ArmorMaterial armorMaterial)
    {
        return chestplateItem(self(), itemName, armorMaterial, ArmorItem::new);
    }
    // endregion

    // region: Leggings
    public final <T extends ArmorItem, P> ItemBuilder<T, S, P> leggingsItem(P parent, String itemName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return armorItem(parent, itemName, armorMaterial, EquipmentSlot.LEGS, factory);
    }

    public final <P> ItemBuilder<ArmorItem, S, P> leggingsItem(P parent, String itemName, ArmorMaterial armorMaterial)
    {
        return leggingsItem(parent, itemName, armorMaterial, ArmorItem::new);
    }

    public final <T extends ArmorItem> ItemBuilder<T, S, S> leggingsItem(String itemName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return leggingsItem(self(), itemName, armorMaterial, factory);
    }

    public final ItemBuilder<ArmorItem, S, S> leggingsItem(String itemName, ArmorMaterial armorMaterial)
    {
        return leggingsItem(self(), itemName, armorMaterial, ArmorItem::new);
    }
    // endregion

    // region: Boots
    public final <T extends ArmorItem, P> ItemBuilder<T, S, P> bootsItem(P parent, String itemName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return armorItem(parent, itemName, armorMaterial, EquipmentSlot.FEET, factory);
    }

    public final <P> ItemBuilder<ArmorItem, S, P> bootsItem(P parent, String itemName, ArmorMaterial armorMaterial)
    {
        return bootsItem(parent, itemName, armorMaterial, ArmorItem::new);
    }

    public final <T extends ArmorItem> ItemBuilder<T, S, S> bootsItem(String itemName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return bootsItem(self(), itemName, armorMaterial, factory);
    }

    public final ItemBuilder<ArmorItem, S, S> bootsItem(String itemName, ArmorMaterial armorMaterial)
    {
        return bootsItem(self(), itemName, armorMaterial, ArmorItem::new);
    }
    // endregion
    // endregion

    // region: HorseArmor
    // region: TexturePath
    public final <T extends CustomHorseArmorItem, P> ItemBuilder<T, S, P> horseArmorItem(P parent, String itemName, int protection, ResourceLocation texturePath, HorseArmorItemFactory<T> factory)
    {
        return item(parent, itemName, factory.toItemFactory(protection, texturePath));
    }

    public final <P> ItemBuilder<CustomHorseArmorItem, S, P> horseArmorItem(P parent, String itemName, int protection, ResourceLocation texturePath)
    {
        return horseArmorItem(parent, itemName, protection, texturePath, CustomHorseArmorItem::new);
    }

    public final <T extends CustomHorseArmorItem> ItemBuilder<T, S, S> horseArmorItem(String itemName, int protection, ResourceLocation texturePath, HorseArmorItemFactory<T> factory)
    {
        return horseArmorItem(self(), itemName, protection, texturePath, factory);
    }

    public final ItemBuilder<CustomHorseArmorItem, S, S> horseArmorItem(String itemName, int protection, ResourceLocation texturePath)
    {
        return horseArmorItem(self(), itemName, protection, texturePath, CustomHorseArmorItem::new);
    }
    // endregion

    // region: TextureName
    public final <T extends CustomHorseArmorItem, P> ItemBuilder<T, S, P> horseArmorItem(P parent, String itemName, int protection, String textureName, HorseArmorItemFactory<T> factory)
    {
        return horseArmorItem(parent, itemName, protection, CustomHorseArmorItem.constructTexturePath(modId, textureName), factory);
    }

    public final <P> ItemBuilder<CustomHorseArmorItem, S, P> horseArmorItem(P parent, String itemName, int protection, String textureName)
    {
        return horseArmorItem(parent, itemName, protection, textureName, CustomHorseArmorItem::new);
    }

    public final <T extends CustomHorseArmorItem> ItemBuilder<T, S, S> horseArmorItem(String itemName, int protection, String textureName, HorseArmorItemFactory<T> factory)
    {
        return horseArmorItem(self(), itemName, protection, textureName, factory);
    }

    public final ItemBuilder<CustomHorseArmorItem, S, S> horseArmorItem(String itemName, int protection, String textureName)
    {
        return horseArmorItem(self(), itemName, protection, textureName, CustomHorseArmorItem::new);
    }
    // endregion

    // region: Generic
    public final <T extends CustomHorseArmorItem, P> ItemBuilder<T, S, P> horseArmorItem(P parent, String itemName, int protection, HorseArmorItemFactory<T> factory)
    {
        var textureName = StringUtils.removeEnd(itemName, "_horse_armor");
        textureName = StringUtils.isBlank(textureName) ? itemName : textureName;

        return horseArmorItem(parent, itemName, protection, textureName, factory);
    }

    public final <P> ItemBuilder<CustomHorseArmorItem, S, P> horseArmorItem(P parent, String itemName, int protection)
    {
        return horseArmorItem(parent, itemName, protection, CustomHorseArmorItem::new);
    }

    public final <T extends CustomHorseArmorItem> ItemBuilder<T, S, S> horseArmorItem(String itemName, int protection, HorseArmorItemFactory<T> factory)
    {
        return horseArmorItem(self(), itemName, protection, factory);
    }

    public final ItemBuilder<CustomHorseArmorItem, S, S> horseArmorItem(String itemName, int protection)
    {
        return horseArmorItem(self(), itemName, protection, CustomHorseArmorItem::new);
    }
    // endregion
    // endregion
    // endregion

    // region: Block
    // region: Generic
    public final <T extends Block, P> BlockBuilder<T, S, P> block(P parent, String blockName, BlockBuilder.Factory<T> factory)
    {
        return new BlockBuilder<>(self(), parent, blockName, factory);
    }

    public final <P> BlockBuilder<Block, S, P> block(P parent, String blockName)
    {
        return block(parent, blockName, Block::new);
    }

    public final <T extends Block> BlockBuilder<T, S, S> block(String blockName, BlockBuilder.Factory<T> factory)
    {
        return block(self(), blockName, factory);
    }

    public final BlockBuilder<Block, S, S> block(String blockName)
    {
        return block(self(), blockName, Block::new);
    }
    // endregion

    // region: MultiBlock
    public final <T extends Block & MultiBlock, P> BlockBuilder<T, S, P> multiBlock(P parent, String blockName, MultiBlockType multiBlockType, MultiBlockFactory<T> factory)
    {
        return block(parent, blockName, factory.toBlockFactory(multiBlockType));
    }

    public final <P> BlockBuilder<SimpleMultiBlock, S, P> multiBlock(P parent, String blockName, MultiBlockType multiBlockType)
    {
        return multiBlock(parent, blockName, multiBlockType, SimpleMultiBlock::new);
    }

    public final <T extends Block & MultiBlock> BlockBuilder<T, S, S> multiBlock(String blockName, MultiBlockType multiBlockType, MultiBlockFactory<T> factory)
    {
        return multiBlock(self(), blockName, multiBlockType, factory);
    }

    public final BlockBuilder<SimpleMultiBlock, S, S> multiBlock(String blockName, MultiBlockType multiBlockType)
    {
        return multiBlock(self(), blockName, multiBlockType, SimpleMultiBlock::new);
    }
    // endregion
    // endregion

    // region: BlockEntity
    public final <T extends BlockEntity, P> BlockEntityBuilder<T, S, P> blockEntity(P parent, String blockEntityName, BlockEntityBuilder.Factory<T> factory)
    {
        return new BlockEntityBuilder<>(self(), parent, blockEntityName, factory);
    }

    public final <T extends BlockEntity> BlockEntityBuilder<T, S, S> blockEntity(String blockEntityName, BlockEntityBuilder.Factory<T> factory)
    {
        return blockEntity(self(), blockEntityName, factory);
    }
    // endregion

    // region: Menu
    public final <T extends AbstractContainerMenu, C extends Screen & MenuAccess<T>> MenuEntry<T> menu(String menuName, MenuBuilder.MenuFactory<T> menuFactory, Supplier<MenuBuilder.ScreenFactory<T, C>> screenFactorySupplier)
    {
        return new MenuBuilder<>(self(), self(), menuName, menuFactory, screenFactorySupplier).register();
    }
    // endregion

    // region: Entity
    public final <T extends Entity, P> EntityBuilder<T, S, P> entity(P parent, String entityName, EntityBuilder.Factory<T> factory)
    {
        return new EntityBuilder<>(self(), parent, entityName, factory);
    }

    public final <T extends Entity> EntityBuilder<T, S, S> entity(String entityName, EntityBuilder.Factory<T> factory)
    {
        return entity(self(), entityName, factory);
    }
    // endregion

    // region: RecipeSerializer & RecipeType
    public <R extends Recipe<?>> RecipeEntry<R> recipe(String recipeName, Supplier<RecipeSerializer<R>> factory)
    {
        return new RecipeSerializerBuilder<>(self(), self(), recipeName, factory).register();
    }
    // endregion
    // endregion

    // region: Registration Utilities
    // region: Delegate
    @ApiStatus.Internal
    public final <T, R extends T> RegistrySupplier<R> getDelegate(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        return Objects.requireNonNull(this.<T, R>getRegistrationOrThrow(registryType, registrationName).delegate);
    }

    @ApiStatus.Internal
    public final <T, R extends T> Optional<RegistrySupplier<R>> getOptionalDelegate(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);
        if(registration == null) return Optional.empty();
        return Optional.of(registration.delegate);
    }
    // endregion

    // region: Entry
    public final <T, R extends T> RegistryEntry<R> get(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        return this.<T, R>getRegistrationOrThrow(registryType, registrationName).registryEntry;
    }

    public final <T, R extends T> Optional<RegistryEntry<R>> getOptional(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);
        return registration == null ? Optional.empty() : Optional.of(registration.registryEntry);
    }

    public final <T> Collection<RegistryEntry<T>> getAll(ResourceKey<? extends Registry<T>> registryType)
    {
        return stream(registryType).toList();
    }

    public final <T> Stream<RegistryEntry<T>> stream(ResourceKey<? extends Registry<T>> registryType)
    {
        return registrations.row(registryType).values().stream().map(registration -> (RegistryEntry<T>) registration.registryEntry);
    }
    // endregion

    // region: Callback
    public final <T, R extends T> S addRegisterCallback(ResourceKey<? extends Registry<T>> registryType, String registrationName, Consumer<R> callback)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);

        if(registration == null) registerCallbacks.put(Pair.of(registryType, registrationName), callback);
        else registration.addCallback(callback);

        return self();
    }

    public final <T> S addRegisterCallback(ResourceKey<? extends Registry<T>> registryType, Runnable callback)
    {
        afterRegisterCallbacks.put(registryType, callback);
        return self();
    }
    // endregion

    public final boolean isRegistered(ResourceKey<? extends Registry<?>> registryType)
    {
        return completedRegistrations.contains(registryType);
    }

    // region: Internal
    @Nullable
    private <T, R extends T> Registration<T, R> getRegistrationUnchecked(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        return registrations.contains(registryType, registrationName) ? (Registration<T, R>) registrations.get(registryType, registrationName) : null;
    }

    private <T, R extends T> Registration<T, R> getRegistrationOrThrow(ResourceKey<? extends Registry<T>> registryType, String registrationName)
    {
        var registration = this.<T, R>getRegistrationUnchecked(registryType, registrationName);
        if(registration == null) throw new IllegalArgumentException("Unknown registration %s for type %s".formatted(registrationName, registryType));
        return registration;
    }
    // endregion
    // endregion

    // region: Internal
    @ApiStatus.Internal
    public final <T, R extends T, P, B extends Builder<T, R, S, P, B>> RegistryEntry<R> accept(B builder, Supplier<R> entryFactory, Function<RegistrySupplier<R>, RegistryEntry<R>> registryEntryFactory)
    {
        var registrationName = builder.getRegistrationName();
        var registryType = builder.getRegistryType();
        var register = getRegister(registryType);
        var registration = new Registration<>(register, builder.getRegistryName(), registryEntryFactory, entryFactory);
        registerCallbacks.removeAll(Pair.of(registryType, registrationName)).forEach(callback -> registration.addCallback((Consumer<R>) callback));
        registrations.put(registryType, registrationName, registration);
        return registration.registryEntry;
    }

    @ApiStatus.Internal
    public final <T> void register(ResourceKey<? extends Registry<T>> registryType)
    {
        if(completedRegistrations.contains(registryType)) return;
        registrations.row(registryType).forEach((key, value) -> value.register());
        getRegister(registryType).register();
    }

    @ApiStatus.Internal
    public final void register()
    {
        forEachRegistry(registry -> register(registry.key()));
    }

    @ApiStatus.Internal
    public final <T> void lateRegister(ResourceKey<? extends Registry<T>> registryType)
    {
        if(completedRegistrations.contains(registryType)) return;
        afterRegisterCallbacks.removeAll(registryType).forEach(Runnable::run);
        completedRegistrations.add(registryType);
    }

    @ApiStatus.Internal
    public final void lateRegister()
    {
        forEachRegistry(registry -> lateRegister(registry.key()));
    }

    @ApiStatus.Internal
    public final void setMod(ModPlatform mod)
    {
        if(this.mod != null) throw new IllegalStateException("AbstractRegistrar#setMod should only ever be called once! Do not manually call this method!");
        if(!modId.equals(mod.getModId())) throw new IllegalStateException("AbstractRegistrar#setMod should be called with ModPlatform of same ModID");
        this.mod = mod;
    }

    private <T> DeferredRegister<T> getRegister(ResourceKey<? extends Registry<T>> registryType)
    {
        return (DeferredRegister<T>) deferredRegisters.computeIfAbsent(registryType, type -> DeferredRegister.create(modId, (ResourceKey) type));
    }
    // endregion

    // mostly used for fabric, to call the #register & #lateRegister methods
    // since fabric does not have any register events to listen to
    // forge uses the register events to call these methods at correct time and in correct orders
    // order in this method might not be perfect 1:1 with forge registration order
    // but its close enough and registration order shouldn't matter as much when running on fabric
    public static void forEachRegistry(Consumer<Registry<?>> consumer)
    {
        // these must go first
        consumer.accept(BuiltInRegistries.FLUID);
        consumer.accept(BuiltInRegistries.BLOCK);
        consumer.accept(BuiltInRegistries.ITEM);

        // all other registries not specified above
        BuiltInRegistries.REGISTRY.forEach(registry -> {
            var registryType = registry.key();
            if(registryType.equals(Registries.FLUID)) return;
            if(registryType.equals(Registries.BLOCK)) return;
            if(registryType.equals(Registries.ITEM)) return;
            consumer.accept(registry);
        });
    }

    private static final class Registration<T, R extends T>
    {
        private final RegistrySupplier<R> delegate;
        private final RegistryEntry<R> registryEntry;
        private final List<Consumer<R>> callbacks = Lists.newArrayList();
        private boolean registered = false;

        private Registration(DeferredRegister<T> register, ResourceLocation registryName, Function<RegistrySupplier<R>, RegistryEntry<R>> registryEntryFactory, Supplier<R> entryFactory)
        {
            delegate = register.register(registryName, entryFactory);
            registryEntry = registryEntryFactory.apply(delegate);
        }

        private void register()
        {
            delegate.listen(value -> {
                if(registered) return;
                callbacks.forEach(callback -> callback.accept(value));
                callbacks.clear();
                registered = true;
            });
        }

        private void addCallback(Consumer<R> consumer)
        {
            if(registered) consumer.accept(registryEntry.get());
            else callbacks.add(consumer);
        }
    }

    // region: Factories
    @FunctionalInterface
    interface SwordItemFactory<T extends SwordItem>
    {
        T create(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties);

        default ItemBuilder.Factory<T> toItemFactory(Tier tier, int attackDamage, float attackSpeed)
        {
            return properties -> create(tier, attackDamage, attackSpeed, properties);
        }
    }

    @FunctionalInterface
    interface PickaxeItemFactory<T extends PickaxeItem>
    {
        T create(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties);

        default ItemBuilder.Factory<T> toItemFactory(Tier tier, int attackDamage, float attackSpeed)
        {
            return properties -> create(tier, attackDamage, attackSpeed, properties);
        }
    }

    @FunctionalInterface
    interface AxeItemFactory<T extends AxeItem>
    {
        T create(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties);

        default ItemBuilder.Factory<T> toItemFactory(Tier tier, float attackDamage, float attackSpeed)
        {
            return properties -> create(tier, attackDamage, attackSpeed, properties);
        }
    }

    @FunctionalInterface
    interface ShovelItemFactory<T extends ShovelItem>
    {
        T create(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties);

        default ItemBuilder.Factory<T> toItemFactory(Tier tier, float attackDamage, float attackSpeed)
        {
            return properties -> create(tier, attackDamage, attackSpeed, properties);
        }
    }

    @FunctionalInterface
    interface HoeItemFactory<T extends HoeItem>
    {
        T create(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties);

        default ItemBuilder.Factory<T> toItemFactory(Tier tier, int attackDamage, float attackSpeed)
        {
            return properties -> create(tier, attackDamage, attackSpeed, properties);
        }
    }

    @FunctionalInterface
    interface ArmorItemFactory<T extends ArmorItem>
    {
        T create(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Item.Properties properties);

        default ItemBuilder.Factory<T> toItemFactory(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot)
        {
            return properties -> create(armorMaterial, equipmentSlot, properties);
        }
    }

    @FunctionalInterface
    interface HorseArmorItemFactory<T extends CustomHorseArmorItem>
    {
        T create(int protection, ResourceLocation texturePath, Item.Properties properties);

        default ItemBuilder.Factory<T> toItemFactory(int protection, ResourceLocation texturePath)
        {
            return properties -> create(protection, texturePath, properties);
        }
    }
    // endregion
}
