package xyz.apex.minecraft.apexcore.common.core;

import joptsimple.internal.Strings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BaseBlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BaseBlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.BlockEntityComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.component.block.types.BlockComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockType;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockTypes;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.BuilderManager;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@ApiStatus.Internal
final class ApexCoreTests
{
    private static final AtomicReference<BlockEntityType<DummyBlockEntity>> DUMMY_BLOCK_ENTITY = new AtomicReference<>();
    private static final AtomicReference<BlockEntityType<InventoryBlockEntity>> INVENTORY_BLOCK_ENTITY = new AtomicReference<>();

    private static final String PROPERTY = "%s.test_elements.enabled".formatted(ApexCore.ID);
    public static final boolean ENABLED = BooleanUtils.toBoolean(System.getProperty(PROPERTY, "false"));
    public static final Marker MARKER = MarkerManager.getMarker("Tests");

    static void register()
    {
        if(!ENABLED)
            return;

        var messages = new String[] {
                "ApexCore Test Elements Enabled!",
                "Errors, Bugs and Glitches may arise, you are on your own.",
                "No support will be provided while Test Elements are Enabled!"
        };

        var maxLen = Stream.of(messages).mapToInt(String::length).max().orElse(1);
        var header = Strings.repeat('*', maxLen + 4);
        ApexCore.LOGGER.warn(MARKER, header);
        Stream.of(messages).map(str -> StringUtils.center(str, maxLen, ' ')).forEach(str -> ApexCore.LOGGER.warn(MARKER, "* {} *", str));
        ApexCore.LOGGER.warn(MARKER, header);

        var builders = BuilderManager.create(ApexCore.ID);

        var dummyBlock = builders.block("dummy", DummyBlock::new)
                                 .copyInitialPropertiesFrom(() -> Blocks.CHEST)
                                 .noLootTable()
                                 .simpleItem()

                                 .<DummyBlockEntity>blockEntity(DummyBlockEntity::new)
                                    .addListener(DUMMY_BLOCK_ENTITY::set)
                                 .end()
         .register();

        var inventoryBlock = builders.block("inventory", InventoryBlock::new)
                                     .copyInitialPropertiesFrom(() -> Blocks.CHEST)
                                     .noLootTable()
                                     .simpleItem()

                                     .<InventoryBlockEntity>blockEntity(InventoryBlockEntity::new)
                                        .addListener(INVENTORY_BLOCK_ENTITY::set)
                                     .end()
         .register();

        var multiBlockCube = builders.block("multi_block/cube", MultiBlockCube::new)
                                     .copyInitialPropertiesFrom(() -> Blocks.DIRT)
                                     .noLootTable()
                                     .simpleItem()
        .register();

        var multiBlockStair = builders.block("multi_block/stair", MultiBlockStair::new)
                                      .copyInitialPropertiesFrom(() -> Blocks.DIRT)
                                      .noLootTable()
                                      .simpleItem()
        .register();

        builders.creativeModeTab("test")
                .icon(() -> inventoryBlock.asStack())
                .displayItems((params, output) -> {
                    output.accept(dummyBlock);
                    output.accept(inventoryBlock);
                    output.accept(multiBlockCube);
                    output.accept(multiBlockStair);
                })
        .register();
    }

    private static final class DummyBlock extends BaseBlockComponentHolder
    {
        private DummyBlock(Properties properties)
        {
            super(properties);
        }

        @Override
        protected BlockEntityType<?> getBlockEntityType()
        {
            return DUMMY_BLOCK_ENTITY.get();
        }
    }

    private static final class DummyBlockEntity extends BaseBlockEntityComponentHolder
    {
        private DummyBlockEntity(BlockEntityType<? extends DummyBlockEntity> blockEntityType, BlockPos pos, BlockState blockState)
        {
            super(blockEntityType, pos, blockState);
        }
    }

    private static final class InventoryBlock extends BaseBlockComponentHolder
    {
        private InventoryBlock(Properties properties)
        {
            super(properties);
        }

        @Override
        protected BlockEntityType<?> getBlockEntityType()
        {
            return INVENTORY_BLOCK_ENTITY.get();
        }
    }

    private static final class InventoryBlockEntity extends BaseBlockEntityComponentHolder
    {
        private InventoryBlockEntity(BlockEntityType<? extends InventoryBlockEntity> blockEntityType, BlockPos pos, BlockState blockState)
        {
            super(blockEntityType, pos, blockState);
        }

        @Override
        protected void registerComponents(BlockEntityComponentRegistrar registrar)
        {
            registrar.register(BlockEntityComponentTypes.NAMEABLE);
            registrar.register(BlockEntityComponentTypes.INVENTORY, component -> component.setSlotCount(9));
        }
    }

    private static final class MultiBlockCube extends BaseBlockComponentHolder
    {
        private MultiBlockCube(Properties properties)
        {
            super(properties);
        }

        @Override
        protected void registerComponents(BlockComponentRegistrar registrar)
        {
            registrar.register(MultiBlockComponent.COMPONENT_TYPE, component -> component.setMultiBlockType(MultiBlockTypes.MB_3x3x3));
        }
    }

    private static final class MultiBlockStair extends BaseBlockComponentHolder
    {
        private final MultiBlockType multiBlockType = MultiBlockType.builder()
                .with("XXX", "XXX", "XXX")
                .with("XXX", "XXX", "   ")
                .with("XXX", "   ", "   ")
        .build();

        private MultiBlockStair(Properties properties)
        {
            super(properties);
        }

        @Override
        protected void registerComponents(BlockComponentRegistrar registrar)
        {
            registrar.register(MultiBlockComponent.COMPONENT_TYPE, component -> component.setMultiBlockType(multiBlockType));
            registrar.register(BlockComponentTypes.HORIZONTAL_FACING);
        }
    }
}