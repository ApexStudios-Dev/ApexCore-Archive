package xyz.apex.minecraft.apexcore.common.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BaseBlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BaseBlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.BlockEntityComponentTypes;
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

    private static final RegistryEntry<CreativeModeTab> TEST_TAB = BUILDERS.creativeModeTab("test").icon(() -> INVENTORY_BLOCK.asStack()).displayItems((params, output) -> {
        output.accept(DUMMY_BLOCK);
        output.accept(INVENTORY_BLOCK);
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
}
