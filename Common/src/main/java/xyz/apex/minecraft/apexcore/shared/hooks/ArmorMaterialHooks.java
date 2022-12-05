package xyz.apex.minecraft.apexcore.shared.hooks;

import xyz.apex.minecraft.apexcore.shared.mixin.MixinArmorMaterials;

public interface ArmorMaterialHooks
{
    static int[] getHealthPerSlot()
    {
        return MixinArmorMaterials.ApexCore$getHealthPerSlot();
    }
}
