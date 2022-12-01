package xyz.apex.minecraft.apexcore.shared.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;

import xyz.apex.minecraft.apexcore.shared.hooks.acessors.ExplosionHook;

@Mixin(Explosion.class)
public abstract class MixinExplosion implements ExplosionHook
{
    @Shadow @Final private float radius;
    @Shadow @Final private double x;
    @Shadow @Final private double y;
    @Shadow @Final private double z;

    @Override
    public float ApexCore$getSize()
    {
        return radius;
    }

    @Override
    public BlockPos ApexCore$getPos()
    {
        return new BlockPos(x, y, z);
    }
}
