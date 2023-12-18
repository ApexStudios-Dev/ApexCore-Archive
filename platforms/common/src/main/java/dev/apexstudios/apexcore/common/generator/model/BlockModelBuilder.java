package dev.apexstudios.apexcore.common.generator.model;

import com.google.gson.JsonObject;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public final class BlockModelBuilder extends ModelBuilder<BlockModelBuilder, Block>
{
    private static final boolean DEFAULT_AMBIENT_OCCLUSION = false;

    private boolean ambientOcclusion = DEFAULT_AMBIENT_OCCLUSION;
    private OptionalLike<ResourceLocation> renderType = OptionalLike.empty();

    BlockModelBuilder(ModelGenerator<Block, BlockModelBuilder> generator, ResourceLocation path)
    {
        super(generator, path);
    }

    public BlockModelBuilder ambientOcclusion(boolean ambientOcclusion)
    {
        this.ambientOcclusion = ambientOcclusion;
        return this;
    }

    public BlockModelBuilder ambientOcclusion()
    {
        return ambientOcclusion(true);
    }

    public BlockModelBuilder renderType(@Nullable ResourceLocation renderType)
    {
        this.renderType = OptionalLike.of(renderType);
        return this;
    }

    public BlockModelBuilder renderType(String ownerId, String renderType)
    {
        return renderType(new ResourceLocation(ownerId, renderType));
    }

    public BlockModelBuilder renderType(String renderType)
    {
        return renderType(new ResourceLocation(renderType));
    }

    @Override
    protected void serializeExtraData(JsonObject root)
    {
        if(ambientOcclusion != DEFAULT_AMBIENT_OCCLUSION)
            root.addProperty("ambientocclusion", ambientOcclusion);

        renderType.map(ResourceLocation::toString).ifPresent(renderType -> root.addProperty("render_type", renderType));
    }
}

/*
    __extends_from: ModelBuilder
    root
        ambientocclusion: boolean, default false
 */