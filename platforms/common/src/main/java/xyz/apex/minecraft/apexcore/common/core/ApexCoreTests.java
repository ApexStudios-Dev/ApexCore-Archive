package xyz.apex.minecraft.apexcore.common.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.BuilderManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.BlockEntityEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.BlockEntry;

@ApiStatus.Internal
final class ApexCoreTests
{
    private static final BuilderManager<BuilderManager.Impl> BUILDERS = BuilderManager.create(ApexCore.ID);

    private static final BlockEntry<DummyBlock> DUMMY_BLOCK = BUILDERS.block("dummy", DummyBlock::new).copyInitialPropertiesFrom(() -> Blocks.CHEST).noLootTable().simpleItem().register();
    private static final BlockEntityEntry<DummyBlockEntity> DUMMY_BLOCK_ENTITY = BUILDERS.<DummyBlockEntity>blockEntity("dummy", DummyBlockEntity::new).validBlock(DUMMY_BLOCK).register();

    private static final BlockEntry<InventoryBlock> INVENTORY_BLOCK = BUILDERS.block("inventory", InventoryBlock::new).copyInitialPropertiesFrom(() -> Blocks.CHEST).noLootTable().simpleItem().register();
    private static final BlockEntityEntry<InventoryBlockEntity> INVENTORY_BLOCK_ENTITY = BUILDERS.<InventoryBlockEntity>blockEntity("inventory", InventoryBlockEntity::new).validBlock(INVENTORY_BLOCK).register();

    private static final MultiBlockType MB_STAIR = MultiBlockType.builder()
            .with("XXX", "XXX", "XXX")
            .with("XXX", "XXX", "   ")
            .with("XXX", "   ", "   ")
    .build();

    private static final BlockEntry<MultiBlockCube> MULTI_BLOCK_CUBE = BUILDERS.block("multi_block/cube", MultiBlockCube::new).copyInitialPropertiesFrom(() -> Blocks.DIRT).noLootTable().simpleItem().register();
    private static final BlockEntry<MultiBlockStair> MULTI_BLOCK_STAIR = BUILDERS.block("multi_block/stair", MultiBlockStair::new).copyInitialPropertiesFrom(() -> Blocks.DIRT).noLootTable().simpleItem().register();

    private static final RegistryEntry<CreativeModeTab> TEST_TAB = BUILDERS.creativeModeTab("test").icon(() -> INVENTORY_BLOCK.asStack()).displayItems((params, output) -> {
        output.accept(DUMMY_BLOCK);
        output.accept(INVENTORY_BLOCK);
        output.accept(MULTI_BLOCK_CUBE);
        output.accept(MULTI_BLOCK_STAIR);
    }).register();

    static void register()
    {
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
        private MultiBlockStair(Properties properties)
        {
            super(properties);
        }

        @Override
        protected void registerComponents(BlockComponentRegistrar registrar)
        {
            registrar.register(MultiBlockComponent.COMPONENT_TYPE, component -> component.setMultiBlockType(MB_STAIR));
            registrar.register(BlockComponentTypes.HORIZONTAL_FACING);
        }
    }
}
