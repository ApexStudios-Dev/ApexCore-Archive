package xyz.apex.minecraft.apexcore.common.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.BuilderManager;

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

    private void enableTestElements()
    {
        var mgr = RegistrarManager.get(ID);

        var builders = BuilderManager.create(mgr);
        builders.item("test_item").register();
        builders.block("test_block").defaultItem().end().register();
        builders.block("test_block_no_item").register();
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
        builders.entityType("test_entity", (entityType, level) -> new Entity(entityType, level)
        {
            @Override
            protected void defineSynchedData()
            {
            }

            @Override
            protected void readAdditionalSaveData(CompoundTag compound)
            {
            }

            @Override
            protected void addAdditionalSaveData(CompoundTag compound)
            {
            }
        }).register();
    }
}
