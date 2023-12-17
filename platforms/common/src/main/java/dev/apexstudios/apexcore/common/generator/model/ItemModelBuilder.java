package dev.apexstudios.apexcore.common.generator.model;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import dev.apexstudios.apexcore.common.util.JsonHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Consumer;

public final class ItemModelBuilder extends ModelBuilder<ItemModelBuilder, Item>
{
    private static final LightType DEFAULT_GUI_LIGHT = LightType.SIDE;

    private LightType guiLight = DEFAULT_GUI_LIGHT;
    private final Int2ObjectMap<List<Consumer<OverrideBuilder>>> overrides = new Int2ObjectOpenHashMap<>();

    ItemModelBuilder(ModelGenerator<Item, ItemModelBuilder> generator, ResourceLocation path)
    {
        super(generator, path);
    }

    // region: Parent
    public ItemModelBuilder parentBlock(Block element)
    {
        var registryName = BuiltInRegistries.BLOCK.getKey(element);
        return parent(ModelGenerator.withModelDir(Registries.BLOCK, registryName));
    }

    public ItemModelBuilder parentBlock(Holder<Block> holder)
    {
        var unwrapped = holder.unwrap();
        unwrapped.ifLeft(this::parentBlock);
        unwrapped.ifRight(this::parentBlock);
        return self();
    }

    public ItemModelBuilder parentBlock(DeferredHolder<Block, ? super Block> holder)
    {
        return parentBlock(holder.registryKey());
    }

    public ItemModelBuilder parentBlock(ResourceKey<Block> registryKey)
    {
        return parent(ModelGenerator.withModelDir(Registries.BLOCK, registryKey.location()));
    }
    // endregion

    // region: Textures
    public String textureLayer(int layer)
    {
        return texture("layer%d".formatted(layer));
    }

    public ItemModelBuilder textureLayer(int layer, ResourceLocation texturePath)
    {
        return textureLayer(layer, texturePath.toString());
    }

    public ItemModelBuilder textureLayer(int layer, String textureOwnerId, String texturePath)
    {
        return textureLayer(layer, "%s:%s".formatted(textureOwnerId, texturePath));
    }

    public ItemModelBuilder textureLayer(int layer, String texturePath)
    {
        return texture("layer%d".formatted(layer), texturePath);
    }

    public ItemModelBuilder textureLayer(int layer, Item element)
    {
        return textureLayer(layer, generator.withModelDir(element));
    }

    public ItemModelBuilder textureLayer(int layer, Holder<Item> holder)
    {
        var unwrapped = holder.unwrap();
        unwrapped.ifLeft(registryKey -> textureLayer(layer, registryKey));
        unwrapped.ifRight(element -> textureLayer(layer, element));
        return self();
    }

    public ItemModelBuilder textureLayer(int layer, DeferredHolder<Item, ? super Item> holder)
    {
        return textureLayer(layer, holder.registryKey());
    }

    public ItemModelBuilder textureLayer(int layer, ResourceKey<Item> registryKey)
    {
        return textureLayer(layer, generator.withModelDir(registryKey.location()));
    }
    // endregion

    public ItemModelBuilder guiLight(LightType guiLight)
    {
        this.guiLight = guiLight;
        return this;
    }

    // region: Overrides
    public ItemModelBuilder override(int overrideIndex, Consumer<OverrideBuilder> consumer)
    {
        overrides.computeIfAbsent(overrideIndex, $ -> Lists.newArrayList()).add(consumer);
        return this;
    }

    public ItemModelBuilder override(Consumer<OverrideBuilder> consumer)
    {
        return override(overrides.size(), consumer);
    }
    // endregion

    public ItemModelBuilder withVanillaArmorTrims(ArmorItem item, boolean hasOverlay)
    {
        return withVanillaArmorTrims(item.getMaterial(), item.getType(), hasOverlay);
    }

    public ItemModelBuilder withVanillaArmorTrims(ArmorItem item)
    {
        return withVanillaArmorTrims(item, false);
    }

