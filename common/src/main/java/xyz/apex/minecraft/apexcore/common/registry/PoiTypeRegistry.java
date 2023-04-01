package xyz.apex.minecraft.apexcore.common.registry;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;

// internal usages, to register custom blocks to a poi type
// use PointOfInterestHooks
public final class PoiTypeRegistry
{
    private static final Map<ResourceKey<PoiType>, PoiTypeRegistry> registries = Maps.newHashMap();

    private final ResourceKey<PoiType> poiType;
    private final Set<BlockState> matchingStates = Sets.newHashSet();

    private PoiTypeRegistry(ResourceKey<PoiType> poiType)
    {
        this.poiType = poiType;
    }

    public ResourceKey<PoiType> pointOfInterest()
    {
        return poiType;
    }

    public boolean isFor(BlockState blockState)
    {
        return matchingStates.contains(blockState);
    }

    public void register(BlockState blockState)
    {
        matchingStates.add(blockState);
    }

    public static PoiTypeRegistry get(ResourceKey<PoiType> poiType)
    {
        return registries.computeIfAbsent(poiType, PoiTypeRegistry::new);
    }
}
