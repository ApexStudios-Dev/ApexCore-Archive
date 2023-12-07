package dev.apexstudios.apexcore.common.generator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;

public abstract class AbstractResourceGenerator<T extends AbstractResourceGenerator<T>> implements ResourceGenerator
{
    protected final String ownerId;
    protected final PackOutput output;

    protected AbstractResourceGenerator(String ownerId, PackOutput output)
    {
        this.ownerId = ownerId;
        this.output = output;
    }

    protected abstract void generate(CachedOutput cache, HolderLookup.Provider registries);
}
