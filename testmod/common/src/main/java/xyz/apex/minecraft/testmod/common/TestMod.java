package xyz.apex.minecraft.testmod.common;

import joptsimple.internal.Strings;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import xyz.apex.lib.Services;
import xyz.apex.minecraft.apexcore.common.core.ApexTags;
import xyz.apex.minecraft.apexcore.common.lib.enchantment.SimpleEnchantment;
import xyz.apex.minecraft.apexcore.common.lib.event.types.ServerEvents;
import xyz.apex.minecraft.apexcore.common.lib.menu.SimpleContainerMenuScreen;
import xyz.apex.minecraft.apexcore.common.lib.registry.Registrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.*;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderTypes;
import xyz.apex.minecraft.apexcore.common.lib.resgen.state.MultiVariantBuilder;
import xyz.apex.minecraft.apexcore.common.lib.resgen.state.PropertyDispatch;
import xyz.apex.minecraft.apexcore.common.lib.resgen.state.Variant;
import xyz.apex.minecraft.testmod.common.block.TestBlockWithEntity;
import xyz.apex.minecraft.testmod.common.block.TestFluidLoggingBlock;
import xyz.apex.minecraft.testmod.common.block.TestHorizontalBlock;
import xyz.apex.minecraft.testmod.common.block.TestMultiBlock;
import xyz.apex.minecraft.testmod.common.block.entity.TestBlockEntity;
import xyz.apex.minecraft.testmod.common.block.entity.TestMultiBlockEntity;
import xyz.apex.minecraft.testmod.common.client.renderer.TestEntityRenderer;
import xyz.apex.minecraft.testmod.common.client.renderer.TestItemStackRenderer;
import xyz.apex.minecraft.testmod.common.entity.TestEntity;
import xyz.apex.minecraft.testmod.common.menu.TestMenu;
import xyz.apex.minecraft.testmod.common.menu.TestMultiBlockMenu;

