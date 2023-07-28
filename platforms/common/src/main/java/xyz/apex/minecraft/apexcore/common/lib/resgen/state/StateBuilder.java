package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.gson.JsonObject;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.resgen.JsonHelper;

import java.util.function.Function;

public final class StateBuilder<T, S extends StateHolder<T, S>>
{
    private final T element;
    @Nullable private VariantBuilder<T, S> variant = null;
    @Nullable private MultiPartBuilder multiPart = null;
    private final Function<T, StateDefinition<T, S>> stateDefinitionFunction;

    @ApiStatus.Internal
    StateBuilder(T element, Function<T, StateDefinition<T, S>> stateDefinitionFunction)
    {
        this.element = element;
        this.stateDefinitionFunction = stateDefinitionFunction;
    }

    public VariantBuilder<T, S> variant()
    {
        if(variant == null)
            variant = new VariantBuilder<>(this);
        return variant;
    }

    public MultiPartBuilder multipart()
    {
        if(multiPart == null)
            multiPart = new MultiPartBuilder(this);
        return multiPart;
    }

    public T getElement()
    {
        return element;
    }

    public StateDefinition<T, S> getStateDefinition()
    {
        return stateDefinitionFunction.apply(element);
    }

    @ApiStatus.Internal
    JsonObject toJson()
    {
        var json = new JsonObject();

        if(variant != null)
            JsonHelper.addJsonIfNotEmpty(json, "variants", variant.toJson());
        if(multiPart != null)
            JsonHelper.addJsonIfNotEmpty(json, "multipart", multiPart.toJson());

        return json;
    }
}
