package xyz.apex.minecraft.apexcore.shared.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.Explosion;

@Mixin(Explosion.class)
public interface ExplosionAccessor
{
    @Accessor("radius") float getSize();
}
