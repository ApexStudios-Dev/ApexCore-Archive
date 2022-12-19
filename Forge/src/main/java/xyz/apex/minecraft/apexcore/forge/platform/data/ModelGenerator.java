package xyz.apex.minecraft.apexcore.forge.platform.data;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import xyz.apex.minecraft.apexcore.forge.platform.data.model.BlockModelBuilder;
import xyz.apex.minecraft.apexcore.forge.platform.data.model.ExistingModelFile;
import xyz.apex.minecraft.apexcore.forge.platform.data.model.ItemModelBuilder;
import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@ApiStatus.Internal
public abstract class ModelGenerator<F extends ModelBuilder<F>, A extends Model.ModelBuilder<A>, P extends ModelProvider<F>> extends DelegatedProvider<P> implements Model<A>
{
    protected static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");
    protected static final ExistingFileHelper.ResourceType MODEL_WITH_EXTENSION = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, "", "models");

    protected final String folder;

    private final Map<ResourceLocation, A> models = Maps.newHashMap();

    @ApiStatus.Internal
    ModelGenerator(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper, String folder)
    {
        super(packOutputSupplier, lookupProviderSupplier, modId, existingFileHelper);

        this.folder = StringUtils.appendIfMissing(folder, "/");
    }

    protected abstract A createModel(F forge, ResourceLocation modelPath);

    @Override
    public ResourceLocation extendWithFolder(ResourceLocation location)
    {
        return Model.extendWith(location, folder);
    }

    @Override
    public ResourceLocation removeFolder(ResourceLocation location)
    {
        return Model.remove(location, folder);
    }

    @Override
    public String extendWithFolder(String location)
    {
        return Model.extendWith(location, folder);
    }

    @Override
    public String removeFolder(String location)
    {
        return Model.remove(location, folder);
    }

    @Override
    public ResourceLocation modLocation(String location)
    {
        return new ResourceLocation(modId, location);
    }

    @Override
    public A modelBuilder(ResourceLocation modelPath)
    {
        return models.computeIfAbsent(modelPath, $ -> createModel(provider.getBuilder(modelPath.toString()), modelPath));
    }

    @Override
    public ModelFile existingModel(ResourceLocation modelPath)
    {
        var model = new ExistingModelFile(modelPath, existingFileHelper);
        model.assertExistence();
        return model;
    }

    @ApiStatus.Internal
    public static final class ItemModelGenerator extends ModelGenerator<net.minecraftforge.client.model.generators.ItemModelBuilder, ItemModelBuilder, ItemModelProvider> implements Model.ItemModel<ItemModelBuilder>
    {
        @ApiStatus.Internal
        ItemModelGenerator(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
        {
            super(packOutputSupplier, lookupProviderSupplier, modId, existingFileHelper, ModelProvider.ITEM_FOLDER);
        }

        @Override
        protected ItemModelProvider createProvider(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
        {
            return new ItemModelProvider(packOutputSupplier.get(), modId, existingFileHelper) {
                @Override
                protected void registerModels()
                {
                    Generators.processDataGenerator(modId, ProviderTypes.ITEM_MODELS, ItemModelGenerator.this);
                }
            };
        }

        @Override
        protected xyz.apex.minecraft.apexcore.forge.platform.data.model.ItemModelBuilder createModel(net.minecraftforge.client.model.generators.ItemModelBuilder forge, ResourceLocation modelPath)
        {
            return new xyz.apex.minecraft.apexcore.forge.platform.data.model.ItemModelBuilder(forge, existingFileHelper, provider::getExistingFile);
        }
    }

    @ApiStatus.Internal
    public static final class BlockModelGenerator extends ModelGenerator<net.minecraftforge.client.model.generators.BlockModelBuilder, BlockModelBuilder, BlockModelProvider> implements Model.BlockModel<BlockModelBuilder>
    {
        @ApiStatus.Internal
        BlockModelGenerator(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
        {
            super(packOutputSupplier, lookupProviderSupplier, modId, existingFileHelper, ModelProvider.BLOCK_FOLDER);
        }

        @Override
        protected BlockModelProvider createProvider(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
        {
            return new BlockModelProvider(packOutputSupplier.get(), modId, existingFileHelper) {
                @Override
                protected void registerModels()
                {
                    Generators.processDataGenerator(modId, ProviderTypes.BLOCK_MODELS, BlockModelGenerator.this);
                }
            };
        }

        @Override
        protected xyz.apex.minecraft.apexcore.forge.platform.data.model.BlockModelBuilder createModel(net.minecraftforge.client.model.generators.BlockModelBuilder forge, ResourceLocation modelPath)
        {
            return new xyz.apex.minecraft.apexcore.forge.platform.data.model.BlockModelBuilder(forge, existingFileHelper, provider::getExistingFile);
        }
    }
}
