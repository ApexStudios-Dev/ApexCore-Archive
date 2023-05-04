package xyz.apex.minecraft.apexcore.common.mixin;

import com.google.common.collect.Iterables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.apex.minecraft.apexcore.common.lib.item.ExtendedSpawnEggItem;

@Mixin(SpawnEggItem.class)
public class MixinSpawnEggItem
{
    @Inject(
            method = "byId",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void ApexCore$byId(@Nullable EntityType<?> entityType, CallbackInfoReturnable<SpawnEggItem> ci)
    {
        var spawnEggItem = ExtendedSpawnEggItem.Mixin$fromEntityType(entityType);
        if(spawnEggItem != null) ci.setReturnValue(spawnEggItem);
    }

    @Inject(
            method = "eggs",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void ApexCore$eggs(CallbackInfoReturnable<Iterable<SpawnEggItem>> ci)
    {
        ci.setReturnValue(Iterables.concat(ci.getReturnValue(), ExtendedSpawnEggItem.Mixin$eggs()));
    }
}
