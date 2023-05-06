package xyz.apex.minecraft.apexcore.forge.lib.hooks;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.hook.EntityHooks;

final class EntityHooksImpl implements EntityHooks
{
    @Override
    public boolean isFakePlayer(@Nullable Entity entity)
    {
        return entity instanceof FakePlayer;
    }
}
