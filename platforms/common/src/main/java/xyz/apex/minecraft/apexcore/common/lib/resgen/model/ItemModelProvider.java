package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import net.minecraft.data.PackOutput;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ResourceTypes;

public abstract class ItemModelProvider extends ModelProvider
{
    public ItemModelProvider(PackOutput output)
    {
        super(output, ResourceTypes.CLIENT_MODEL_ITEM);
    }
}
