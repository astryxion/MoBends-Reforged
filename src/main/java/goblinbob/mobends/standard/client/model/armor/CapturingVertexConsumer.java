package goblinbob.mobends.standard.client.model.armor;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import java.util.ArrayList;
import java.util.List;

public class CapturingVertexConsumer implements IVertexBuilder
{
    private final List<CapturedVertex> vertices = new ArrayList<>();

    private boolean hasCurrentVertex = false;
    private float x, y, z;
    private float red = 1.0f, green = 1.0f, blue = 1.0f, alpha = 1.0f;
    private float u, v;
    private int overlayUV;
    private int lightmapUV;
    private float normalX, normalY, normalZ;

    public CapturingVertexConsumer()
    {
    }

    private void flushCurrentVertex()
    {
        if (hasCurrentVertex)
        {
            vertices.add(new CapturedVertex(
                    x, y, z,
                    red, green, blue, alpha,
                    u, v,
                    overlayUV, lightmapUV,
                    normalX, normalY, normalZ
            ));
            hasCurrentVertex = false;
        }
    }

    public List<CapturedVertex> getVertices()
    {
        flushCurrentVertex();
        return vertices;
    }

    public void clear()
    {
        vertices.clear();
        hasCurrentVertex = false;
        red = green = blue = 1.0f;
        alpha = 1.0f;
        u = v = 0.0f;
        overlayUV = 0;
        lightmapUV = 0;
        normalX = normalY = normalZ = 0.0f;
    }

    public int getVertexCount()
    {
        return vertices.size() + (hasCurrentVertex ? 1 : 0);
    }

    public IVertexBuilder addVertex(float x, float y, float z)
    {
        flushCurrentVertex();

        this.x = x;
        this.y = y;
        this.z = z;

        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
        this.u = 0.0f;
        this.v = 0.0f;
        this.overlayUV = 0;
        this.lightmapUV = 0;
        this.normalX = 0.0f;
        this.normalY = 0.0f;
        this.normalZ = 0.0f;

        this.hasCurrentVertex = true;

        return this;
    }

    public IVertexBuilder setColor(int packedColor)
    {
        this.alpha = ((packedColor >> 24) & 0xFF) / 255.0f;
        this.red = ((packedColor >> 16) & 0xFF) / 255.0f;
        this.green = ((packedColor >> 8) & 0xFF) / 255.0f;
        this.blue = (packedColor & 0xFF) / 255.0f;
        return this;
    }

    public IVertexBuilder setColor(int red, int green, int blue, int alpha)
    {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
        this.alpha = alpha / 255.0f;
        return this;
    }

    public IVertexBuilder setUv(float u, float v)
    {
        this.u = u;
        this.v = v;
        return this;
    }

    public IVertexBuilder setOverlay(int overlay)
    {
        this.overlayUV = overlay;
        return this;
    }

    public IVertexBuilder setLight(int light)
    {
        this.lightmapUV = light;
        return this;
    }

    public IVertexBuilder setNormal(float x, float y, float z)
    {
        this.normalX = x;
        this.normalY = y;
        this.normalZ = z;
        return this;
    }

    public IVertexBuilder setUv1(int u, int v)
    {
        this.overlayUV = (v << 16) | (u & 0xFFFF);
        return this;
    }

    public IVertexBuilder setUv2(int u, int v)
    {
        this.lightmapUV = (v << 16) | (u & 0xFFFF);
        return this;
    }

    public IVertexBuilder vertex(double x, double y, double z)
    {
        return addVertex((float) x, (float) y, (float) z);
    }

    public IVertexBuilder color(int red, int green, int blue, int alpha)
    {
        return setColor(red, green, blue, alpha);
    }

    public IVertexBuilder color(float red, float green, float blue, float alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        return this;
    }

    public IVertexBuilder uv(float u, float v)
    {
        return setUv(u, v);
    }

    public IVertexBuilder overlayCoords(int u, int v)
    {
        return setUv1(u, v);
    }

    public IVertexBuilder overlayCoords(int overlay)
    {
        return setOverlay(overlay);
    }

    public IVertexBuilder uv2(int lightmap)
    {
        return setLight(lightmap);
    }

    public IVertexBuilder uv2(int u, int v)
    {
        return setUv2(u, v);
    }

    public IVertexBuilder normal(float x, float y, float z)
    {
        return setNormal(x, y, z);
    }

    public void endVertex()
    {
        flushCurrentVertex();
    }

    public void defaultColor(int red, int green, int blue, int alpha)
    {
    }

    public void unsetDefaultColor()
    {
    }
}
