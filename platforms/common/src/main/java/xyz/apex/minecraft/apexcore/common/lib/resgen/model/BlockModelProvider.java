package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import net.minecraft.data.PackOutput;

public abstract class BlockModelProvider extends ModelProvider
{
    public BlockModelProvider(PackOutput output)
    {
        super(output, "block");
    }
}
