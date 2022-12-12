package xyz.apex.minecraft.apexcore.shared.data;

import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.LazyLike;

import java.util.function.Supplier;

public class DataContext<R, E extends RegistryEntry<R>> implements Supplier<R>, LazyLike<R>
{
    private final E entry;

    public DataContext(E entry)
    {
        this.entry = entry;
    }

    public E getEntry()
    {
        return entry;
    }

    @Override
    public R get()
    {
        return entry.get();
    }
}
