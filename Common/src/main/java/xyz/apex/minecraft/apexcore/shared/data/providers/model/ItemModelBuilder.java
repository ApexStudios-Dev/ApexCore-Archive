package xyz.apex.minecraft.apexcore.shared.data.providers.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class ItemModelBuilder extends ModelBuilder<ItemModelBuilder>
{
    private final List<OverrideBuilder> overrides = Lists.newArrayList();

    @ApiStatus.Internal
    public ItemModelBuilder(ResourceLocation modelPath)
    {
        super(Registries.ITEM, modelPath);
    }

    // region: Overrides
    public OverrideBuilder override()
    {
        var result = new OverrideBuilder(this);
        overrides.add(result);
        return result;
    }

    public OverrideBuilder override(int index)
    {
        return overrides.get(index);
    }
    // endregion

    public static final class OverrideBuilder extends Builder<OverrideBuilder, ItemModelBuilder>
    {
        private final Object2FloatMap<ResourceLocation> predicates = new Object2FloatOpenHashMap<>();
        @Nullable private ResourceLocation modelPath;

        @ApiStatus.Internal
        private OverrideBuilder(ItemModelBuilder parent)
        {
            super(parent);
        }

        // region: Model
        public OverrideBuilder model(ResourceLocation modelPath)
        {
            this.modelPath = modelPath;
            return this;
        }

        public OverrideBuilder model(String namespace, String path)
        {
            return model(new ResourceLocation(namespace, path));
        }

        public OverrideBuilder model(String path)
        {
            return model(new ResourceLocation(path));
        }

        public OverrideBuilder model(ModelBuilder<?> model)
        {
            return model(model.getModelPath());
        }
        // endregion

        // region
        public OverrideBuilder predicate(ResourceLocation name, float value)
        {
            predicates.put(name, value);
            return this;
        }

        public OverrideBuilder predicate(String modId, String name, float value)
        {
            return predicate(new ResourceLocation(modId, name), value);
        }

        public OverrideBuilder predicate(String name, float value)
        {
            return predicate(new ResourceLocation(name), value);
        }
        // endregion

        @ApiStatus.Internal
        @Nullable
        @Override
        protected JsonElement toJson()
        {
            if(modelPath == null && predicates.isEmpty()) return null;
            Preconditions.checkNotNull(modelPath);

            var root = new JsonObject();
            root.addProperty("model", modelPath.toString());

            var predicatesJson = new JsonObject();
            predicates.forEach((key, value) -> predicatesJson.addProperty(key.toString(), value));
            ModelBuilder.addJson(root, "predicate", predicatesJson);

            return root;
        }
    }
}
