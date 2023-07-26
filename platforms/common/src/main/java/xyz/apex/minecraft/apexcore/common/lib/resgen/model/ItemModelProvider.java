package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import net.minecraft.data.PackOutput;

public abstract class ItemModelProvider extends ModelProvider
{
    public ItemModelProvider(PackOutput output)
    {
        super(output, "item");
    }
}
