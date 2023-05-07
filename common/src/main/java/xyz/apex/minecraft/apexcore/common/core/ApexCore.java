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
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.hook.CreativeModeTabHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.GameRuleHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.BuilderManager;

import java.util.Collections;

public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();

    default void bootstrap()
    {
        Services.bootstrap();
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
        }).defaultSpawnEgg(0x0, 0x161616).renderer(() -> NoopRenderer::new).attributes(LivingEntity::createLivingAttributes).register();
        GameRuleHooks.get().registerBoolean(ID, "testing_game_rule", GameRules.Category.MISC, false);

        CreativeModeTabHooks.get().register(ID, "test", builder -> builder
                .icon(testItem::asStack)
                .displayItems((params, output) -> items.entries()
                        .filter(RegistryEntry::isPresent)
                        .map(RegistryEntry::get)
                        .filter(item -> item.isEnabled(params.enabledFeatures()))
                        .forEach(output::accept)
                )
        );

        CreativeModeTabHooks.get().modify(CreativeModeTabs.SPAWN_EGGS, b -> b.accept(() -> items.get(testEntity.getRegistryName().withSuffix("_spawn_egg")).get()));
    }
}
