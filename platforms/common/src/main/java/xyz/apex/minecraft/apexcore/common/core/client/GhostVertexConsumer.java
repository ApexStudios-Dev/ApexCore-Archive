package xyz.apex.minecraft.apexcore.common.core.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

@SideOnly(PhysicalSide.CLIENT)
public record GhostVertexConsumer(VertexConsumer delegate) implements VertexConsumer
{
    public int alpha(int alpha)
    {
        return (alpha * ghostAlpha()) / 0xFF;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z)
    {
        return delegate.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha)
    {
        return delegate.color(red, green, blue, alpha(alpha));
    }

    @Override
    public VertexConsumer uv(float u, float v)
    {
        return delegate.uv(u, v);
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v)
    {
        return delegate.overlayCoords(u, v);
    }

    @Override
    public VertexConsumer uv2(int u, int v)
    {
        return delegate.uv2(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z)
    {
        return delegate.normal(x, y, z);
    }

    @Override
    public void endVertex()
    {
        delegate.endVertex();
    }

    @Override
    public void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA)
    {
        delegate.defaultColor(defaultR, defaultG, defaultB, defaultA);
    }

    @Override
    public void unsetDefaultColor()
    {
        delegate.unsetDefaultColor();
    }

    public static int ghostAlpha()
    {
        if(Minecraft.getInstance().options.graphicsMode().get() == GraphicsStatus.FAST)
            return 127;

        var period = 2500D;
        var timer = Util.getMillis() % period;
        var offset = Math.cos((float) ((2D / period) * Math.PI * timer));
        return (int) ((.55D - .2D * offset) * 255D);
    }
}
