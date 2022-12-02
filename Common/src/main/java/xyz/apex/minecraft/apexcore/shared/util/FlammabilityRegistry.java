package xyz.apex.minecraft.apexcore.shared.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.OptionalInt;

// NOTE: This system overrides Forge & Fabrics flammability systems if a given entry exists for a block
//  This is due to the way it is implemented using mixins, if an entry exists we directly redirect the getBurnOdds & getIgniteOdds functions
//  in FireBlock to return the values from this registry
//  Use this registry for custom modded blocks only, that do not / can not make use of Forge / Fabrics built-in systems
public final class FlammabilityRegistry
{
    private static final Map<ResourceLocation, Entry> REGISTRY = Maps.newHashMap();
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(ResourceLocation registryName, int burnOdds, int igniteOdds)
    {
        Validate.inclusiveBetween(burnOdds, 0, 100, "Burn odds must be between 0-100 (inclusive)");
        Validate.inclusiveBetween(igniteOdds, 0, 100, "Burn odds must be between 0-100 (inclusive)");
        Validate.isTrue(!REGISTRY.containsKey(registryName), "Attempt to register duplicate flammability odds %s", registryName);
        LOGGER.info("Registering custom Flammability for Block: {} (BurnOdds: {}, IgniteOdds: {})", registryName, burnOdds, igniteOdds);
        REGISTRY.put(registryName, new Entry(burnOdds, igniteOdds));
    }

    public static OptionalInt lookupBurnOdds(ResourceLocation registryName)
    {
        return REGISTRY.containsKey(registryName) ? OptionalInt.of(REGISTRY.get(registryName).burnOdds) : OptionalInt.empty();
    }

    public static int getBurnOddsOrDefault(ResourceLocation registryName, int defaultBurnOdds)
    {
        return lookupBurnOdds(registryName).orElse(defaultBurnOdds);
    }

    public static int getBurnOdds(ResourceLocation registryName)
    {
        return getBurnOddsOrDefault(registryName, 0);
    }

    public static OptionalInt lookupIgniteOdds(ResourceLocation registryName)
    {
        return REGISTRY.containsKey(registryName) ? OptionalInt.of(REGISTRY.get(registryName).igniteOdds) : OptionalInt.empty();
    }

    public static int getIgniteOddsOrDefault(ResourceLocation registryName, int defaultIgniteOdds)
    {
        return lookupIgniteOdds(registryName).orElse(defaultIgniteOdds);
    }

    public static int getIgniteOdds(ResourceLocation registryName)
    {
        return getIgniteOddsOrDefault(registryName, 0);
    }

    private record Entry(int burnOdds, int igniteOdds) { }
}
