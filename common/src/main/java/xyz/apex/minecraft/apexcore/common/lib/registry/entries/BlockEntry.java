package xyz.apex.minecraft.apexcore.common.lib.registry.entries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.BlockBuilder;

import java.util.Optional;

/**
 * Main RegistryEntry class for all Block entries.
 * <p>
 * While the constructor is publicly visible, you should never invoke or create instance of this class yourself.
 * Instances of this class are provided when registered using the {@link BlockBuilder} class.
 *
 * @param <T> Type of block.
 */
public final class BlockEntry<T extends Block> extends RegistryEntry.Delegated<T> implements ItemLikeEntry<T>, FeatureElementEntry<T>
{
    /**
     * DO NOT MANUALLY CALL PUBLIC FOR INTERNAL USAGES ONLY
     */
    @ApiStatus.Internal
    public BlockEntry(RegistryEntry<T> delegate)
    {
        super(delegate);
    }

    /**
     * Returns true if the given block state contains this registry entry.
     *
     * @param blockState Block state to validate.
     * @return True if the given block state contains this registry entry.
     */
    public boolean hasBlockState(BlockState blockState)
    {
        return map(blockState::is).orElse(false);
    }

    /**
     * @return The default block state for this registry entry.
     */
    public BlockState defaultBlockState()
    {
        return map(Block::defaultBlockState).orElseGet(Blocks.AIR::defaultBlockState);
    }

    /**
     * Returns the default block state for this registry entry with the given property applied, if block state allows it.
     *
     * @param property Block state property to be applied.
     * @param value    Block state property value to be set.
     * @param <P>      Type of block state property.
     * @param <V>      Type of block state property value.
     * @return Default block state for this registry entry with the given property applied, if block state allows it.
     */
    public <P extends Comparable<P>, V extends P> BlockState withProperty(Property<P> property, V value)
    {
        return defaultBlockState().trySetValue(property, value);
    }

    /**
     * Returns optional containing the default value of given block state property, if default block state contains it, otherwise empty.
     *
     * @param property Block state property to look up default value for.
     * @param <P>      Type of block state property.
     * @return Optional containing the default value of given block state property, if default block state contains it, otherwise empty.
     */
    public <P extends Comparable<P>> Optional<P> tryGetDefaultValue(Property<P> property)
    {
        return defaultBlockState().getOptionalValue(property);
    }
}
