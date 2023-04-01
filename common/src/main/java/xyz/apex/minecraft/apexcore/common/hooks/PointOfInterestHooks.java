package xyz.apex.minecraft.apexcore.common.hooks;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import xyz.apex.minecraft.apexcore.common.mixin.AccessorPoiTypes;
import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;
import xyz.apex.minecraft.apexcore.common.registry.PoiTypeRegistry;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface PointOfInterestHooks extends PlatformHolder
{
    // must be called after block & poi type have registered
    @SafeVarargs
    static void registerHomePoint(Supplier<? extends Block>... blocks)
    {
        Stream.of(blocks)
                .map(Supplier::get)
                .map(PoiTypes::getBlockStates)
                .flatMap(Collection::stream)
                .filter(blockState -> blockState.hasProperty(BlockStateProperties.BED_PART))
                .filter(blockState -> blockState.getValue(BlockStateProperties.BED_PART) == BedPart.HEAD)
                .forEach(blockState -> {
                    if(PoiTypes.BEDS instanceof ImmutableSet<BlockState>) PoiTypes.BEDS = Sets.newHashSet(PoiTypes.BEDS);
                    PoiTypes.BEDS.add(blockState);

                    // could not make public & mutable using either aw or mixins
                    // so we keep track of our own list of valid block states
                    // and inject into the PoiType#is method
                    // and changes its return value if our custom list
                    // contains a matching block state
                    // if not the vanilla function takes over
                    /*BuiltInRegistries.POINT_OF_INTEREST_TYPE
                            .getOrThrow(PoiTypes.HOME)
                            .matchingStates() // vanilla this is potentially immutable, but we have a mixin injection to make sure it's not
                            .add(blockState)
                    ;*/
                    PoiTypeRegistry.get(PoiTypes.HOME).register(blockState);

                    AccessorPoiTypes.ApexCore$getTypeByStateMap().put(
                            blockState,
                            BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(PoiTypes.HOME)
                    );
                });
    }
}
