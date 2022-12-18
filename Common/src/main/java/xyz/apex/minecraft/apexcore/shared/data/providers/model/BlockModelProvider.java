package xyz.apex.minecraft.apexcore.shared.data.providers.model;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;

import java.util.function.Supplier;

public final class BlockModelProvider extends ModelProvider<BlockModelBuilder>
{
    @ApiStatus.Internal
    public BlockModelProvider(PackOutput packOutput, String modId)
    {
        super(packOutput, modId, Registries.BLOCK, BlockModelBuilder::new);
    }

    // region: Cube
    public BlockModelBuilder cubeAll(Block block)
    {
        return cubeAll(BuiltInRegistries.BLOCK.getKey(block));
    }

    public BlockModelBuilder cubeAll(Supplier<? extends Block> block)
    {
        return cubeAll(block.get());
    }

    public BlockModelBuilder cubeAll(ResourceLocation blockName)
    {
        return withParent(extendWithFolder(blockName), new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "block/cube_all"))
                .texture(TextureSlot.ALL, extendWithFolder(blockName))
                .texture(TextureSlot.PARTICLE, extendWithFolder(blockName))
        ;
    }
    // endregion

    @Override
    protected void registerModels()
    {
        Generators.processDataGenerator(modId, ProviderTypes.BLOCK_MODELS, this);
    }

    @Override
    public String getName()
    {
        return "BlockModels";
    }
}
