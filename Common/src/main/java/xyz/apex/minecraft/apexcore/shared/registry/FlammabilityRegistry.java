package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;

import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;

import java.util.Map;
import java.util.OptionalInt;

// NOTE: This system overrides Forge & Fabrics flammability systems if a given entry exists for a block
//  This is due to the way it is implemented using mixins, if an entry exists we directly redirect the getBurnOdds & getIgniteOdds functions
//  in FireBlock to return the values from this registry
//  Use this registry for custom modded blocks only, that do not / can not make use of Forge / Fabrics built-in systems
public final class FlammabilityRegistry
{
    private static final Map<ResourceLocation, Entry> REGISTRY = Maps.newHashMap();

    public static void register(ResourceLocation registryName, int burnOdds, int igniteOdds)
    {
        Validate.inclusiveBetween(0, 100, burnOdds, "Burn odds must be between 0-100 (inclusive)");
        Validate.inclusiveBetween(0, 100, igniteOdds, "Burn odds must be between 0-100 (inclusive)");
        Validate.isTrue(!REGISTRY.containsKey(registryName), "Attempt to register duplicate flammability odds %s", registryName);
        Platform.INSTANCE.getLogger().info("Registering custom Flammability for Block: {} (BurnOdds: {}, IgniteOdds: {})", registryName, burnOdds, igniteOdds);
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