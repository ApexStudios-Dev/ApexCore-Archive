package xyz.apex.minecraft.apexcore.shared.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.item.ArmorMaterials;

@Mixin(ArmorMaterials.class)
public interface AccessorArmorMaterials
{
    @Accessor("HEALTH_PER_SLOT")
    static int[] getHealthPerSlot()
    {
        throw new AssertionError("Internal Mixins Error!");
    }
}
