package xyz.apex.minecraft.apexcore.common.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.apex.minecraft.apexcore.common.registry.PoiTypeRegistry;

@Mixin(PoiType.class)
public abstract class MixinPoiType
{
    @Inject(
            method = "is",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ApexCore$is(BlockState blockState, CallbackInfoReturnable<Boolean> cir)
    {
        var key = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getResourceKey((PoiType) (Object) this).orElse(null);
        if(key == null) return;
        var registry = PoiTypeRegistry.get(key);
        if(registry.isFor(blockState)) cir.setReturnValue(true);
    }
}
