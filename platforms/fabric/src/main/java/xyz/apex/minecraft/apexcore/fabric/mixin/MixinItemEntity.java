package xyz.apex.minecraft.apexcore.fabric.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;

@Mixin(ItemEntity.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinItemEntity
{
    @Inject(
            method = "playerTouch",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;"
            ),
            allow = 1,
            cancellable = true
    )
    private void ApexCore$playerTouch(Player player, CallbackInfo ci)
    {
        var self = (ItemEntity) (Object) this;

        if(PlayerEvents.PICKUP_ITEM.post().handle(player, self))
            ci.cancel();
    }
}