import java.util.stream.Stream;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.commands.Commands.literal;
import static xyz.apex.minecraft.apexcore.common.lib.resgen.loot.EntityLootProvider.ENTITY_ON_FIRE;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface TestMod
{
    String ID = "apexcore_testmod";
    Logger LOGGER = LogManager.getLogger();
    TestMod INSTANCE = Services.singleton(TestMod.class);

    Registrar REGISTRAR = Registrar.create(ID);

    ItemEntry<Item> TEST_ITEM = REGISTRAR
            .object("test_item")
            .item()
            .defaultModel()
            .model((provider, lookup, entry) -> provider.entity(entry.value()))
            .renderer(() -> TestItemStackRenderer::new)
    .register();

    BlockEntry<Block> TEST_BLOCK = REGISTRAR
            .object("test_block")
            .block()
            .defaultBlockState((provider, lookup, entry) -> provider.cubeAll(entry.value(), "block/debug"))
            .defaultItem()
    .register();

    EnchantmentEntry<SimpleEnchantment> TEST_ENCHANTMENT = REGISTRAR
            .object("test_enchantment")
            .enchantment(EnchantmentCategory.DIGGER)
            .equipmentSlots(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)
    .register();

    EntityEntry<TestEntity> TEST_ENTITY = REGISTRAR
            .object("test_entity")
            .entity(MobCategory.CREATURE, TestEntity::new)
            .attributes(TestEntity::attributes)
            .renderer(() -> () -> TestEntityRenderer::new)
            .defaultSpawnEgg(0xF2D7D5, 0xFFC300)
            .sized(.9F, .9F)
            .clientTrackingRange(10)
            .tag(EntityTypeTags.DISMOUNTS_UNDERWATER)
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules)
            .lootTable((provider, entry) -> provider.add(entry.value(), LootTable
                    .lootTable()
                    .withPool(LootPool
                            .lootPool()
                            .setRolls(ConstantValue.exactly(1F))
                            .add(LootItem
                                    .lootTableItem(Items.PORKCHOP)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 3F)))
                                    .apply(SmeltItemFunction
                                            .smelted()
                                            .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))
                                    )
                                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0F, 1F)))
                            )
                    )
            ))
    .register();

    BlockEntry<TestBlockWithEntity> TEST_BLOCK_WITH_ENTITY = REGISTRAR
            .object("test_block_with_entity")
            .block(TestBlockWithEntity::new)
            .copyInitialPropertiesFrom(TEST_BLOCK::value)
            .defaultBlockState((provider, lookup, entry) -> provider.cubeAll(entry.value(), "block/debug2"))
            .defaultItem()
            .defaultBlockEntity(TestBlockEntity::new)
    .register();

    BlockEntityEntry<TestBlockEntity> TEST_BLOCK_ENTITY = BlockEntityEntry.cast(REGISTRAR.get(Registries.BLOCK_ENTITY_TYPE));
    MenuEntry<TestMenu> TEST_MENU = REGISTRAR.object("test_menu").<TestMenu, SimpleContainerMenuScreen<TestMenu>>menu(TestMenu::new, () -> () -> SimpleContainerMenuScreen::new);

    BlockEntry<TestHorizontalBlock> TEST_HORIZONTAL_BLOCK = REGISTRAR
            .object("test_horizontal_block")
            .block(TestHorizontalBlock::new)
            .copyInitialPropertiesFrom(TEST_BLOCK::value)
            .blockState((lookup, entry) -> MultiVariantBuilder
                    .builder(entry.value(), Variant
                            .variant()
                            .model(lookup.lookup(ProviderTypes.MODELS).orientableWithBottom(
                                    entry.value(),
                                    "block/diamond_ore",
                                    "block/stone",
                                    "block/dirt",
                                    "block/oak_planks"
                            ))
                    )
                    .with(PropertyDispatch
                            .property(BlockStateProperties.HORIZONTAL_FACING)
                            .select(Direction.EAST, Variant.variant().yRot(Variant.Rotation.R90))
                            .select(Direction.SOUTH, Variant.variant().yRot(Variant.Rotation.R180))
                            .select(Direction.WEST, Variant.variant().yRot(Variant.Rotation.R270))
                            .select(Direction.NORTH, Variant.variant())
                    )
            )
            .defaultItem()
    .register();

    BlockEntry<TestFluidLoggingBlock> TEST_FLUID_LOGGING_BLOCK = REGISTRAR
            .object("test_fluid_logging_block")
            .block(TestFluidLoggingBlock::new)
            .defaultBlockState((provider, lookup, entry) -> provider.withParent(entry.value(), "block/oak_stairs"))
            .defaultItem()
    .register();

    BlockEntry<TestMultiBlock> TEST_MULTI_BLOCK = REGISTRAR
            .object("test_multi_block")
            .block(TestMultiBlock::new)
            .defaultBlockState((provider, lookup, entry) -> provider.cubeAll(entry.value(), "block/debug2"))
            .tag(ApexTags.Blocks.PLACEMENT_VISUALIZER)
            .defaultItem()
            .defaultBlockEntity(TestMultiBlockEntity::new)
    .register();

    BlockEntityEntry<TestMultiBlockEntity> TEST_MULTI_BLOCK_ENTITY = BlockEntityEntry.cast(REGISTRAR.get(Registries.BLOCK_ENTITY_TYPE));
    MenuEntry<TestMultiBlockMenu> TEST_MULTI_BLOCK_MENU = REGISTRAR.<TestMultiBlockMenu, SimpleContainerMenuScreen<TestMultiBlockMenu>>menu(TestMultiBlockMenu::forNetwork, () -> () -> SimpleContainerMenuScreen::new);

    RegistryEntry<CreativeModeTab> CREATIVE_MODE_TAB = REGISTRAR
            .creativeModeTab("test")
            .lang("en_us", "ApexCore - Test Elements")
            .icon(TEST_BLOCK::asStack)
            .displayItems((parameters, output) -> {
                output.accept(TEST_ITEM);
                output.accept(TEST_BLOCK);
                output.accept(TEST_ENCHANTMENT.asStack(1));
                output.accept(TEST_ENTITY);
                output.accept(TEST_BLOCK_WITH_ENTITY);
                output.accept(TEST_HORIZONTAL_BLOCK);
                output.accept(TEST_FLUID_LOGGING_BLOCK);
                output.accept(TEST_MULTI_BLOCK);
            })
    .register();

    @MustBeInvokedByOverriders
    default void bootstrap()
    {
        var messages = new String[] {
                "ApexCore Test Elements Enabled!",
                "Errors, Bugs and Glitches may arise, you are on your own.",
                "No support will be provided while Test Elements are Enabled!"
        };

        var maxLen = Stream.of(messages).mapToInt(String::length).max().orElse(1);
        var header = Strings.repeat('*', maxLen + 4);
        LOGGER.warn(header);
        Stream.of(messages).map(str -> StringUtils.center(str, maxLen, ' ')).forEach(str -> LOGGER.warn("* {} *", str));
        LOGGER.warn(header);

        REGISTRAR.register();
        registerGenerators();

        ServerEvents.REGISTER_COMMANDS.addListener((dispatcher, environment, context) -> dispatcher.register(
                literal("hello")
                        .executes(ctx -> {
                            ctx.getSource().sendSystemMessage(Component.literal("Hello %s".formatted(ctx.getSource().getTextName())));
                            return SINGLE_SUCCESS;
                        })
        ));
    }

    private void registerGenerators()
    {
        var descriptionKey = "pack.%s.description".formatted(ID);

        ProviderTypes.LANGUAGES.addListener(ID, (provider, lookup) -> provider
                .enUS()
                    .add(descriptionKey, "ApexCore - TestMod")
                .end()
        );

        ProviderTypes.registerDefaultMcMetaGenerator(ID, Component.translatable(descriptionKey));
    }
}
