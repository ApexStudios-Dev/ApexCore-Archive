package xyz.apex.minecraft.apexcore.common.lib.multiblock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Simple pattern implementation for multi blocks.
 */
public final class MultiBlockPattern implements Iterable<BlockPos>
{
    /**
     * Index for origin point of all multi blocks.
     */
    public static final int INDEX_ORIGIN = 0;

    private final List<BlockPos> localPositions;

    private MultiBlockPattern(Builder builder)
    {
        // parse strings into local positions
        // whitespace - empty block (skip)
        // none whitespace - block bound to this block pattern
        var positions = parsePositions(builder.pattern);

        // copy positions into immutable list
        localPositions = ImmutableList.copyOf(positions);
    }

    /**
     * @return Immutable list of all local space positions.
     */
    public List<BlockPos> getPositions()
    {
        return localPositions;
    }

    /**
     * Returns local space position at given index.
     * <p>
     * Uses modulo on index, so any out of bounds indexes wrap back around, instead of throwing {@link ArrayIndexOutOfBoundsException}.
     *
     * @param index Index to lookup local space position at.
     * @return Local space position at given index.
     */
    public BlockPos getPosition(int index)
    {
        // use mod to wrap around if we go out of bounds
        return localPositions.get(Mth.positiveModulo(index, localPositions.size()));
    }

    /**
     * @return Origin point for this multi block, in local space coords.
     */
    public BlockPos getOrigin()
    {
        return getPosition(INDEX_ORIGIN);
    }

    /**
     * @return Stream iterating over all local space positions.
     */
    public Stream<BlockPos> stream()
    {
        return localPositions.stream();
    }

    @Override
    public Iterator<BlockPos> iterator()
    {
        return localPositions.iterator();
    }

    private static List<BlockPos> parsePositions(List<String[]> patterns)
    {
        var positions = Lists.<BlockPos>newArrayList();
        var pos = new BlockPos.MutableBlockPos(0, 0, 0);

        for(var z = 0; z < patterns.size(); z++)
        {
            var pattern = patterns.get(z);
            pos = pos.setZ(z);

            for(var x = 0; x < pattern.length; x++)
            {
                var line = pattern[x];
                pos = pos.setX(x);

                for(var y = 0; y < line.length(); y++)
                {
                    var c = line.charAt(y);
                    pos = pos.setY(y);
                    if(Character.isWhitespace(c)) continue;
                    positions.add(pos.immutable());
                }
            }
        }

        return positions;
    }

    /**
     * @return Builder to build a new multi block pattern
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder for building a new multi block pattern.
     */
    public static final class Builder
    {
        private final List<String[]> pattern = Lists.newArrayList();

        private Builder()
        {
        }

        /**
         * Add a new set of lines to the pattern.
         * <p>
         * Each call to this method increases the Z component of the pattern.
         * <p>
         * Each element in given strings is assigned to a Y coordinate.
         * <p>
         * Each String in the {@code lines} array is assigned to a X coordinate.
         * <p>
         * Any whitespace is treated as AIR matching.
         * <p>
         * Any character other than whitespace matches the block bound with this pattern.
         * <p>
         * For example, a 3x3x3 pattern would look like so
         * <pre>{@code builder()
         *      .with("XXX", "XXX", "XXX")
         *      .with("XXX", "XXX", "XXX")
         *      .with("XXX", "XXX", "XXX")
         *      .build()
         * }</pre>
         *
         * @param lines Pattern lines.
         * @return This builder instance.
         */
        public Builder with(String... lines)
        {
            pattern.add(lines);
            return this;
        }

        /**
         * @return Compiles the builder down to a multi block pattern.
         */
        public MultiBlockPattern build()
        {
            return new MultiBlockPattern(this);
        }
    }
}
