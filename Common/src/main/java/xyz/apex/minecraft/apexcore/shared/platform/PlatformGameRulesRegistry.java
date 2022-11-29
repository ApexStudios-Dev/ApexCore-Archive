package xyz.apex.minecraft.apexcore.shared.platform;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import java.util.function.BiConsumer;

@ApiStatus.NonExtendable
public interface PlatformGameRulesRegistry extends PlatformHolder
{
    <T extends GameRules.Value<T>> GameRules.Key<T> register(String gameRuleName, GameRules.Category category, GameRules.Type<T> type);

    default GameRules.Key<GameRules.BooleanValue> register(String ruleName, GameRules.Category category, boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
    {
        return register(ruleName, category, createBooleanType(defaultValue, changeListener));
    }

    default GameRules.Key<GameRules.BooleanValue> register(String ruleName, GameRules.Category category, boolean defaultValue)
    {
        return register(ruleName, category, defaultValue, (server, value) -> { });
    }

    default GameRules.Key<GameRules.IntegerValue> register(String ruleName, GameRules.Category category, int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        return register(ruleName, category, createIntegerType(defaultValue, changeListener));
    }

    default GameRules.Key<GameRules.IntegerValue> register(String ruleName, GameRules.Category category, int defaultValue)
    {
        return register(ruleName, category, defaultValue, (server, value) -> { });
    }

    GameRules.Type<GameRules.BooleanValue> createBooleanType(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener);

    default GameRules.Type<GameRules.BooleanValue> createBooleanType(boolean defaultValue)
    {
        return createBooleanType(defaultValue, (server, valuer) -> { });
    }

    GameRules.Type<GameRules.IntegerValue> createIntegerType(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener);

    default GameRules.Type<GameRules.IntegerValue> createIntegerType(int defaultValue)
    {
        return createIntegerType(defaultValue, (server, value) -> { });
    }
}
