package xyz.apex.minecraft.apexcore.neoforge.lib.hook;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.GameRuleHooks;

import java.util.function.BiConsumer;

@ApiStatus.Internal
public final class GameRuleHooksImpl implements GameRuleHooks
{
    @Override
    public GameRules.Type<GameRules.BooleanValue> createBooleanValue(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
    {
        return GameRules.BooleanValue.create(defaultValue, changeListener);
    }

    @Override
    public GameRules.Type<GameRules.BooleanValue> createBooleanValue(boolean defaultValue)
    {
        return GameRules.BooleanValue.create(defaultValue);
    }

    @Override
    public GameRules.Key<GameRules.BooleanValue> registerBoolean(String ownerId, String registrationName, GameRules.Category gameRuleCategory, boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
    {
        return register(ownerId, registrationName, gameRuleCategory, createBooleanValue(defaultValue, changeListener));
    }

    @Override
    public GameRules.Key<GameRules.BooleanValue> registerBoolean(String ownerId, String registrationName, GameRules.Category gameRuleCategory, boolean defaultValue)
    {
        return register(ownerId, registrationName, gameRuleCategory, createBooleanValue(defaultValue));
    }

    @Override
    public GameRules.Type<GameRules.IntegerValue> createIntegerValue(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        return GameRules.IntegerValue.create(defaultValue, changeListener);
    }

    @Override
    public GameRules.Type<GameRules.IntegerValue> createIntegerValue(int defaultValue)
    {
        return GameRules.IntegerValue.create(defaultValue);
    }

    @Override
    public GameRules.Key<GameRules.IntegerValue> registerInteger(String ownerId, String registrationName, GameRules.Category gameRuleCategory, int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        return register(ownerId, registrationName, gameRuleCategory, createIntegerValue(defaultValue, changeListener));
    }

    @Override
    public GameRules.Key<GameRules.IntegerValue> registerInteger(String ownerId, String registrationName, GameRules.Category gameRuleCategory, int defaultValue)
    {
        return register(ownerId, registrationName, gameRuleCategory, createIntegerValue(defaultValue));
    }

    @Override
    public <T extends GameRules.Value<T>> GameRules.Key<T> register(String ownerId, String registrationName, GameRules.Category gameRuleCategory, GameRules.Type<T> gameRuleType)
    {
        return GameRules.register("%s:%s".formatted(ownerId, registrationName), gameRuleCategory, gameRuleType);
    }
}
