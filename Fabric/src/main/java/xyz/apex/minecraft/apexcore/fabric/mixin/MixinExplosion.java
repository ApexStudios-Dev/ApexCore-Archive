package xyz.apex.minecraft.apexcore.fabric.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import xyz.apex.minecraft.apexcore.shared.event.events.ExplosionEvent;
import xyz.apex.minecraft.apexcore.shared.hooks.ExplosionHooks;

import java.util.List;
import java.util.Set;

@Mixin(Explosion.class)
public abstract class MixinExplosion
{
    @Shadow @Final private Level level;

    @Shadow @Final private double x;

    @Shadow @Final private double y;

    @Shadow @Final private double z;

    @Inject(
            method = "explode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V",
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void ApexCore$explode(
            CallbackInfo ci,
            Set<BlockPos> affectedBlocks, int i, float f, int k1, int l1, int i2, int i1, int j2, int j1, List<Entity> affectedEntities
    )
    {
        var explosion = (Explosion) (Object) this;
        var toBlow = explosion.getToBlow();
        var pos = ExplosionHooks.getPos(explosion);

        var event = new ExplosionEvent.Detonate(level, pos, explosion, affectedEntities, toBlow);
        ExplosionEvent.DETONATE.post(event);
    }
}
