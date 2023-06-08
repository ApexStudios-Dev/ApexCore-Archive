package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;

/**
 * Hooks for registering custom game rule types.
 */
@ApiStatus.NonExtendable
public interface GameRuleHooks
{
    /**
     * Returns new boolean value type.
     *
     * @param defaultValue   Default value for the game rule.
     * @param changeListener Listener invoked when ever the game rule is changed.
     * @return New boolean value type.
     */
    GameRules.Type<GameRules.BooleanValue> createBooleanValue(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener);

    /**
     * Returns new boolean value type.
     *
     * @param defaultValue Default value for the game rule.
     * @return New boolean value type.
     */
    GameRules.Type<GameRules.BooleanValue> createBooleanValue(boolean defaultValue);

    /**
     * Registers new boolean game rule type.
     * <p>
     * Constructs a new boolean value type with given default value and change listener.
     *
     * @param ownerId          Owner id to register game rule for.
     * @param registrationName Registration name of the game rule.
     * @param gameRuleCategory Category for the game rule.
     * @param defaultValue     Default value for the game rule.
     * @param changeListener   Listener invoked when ever the game rule is changed.
     * @return New registered boolean game rule type.
     */
    GameRules.Key<GameRules.BooleanValue> registerBoolean(String ownerId, String registrationName, GameRules.Category gameRuleCategory, boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener);

    /**
     * Registers new boolean game rule type.
     * <p>
     * Constructs a new boolean value type with given default value.
     *
     * @param ownerId          Owner id to register game rule for.
     * @param registrationName Registration name of the game rule.
     * @param gameRuleCategory Category for the game rule.
     * @param defaultValue     Default value for the game rule.
     * @return New registered boolean game rule type.
     */
    GameRules.Key<GameRules.BooleanValue> registerBoolean(String ownerId, String registrationName, GameRules.Category gameRuleCategory, boolean defaultValue);

    /**
     * Returns new integer value type.
     *
     * @param defaultValue   Default value for the game rule.
     * @param changeListener Listener invoked when ever the game rule is changed.
     * @return New integer value type.
     */
    GameRules.Type<GameRules.IntegerValue> createIntegerValue(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener);

    /**
     * Returns new integer value type.
     *
     * @param defaultValue Default value for the game rule.
     * @return New integer value type.
     */
    GameRules.Type<GameRules.IntegerValue> createIntegerValue(int defaultValue);

    /**
     * Registers new integer game rule type.
     * <p>
     * Constructs a new integer value type with given default value and change listener.
     *
     * @param ownerId          Owner id to register game rule for.
     * @param registrationName Registration name of the game rule.
     * @param gameRuleCategory Category for the game rule.
     * @param defaultValue     Default value for the game rule.
     * @param changeListener   Listener invoked when ever the game rule is changed.
     * @return New registered integer game rule type.
     */
    GameRules.Key<GameRules.IntegerValue> registerInteger(String ownerId, String registrationName, GameRules.Category gameRuleCategory, int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener);

    /**
     * Registers new integer game rule type.
     * <p>
     * Constructs a new integer value type with given default value and change listener.
     *
     * @param ownerId          Owner id to register game rule for.
     * @param registrationName Registration name of the game rule.
     * @param gameRuleCategory Category for the game rule.
     * @param defaultValue     Default value for the game rule.
     * @return New registered integer game rule type.
     */
    GameRules.Key<GameRules.IntegerValue> registerInteger(String ownerId, String registrationName, GameRules.Category gameRuleCategory, int defaultValue);

    /**
     * Registers new game rule type with given category and value type.
     *
     * @param ownerId          Owner id to register game rule for.
     * @param registrationName Registration name of the game rule.
     * @param gameRuleCategory Category for the game rule.
     * @param gameRuleType     Value type for the game rule.
     * @param <T>              Type of game rule.
     * @return New registered game rule type.
     */
    <T extends GameRules.Value<T>> GameRules.Key<T> register(String ownerId, String registrationName, GameRules.Category gameRuleCategory, GameRules.Type<T> gameRuleType);

    /**
     * @return Global instance.
     */
    static GameRuleHooks get()
    {
        return Hooks.get().gameRules();
    }
}
