package goblinbob.mobends.forge.platform;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.VertexFormat;
import goblinbob.mobends.api.rendering.DrawMode;
import goblinbob.mobends.api.rendering.IBufferBuilder;
import goblinbob.mobends.api.rendering.ITesselator;
import goblinbob.mobends.api.rendering.VertexFormatType;
import org.lwjgl.opengl.GL11;

public class ForgeTesselator implements ITesselator
{
    private final Tessellator tesselator;

    public ForgeTesselator()
    {
        this.tesselator = Tessellator.getInstance();
    }

    @Override
    public IBufferBuilder begin(DrawMode mode, VertexFormatType format)
    {
        int mcMode = mapDrawMode(mode);
        VertexFormat mcFormat = mapVertexFormat(format);

        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(mcMode, mcFormat);
        return new ForgeBufferBuilder(builder);
    }

    @Override
    public void endAndDraw(IBufferBuilder builder)
    {
        ForgeBufferBuilder forgeBuilder = (ForgeBufferBuilder) builder;

        forgeBuilder.finishVertex();

        BufferBuilder nativeBuilder = (BufferBuilder) builder.getNative();
        nativeBuilder.end();
        WorldVertexBufferUploader.end(nativeBuilder);
    }

    @Override
    public Object getNative()
    {
        return tesselator;
    }

    private int mapDrawMode(DrawMode mode)
    {
        switch (mode) {
            case QUADS:
                return GL11.GL_QUADS;
            case TRIANGLES:
                return GL11.GL_TRIANGLES;
            case TRIANGLE_STRIP:
                return GL11.GL_TRIANGLE_STRIP;
            case TRIANGLE_FAN:
                return GL11.GL_TRIANGLE_FAN;
            case LINES:
                return GL11.GL_LINES;
            case LINE_STRIP:
                return GL11.GL_LINE_STRIP;
            case DEBUG_LINES:
                return GL11.GL_LINES;
            case DEBUG_LINE_STRIP:
                return GL11.GL_LINE_STRIP;
        }
        return GL11.GL_QUADS;
    }

    private VertexFormat mapVertexFormat(VertexFormatType format)
    {
        switch (format) {
            case POSITION:
                return DefaultVertexFormats.POSITION;
            case POSITION_COLOR:
                return DefaultVertexFormats.POSITION_COLOR;
            case POSITION_TEX:
                return DefaultVertexFormats.POSITION_TEX;
            case POSITION_TEX_COLOR:
                return DefaultVertexFormats.POSITION_TEX_COLOR;
            case POSITION_TEX_COLOR_NORMAL:
                return DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL;
        }
        return DefaultVertexFormats.POSITION;
    }
}
