package xyz.apex.minecraft.apexcore.common.mixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.util.ExtendedBlockEntityTicker;

@Mixin(LevelChunk.class)
public abstract class MixinLevelChunk
{
    @Unique
    private final ThreadLocal<BlockEntity> blockEntity = new ThreadLocal<>();

    @Inject(
            method = "updateBlockEntityTicker",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;getBlockState()Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private <T extends BlockEntity> void storeBlockEntity(T blockEntity, CallbackInfo ci)
    {
        this.blockEntity.set(blockEntity);
    }

    @Inject(
            method = "updateBlockEntityTicker",
            at = @At(value = "RETURN")
    )
    private <T extends BlockEntity> void clearBlockEntity(T blockEntity, CallbackInfo ci)
    {
        this.blockEntity.remove();
    }

    @Nullable
    @Redirect(
            method = "updateBlockEntityTicker",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getTicker(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/entity/BlockEntityType;)Lnet/minecraft/world/level/block/entity/BlockEntityTicker;"
            )
    )
    private <T extends BlockEntity> BlockEntityTicker<?> ApexCore$updateBlockEntityTicker(BlockState blockState, Level level, BlockEntityType<T> blockEntityType)
    {
        var blockEntity = this.blockEntity.get();
        if(blockEntity == null || blockEntityType != blockEntity.getType()) return blockState.getTicker(level, blockEntityType);
        if(!(blockState.getBlock() instanceof ExtendedBlockEntityTicker extended)) return blockState.getTicker(level, blockEntityType);
        return extended.createBlockEntityTicker(level, (T) blockEntity);
    }
}
