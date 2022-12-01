package xyz.apex.minecraft.apexcore.shared.hooks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;

import xyz.apex.minecraft.apexcore.shared.hooks.acessors.ExplosionHook;

public interface ExplosionHooks
{
    static float getSize(Explosion explosion)
    {
        return Hooks.get(explosion, ExplosionHook.class, ExplosionHook::ApexCore$getSize);
    }

    static BlockPos getPos(Explosion explosion)
    {
        return Hooks.get(explosion, ExplosionHook.class, ExplosionHook::ApexCore$getPos);
    }
}
