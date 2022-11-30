package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformGameRulesRegistry;

import java.util.function.BiConsumer;

@SuppressWarnings("NullableProblems")
public final class FabricGameRulesRegistry extends FabricHolder implements PlatformGameRulesRegistry
{
    FabricGameRulesRegistry(FabricPlatform platform)
    {
        super(platform);
    }

    @Override
    public <T extends GameRules.Value<T>> GameRules.Key<T> register(String gameRuleName, GameRules.Category category, GameRules.Type<T> type)
    {
        return GameRuleRegistry.register(gameRuleName, category, type);
    }

    @Override
    public GameRules.Type<GameRules.BooleanValue> createBooleanType(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
    {
        return GameRuleFactory.createBooleanRule(defaultValue, changeListener);
    }

    @Override
    public GameRules.Type<GameRules.IntegerValue> createIntegerType(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
    {
        return GameRuleFactory.createIntRule(defaultValue, changeListener);
    }
}
