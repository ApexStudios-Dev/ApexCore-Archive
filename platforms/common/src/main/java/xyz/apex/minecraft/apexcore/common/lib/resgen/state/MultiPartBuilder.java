package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.apache.commons.lang3.ArrayUtils;
import xyz.apex.minecraft.apexcore.common.lib.resgen.JsonHelper;

import java.util.List;

public final class MultiPartBuilder implements BlockStateGenerator
{
    private final List<Entry> entries = Lists.newArrayList();
    private final Block block;

    private MultiPartBuilder(Block block)
    {
        this.block = block;
    }

    @Override
    public Block block()
    {
        return block;
    }

    public MultiPartBuilder with(Variant variant)
    {
        entries.add(new Entry(variant));
        return this;
    }

    public MultiPartBuilder with(Variant first, Variant... others)
    {
        entries.add(new Entry(first, others));
        return this;
    }

    public MultiPartBuilder with(Condition condition, Variant variant)
    {
        entries.add(new ConditionalEntry(condition, variant));
        return this;
    }

    public MultiPartBuilder with(Condition condition, Variant first, Variant... others)
    {
        entries.add(new ConditionalEntry(condition, first, others));
        return this;
    }

    @Override
    public JsonElement toJson()
    {
        var stateDefinition = block.getStateDefinition();
        entries.forEach(entry -> entry.validate(stateDefinition));

        var entriesJson = new JsonArray();
        entries.stream().map(Entry::toJson).forEach(entryJson -> JsonHelper.addJsonIfNotEmpty(entriesJson, entryJson));
        var json = new JsonObject();
        JsonHelper.addJsonIfNotEmpty(json, "multipart", entriesJson);
        return json;
    }

    public static MultiPartBuilder builder(Block block)
    {
        return new MultiPartBuilder(block);
    }

    private static class Entry
    {
        private final List<Variant> variants;

        protected Entry(Variant first, Variant... others)
        {
            variants = List.of(ArrayUtils.addFirst(others, first));
        }

        protected void validate(StateDefinition<Block, BlockState> stateDefinition)
        {
        }

        protected JsonObject toJson()
        {
            var json = new JsonObject();
            JsonHelper.addJsonIfNotEmpty(json, "apply", Variant.toJson(variants));
            return json;
        }
    }

    private static final class ConditionalEntry extends Entry
    {
        private final Condition condition;

        private ConditionalEntry(Condition condition, Variant first, Variant... others)
        {
            super(first, others);

            this.condition = condition;
        }

        @Override
        protected void validate(StateDefinition<Block, BlockState> stateDefinition)
        {
            condition.validate(stateDefinition);
        }

        @Override
        protected JsonObject toJson()
        {
            var json = super.toJson();
            JsonHelper.addJsonIfNotEmpty(json, "when", condition.toJson());
            return json;
        }
    }
}
