package xyz.apex.minecraft.apexcore.shared.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.registry.FlammabilityRegistry;

import java.util.function.IntConsumer;

@Mixin(FireBlock.class)
public abstract class MixinFireBlock
{
    @Inject(
            method = "getBurnOdds",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void ApexCore$getBurnOdds(BlockState blockState, CallbackInfoReturnable<Integer> cir)
    {
        overrideFlammabilityOdds(blockState, true, cir::setReturnValue);
    }

    @Inject(
            method = "getIgniteOdds(Lnet/minecraft/world/level/block/state/BlockState;)I",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void ApexCore$getIgniteOdds(BlockState blockState, CallbackInfoReturnable<Integer> cir)
    {
        overrideFlammabilityOdds(blockState, false, cir::setReturnValue);
    }

    @Unique
    private void overrideFlammabilityOdds(BlockState blockState, boolean isBurn, IntConsumer oddsConsumer)
    {
        // obtain registry name for this block
        var registryName = BuiltInRegistries.BLOCK.getKey(blockState.getBlock());
        // look up the custom registered flammability odds, if they exist
        // redirect the returned value to use the custom registered ones, using the consumer
        (isBurn ? FlammabilityRegistry.lookupBurnOdds(registryName) : FlammabilityRegistry.lookupIgniteOdds(registryName)).ifPresent(oddsConsumer);
    }
}
