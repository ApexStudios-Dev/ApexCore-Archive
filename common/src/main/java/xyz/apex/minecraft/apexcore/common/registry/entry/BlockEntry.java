package xyz.apex.minecraft.apexcore.common.registry.entry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;

public final class BlockEntry<T extends Block> extends RegistryEntry<T> implements ItemLikeEntry<T>, FeatureElementEntry<T>
{
    public BlockEntry(ResourceLocation registryName)
    {
        super(Registries.BLOCK, registryName);
    }

    public BlockState defaultBlockState()
    {
        return get().defaultBlockState();
    }

    public boolean hasBlockState(BlockState blockState)
    {
        return blockState.is(get());
    }

    public <P extends Comparable<P>> BlockState withProperty(Property<P> property, P value)
    {
        return defaultBlockState().trySetValue(property, value);
    }
}
