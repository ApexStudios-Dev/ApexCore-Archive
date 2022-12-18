package xyz.apex.minecraft.apexcore.shared.data.providers.model;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.platform.ForPlatform;
import xyz.apex.minecraft.apexcore.shared.platform.Platform;

import java.util.Optional;

public final class BlockModelBuilder extends ModelBuilder<BlockModelBuilder>
{
    @Nullable private ResourceLocation renderType;

    @ApiStatus.Internal
    public BlockModelBuilder(ResourceLocation modelPath)
    {
        super(Registries.BLOCK, modelPath);
    }

    // region: RenderType
    @ForPlatform(Platform.Type.FORGE)
    public BlockModelBuilder renderType(String modId, String name)
    {
        return renderType(new ResourceLocation(modId, name));
    }

    @ForPlatform(Platform.Type.FORGE)
    public BlockModelBuilder renderType(String name)
    {
        return renderType(new ResourceLocation(name));
    }

    // forge allows block models to be given a render type property
    // which dictates what render type to be used when rendering the block in world
    @ForPlatform(Platform.Type.FORGE)
    public BlockModelBuilder renderType(@Nullable ResourceLocation renderType)
    {
        this.renderType = renderType;
        return self;
    }

    public Optional<ResourceLocation> getRenderType()
    {
        return Optional.ofNullable(renderType);
    }
    // endregion

    @Override
    protected JsonObject toJson()
    {
        var root = super.toJson();
        if(renderType != null) root.addProperty("render_type", renderType.toString());
        return root;
    }
}
