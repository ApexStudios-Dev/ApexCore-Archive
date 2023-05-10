package xyz.apex.minecraft.apexcore.common.core;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.component.ComponentType;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.component.block.EntityBlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.hook.CreativeModeTabHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.GameRuleHooks;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.BuilderManager;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();

    default void bootstrap()
    {
        Services.bootstrap();
        ComponentType.bootstrap();
        // enableTestElements();
        RegistrarManager.register(ID);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void enableTestElements()
    {
        var mgr = RegistrarManager.get(ID);
        var items = mgr.get(Registries.ITEM);

        var builders = BuilderManager.create(mgr);
        var testItem = builders.item("test_item").register();
        builders.block("test_block").defaultItem().end().register();
        builders.block("test_block_no_item").register();
        builders.block("block_with_cutout").renderType(() -> RenderType::cutout).register();
        builders.block("block_and_item_with_differing_names").defaultItem().registrationModifier(str -> "item_and_block_with_differing_names").end().register();
        builders.block("block_with_entity", properties -> new BaseEntityBlock(properties)
        {
            @Nullable
            @Override
            public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
            {
                return mgr.get(Registries.BLOCK_ENTITY_TYPE).get("block_with_entity").orElseThrow().create(pos, state);
            }
        }).simpleBlockEntity((blockEntityType, pos, blockState) -> new BlockEntity(blockEntityType, pos, blockState)
        {
        }).simpleItem().register();
        var testEntity = builders.entity("test_entity", (entityType, level) -> new LivingEntity((EntityType) entityType, level)
        {
            @Override
            public Iterable<ItemStack> getArmorSlots()
            {
                return Collections.emptyList();
            }

            @Override
            public ItemStack getItemBySlot(EquipmentSlot slot)
            {
                return ItemStack.EMPTY;
            }

            @Override
            public void setItemSlot(EquipmentSlot slot, ItemStack stack)
            {
            }

            @Override
            public HumanoidArm getMainArm()
            {
                return HumanoidArm.LEFT;
            }
        }).defaultSpawnEgg(0x0, 0x161616).renderer(() -> () -> NoopRenderer::new).attributes(LivingEntity::createLivingAttributes).register();

        builders.block("component_block", properties -> new BlockComponentHolder(properties)
        {
            @Override
            protected void registerComponents(Registrar registrar)
            {
                registrar.register(BlockComponentTypes.WATER_LOGGED);
            }
        }).simpleItem().register();

        var enchantment = builders.enchantment("shiny", EnchantmentCategory.ARMOR).armorSlots().rarity(Enchantment.Rarity.RARE).register();

        GameRuleHooks.get().registerBoolean(ID, "testing_game_rule", GameRules.Category.MISC, false);

        CreativeModeTabHooks.get().register(ID, "test", builder -> builder
                .icon(testItem::asStack)
                .displayItems((params, output) -> {
                    items.entries()
                            .filter(RegistryEntry::isPresent)
                            .map(RegistryEntry::get)
                            .filter(item -> item.isEnabled(params.enabledFeatures()))
                            .forEach(output::accept);

                    // add stack to tab for every level of this enchantment
                    enchantment.ifPresent(ench -> {
                        for(var i = ench.getMinLevel(); i <= ench.getMaxLevel(); i++)
                        {
                            output.accept(enchantment.asStack(i));
                        }
                    });
                })
        );

        CreativeModeTabHooks.get().modify(CreativeModeTabs.SPAWN_EGGS, b -> b.accept(() -> items.get(testEntity.getRegistryName().withSuffix("_spawn_egg")).get()));

        ModLoader.get().findMod(ID).orElseThrow(); // throw exception if we cant find apexcore mod

        // Kills server as we try to create instance of AbstractContainerScreen which is not present server side.
        /*var menu = new AtomicReference<MenuType<?>>();

        class NoopMenu extends AbstractContainerMenu
        {
            private final ContainerLevelAccess levelAccess;

            private NoopMenu(int windowId, ContainerLevelAccess levelAccess)
            {
                super(menu.get(), windowId);

                this.levelAccess = levelAccess;
            }

            @Override
            public ItemStack quickMoveStack(Player player, int index)
            {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean stillValid(Player player)
            {
                return true;
            }
        }

        var menuEntry = MenuEntry.<NoopMenu, AbstractContainerScreen<NoopMenu>>register(
                ID, "test_menu",
                (windowId, playerInventory, extraData) -> new NoopMenu(windowId, ContainerLevelAccess.create(playerInventory.player.level, extraData.readBlockPos())),
                (windowId, playerInventory) -> new NoopMenu(windowId, ContainerLevelAccess.NULL),
                () -> () -> ($, playerInventory, displayName) -> new AbstractContainerScreen<>($, playerInventory, displayName)
                {
                    @Override
                    protected void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY)
                    {
                        renderBackground(pose);
                        drawString(pose, font, "%d, %d".formatted(mouseX, mouseY), mouseX + 8, mouseY + 8, 0);

                        menu.levelAccess.execute((level, pos) -> {
                            var blockState = level.getBlockState(pos);
                            if(blockState.isAir()) return;
                            var seed = (int) blockState.getSeed(pos);
                            var item = blockState.getBlock().asItem().getDefaultInstance();
                            itemRenderer.renderAndDecorateItem(pose, item, mouseX, mouseY, seed, 0);
                        });
                    }
                }
        );
        menuEntry.addListener(menu::set);
        builders.block("menu_block", properties -> new Block(properties)
        {
            @Override
            public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
            {
                if(player instanceof ServerPlayer sp)
                {
                    menuEntry.open(sp, getName(), data -> data.writeBlockPos(pos.below()));
                    return InteractionResult.SUCCESS;
                }
                return super.use(state, level, pos, player, hand, hit);
            }

            @Override
            public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
            {
                return menuEntry.asProvider(getName(), data -> data.writeBlockPos(pos.below()));
            }
        }).simpleItem().register();*/

        class MultiBlockEntityTest extends BlockEntityComponentHolder
        {
            public MultiBlockEntityTest(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState)
            {
                super(blockEntityType, pos, blockState);
            }

            @Override
            protected void registerComponents(Registrar registrar)
            {
                registrar.register(BlockEntityComponentTypes.MULTI_BLOCK);
            }
        }

        var multiBlockEntity = new AtomicReference<BlockEntityType<MultiBlockEntityTest>>();

        builders.block("multi_block_test", properties -> new EntityBlockComponentHolder(multiBlockEntity::get, properties)
        {
            @Override
            protected void registerComponents(Registrar registrar)
            {
                registrar.register(BlockComponentTypes.MULTI_BLOCK, component -> component.withPattern(MultiBlockPatterns.MB_3x3x3));
            }
        }).simpleItem().blockEntity(MultiBlockEntityTest::new).addListener(multiBlockEntity::set).end().register();
    }
}
