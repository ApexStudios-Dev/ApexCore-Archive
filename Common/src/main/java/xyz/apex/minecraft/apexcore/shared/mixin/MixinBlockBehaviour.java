package xyz.apex.minecraft.apexcore.shared.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import xyz.apex.minecraft.apexcore.shared.registry.HitBoxRegistry;

@Mixin(BlockBehaviour.class)
public abstract class MixinBlockBehaviour
{
    @Inject(
            method = "getShape",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ApexCore$getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext ctx, CallbackInfoReturnable<VoxelShape> cir)
    {
        HitBoxRegistry.findForBlock(blockState.getBlock()).ifPresent(entry -> {
            var shape = entry.getShape(blockState);
            cir.setReturnValue(shape);
            cir.cancel();
        });
    }
}
