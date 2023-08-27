package xyz.apex.minecraft.apexcore.common.lib.support;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;

@ApiStatus.NonExtendable
public interface SupportHelper
{
    static boolean has(Player player, SupportLevel level)
    {
        return has(player.getGameProfile(), level);
    }

    static boolean hasAny(Player player, SupportLevel level, SupportLevel... levels)
    {
        return hasAny(player.getGameProfile(), level, levels);
    }

    static Set<SupportLevel> all(Player player)
    {
        return all(player.getGameProfile());
    }

    static SupportLevel get(Player player)
    {
        return get(player.getGameProfile());
    }

    static boolean has(GameProfile profile, SupportLevel level)
    {
        return all(profile).stream().anyMatch(level::matches);
    }

    static boolean hasAny(GameProfile profile, SupportLevel level, SupportLevel... levels)
    {
        for(var playerLevel : all(profile))
        {
            if(level.matches(playerLevel))
                return true;

            for(var otherLevel : levels)
            {
                if(otherLevel.matches(playerLevel))
                    return true;
            }
        }

        return false;
    }

    static Set<SupportLevel> all(GameProfile profile)
    {
        return SupportManager.INSTANCE.get(profile);
    }

    static SupportLevel get(GameProfile profile)
    {
        var best = SupportLevel.NONE;

        for(var level : all(profile))
        {
            if(level.ordinal() < best.ordinal())
                best = level;
        }

        return best;
    }
}
