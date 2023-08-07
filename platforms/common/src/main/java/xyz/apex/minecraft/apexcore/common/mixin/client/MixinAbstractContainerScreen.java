package xyz.apex.minecraft.apexcore.common.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.apex.minecraft.apexcore.common.lib.menu.SimpleContainerMenuScreen;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen<T extends AbstractContainerMenu>
{
    // exists because for some strange reason
    // using ATs to make renderSlot overridable
    // and implementing manually would not work
    // when ApexCore is included as dependency on
    // child mods
    @Inject(
            method = "renderSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void ApexCore$renderSlot(GuiGraphics graphics, Slot slot, CallbackInfo ci)
    {
        var screen = (AbstractContainerScreen<T>) (Object) this;

        if(screen instanceof SimpleContainerMenuScreen<T>)
            graphics.blitSprite(SimpleContainerMenuScreen.SPRITE_SLOT, slot.x - 1, slot.y - 1, 18, 18);
    }
}
