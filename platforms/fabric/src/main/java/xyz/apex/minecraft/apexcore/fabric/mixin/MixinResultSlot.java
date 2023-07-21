package xyz.apex.minecraft.apexcore.fabric.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.event.types.PlayerEvents;

@Mixin(ResultSlot.class)
@ApiStatus.Internal
@ApiStatus.NonExtendable
public class MixinResultSlot
{
    @Shadow private int removeCount;
    @Shadow @Final private Player player;
    @Shadow @Final private CraftingContainer craftSlots;

    @Inject(
            method = "checkTakeAchievements",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/inventory/ResultSlot;removeCount:I",
                    ordinal = 2
            )
    )
    private void ApexCore$checkTakeAchievements(ItemStack stack, CallbackInfo ci)
    {
        if(removeCount > 2)
            PlayerEvents.CRAFT_ITEM.post().handle(player, stack, craftSlots);
    }
}
