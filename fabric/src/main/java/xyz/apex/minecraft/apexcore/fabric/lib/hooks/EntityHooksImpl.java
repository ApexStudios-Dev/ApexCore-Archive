package xyz.apex.minecraft.apexcore.fabric.lib.hooks;

import com.google.common.base.Suppliers;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.hook.EntityHooks;

import java.util.function.Supplier;

final class EntityHooksImpl implements EntityHooks
{
    private final Supplier<Boolean> isApiPresent = Suppliers.memoize(() -> {
        try
        {
            Class.forName("net.fabricmc.fabric.api.entity.FakePlayer");
            return true;
        }
        catch(Throwable t)
        {
            return false;
        }
    });

    @Override
    public boolean isFakePlayer(@Nullable Entity entity)
    {
        // TODO: Change to just check instance of in 1.20
        //  Fake-Player-API added during a 1.19.4 build, not all of 1.19.4 will have the API
        if(isApiPresent.get()) return entity instanceof FakePlayer;
        return entity instanceof ServerPlayer && entity.getClass() != ServerPlayer.class;
    }
}