    public ItemModelBuilder withVanillaArmorTrims(ArmorMaterial armorMaterial, ArmorItem.Type armorType, boolean hasOverlay)
    {
        final var BASE_LAYER = 0;
        final var OVERLAY_LAYER = BASE_LAYER + 1;
        final var TRIM_OVERLAY_LAYER = OVERLAY_LAYER + 1;
        final var TRIM_NO_OVERLAY_LAYER = BASE_LAYER + 1;

        var baseTexture = generator.withModelDir(new ResourceLocation(textureLayer(BASE_LAYER)));
        var overlayTexture = baseTexture.withSuffix("_overlay");

        if(hasOverlay)
            textureLayer(OVERLAY_LAYER, overlayTexture);

        for(var trimModelData : ItemModelGenerators.GENERATED_TRIM_MODELS)
        {
            var trimModel = path().withSuffix("_%s_trim".formatted(trimModelData.name(armorMaterial)));
            var trimTexture = new ResourceLocation(baseTexture.getNamespace(), "trims/items/%s_trim_%s".formatted(armorType.getName(), trimModelData.name()));

            generator.model(trimModel, model -> {
                model.parent(ModelFile.ITEM_GENERATED).textureLayer(BASE_LAYER, baseTexture);

                if(armorMaterial == ArmorMaterials.LEATHER || hasOverlay)
                {
                    model.textureLayer(TRIM_OVERLAY_LAYER, overlayTexture)
                         .textureLayer(TRIM_OVERLAY_LAYER, trimTexture);
                }
                else
                    model.textureLayer(TRIM_NO_OVERLAY_LAYER, trimTexture);
            });

            override(override -> override
                    .armorTrim(trimModelData.itemModelIndex())
                    .model(trimModel)
            );
        }

        return this;
    }

    public ItemModelBuilder withVanillaArmorTrims(ArmorMaterial armorMaterial, ArmorItem.Type armorType)
    {
        return withVanillaArmorTrims(armorMaterial, armorType, false);
    }

    @Override
    protected void serializeExtraData(JsonObject root)
    {
        if(guiLight != DEFAULT_GUI_LIGHT)
            root.addProperty("gui_light", guiLight.getSerializedName());

        var overridesJson = new JsonArray();

        overrides.forEach((overrideIndex, callbacks) -> {
            var builder = new OverrideBuilder();
            callbacks.forEach(callback -> callback.accept(builder));
            JsonHelper.appendIfNoneNull(overridesJson, builder.toJson());
        });

        JsonHelper.appendIfNoneNull(root, "overrides", overridesJson);
    }

    public enum LightType implements StringRepresentable
    {
        FRONT("front"),
        SIDE("side");

        private final String serializedName;

        LightType(String serializedName)
        {
            this.serializedName = serializedName;
        }

        @Override
        public String getSerializedName()
        {
            return serializedName;
        }
    }

    public static final class OverrideBuilder
    {
        private ModelFile model = ModelFile.MISSING;
        private final Object2FloatMap<ResourceLocation> predicates = new Object2FloatOpenHashMap<>();

        private OverrideBuilder()
        {
        }

        public OverrideBuilder model(ModelFile model)
        {
            this.model = model;
            return this;
        }

        public OverrideBuilder model(ResourceLocation modelPath)
        {
            return model(ModelFile.of(modelPath));
        }

        public OverrideBuilder model(String ownerId, String modelPath)
        {
            return model(ModelFile.of(ownerId, modelPath));
        }

        public OverrideBuilder model(String modelPath)
        {
            return model(ModelFile.of(modelPath));
        }

        public OverrideBuilder itemModel(Item item)
        {
            var registryName = BuiltInRegistries.ITEM.getKey(item);
            return model(ModelGenerator.withModelDir(Registries.ITEM, registryName));
        }

        public OverrideBuilder itemModel(Holder<Item> holder)
        {
            var unwrapped = holder.unwrap();
            unwrapped.ifLeft(this::itemModel);
            unwrapped.ifRight(this::itemModel);
            return this;
        }

        public OverrideBuilder itemModel(DeferredHolder<Item, ? super Item> holder)
        {
            return itemModel(holder.registryKey());
        }

