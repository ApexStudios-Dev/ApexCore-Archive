package xyz.apex.minecraft.apexcore.fabric.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;

import xyz.apex.minecraft.apexcore.shared.event.EventHelper;
import xyz.apex.minecraft.apexcore.shared.event.events.ExplosionEvents;

@Mixin(Level.class)
public abstract class LevelMixin
{
    @Inject(
            method = "explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;Z)Lnet/minecraft/world/level/Explosion;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Explosion;explode()V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void ApexCore$explode(
            @Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, Level.ExplosionInteraction explosionInteraction, boolean spawnParticles,
            CallbackInfoReturnable<Explosion> cir,
            Explosion.BlockInteraction blockInteraction, Explosion explosion
    )
    {
        EventHelper.processMixinEvent(ExplosionEvents.START, new ExplosionEvents.Start(
                (Level) (Object) this,
                new BlockPos(x, y, z),
                explosion
        ), cir, explosion);
    }
}
