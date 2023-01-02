package xyz.apex.minecraft.apexcore.shared.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;

public interface EnhancedArmorMaterial extends ArmorMaterial
{
    ResourceLocation getRegistryName();

    @Override
    default String getName()
    {
        return getRegistryName().toString();
    }
}
