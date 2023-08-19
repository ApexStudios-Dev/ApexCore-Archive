package xyz.apex.minecraft.testmod.common.client.renderer;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.testmod.common.entity.TestEntity;

@SideOnly(PhysicalSide.CLIENT)
public final class TestEntityRenderer extends MobRenderer<TestEntity, PigModel<TestEntity>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/pig/pig.png");

    public TestEntityRenderer(EntityRendererProvider.Context context)
    {
        super(context, new PigModel<>(context.bakeLayer(ModelLayers.PIG)), .7F);
    }

    @Override
    public ResourceLocation getTextureLocation(TestEntity entity)
    {
        return TEXTURE;
    }
}
