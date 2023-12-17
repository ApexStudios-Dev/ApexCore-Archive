package dev.apexstudios.apexcore.common.generator.model;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public final class BlockModelBuilder extends ModelBuilder<BlockModelBuilder, Block>
{
    private static final boolean DEFAULT_AMBIENT_OCCLUSION = false;

    private boolean ambientOcclusion = DEFAULT_AMBIENT_OCCLUSION;

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

    @Override
    protected void serializeExtraData(JsonObject root)
    {
        if(ambientOcclusion != DEFAULT_AMBIENT_OCCLUSION)
            root.addProperty("ambientocclusion", ambientOcclusion);
    }
}

/*
    __extends_from: ModelBuilder
    root
        ambientocclusion: boolean, default false
 */