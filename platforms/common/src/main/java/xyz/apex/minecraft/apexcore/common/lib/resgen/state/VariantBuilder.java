package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.resgen.JsonHelper;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class VariantBuilder<T, S extends StateHolder<T, S>>
{
    private final StateBuilder<T, S> parent;
    private final Map<S, StateModelBuilder> models = Maps.newHashMap();
    private final Set<Property<?>> ignored = Sets.newHashSet();

    @ApiStatus.Internal
    VariantBuilder(StateBuilder<T, S> parent)
    {
        this.parent = parent;
    }

    public VariantBuilder<T, S> ignore(Property<?>... ignored)
    {
        Collections.addAll(this.ignored, ignored);
        return this;
    }

    public VariantBuilder<T, S> forAllStates(Function<S, StateModelBuilder> mapper)
    {
        models.clear();
        var seen = Sets.<S>newHashSet();

        parent.getStateDefinition()
              .getPossibleStates()
              .stream()
              .filter(seen::add)
              .forEach(state -> models.put(
                      state,
                      mapper.apply(state)
              ))
        ;

        return this;
    }

    public StateBuilder<T, S> end()
    {
        return parent;
    }

    @ApiStatus.Internal
    JsonObject toJson()
    {
        var json = new JsonObject();

        if(!models.isEmpty())
        {
            for(var state : parent.getStateDefinition().getPossibleStates())
            {
                var model = models.get(state);

                if(model == null)
                    continue;

                var propertyValues = Maps.newLinkedHashMap(state.getValues());
                ignored.forEach(propertyValues::remove);
                var key = compileStateKey(propertyValues);

                var stateJson = model.toJson();
                JsonHelper.addJsonIfNotEmpty(json, key, stateJson);
            }
        }

        return json;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private String compileStateKey(Map<Property<?>, Comparable<?>> properties)
    {
        var result = new StringBuilder();
        var comma = false;

        for(var entry : properties.entrySet())
        {
            if(comma)
                result.append(',');

            var property = entry.getKey();
            result.append(property.getName()).append('=').append(((Property) property).getName(entry.getValue()));
            comma = true;
        }

        return result.toString();
    }
}