        public OverrideBuilder itemModel(ResourceKey<Item> registryKey)
        {
            return model(ModelGenerator.withModelDir(Registries.ITEM, registryKey.location()));
        }

        public OverrideBuilder blockModel(Block block)
        {
            var registryName = BuiltInRegistries.BLOCK.getKey(block);
            return model(ModelGenerator.withModelDir(Registries.BLOCK, registryName));
        }

        public OverrideBuilder blockModel(Holder<Block> holder)
        {
            var unwrapped = holder.unwrap();
            unwrapped.ifLeft(this::blockModel);
            unwrapped.ifRight(this::blockModel);
            return this;
        }

        public OverrideBuilder blockModel(DeferredHolder<Block, ? super Block> holder)
        {
            return blockModel(holder.registryKey());
        }

        public OverrideBuilder blockModel(ResourceKey<Block> registryKey)
        {
            return model(ModelGenerator.withModelDir(Registries.BLOCK, registryKey.location()));
        }

        public OverrideBuilder predicate(ResourceLocation predicateName, float value)
        {
            predicates.put(predicateName, value);
            return this;
        }

        public OverrideBuilder predicate(String ownerId, String predicateName, float value)
        {
            return predicate(new ResourceLocation(ownerId, predicateName), value);
        }

        public OverrideBuilder predicate(String predicateName, float value)
        {
            return predicate(new ResourceLocation(predicateName), value);
        }

        public OverrideBuilder predicate(ResourceLocation predicateName, boolean value)
        {
            return predicate(predicateName, value ? 1F : 0F);
        }

        public OverrideBuilder predicate(String ownerId, String predicateName, boolean value)
        {
            return predicate(new ResourceLocation(ownerId, predicateName), value);
        }

        public OverrideBuilder predicate(String predicateName, boolean value)
        {
            return predicate(new ResourceLocation(predicateName), value);
        }

        // below here is a bunch of helpers to quickly add vanilla item property override predicates
        public OverrideBuilder customModelData(int customModelData)
        {
            return predicate("custom_model_data", customModelData);
        }

        public OverrideBuilder damaged(boolean damaged)
        {
            return predicate("damaged", damaged);
        }

        public OverrideBuilder damage(float damagePercent)
        {
            return predicate("damage", damagePercent);
        }

        public OverrideBuilder leftHanded(boolean leftHanded)
        {
            return predicate("lefthanded", leftHanded);
        }

        public OverrideBuilder coolDown(float coolDownPercent)
        {
            return predicate("cooldown", coolDownPercent);
        }

        public OverrideBuilder filled(float fillPercent)
        {
            return predicate("filled", fillPercent);
        }

        public OverrideBuilder pull(float pullTicks)
        {
            return predicate("pull", pullTicks);
        }

        public OverrideBuilder pulling(boolean broken)
        {
            return predicate("pulling", broken);
        }

        public OverrideBuilder broken(boolean broken)
        {
            return predicate("broken", broken);
        }

        public OverrideBuilder casting(boolean casting)
        {
            return predicate("cast", casting);
        }

        public OverrideBuilder blocking(boolean blocking)
        {
            return predicate("blocking", blocking);
        }

        public OverrideBuilder throwing(boolean throwing)
        {
            return predicate("throwing", throwing);
        }

        public OverrideBuilder armorTrim(float trimModelIndex)
        {
            return predicate(ItemModelGenerators.TRIM_TYPE_PREDICATE_ID, trimModelIndex);
        }

        private JsonElement toJson()
        {
            var root = new JsonObject();

            root.addProperty("model", model.path().toString());

            var predicatesJson = new JsonObject();
            predicates.forEach((predicateName, value) -> predicatesJson.addProperty(predicateName.toString(), value));
            JsonHelper.appendIfNoneNull(root, "predicates", predicatesJson);

            return root;
        }
    }
}

/*
    __extends_from: ModelBuilder
    __note: item models can parent block models
    root
        gui_light: [ front | side ], default side
        textures
            layerN
        overrides
                predicate: ResourceLocation
                model: ModelFile
 */