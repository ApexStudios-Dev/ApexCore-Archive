package dev.apexstudios.apexcore.common.mixin;

import com.google.common.collect.Iterables;
import dev.apexstudios.apexcore.common.registry.generic.DeferredSpawnEggItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public abstract class MixinSpawnEggItem
{
    @Inject(
            method = "byId",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void ApexCore$byId(@Nullable EntityType<?> entityType, CallbackInfoReturnable<SpawnEggItem> cir)
    {
        var deferred = DeferredSpawnEggItem.deferredOnlyById(entityType);

        if(deferred != null)
            cir.setReturnValue(deferred);
    }

    @Inject(
            method = "eggs",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void ApexCore$eggs(CallbackInfoReturnable<Iterable<SpawnEggItem>> cir)
    {
        cir.setReturnValue(Iterables.concat(cir.getReturnValue(), DeferredSpawnEggItem.deferredOnly()));
    }
}
