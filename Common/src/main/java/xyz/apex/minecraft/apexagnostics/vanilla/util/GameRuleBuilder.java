package xyz.apex.minecraft.apexagnostics.vanilla.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import xyz.apex.minecraft.apexagnostics.vanilla.Platform;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class GameRuleBuilder<T, G extends GameRules.Value<G>>
{
    private static final Logger LOGGER = LogManager.getLogger();

    private final String modId;
    private final String gameRuleName;
    private final Supplier<T> defaultValue;
    private final BiFunction<T, BiConsumer<MinecraftServer, G>, GameRules.Type<G>> typeSupplier;
    private GameRules.Category category = GameRules.Category.MISC;
    private BiConsumer<MinecraftServer, G> changeListener = (server, value) -> { };

    private GameRuleBuilder(String modId, String gameRuleName, Supplier<T> defaultValue, BiFunction<T, BiConsumer<MinecraftServer, G>, GameRules.Type<G>> typeSupplier)
    {
        this.modId = modId;
        this.gameRuleName = gameRuleName;
        this.defaultValue = defaultValue;
        this.typeSupplier = typeSupplier;
    }

    public GameRuleBuilder<T, G> changeListener(BiConsumer<MinecraftServer, G> changeListener)
    {
        this.changeListener = this.changeListener.andThen(changeListener);
        return this;
    }

    public GameRuleBuilder<T, G> category(GameRules.Category category)
    {
        this.category = category;
        return this;
    }

    public GameRuleBuilder<T, G> player()
    {
        return category(GameRules.Category.PLAYER);
    }

    public GameRuleBuilder<T, G> mobs()
    {
        return category(GameRules.Category.MOBS);
    }

    public GameRuleBuilder<T, G> spawning()
    {
        return category(GameRules.Category.SPAWNING);
    }

    public GameRuleBuilder<T, G> drops()
    {
        return category(GameRules.Category.DROPS);
    }

    public GameRuleBuilder<T, G> updates()
    {
        return category(GameRules.Category.UPDATES);
    }

    public GameRuleBuilder<T, G> chat()
    {
        return category(GameRules.Category.CHAT);
    }

    public GameRuleBuilder<T, G> misc()
    {
        return category(GameRules.Category.MISC);
    }

    public GameRules.Key<G> build()
    {
        var gameRuleName = "%s:%s".formatted(modId, this.gameRuleName);
        var type = typeSupplier.apply(defaultValue.get(), changeListener);
        LOGGER.info("Registering GameRule '{}'..", gameRuleName);
        return Platform.INSTANCE.registerGameRule(gameRuleName, category, type);
    }

    public static GameRuleBuilder<Boolean, GameRules.BooleanValue> bool(String modId, String gameRuleName, boolean defaultValue)
    {
        return new GameRuleBuilder<>(modId, gameRuleName, () -> defaultValue, Platform.INSTANCE::registerGameRuleBooleanValue);
    }

    public static GameRuleBuilder<Integer, GameRules.IntegerValue> integer(String modId, String gameRuleName, int defaultValue)
    {
        return new GameRuleBuilder<>(modId, gameRuleName, () -> defaultValue, Platform.INSTANCE::registerGameRuleIntegerValue);
    }
}
