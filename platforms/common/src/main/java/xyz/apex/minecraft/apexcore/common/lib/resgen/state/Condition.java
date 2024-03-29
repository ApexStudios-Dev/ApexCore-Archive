package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.gson.JsonElement;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public sealed interface Condition permits CompositeCondition, TerminalCondition
{
    void validate(StateDefinition<Block, BlockState> stateDefinition);

    JsonElement toJson();

    static TerminalCondition condition()
    {
        return new TerminalCondition();
    }

    static CompositeCondition or(Condition condition)
    {
        return composite(Operation.OR, condition);
    }

    static CompositeCondition or(Condition first, Condition... others)
    {
        return composite(Operation.OR, first, others);
    }

    static CompositeCondition and(Condition condition)
    {
        return composite(Operation.AND, condition);
    }

    static CompositeCondition and(Condition first, Condition... others)
    {
        return composite(Operation.AND, first, others);
    }

    private static CompositeCondition composite(Operation operation, Condition condition)
    {
        return new CompositeCondition(operation, condition);
    }

    private static CompositeCondition composite(Operation operation, Condition first, Condition... others)
    {
        return new CompositeCondition(operation, first, others);
    }

    enum Operation implements StringRepresentable
    {
        AND("AND"),
        OR("OR");

        private final String serializedName;

        Operation(String serializedName)
        {
            this.serializedName = serializedName;
        }

        @Override
        public String getSerializedName()
        {
            return serializedName;
        }
    }
}
