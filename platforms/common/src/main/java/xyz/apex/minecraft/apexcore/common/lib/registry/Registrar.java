package xyz.apex.minecraft.apexcore.common.lib.registry;

import java.util.function.UnaryOperator;

/**
 * Basic implementation of {@link AbstractRegistrar}.
 *
 * @see AbstractRegistrar
 */
public final class Registrar extends AbstractRegistrar<Registrar>
{
    private Registrar(String ownerId)
    {
        super(ownerId);
    }

    /**
     * Constructs new Registrar, applying the given modifiers.
     *
     * @param ownerId Owning mod id.
     * @param modifier Callback to modify registrar post creation.
     * @return New Registrar instance.
     */
    public static Registrar create(String ownerId, UnaryOperator<Registrar> modifier)
    {
        return modifier.apply(new Registrar(ownerId));
    }

    /**
     * Constructs new Registrar.
     *
     * @param ownerId Owning mod id.
     * @return New Registrar instance.
     */
    public static Registrar create(String ownerId)
    {
        return create(ownerId, UnaryOperator.identity());
    }
}
