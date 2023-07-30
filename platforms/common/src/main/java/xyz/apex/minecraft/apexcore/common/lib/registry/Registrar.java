package xyz.apex.minecraft.apexcore.common.lib.registry;

import java.util.function.UnaryOperator;

public final class Registrar extends AbstractRegistrar<Registrar>
{
    private Registrar(String ownerId)
    {
        super(ownerId);
    }

    public static Registrar create(String ownerId, UnaryOperator<Registrar> modifier)
    {
        return modifier.apply(new Registrar(ownerId));
    }

    public static Registrar create(String ownerId)
    {
        return create(ownerId, UnaryOperator.identity());
    }
}
