package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.apache.commons.lang3.ArrayUtils;
import xyz.apex.minecraft.apexcore.common.lib.resgen.JsonHelper;

import java.util.List;

public final class CompositeCondition implements Condition
{
    private final Operation operation;
    private final List<Condition> conditions;

    CompositeCondition(Operation operation, Condition first, Condition... others)
    {
        this.operation = operation;
        this.conditions = List.of(ArrayUtils.addFirst(others, first));
    }

    @Override
    public void validate(StateDefinition<Block, BlockState> stateDefinition)
    {
        conditions.forEach(condition -> condition.validate(stateDefinition));
    }

    @Override
    public JsonElement toJson()
    {
        var conditionsJson = new JsonArray();
        conditions.stream().map(Condition::toJson).forEach(condition -> JsonHelper.addJsonIfNotEmpty(conditionsJson, condition));

        var json = new JsonObject();
        JsonHelper.addJsonIfNotEmpty(json, operation.getSerializedName(), conditionsJson);
        return json;
    }
}
