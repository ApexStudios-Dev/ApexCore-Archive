package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.Maps;

import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.OptionalInt;

// NOTE: This system overrides Forge & Fabrics flammability systems if a given entry exists for a block
//  This is due to the way it is implemented using mixins, if an entry exists we directly redirect the getBurnOdds & getIgniteOdds functions
//  in FireBlock to return the values from this registry
//  Use this registry for custom modded blocks only, that do not / can not make use of Forge / Fabrics built-in systems
public final class FlammabilityRegistry
{
    private static final Map<Block, Entry> REGISTRY = Maps.newHashMap();

    public static void register(Block block, int burnOdds, int igniteOdds)
    {
        REGISTRY.put(block, new Entry(burnOdds, igniteOdds));
    }

    public static OptionalInt lookupBurnOdds(Block block)
    {
        return REGISTRY.containsKey(block) ? OptionalInt.of(REGISTRY.get(block).burnOdds) : OptionalInt.empty();
    }

    public static int getBurnOddsOrDefault(Block block, int defaultBurnOdds)
    {
        return lookupBurnOdds(block).orElse(defaultBurnOdds);
    }

    public static int getBurnOdds(Block block)
    {
        return getBurnOddsOrDefault(block, 0);
    }

    public static OptionalInt lookupIgniteOdds(Block block)
    {
        return REGISTRY.containsKey(block) ? OptionalInt.of(REGISTRY.get(block).igniteOdds) : OptionalInt.empty();
    }

    public static int getIgniteOddsOrDefault(Block block, int defaultIgniteOdds)
    {
        return lookupIgniteOdds(block).orElse(defaultIgniteOdds);
    }

    public static int getIgniteOdds(Block block)
    {
        return getIgniteOddsOrDefault(block, 0);
    }

    private record Entry(int burnOdds, int igniteOdds) {}
}
