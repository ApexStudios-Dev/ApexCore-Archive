package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.BedBlock;
import xyz.apex.minecraft.apexcore.common.component.block.types.BedBlockComponent;

final class FabricEvents extends FabricPlatformHolder
{
    FabricEvents(FabricPlatform platform)
    {
        super(platform);

        registerBedEvents();
    }

    private void registerBedEvents()
    {
        // required for our bed component to actually function
        // without these, entities will be
        // - immediately kicked out of our bed when they try sleep in them
        // - face the wrong way while sleeping
        // - and potentially wake up at invalid positions
        EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, blockState, vanillaResult) -> BedBlockComponent.isComponableBed(blockState) ? InteractionResult.sidedSuccess(entity.level.isClientSide) : InteractionResult.PASS);

        EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register((entity, sleepingPos, sleepingDirection) -> {
            var blockState = entity.level.getBlockState(sleepingPos);
            if(!BedBlockComponent.isComponableBed(blockState)) return sleepingDirection;
            return blockState.getValue(BedBlockComponent.FACING);
        });

        EntitySleepEvents.SET_BED_OCCUPATION_STATE.register((entity, sleepingPos, bedState, occupied) -> {
            if(!BedBlockComponent.isComponableBed(bedState)) return false;
            return BedBlockComponent.setOccupied(entity.level, sleepingPos, bedState, occupied);
        });

        EntitySleepEvents.MODIFY_WAKE_UP_POSITION.register((entity, sleepingPos, bedState, wakeUpPos) -> {
            if(!BedBlockComponent.isComponableBed(bedState)) return wakeUpPos;
            return BedBlock.findStandUpPosition(entity.getType(), entity.level, sleepingPos, bedState.getValue(BedBlockComponent.FACING), entity.getYRot()).orElse(wakeUpPos);
        });
    }
}
