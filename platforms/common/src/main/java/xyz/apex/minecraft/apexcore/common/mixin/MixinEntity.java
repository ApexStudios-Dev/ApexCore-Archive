package xyz.apex.minecraft.apexcore.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;

@Mixin(Entity.class)
public abstract class MixinEntity
{
    @Shadow private Level level;

    @Inject(
            method = "move",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;updateEntityAfterFallOn(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void ApexCore$move(MoverType moverType, Vec3 pos, CallbackInfo ci, Vec3 collisionPos, double d, boolean bl, boolean bl2, BlockPos blockPos, BlockState blockState, Block block)
    {
        var self = (Entity) (Object) this;

        EntityEvents.AFTER_ENTITY_FALL_ON.post().handle(self, level, blockPos, blockState);
    }
}
