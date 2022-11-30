package xyz.apex.minecraft.apexcore.shared.hooks;

import org.apache.commons.lang3.Validate;

import java.util.function.Function;

public interface Hooks
{
    static <S, H, T> T get(S src, Class<H> hookType, Function<H, T> getter)
    {
        Validate.isInstanceOf(hookType, src);
        return getter.apply(hookType.cast(src));
    }
}
