package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import java.util.function.BiConsumer;

public interface GameRuleHooks
{
    // region: Boolean
    static GameRules.Type<GameRules.BooleanValue> createBoolean(boolean defaultValue)
    {
        return createBoolean(defaultValue, GameRuleHooks::defaultOnChanged);
    }

    static GameRules.Type<GameRules.BooleanValue> createBoolean(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> onChanged)
    {
        return GameRules.BooleanValue.create(defaultValue, onChanged);
    }

    static GameRules.Key<GameRules.BooleanValue> registerBoolean(String ownerId, String registrationName, GameRules.Category category, boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> onChanged)
    {
        return register(ownerId, registrationName, category, createBoolean(defaultValue, onChanged));
    }

    static GameRules.Key<GameRules.BooleanValue> registerBoolean(String ownerId, String registrationName, GameRules.Category category, boolean defaultValue)
    {
        return registerBoolean(ownerId, registrationName, category, defaultValue, GameRuleHooks::defaultOnChanged);
    }
    // endregion

    // region: Integer
    static GameRules.Type<GameRules.IntegerValue> createInteger(int defaultValue)
    {
        return createInteger(defaultValue, GameRuleHooks::defaultOnChanged);
    }

    static GameRules.Type<GameRules.IntegerValue> createInteger(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> onChanged)
    {
        return GameRules.IntegerValue.create(defaultValue, onChanged);
    }

    static GameRules.Key<GameRules.IntegerValue> registerInteger(String ownerId, String registrationName, GameRules.Category category, int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> onChanged)
    {
        return register(ownerId, registrationName, category, createInteger(defaultValue, onChanged));
    }

    static GameRules.Key<GameRules.IntegerValue> registerInteger(String ownerId, String registrationName, GameRules.Category category, int defaultValue)
    {
        return registerInteger(ownerId, registrationName, category, defaultValue, GameRuleHooks::defaultOnChanged);
    }
    // endregion

    static <T extends GameRules.Value<T>> GameRules.Key<T> register(String ownerId, String registrationName, GameRules.Category category, GameRules.Type<T> type)
    {
        return GameRules.register("%s:%s".formatted(ownerId, registrationName), category, type);
    }

    private static <T extends GameRules.Value<T>> void defaultOnChanged(MinecraftServer server, T newValue) {}
}
