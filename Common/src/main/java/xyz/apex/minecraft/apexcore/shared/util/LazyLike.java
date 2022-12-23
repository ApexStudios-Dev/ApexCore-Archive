package xyz.apex.minecraft.apexcore.shared.util;

import java.util.function.Supplier;

// implementations should make #get lazy
// construct the result upon first call
// and return that same result on subsequent calls
public interface LazyLike<T> extends Supplier<T>
{}
