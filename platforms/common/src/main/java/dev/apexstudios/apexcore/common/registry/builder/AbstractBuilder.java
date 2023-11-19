package dev.apexstudios.apexcore.common.registry.builder;

import com.google.errorprone.annotations.DoNotCall;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.covers1624.quack.util.LazyValue;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractBuilder<O extends AbstractRegister<O>, P, T, R extends T, H extends DeferredHolder<T, R>, B extends AbstractBuilder<O, P, T, R, H, B>>
{
    protected final O owner;
    protected final P parent;
    protected final ResourceKey<? extends Registry<T>> registryType;
    protected final ResourceKey<T> valueKey;
    private final Supplier<H> holderFactory;
    private final BuilderHelper helper;

    protected AbstractBuilder(O owner, P parent, ResourceKey<? extends Registry<T>> registryType, String valueName, Function<ResourceKey<T>, H> holderFactory, BuilderHelper helper)
    {
        this.owner = owner;
        this.parent = parent;
        this.registryType = registryType;
        this.helper = helper;

        valueKey = ResourceKey.create(registryType, new ResourceLocation(owner.ownerId(), valueName));
        this.holderFactory = new LazyValue<>(() -> holderFactory.apply(valueKey));

        onRegister(this::onRegister);
    }

    public final O getOwner()
    {
        return owner;
    }

    public final P getParent()
    {
        return parent;
    }

    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registryType;
    }

    public final ResourceKey<T> getValueKey()
    {
        return valueKey;
    }

    public final ResourceLocation getValueName()
    {
        return valueKey.location();
    }

    public final String getOwnerId()
    {
        return owner.ownerId();
    }

    public final H asHolder()
    {
        return holderFactory.get();
    }

    public final H register()
    {
        return helper.register(registryType, holderFactory, this::createValue);
    }

    public final P build()
    {
        register();
        return parent;
    }

    public final B onRegister(Consumer<R> listener)
    {
        owner.addRegisterListener(registryType, getValueName().getPath(), listener);
        return self();
    }

    public final <OR> B onRegisterAfter(ResourceKey<? extends Registry<OR>> dependencyType, Consumer<R> listener)
    {
        return onRegister(value -> {
            if(owner.isRegistered(dependencyType))
                listener.accept(value);
            else
                owner.addRegisterListener(dependencyType, () -> listener.accept(value));
        });
    }

    @ApiStatus.OverrideOnly
    @DoNotCall
    protected void onRegister(R value)
    {
    }

    protected final B self()
    {
        return (B) this;
    }

    @DoNotCall protected abstract R createValue();
}
