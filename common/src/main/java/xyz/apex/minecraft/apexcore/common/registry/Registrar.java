package xyz.apex.minecraft.apexcore.common.registry;

import java.util.function.UnaryOperator;

public final class Registrar extends AbstractRegistrar<Registrar>
{
    private Registrar(String modId)
    {
        super(modId);
    }

    public static Registrar create(String modId, UnaryOperator<Registrar> modifier)
    {
        return modifier.apply(new Registrar(modId));
    }

    public static Registrar create(String modId)
    {
        return create(modId, UnaryOperator.identity());
    }
}
