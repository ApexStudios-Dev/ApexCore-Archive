package dev.apexstudios.apexcore.common.registry;

import java.util.function.UnaryOperator;

public final class Register extends AbstractRegister<Register>
{
    private Register(String ownerId)
    {
        super(ownerId);
    }

    public static Register create(String ownerId, UnaryOperator<Register> modifier)
    {
        return modifier.apply(new Register(ownerId));
    }

    public static Register create(String ownerId)
    {
        return create(ownerId, UnaryOperator.identity());
    }
}
