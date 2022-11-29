package xyz.apex.minecraft.apexcore.forge.platform;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformGameRulesRegistry;

import java.util.function.BiConsumer;

public final class ForgeGameRulesRegistry implements PlatformGameRulesRegistry
{
    private final ForgePlatform platform;

    ForgeGameRulesRegistry(ForgePlatform platform)
    {
        this.platform = platform;
    }

    @Override
    public <T extends GameRules.Value<T>> GameRules.Key<T> register(String gameRuleName, GameRules.Category category, GameRules.Type<T> type)
    {
        return GameRules.register(gameRuleName, category, type);
    }

    @Override
    public GameRules.Type<GameRules.BooleanValue> createBooleanType(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
    {
        return GameRules.BooleanValue.create(defaultValue, changeListener);
    }

    @Override
    public GameRules.Type<GameRules.IntegerValue> createIntegerType(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        return GameRules.IntegerValue.create(defaultValue, changeListener);
    }

    @Override
    public Platform platform()
    {
        return platform;
    }
}
