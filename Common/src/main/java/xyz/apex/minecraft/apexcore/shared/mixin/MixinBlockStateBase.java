package xyz.apex.minecraft.apexcore.shared.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import xyz.apex.minecraft.apexcore.shared.registry.HitBoxRegistry;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockStateBase
{
    @Inject(
            method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ApexCore$getShape(BlockGetter level, BlockPos pos, CollisionContext ctx, CallbackInfoReturnable<VoxelShape> cir)
    {
        var self = (BlockBehaviour.BlockStateBase) (Object) this;
        HitBoxRegistry.findForBlock(self.getBlock()).ifPresent(entry -> {
            var shape = entry.getShape(self.asState());
            cir.setReturnValue(shape);
        });
    }
}
