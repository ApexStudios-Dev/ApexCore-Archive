package xyz.apex.minecraft.apexcore.fabric.mixin.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;

import java.util.Map;

/*
    Forge allows armor model textures to be loaded
    from the mods assets folder

    Fabric does not seem to have a hook for this,
    so I custom implemented one here

    Fabric, please fix :)

    How it works,
        If armor material name contains a `:`,
        this tells the system that a mod id is present

        The material name is then split by this token to grab the mod id & material name
        `testmod:lead` gets split into `testmod` & `lead`

        We then use these 2 new strings to construct the texture path similarly to vanilla & under a similar file tree
        but moved off of the `minecraft` namespace and onto the given namespace

        tldr; armor layer textures are now loaded from `<mod_id>:textures/models/armor/<material_name>_layer_<idx>[_overlay].png`

        note; if no split token (':') is found the default vanilla logic is used, and textures are loaded from default location
 */
@Mixin(HumanoidArmorLayer.class)
public abstract class MixinHumanoidArmorLayer
{
    @Shadow @Final private static Map<String, ResourceLocation> ARMOR_LOCATION_CACHE;

    @Inject(
            method = "getArmorLocation",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ApexCore$getArmorLocation(ArmorItem item, boolean isLegs, @Nullable String suffix, CallbackInfoReturnable<ResourceLocation> cir)
    {
        var materialName = item.getMaterial().getName();
        var splitIndex = materialName.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);
        if(splitIndex == -1) return;

        var modId = materialName.substring(0, splitIndex);
        var name = materialName.substring(splitIndex + 1);
        var layerIndex = isLegs ? 2 : 1;
        var strSuffix = suffix == null ? "" : "_%s".formatted(suffix);

        var texturePath = "%s:textures/models/armor/%s_layer_%d%s.png".formatted(
                modId,
                name,
                layerIndex,
                strSuffix
        );

        var texture = ARMOR_LOCATION_CACHE.computeIfAbsent(texturePath, ResourceLocation::new);
        cir.setReturnValue(texture);
    }
}
