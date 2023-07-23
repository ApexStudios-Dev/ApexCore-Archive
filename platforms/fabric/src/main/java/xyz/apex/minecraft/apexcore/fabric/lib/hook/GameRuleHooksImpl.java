package xyz.apex.minecraft.apexcore.fabric.lib.hook;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
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
        return GameRuleFactory.createBooleanRule(defaultValue, changeListener);
    }

    @Override
    public GameRules.Type<GameRules.BooleanValue> createBooleanValue(boolean defaultValue)
    {
        return GameRuleFactory.createBooleanRule(defaultValue);
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
        return GameRuleFactory.createIntRule(defaultValue, changeListener);
    }

    @Override
    public GameRules.Type<GameRules.IntegerValue> createIntegerValue(int defaultValue)
    {
        return GameRuleFactory.createIntRule(defaultValue);
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
        return GameRuleRegistry.register("%s:%s".formatted(ownerId, registrationName), gameRuleCategory, gameRuleType);
    }
}
