package xyz.apex.minecraft.apexcore.shared.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.item.ArmorMaterials;

@Mixin(ArmorMaterials.class)
public interface MixinArmorMaterials
{
    @Accessor("HEALTH_PER_SLOT")
    static int[] ApexCore$getHealthPerSlot()
    {
        throw new AssertionError();
    }
}
