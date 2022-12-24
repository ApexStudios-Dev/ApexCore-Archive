package xyz.apex.minecraft.apexcore.shared.event;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

public interface EventHelper
{
    static <T extends Event> boolean processMixinEvent(EventType<T> event, T val, CallbackInfo ci)
    {
        var canceled = event.post(val);
        if(canceled && ci.isCancellable()) ci.cancel();
        return canceled;
    }

    static <T extends Event, R> boolean processMixinEvent(EventType<T> event, T val, CallbackInfoReturnable<R> cir, Supplier<R> canceledResult)
    {
        var canceled = processMixinEvent(event, val, cir);
        if(canceled && cir.isCancellable()) cir.setReturnValue(canceledResult.get());
        return canceled;
    }

    static <T extends Event, R> boolean processMixinEvent(EventType<T> event, T val, CallbackInfoReturnable<R> cir, R canceledResult)
    {
        var canceled = processMixinEvent(event, val, cir);
        if(canceled && cir.isCancellable()) cir.setReturnValue(canceledResult);
        return canceled;
    }
}
