package xyz.apex.minecraft.apexcore.common.lib.multiblock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;

public final class MultiBlockType
{
    private static final Int2ObjectMap<IntegerProperty> BLOCK_STATE_PROPERTIES = new Int2ObjectOpenHashMap<>();

    private final List<BlockPos> localPositions;
    private final boolean renderAtOriginOnly;

    private MultiBlockType(List<String[]> pattern, boolean renderAtOriginOnly)
    {
        this.renderAtOriginOnly = renderAtOriginOnly;

        var positions = Sets.<BlockPos>newHashSet();

        for(var y = 0; y < pattern.size(); y++)
        {
            var aisle = pattern.get(y);

            for(var z = 0; z < aisle.length; z++)
            {
                var str = aisle[z];

                for(var x = 0; x < str.length(); x++)
                {
                    var c = str.charAt(x);

                    if(!Character.isWhitespace(c))
                        positions.add(new BlockPos(x, y, z));
                }
            }
        }

        localPositions = ImmutableList.copyOf(positions);
    }

    public boolean renderAtOriginOnly()
    {
        return renderAtOriginOnly;
    }

    public List<BlockPos> getLocalPositions()
    {
        return localPositions;
    }

    public int size()
    {
        return localPositions.size();
    }

    public IntegerProperty getProperty()
    {
        return property(localPositions.size());
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static IntegerProperty property(int max)
    {
        return BLOCK_STATE_PROPERTIES.computeIfAbsent(max, $ -> IntegerProperty.create("multi_block_index", 0, $));
    }

    public static final class Builder
    {
        private final List<String[]> pattern = Lists.newArrayList();
        private boolean renderAtOriginOnly = false;

        private Builder()
        {
        }

        public Builder with(String... aisle)
        {
            pattern.add(aisle);
            return this;
        }

        public Builder renderAtOriginOnly()
        {
            renderAtOriginOnly = true;
            return this;
        }

        public MultiBlockType build()
        {
            return new MultiBlockType(pattern, renderAtOriginOnly);
        }
    }
}
